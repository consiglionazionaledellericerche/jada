package it.cnr.jada.util.action;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
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

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

// Referenced classes of package it.cnr.jada.util.action:
//			  AbstractSelezionatoreBP, Selection, SelectionListener, FormBP, 
//			  BulkBP

public class SelezionatoreListaBP extends AbstractSelezionatoreBP
	implements Serializable
{

	public SelezionatoreListaBP()
	{
		this("");
	}

	public SelezionatoreListaBP(String s)
	{
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
		throws BusinessProcessException
	{
		if(selectionListener != null)
			selectionListener.clearSelection(actioncontext);
		super.clearSelection(actioncontext);
	}

	protected void closed()
		throws BusinessProcessException
	{
		super.closed();
		try
		{
			EJBCommonServices.closeRemoteIterator(iterator);
		}
		catch(RemoteException remoteexception)
		{
			throw handleException(remoteexception);
		}
	}

	public Button[] createNavigatorToolbar()
	{
		Button abutton[] = new Button[4];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previousFrame");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previous");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.next");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.nextFrame");
		return abutton;
	}

	public Button[] createToolbar()
	{
		Button abutton[] = new Button[5];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.multiSelection");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.hiddenColumn");
		setMostraHideColumns(true);
		return abutton;
	}
	@Override
	public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
		Dictionary hiddenColumns = null;
		if (pagecontext.getSession().getAttribute("UserContext") != null){
			UserContext userContext = (UserContext) pagecontext.getSession().getAttribute("UserContext");
			hiddenColumns = userContext.getHiddenColumns();
		}
		for (Enumeration columns = hiddenColumns.keys(); columns.hasMoreElements();){
			if (((String)columns.nextElement()).startsWith(getPath()))
				setHiddenColumnButtonHidden(false);
		}
		super.writeToolbar(pagecontext);
	}
	
	public RemoteIterator detachIterator()
	{
		try
		{
			RemoteIterator remoteiterator = iterator;
			return remoteiterator;
		}
		finally
		{
			iterator = null;
		}
	}

	public void disableSelection()
	{
		table.setOnselect(null);
	}

	public Enumeration fetchPage(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
			if(pagedIterator != null)
			{
				pagedIterator.moveToPage(currentPage);
				setPageContents(actioncontext, (OggettoBulk[])pagedIterator.nextPage());
			} else
			{
				int i = currentPage * pageSize;
				pageContents = new OggettoBulk[Math.min(pageSize, elementsCount - i)];
				iterator.moveTo(i);
				for(int j = 0; j < pageSize && i < elementsCount; i++)
				{
					pageContents[j] = (OggettoBulk)iterator.nextElement();
					j++;
				}

				setPageContents(actioncontext, pageContents);
			}
			setPageContents(actioncontext, initializeBulks(actioncontext, pageContents));
			return new ArrayEnumeration(pageContents);
		}
		catch(RemoteException remoteexception)
		{
			throw new BusinessProcessException(remoteexception);
		}
	}

	public BulkInfo getBulkInfo()
	{
		return bulkInfo;
	}

	public Dictionary getColumns()
	{
		return columns;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public Object getElementAt(ActionContext actioncontext, int i)
		throws BusinessProcessException
	{
		try
		{
			int j = i / pageSize;
			int k = i % pageSize;
			if(j == currentPage)
			{
				return pageContents[k];
			} else
			{
				iterator.moveTo(i);
				return iterator.nextElement();
			}
		}
		catch(RemoteException remoteexception)
		{
			throw new BusinessProcessException(remoteexception);
		}
	}

	public int getElementsCount()
	{
		return elementsCount;
	}

	protected int getFirstElementIndexOnCurrentPage()
	{
		return currentPage * pageSize;
	}

	public int getFirstPage()
	{
		return firstPage;
	}

	public String getFormTitle()
	{
		String formTitle = null;
		if(bulkInfo == null)
			formTitle = super.getFormTitle();
		else
			formTitle = super.getFormTitle() + " - " + bulkInfo.getShortDescription();
		if (iterator != null)
			formTitle = formTitle + " - Trovate " + elementsCount + " righe";
		return formTitle;
	}

	public RemoteIterator getIterator()
	{
		return iterator;
	}

	public int getLastPage()
		throws RemoteException
	{
		return Math.min(firstPage + pageFrameSize, pageCount);
	}

	public Button[] getNavigatorToolbar()
	{
		if(navigatorToolbar == null)
			navigatorToolbar = createNavigatorToolbar();
		return navigatorToolbar;
	}

	public ObjectReplacer getObjectReplacer()
	{
		return objectReplacer;
	}

	public int getOrderBy(String s)
	{
		try
		{
			return ((RemoteOrderable)iterator).getOrderBy(s);
		}
		catch(RemoteException remoteexception)
		{
			throw new DetailedRuntimeException(remoteexception);
		}
	}

	public OggettoBulk[] getPageContents()
	{
		return pageContents;
	}

	public int getPageCount()
		throws RemoteException
	{
		return pageCount;
	}

	public int getPageFrameSize()
	{
		return pageFrameSize;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public SelectionListener getSelectionListener()
	{
		return selectionListener;
	}

	public void goToPage(ActionContext actioncontext, int i)
		throws BusinessProcessException
	{
		try
		{
			setCurrentPage(i);
			fetchPage(actioncontext);
		}
		catch(RemoteException remoteexception)
		{
			throw new BusinessProcessException(remoteexception);
		}
	}

	protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
		throws BusinessProcessException
	{
		super.init(config, actioncontext);
		try
		{
			pageSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageSize"));
		}
		catch(NumberFormatException _ex) { }
		try
		{
			pageFrameSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageFrameSize"));
		}
		catch(NumberFormatException _ex) { }
	}

	public OggettoBulk[] initializeBulks(ActionContext actioncontext, OggettoBulk aoggettobulk[])
		throws BusinessProcessException
	{
		if(objectReplacer != null)
		{
			for(int i = 0; i < aoggettobulk.length; i++)
				aoggettobulk[i] = (OggettoBulk)objectReplacer.replaceObject(aoggettobulk[i]);

		}
		return aoggettobulk;
	}

	public boolean isMultiSelection()
	{
		return table.isMultiSelection();
	}

	public boolean isMultiSelectionButtonHidden()
	{
		return !table.isMultiSelection();
	}
	public boolean isExcelButtonHidden(){
		Query query = null;
		try {
			if (iterator instanceof TransactionalBulkLoaderIterator){
				query = ((TransactionalBulkLoaderIterator)iterator).getQuery();
			}else if (iterator instanceof BulkLoaderIterator){
				query = ((BulkLoaderIterator)iterator).getQuery();
			}
		} catch (DetailedRuntimeException e) {
		} catch (RemoteException e) {}
		return query==null;
	}

	public boolean isNextButtonEnabled()
	{
		return currentPage + 1 < pageCount;
	}

	public boolean isNextFrameButtonEnabled()
	{
		return currentPage + pageFrameSize + 1 < pageCount;
	}

	public boolean isOrderableBy(String s)
	{
		try
		{
			return ((RemoteOrderable)iterator).isOrderableBy(s);
		}
		catch(RemoteException remoteexception)
		{
			throw new DetailedRuntimeException(remoteexception);
		}
	}

	public boolean isPreviousButtonEnabled()
	{
		return currentPage > 0;
	}

	public boolean isPreviousFrameButtonEnabled()
	{
		return currentPage - pageFrameSize > 0;
	}

	public Iterator iterator()
	{
		if(super.selection.size() > 0)
			return super.selection.iterator(Arrays.asList(pageContents), currentPage * pageSize, pageSize);
		if(super.selection.getFocus() < 0)
			return Collections.EMPTY_LIST.iterator();
		else
			return Collections.singleton(getFocusedElement()).iterator();
	}

	public Enumeration refetchPage(ActionContext actioncontext)
		throws BusinessProcessException
	{
		pageContents = null;
		return fetchPage(actioncontext);
	}

	public void refresh(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
			clearSelection(actioncontext);
			pageContents = null;
			iterator.refresh();
			elementsCount = iterator.countElements();
			pageCount = ((elementsCount + pageSize) - 1) / pageSize;
			if(currentPage >= pageCount)
				currentPage = Math.max(pageCount - 1, 0);
			fetchPage(actioncontext);
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}

	public void reset()
		throws RemoteException
	{
		firstPage = 0;
		pageContents = null;
		currentPage = 0;
		elementsCount = iterator.countElements();
		pageCount = ((elementsCount + pageSize) - 1) / pageSize;
	}

	public void reset(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
			super.reset(actioncontext);
			reset();
			fetchPage(actioncontext);
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}

	public Selection saveSelection(ActionContext actioncontext)
		throws BusinessProcessException
	{
		if(selectionListener != null)
		{
			java.util.BitSet bitset = super.selection.getSelection(currentPage * pageSize, pageSize);
			setSelection(actioncontext);
			java.util.BitSet bitset1 = super.selection.getSelection(currentPage * pageSize, pageSize);
			try
			{
				super.selection.setSelection(currentPage * pageSize, pageSize, selectionListener.setSelection(actioncontext, pageContents, bitset, bitset1));
			}
			catch(BusinessProcessException businessprocessexception)
			{
				super.selection.setSelection(currentPage * pageSize, pageSize, bitset);
				throw businessprocessexception;
			}
			return super.selection;
		} else
		{
			return setSelection(actioncontext);
		}
	}

	public void selectAll(ActionContext actioncontext)
		throws BusinessProcessException
	{
		if(selectionListener != null)
			selectionListener.selectAll(actioncontext);
		super.selection.setSelection(0, elementsCount);
	}

	public void setBulkInfo(BulkInfo bulkinfo)
	{
		bulkInfo = bulkinfo;
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
	}

	public void setColumns(Dictionary dictionary)
	{
		columns = dictionary;
		table.setColumns(dictionary);
	}

	public void setCurrentPage(int i)
		throws RemoteException
	{
		currentPage = Math.min(pageCount - 1, Math.max(0, i));
		pageContents = null;
		firstPage = pageFrameSize * (currentPage / pageFrameSize);
	}

	public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator)
		throws RemoteException, BusinessProcessException
	{
		setIterator(actioncontext, remoteiterator, pageSize, pageFrameSize);
	}

	public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator, int i, int j)
		throws RemoteException, BusinessProcessException
	{
		EJBCommonServices.closeRemoteIterator(iterator);
		iterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
		if(remoteiterator instanceof RemotePagedIterator)
			pagedIterator = (RemotePagedIterator)remoteiterator;
		pageFrameSize = j;
		setPageSize(i);
		reset(actioncontext);
	}

	public void setMultiSelection(boolean flag)
	{
		table.setMultiSelection(flag);
		if(flag)
			table.setOnselect(null);
	}

	public void setObjectReplacer(ObjectReplacer objectreplacer)
	{
		objectReplacer = objectreplacer;
	}

	public void setOrderBy(ActionContext actioncontext, String s, int i)
	{
		try
		{
			((RemoteOrderable)iterator).setOrderBy(s, i);
		}
		catch(RemoteException remoteexception)
		{
			throw new DetailedRuntimeException(remoteexception);
		}
	}

	protected void setPageContents(OggettoBulk aoggettobulk[])
	{
		pageContents = aoggettobulk;
	}

	protected void setPageContents(ActionContext actioncontext, OggettoBulk aoggettobulk[])
		throws BusinessProcessException
	{
		if(selectionListener != null)
			super.selection.setSelection(currentPage * pageSize, pageSize, selectionListener.getSelection(actioncontext, aoggettobulk, super.selection.getSelection(currentPage * pageSize, pageSize)));
		setPageContents(aoggettobulk);
	}

	public void setPageFrameSize(int i)
	{
		pageFrameSize = i;
	}

	public void setPageSize(int i)
		throws RemoteException
	{
		pageSize = i;
		if(pagedIterator != null)
			pagedIterator.setPageSize(pageSize);
	}

	public Selection setSelection(ActionContext actioncontext)
	{
		super.selection.setSelection(actioncontext, "mainTable", currentPage * pageSize, pageSize);
		return super.selection;
	}

	public void setSelectionListener(ActionContext actioncontext, SelectionListener selectionlistener)
		throws BusinessProcessException
	{
		selectionListener = selectionlistener;
		if(selectionListener != null)
			selectionListener.initializeSelection(actioncontext);
	}

	public int size()
	{
		return elementsCount;
	}

	public void writeHTMLNavigator(JspWriter jspwriter)
		throws IOException, ServletException
	{
		Button abutton[] = getNavigatorToolbar();
		jspwriter.println("<div class=\"Toolbar\">");
		jspwriter.println("<table cellspacing=\"0\" cellpadding=\"0\">");
		jspwriter.println("<tr align=center valign=middle>");
		jspwriter.print("<td>");
		abutton[0].write(jspwriter, this);
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[1].write(jspwriter, this);
		jspwriter.println("</td>");
		getLastPage();
		for(int i = getFirstPage(); i < getLastPage(); i++)
		{
			jspwriter.print("<td width=\"16\">");
			if(getCurrentPage() != i)
				JSPUtils.button(jspwriter, null, String.valueOf(i), "javascript:submitForm('doGotoPage(" + i + ")')");
			else
				JSPUtils.button(jspwriter, null, String.valueOf(i), null, "background: Highlight;color: HighlightText;");
			jspwriter.println("</td>");
		}

		jspwriter.print("<td>");
		abutton[2].write(jspwriter, this);
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[3].write(jspwriter, this);
		jspwriter.println("</td>");
		for(int j = 4; j < abutton.length; j++)
		{
			jspwriter.print("<td>");
			abutton[j].write(jspwriter, this);
			jspwriter.println("</td>");
		}

		jspwriter.println("</tr>");
		jspwriter.println("</table>");
		jspwriter.println("</div>");
	}

	public void writeHTMLTable(PageContext pagecontext, String s, String s1) throws IOException, ServletException{
		Object bp;
		pagecontext.getOut();
		table.setSelection(super.selection);
		table.setRows(new ArrayEnumeration(pageContents));
		if (getClass().getName().equalsIgnoreCase(SelezionatoreListaBP.class.getName()))
			bp = getParent();  
		else
			bp = this;
		Dictionary hiddenColumns = null;
		if (pagecontext.getSession().getAttribute("UserContext") != null){
			UserContext userContext = (UserContext) pagecontext.getSession().getAttribute("UserContext");
			hiddenColumns = userContext.getHiddenColumns();
		}
		table.writeScrolledTable(bp, pagecontext.getOut(), s, s1, getFieldValidationMap(), currentPage * pageSize, mostraHideColumns, hiddenColumns, getPath());
	}
	
	public boolean isHiddenColumnButtonHidden(){
		return hiddenColumnButtonHidden;
	}
	
	public boolean isMostraHideColumns() {
		return mostraHideColumns;
	}

	public void setMostraHideColumns(boolean mostraHideColumns) {
		this.mostraHideColumns = mostraHideColumns;
	}

	public void setHiddenColumnButtonHidden(boolean hiddenColumnButtonHidden) {
		this.hiddenColumnButtonHidden = hiddenColumnButtonHidden;
	}
	
	private RemoteIterator iterator;
	private Dictionary columns;
	private int currentPage;
	private Forward forward;
	private OggettoBulk pageContents[];
	private int firstPage;
	private int pageFrameSize;
	private int pageSize;
	private int pageCount;
	private int elementsCount;
	private RemotePagedIterator pagedIterator;
	protected Table table;
	private Button navigatorToolbar[];
	private BulkInfo bulkInfo;
	private ObjectReplacer objectReplacer;
	private SelectionListener selectionListener;
	private boolean mostraHideColumns = false; 
	private boolean hiddenColumnButtonHidden = true;

}