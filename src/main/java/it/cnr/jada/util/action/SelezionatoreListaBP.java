/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.util.action;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.jsp.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.NoSuchEJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class SelezionatoreListaBP extends AbstractSelezionatoreBP
        implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SelezionatoreListaBP.class);
    protected Table table;
    private RemoteIterator iterator;
    private Dictionary columns;
    private int currentPage;
    private Forward forward;
    private OggettoBulk[] pageContents;
    private int firstPage;
    private int pageFrameSize;
    private int pageSize;
    private int pageCount;
    private int elementsCount;
    private RemotePagedIterator pagedIterator;
    private Button[] navigatorToolbar;
    private BulkInfo bulkInfo;
    private ObjectReplacer objectReplacer;
    private SelectionListener selectionListener;
    private boolean mostraHideColumns = false;
    private boolean hiddenColumnButtonHidden = true;

    private FormField formField;
    private CondizioneComplessaBulk condizioneCorrente;

    public SelezionatoreListaBP() {
        this("");
    }

    public SelezionatoreListaBP(String s) {
        super(s);
        pageFrameSize = 10;
        pageSize = 10;
        table = new Table("mainTable");
        table.setOrderable(this);
        table.setSelection(super.selection = new Selection());
        table.setOnselect("javascript:select");
        table.setOnsort("javascript:sort");
        table.setOnHiddenColumn("javascript:hiddenColumn");
    }

    public void clearSelection(ActionContext actioncontext)
            throws BusinessProcessException {
        if (selectionListener != null)
            selectionListener.clearSelection(actioncontext);
        super.clearSelection(actioncontext);
    }

    protected void closed()
            throws BusinessProcessException {
        super.closed();
        try {
            EJBCommonServices.closeRemoteIterator((ActionContext) null, iterator);
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        }
    }

    protected void closed(ActionContext context) throws BusinessProcessException {
        super.closed(context);
        try {
            EJBCommonServices.closeRemoteIterator(context, iterator);
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        }
    }

    protected void closed(HttpSession session) throws BusinessProcessException {
        super.closed();
        try {
            EJBCommonServices.closeRemoteIterator(session, iterator);
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        }
    }

    public Button[] createNavigatorToolbar() {
        Button[] abutton = new Button[4];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previousFrame");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previous");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.next");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.nextFrame");
        return abutton;
    }

    public Button[] createToolbar() {
        return Optional.ofNullable(createToolbarList())
                .map(buttons -> buttons.toArray(new Button[buttons.size()]))
                .orElse(new Button[0]);
    }

    public List<Button> createToolbarList() {
        final Optional<SearchProvider> searchProvider = Optional.ofNullable(getParent())
                .filter(CRUDBP.class::isInstance)
                .map(CRUDBP.class::cast)
                .map(crudbp1 -> crudbp1.getSearchProvider());
        List<Button> buttons = new ArrayList<Button>();
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print"));
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel"));
        if (searchProvider.isPresent() || Optional.ofNullable(this).filter(SearchProvider.class::isInstance).isPresent()) {
            Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearchFilter");
            button.setSeparator(true);
            buttons.add(button);
            buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearchRemoveFilter"));
        }
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.multiSelection"));
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll"));
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.deselectAll"));
        buttons.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.hiddenColumn"));
        setMostraHideColumns(true);
        return buttons;
    }


    public boolean isFilterButtonHidden() {
        return false;
    }

    public boolean isRemoveFilterButtonHidden() {
        return condizioneCorrente == null;
    }

    public CondizioneComplessaBulk getCondizioneCorrente() {
        return condizioneCorrente;
    }

    public void setCondizioneCorrente(CondizioneComplessaBulk condizioneCorrente) {
        this.condizioneCorrente = condizioneCorrente;
    }

    @Override
    public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
        Dictionary hiddenColumns = null;
        if (pagecontext.getSession().getAttribute("UserContext") != null) {
            UserContext userContext = (UserContext) pagecontext.getSession().getAttribute("UserContext");
            hiddenColumns = userContext.getHiddenColumns();
        }
        for (Enumeration columns = hiddenColumns.keys(); columns.hasMoreElements(); ) {
            if (((String) columns.nextElement()).startsWith(getPath()))
                setHiddenColumnButtonHidden(false);
        }
        super.writeToolbar(pagecontext);
    }

    public RemoteIterator detachIterator() {
        try {
            RemoteIterator remoteiterator = iterator;
            return remoteiterator;
        } finally {
            iterator = null;
        }
    }

    public void disableSelection() {
        table.setOnselect(null);
    }

    public Enumeration fetchPage(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            if (pagedIterator != null) {
                pagedIterator.moveToPage(currentPage);
                setPageContents(actioncontext, (OggettoBulk[]) pagedIterator.nextPage());
            } else {
                int i = currentPage * pageSize;
                pageContents = new OggettoBulk[Math.min(pageSize, elementsCount - i)];
                iterator.moveTo(i);
                for (int j = 0; j < pageSize && i < elementsCount; i++) {
                    pageContents[j] = (OggettoBulk) iterator.nextElement();
                    j++;
                }

                setPageContents(actioncontext, pageContents);
            }
            setPageContents(actioncontext, initializeBulks(actioncontext, pageContents));
            return new ArrayEnumeration(pageContents);
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        }
    }

    public BulkInfo getBulkInfo() {
        return bulkInfo;
    }

    public void setBulkInfo(BulkInfo bulkinfo) {
        bulkInfo = bulkinfo;
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
    }

    public Dictionary getColumns() {
        return columns;
    }

    public void setColumns(Dictionary dictionary) {
        columns = dictionary;
        table.setColumns(dictionary);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int i)
            throws RemoteException {
        currentPage = Math.min(pageCount - 1, Math.max(0, i));
        pageContents = null;
        firstPage = pageFrameSize * (currentPage / pageFrameSize);
    }

    public Object getElementAt(ActionContext actioncontext, int i)
            throws BusinessProcessException {
        try {
            int j = i / pageSize;
            int k = i % pageSize;
            if (j == currentPage) {
                return pageContents[k];
            } else {
                iterator.moveTo(i);
                return iterator.nextElement();
            }
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        }
    }

    public int getElementsCount() {
        return elementsCount;
    }

    protected int getFirstElementIndexOnCurrentPage() {
        return currentPage * pageSize;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public String getFormTitle() {
        String formTitle = null;
        if (bulkInfo == null)
            formTitle = super.getFormTitle();
        else
            formTitle = super.getFormTitle() + " - " + bulkInfo.getShortDescription();
        if (iterator != null)
            formTitle = formTitle + " - Trovate " + elementsCount + " righe";
        return formTitle;
    }

    public RemoteIterator getIterator() {
        return iterator;
    }

    public int getLastPage()
            throws RemoteException {
        return Math.min(firstPage + pageFrameSize, pageCount);
    }

    public Button[] getNavigatorToolbar() {
        if (navigatorToolbar == null)
            navigatorToolbar = createNavigatorToolbar();
        return navigatorToolbar;
    }

    public ObjectReplacer getObjectReplacer() {
        return objectReplacer;
    }

    public void setObjectReplacer(ObjectReplacer objectreplacer) {
        objectReplacer = objectreplacer;
    }

    public int getOrderBy(String s) {
        try {
            return ((RemoteOrderable) iterator).getOrderBy(s);
        } catch (RemoteException remoteexception) {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    public OggettoBulk[] getPageContents() {
        return pageContents;
    }

    protected void setPageContents(OggettoBulk aoggettobulk[]) {
        pageContents = aoggettobulk;
    }

    public int getPageCount()
            throws RemoteException {
        return pageCount;
    }

    public int getPageFrameSize() {
        return pageFrameSize;
    }

    public void setPageFrameSize(int i) {
        pageFrameSize = i;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int i)
            throws RemoteException {
        pageSize = i;
        if (pagedIterator != null)
            pagedIterator.setPageSize(pageSize);
    }

    public SelectionListener getSelectionListener() {
        return selectionListener;
    }

    public void goToPage(ActionContext actioncontext, int i)
            throws BusinessProcessException {
        try {
            setCurrentPage(i);
            fetchPage(actioncontext);
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        }
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            pageSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageSize"));
        } catch (NumberFormatException _ex) {
        }
        try {
            pageFrameSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageFrameSize"));
        } catch (NumberFormatException _ex) {
        }
    }

    public OggettoBulk[] initializeBulks(ActionContext actioncontext, OggettoBulk aoggettobulk[])
            throws BusinessProcessException {
        if (objectReplacer != null) {
            for (int i = 0; i < aoggettobulk.length; i++)
                aoggettobulk[i] = (OggettoBulk) objectReplacer.replaceObject(aoggettobulk[i]);

        }
        return aoggettobulk;
    }

    public boolean isMultiSelection() {
        return table.isMultiSelection();
    }

    public void setMultiSelection(boolean flag) {
        table.setMultiSelection(flag);
        if (flag)
            table.setOnselect(null);
    }

    public boolean isMultiSelectionButtonHidden() {
        return !table.isMultiSelection();
    }

    public boolean isExcelButtonHidden() {
        Query query = null;
        try {
            if (iterator instanceof TransactionalBulkLoaderIterator) {
                query = ((TransactionalBulkLoaderIterator) iterator).getQuery();
            } else if (iterator instanceof BulkLoaderIterator) {
                query = ((BulkLoaderIterator) iterator).getQuery();
            }
        } catch (DetailedRuntimeException | RemoteException | NoSuchEJBException e) {
            logger.warn("isExcelButtonHidden :", e);
        }
        return query == null;
    }

    public boolean isNextButtonEnabled() {
        return currentPage + 1 < pageCount;
    }

    public boolean isNextFrameButtonEnabled() {
        return currentPage + pageFrameSize + 1 < pageCount;
    }

    public boolean isOrderableBy(String s) {
        try {
            return ((RemoteOrderable) iterator).isOrderableBy(s);
        } catch (RemoteException remoteexception) {
            throw new DetailedRuntimeException(remoteexception);
        } catch (javax.ejb.ConcurrentAccessException concurrentAccessException) {
            logger.info("ConcurrentAccessException");
            return false;
        } catch (javax.ejb.NoSuchEJBException _ex) {
            return false;
        }
    }

    public boolean isPreviousButtonEnabled() {
        return currentPage > 0;
    }

    public boolean isPreviousFrameButtonEnabled() {
        return currentPage - pageFrameSize > 0;
    }

    public Iterator iterator() {
        if (super.selection.size() > 0)
            return super.selection.iterator(Arrays.asList(pageContents), currentPage * pageSize, pageSize);
        if (super.selection.getFocus() < 0)
            return Collections.EMPTY_LIST.iterator();
        else
            return Collections.singleton(getFocusedElement()).iterator();
    }

    public Enumeration refetchPage(ActionContext actioncontext)
            throws BusinessProcessException {
        pageContents = null;
        return fetchPage(actioncontext);
    }

    public void refresh(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            clearSelection(actioncontext);
            pageContents = null;
            iterator.refresh();
            elementsCount = iterator.countElements();
            pageCount = ((elementsCount + pageSize) - 1) / pageSize;
            if (currentPage >= pageCount)
                currentPage = Math.max(pageCount - 1, 0);
            fetchPage(actioncontext);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    public void reset()
            throws RemoteException {
        firstPage = 0;
        pageContents = null;
        currentPage = 0;
        elementsCount = iterator.countElements();
        pageCount = ((elementsCount + pageSize) - 1) / pageSize;
    }

    public void reset(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            super.reset(actioncontext);
            reset();
            fetchPage(actioncontext);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    public Selection saveSelection(ActionContext actioncontext)
            throws BusinessProcessException {
        if (selectionListener != null) {
            java.util.BitSet bitset = super.selection.getSelection(currentPage * pageSize, pageSize);
            setSelection(actioncontext);
            java.util.BitSet bitset1 = super.selection.getSelection(currentPage * pageSize, pageSize);
            try {
                super.selection.setSelection(currentPage * pageSize, pageSize, selectionListener.setSelection(actioncontext, pageContents, bitset, bitset1));
            } catch (BusinessProcessException businessprocessexception) {
                super.selection.setSelection(currentPage * pageSize, pageSize, bitset);
                throw businessprocessexception;
            }
            return super.selection;
        } else {
            return setSelection(actioncontext);
        }
    }

    public void selectAll(ActionContext actioncontext)
            throws BusinessProcessException {
        if (selectionListener != null)
            selectionListener.selectAll(actioncontext);
        super.selection.setSelection(0, elementsCount);
    }

    public void deSelectAll(ActionContext actioncontext)
            throws BusinessProcessException {
        clearSelection(actioncontext);
    }

    public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator)
            throws RemoteException, BusinessProcessException {
        setIterator(actioncontext, remoteiterator, pageSize, pageFrameSize);
    }

    public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator, int i, int j)
            throws RemoteException, BusinessProcessException {
        EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
        iterator = remoteiterator;
        if (remoteiterator instanceof RemotePagedIterator)
            pagedIterator = (RemotePagedIterator) remoteiterator;
        pageFrameSize = j;
        setPageSize(i);
        reset(actioncontext);
    }

    public void setOrderBy(ActionContext actioncontext, String s, int i) {
        try {
            ((RemoteOrderable) iterator).setOrderBy(s, i);
        } catch (RemoteException remoteexception) {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    protected void setPageContents(ActionContext actioncontext, OggettoBulk aoggettobulk[])
            throws BusinessProcessException {
        if (selectionListener != null)
            super.selection.setSelection(currentPage * pageSize, pageSize, selectionListener.getSelection(actioncontext, aoggettobulk, super.selection.getSelection(currentPage * pageSize, pageSize)));
        setPageContents(aoggettobulk);
    }

    public Selection setSelection(ActionContext actioncontext) {
        super.selection.setSelection(actioncontext, "mainTable", currentPage * pageSize, pageSize);
        return super.selection;
    }

    public void setSelectionListener(ActionContext actioncontext, SelectionListener selectionlistener)
            throws BusinessProcessException {
        selectionListener = selectionlistener;
        if (selectionListener != null)
            selectionListener.initializeSelection(actioncontext);
    }

    public int size() {
        return elementsCount;
    }

    public void writeHTMLNavigator(JspWriter jspwriter)
            throws IOException, ServletException {
        Button[] abutton = getNavigatorToolbar();
        boolean isBootstrap = this.getParentRoot().isBootstrap();
        if (isBootstrap) {
            jspwriter.println("<nav aria-label=\"Page navigation\"><ul class=\"pagination justify-content-center\">");

            jspwriter.println("<li class=\"page-item ");
            if (!isPreviousFrameButtonEnabled())
                jspwriter.print("disabled");
            jspwriter.print("\">");
            jspwriter.println("<a class=\"page-link ");
            if (isPreviousFrameButtonEnabled())
                jspwriter.print("text-primary");
            jspwriter.print("\" ");
            jspwriter.print("onclick=\"javascript:submitForm('doPreviousFrame')\">");
            jspwriter.println("<i class=\"fa fa-fast-backward\" aria-hidden=\"true\"></i>");
            jspwriter.println("</a>");
            jspwriter.println("</li>");

            jspwriter.println("<li class=\"page-item ");
            if (!isPreviousButtonEnabled())
                jspwriter.print("disabled");
            jspwriter.print("\">");
            jspwriter.println("<a class=\"page-link ");
            if (isPreviousButtonEnabled())
                jspwriter.print("text-primary");
            jspwriter.print("\" ");
            jspwriter.print("onclick=\"javascript:submitForm('doPreviousPage')\">");
            jspwriter.println("<i class=\"fa fa-backward\" aria-hidden=\"true\"></i>");
            jspwriter.println("</a>");
            jspwriter.println("</li>");

            getLastPage();
            for (int i = getFirstPage(); i < getLastPage(); i++) {
                jspwriter.println("<li class=\"page-item ");
                if (getCurrentPage() != i) {
                    jspwriter.print("\">");
                    jspwriter.println("<a class=\"page-link text-primary\" ");
                    jspwriter.print("onclick=\"" + "javascript:submitForm('doGotoPage(" + i + ")')" + "\">");
                    jspwriter.print(i);
                    jspwriter.print("</a>");
                } else {
                    jspwriter.print("active\">");
                    jspwriter.println("<span class=\"page-link\">");
                    jspwriter.print(i);
                    jspwriter.print(" <span class=\"sr-only\">(current)</span></span>");
                }
                jspwriter.println("</li>");
            }

            jspwriter.println("<li class=\"page-item ");
            if (!isNextButtonEnabled())
                jspwriter.print("disabled");
            jspwriter.print("\">");
            jspwriter.println("<a class=\"page-link ");
            if (isNextButtonEnabled())
                jspwriter.print("text-primary");
            jspwriter.print("\" ");
            jspwriter.print("onclick=\"javascript:submitForm('doNextPage')\">");
            jspwriter.println("<i class=\"fa fa-forward\" aria-hidden=\"true\"></i>");
            jspwriter.println("</a>");
            jspwriter.println("</li>");

            jspwriter.println("<li class=\"page-item ");
            if (!isNextFrameButtonEnabled())
                jspwriter.print("disabled");
            jspwriter.print("\">");
            jspwriter.println("<a class=\"page-link ");
            if (isNextFrameButtonEnabled())
                jspwriter.print("text-primary");
            jspwriter.print("\" ");
            jspwriter.print("onclick=\"javascript:submitForm('doNextFrame')\">");
            jspwriter.println("<i class=\"fa fa-fast-forward\" aria-hidden=\"true\"></i>");
            jspwriter.println("</a>");
            jspwriter.println("</li>");

            for (int j = 4; j < abutton.length; j++) {
                jspwriter.println("<li class=\"page-item\">");
                abutton[j].write(jspwriter, this, isBootstrap);
                jspwriter.println("</li>");
            }
            jspwriter.println("</ul></nav>");
        } else {
            jspwriter.println("<div class=\"Toolbar\">");
            jspwriter.println("<table cellspacing=\"0\" cellpadding=\"0\">");
            jspwriter.println("<tr align=center valign=middle>");
            jspwriter.print("<td>");
            abutton[0].write(jspwriter, this, isBootstrap);
            jspwriter.println("</td>");
            jspwriter.print("<td>");
            abutton[1].write(jspwriter, this, isBootstrap);
            jspwriter.println("</td>");
            getLastPage();
            for (int i = getFirstPage(); i < getLastPage(); i++) {
                jspwriter.print("<td width=\"16\">");
                if (getCurrentPage() != i)
                    JSPUtils.button(jspwriter, null, String.valueOf(i), "javascript:submitForm('doGotoPage(" + i + ")')", isBootstrap);
                else
                    JSPUtils.button(jspwriter, null, String.valueOf(i), null, "background: Highlight;color: HighlightText;", isBootstrap);
                jspwriter.println("</td>");
            }

            jspwriter.print("<td>");
            abutton[2].write(jspwriter, this, isBootstrap);
            jspwriter.println("</td>");
            jspwriter.print("<td>");
            abutton[3].write(jspwriter, this, isBootstrap);
            jspwriter.println("</td>");
            for (int j = 4; j < abutton.length; j++) {
                jspwriter.print("<td>");
                abutton[j].write(jspwriter, this, isBootstrap);
                jspwriter.println("</td>");
            }
            jspwriter.println("</tr>");
            jspwriter.println("</table>");
            jspwriter.println("</div>");
        }
    }

    public void writeHTMLTable(PageContext pagecontext, String s, String s1) throws IOException, ServletException {
        Object bp;
        pagecontext.getOut();
        table.setSelection(super.selection);
        table.setRows(new ArrayEnumeration(pageContents));
        if (getClass().getName().equalsIgnoreCase(SelezionatoreListaBP.class.getName()))
            bp = getParent();
        else
            bp = this;
        Dictionary hiddenColumns = null;
        if (pagecontext.getSession().getAttribute("UserContext") != null) {
            UserContext userContext = (UserContext) pagecontext.getSession().getAttribute("UserContext");
            hiddenColumns = userContext.getHiddenColumns();
        }
        table.writeScrolledTable(bp, pagecontext.getOut(), s, s1, getFieldValidationMap(), currentPage * pageSize, mostraHideColumns, hiddenColumns, getPath(), this.getParentRoot().isBootstrap());
    }

    public boolean isHiddenColumnButtonHidden() {
        return hiddenColumnButtonHidden;
    }

    public void setHiddenColumnButtonHidden(boolean hiddenColumnButtonHidden) {
        this.hiddenColumnButtonHidden = hiddenColumnButtonHidden;
    }

    public boolean isMostraHideColumns() {
        return mostraHideColumns;
    }

    public void setMostraHideColumns(boolean mostraHideColumns) {
        this.mostraHideColumns = mostraHideColumns;
    }

    public FormField getFormField() {
        return formField;
    }

    public void setFormField(FormField formField) {
        this.formField = formField;
    }

}