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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ROWrapper;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.jsp.Button;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.*;
import java.text.DateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Optional;

// Referenced classes of package it.cnr.jada.util.action:
//            BulkBP, FormBP, CRUDController, SearchProvider

public abstract class CRUDBP extends BulkBP
        implements Serializable {
    private final SearchProvider searchProvider;
    private int status;
    private boolean bringBack;
    private OggettoBulk bulkClone;
    private boolean editOnly;
    private boolean saveOnBringBack;

    public CRUDBP() {
        status = 1;
        bulkClone = null;
        editOnly = false;
        saveOnBringBack = false;
        searchProvider = new MainSearchProvider();
    }

    public CRUDBP(String s) {
        super(s);
        status = 1;
        bulkClone = null;
        editOnly = false;
        saveOnBringBack = false;
        searchProvider = new MainSearchProvider();
        bringBack = s != null && s.indexOf('R') >= 0;
        if (bringBack)
            setEditOnly(s != null && s.indexOf('S') >= 0);
        setSaveOnBringBack(s != null && s.indexOf('W') >= 0);
    }

    protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag)
            throws BusinessProcessException {
        try {
            if (flag)
                oggettobulk = initializeModelForEdit(actioncontext, oggettobulk);
            setStatus(!isEditable() || (oggettobulk instanceof ROWrapper) ? 5 : 2);
            setModel(actioncontext, oggettobulk);
            setDirty(false);
            resetTabs(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk cloneBulk(OggettoBulk oggettobulk)
            throws IOException, ClassNotFoundException {
        if (oggettobulk == null) {
            bulkClone = null;
            return null;
        }
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        ObjectOutputStream objectoutputstream = null;
        try {
            objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);
            objectoutputstream.writeObject(oggettobulk);
            objectoutputstream.flush();
            objectoutputstream.close();
        } catch (IOException ioexception) {
            if (objectoutputstream != null)
                objectoutputstream.close();
            else
                bytearrayoutputstream.close();
            throw ioexception;
        }
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(bytearrayoutputstream.toByteArray());
        ObjectInputStream objectinputstream = null;
        try {
            objectinputstream = new ObjectInputStream(bytearrayinputstream);
            OggettoBulk oggettobulk1 = (OggettoBulk) objectinputstream.readObject();
            objectinputstream.close();
            bulkClone = oggettobulk;
            return oggettobulk1;
        } catch (ClassNotFoundException classnotfoundexception) {
            objectinputstream.close();
            throw classnotfoundexception;
        } catch (IOException ioexception1) {
            if (objectinputstream != null)
                objectinputstream.close();
            else
                bytearrayinputstream.close();
            throw ioexception1;
        }
    }

    public void closeForm(PageContext pagecontext)
            throws IOException, ServletException {
        final JspWriter out = pagecontext.getOut();
        out.println("<tr><td>");
        Optional.ofNullable(getModel())
                .filter(oggettoBulk -> Optional.ofNullable(oggettoBulk.getUtcr()).isPresent())
                .ifPresent(oggettoBulk -> {
                    try {
                        if (HttpActionContext.isFromBootstrap(pagecontext)) {
                            out.print("<div class=\"footer-created-info alert alert-info\">");
                            out.print("Creato il ");
                            out.print(DateTimeFormatter.ofPattern("dd MMMM yyyy 'alle' HH:mm")
                                    .format(oggettoBulk.getDacr().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                    )
                            );
                            out.print(" da ");
                            out.print(oggettoBulk.getUtcr());
                            out.print(" modificato il ");
                            out.print(DateTimeFormatter.ofPattern("dd MMMM yyyy 'alle' HH:mm")
                                    .format(oggettoBulk.getDuva().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                    )
                            );
                            out.print(" da ");
                            out.print(oggettoBulk.getUtuv());
                            out.println("</div>");
                        } else {
                            out.print("<div style=\"border: 1px inset\">");
                            out.print("<span class=\"FormLabel\">Ultima variazione: </span><span class=\"FormInput\">");
                            out.print(DateFormat.getDateTimeInstance().format(getModel().getDuva()));
                            out.print(" (");
                            out.print(getModel().getUtuv());
                            out.print(')');
                            out.println("</span></div>");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        out.println("</td></tr>");
        super.closeForm(pagecontext);
    }

    public abstract void create(ActionContext actioncontext)
            throws BusinessProcessException;

    public abstract OggettoBulk createEmptyModel(ActionContext actioncontext)
            throws BusinessProcessException;

    public abstract OggettoBulk createEmptyModelForFreeSearch(ActionContext actioncontext)
            throws BusinessProcessException;

    public abstract OggettoBulk createEmptyModelForSearch(ActionContext actioncontext)
            throws BusinessProcessException;

    protected Button[] createToolbar() {
        Button[] abutton = new Button[10];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startLastSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
        return abutton;
    }

    public abstract void delete(ActionContext actioncontext)
            throws BusinessProcessException;

    public void edit(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        edit(actioncontext, oggettobulk, true);
    }

    public void edit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag)
            throws BusinessProcessException {
        try {
            OggettoBulk oggettobulk1 = cloneBulk(oggettobulk);
            oggettobulk1.setUser(actioncontext.getUserInfo().getUserid());
            basicEdit(actioncontext, oggettobulk1, flag);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public abstract RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
            throws BusinessProcessException;

    public abstract RemoteIterator lastFind(ActionContext actioncontext)
            throws BusinessProcessException;

    public OggettoBulk getBringBackModel() {
        return getModel();
    }

    public OggettoBulk getBringBackModel(ActionContext actioncontext)
            throws BusinessProcessException {
        return getBringBackModel();
    }

    public OggettoBulk getBulkClone() {
        return bulkClone;
    }

    public String getFormTitle() {
        StringBuffer stringbuffer = new StringBuffer(super.getFormTitle());
        stringbuffer.append(" - ");
        switch (getStatus()) {
            case INSERT: // '\001'
                stringbuffer.append("Inserimento");
                break;

            case EDIT: // '\002'
                stringbuffer.append("Modifica");
                break;

            case SEARCH: // '\0'
                stringbuffer.append("Ricerca");
                break;

            case VIEW: // '\005'
                stringbuffer.append("Visualizza");
                break;
        }
        return stringbuffer.toString();
    }

    public String getFreeSearchSet() {
        return null;
    }

    public BulkInfo getSearchBulkInfo() {
        return getBulkInfo();
    }

    public final SearchProvider getSearchProvider() {
        return searchProvider;
    }

    public Dictionary getSearchResultColumns() {
        if (getSearchResultColumnSet() == null)
            return getModel().getBulkInfo().getColumnFieldPropertyDictionary();
        else
            return getModel().getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet());
    }

    public abstract String getSearchResultColumnSet();

    protected String getStandardAction() {
        return "CRUDAction";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int i) {
        status = i;
    }

    protected void handleUserTransactionTimeout(ActionContext actioncontext)
            throws BusinessProcessException {
        closeAllChildren();
        actioncontext.setBusinessProcess(this);
        if (isInserting())
            reset(actioncontext);
        else if (isSearching())
            resetForSearch(actioncontext);
        else
            edit(actioncontext, getModel());
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        initialize(actioncontext);
    }

    protected void initialize(ActionContext actioncontext)
            throws BusinessProcessException {
        if (isEditable())
            reset(actioncontext);
        else
            resetForSearch(actioncontext);
    }

    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return oggettobulk.initializeForEdit(this, actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk initializeModelForFreeSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return oggettobulk.initializeForFreeSearch(this, actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return oggettobulk.initializeForInsert(this, actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return oggettobulk.initializeForSearch(this, actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public boolean isBringBack() {
        return bringBack;
    }

    public void setBringBack(boolean flag) {
        bringBack = flag;
    }

    public boolean isBringbackButtonEnabled() {
        return isEditing() || isInserting();
    }

    public boolean isBringbackButtonHidden() {
        return !isBringBack();
    }

    public boolean isDeleteButtonEnabled() {
        return isEditable() && isEditing() && !isDirty();
    }

    public boolean isDeleteButtonHidden() {
        return isEditOnly();
    }

    public boolean isEditOnly() {
        return editOnly;
    }

    public void setEditOnly(boolean flag) {
        editOnly = flag;
    }

    public boolean isFreeSearchButtonHidden() {
        return isEditOnly();
    }

    public boolean isInputReadonly() {
        return super.isInputReadonly() || !isEditable() && !isSearching();
    }

    public boolean isNewButtonEnabled() {
        return isEditable();
    }

    public boolean isNewButtonHidden() {
        return isEditOnly();
    }

    public boolean isNewModel() {
        return isInserting();
    }

    public boolean isPrintButtonHidden() {
        return isEditOnly() || getPrintbp() == null;
    }

    public boolean isSaveButtonEnabled() {
        return isEditable() && (isEditing() || isInserting());
    }

    public boolean isSaveButtonHidden() {
        return isSaveOnBringBack() && isEditOnly();
    }

    public boolean isSaveOnBringBack() {
        return saveOnBringBack;
    }

    public void setSaveOnBringBack(boolean flag) {
        saveOnBringBack = flag;
    }

    public boolean isSearchButtonHidden() {
        return isEditOnly() || isSearching();
    }

    public boolean isStartSearchButtonHidden() {
        return isEditOnly() || !isSearching();
    }

    public boolean isUndoBringBackButtonEnabled() {
        return isEditable() && (isEditing() || isInserting()) && isEditOnly();
    }

    public boolean isUndoBringBackButtonHidden() {
        return !isEditOnly();
    }

    public void reset(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            rollbackUserTransaction();
            setStatus(1);
            basicSetModel(actioncontext, createEmptyModel(actioncontext));
            setDirty(false);
            resetChildren(actioncontext);
            resetTabs(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public void reset(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            rollbackUserTransaction();
            setStatus(1);
            basicSetModel(actioncontext, oggettobulk);
            setDirty(false);
            resetChildren(actioncontext);
            resetTabs(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public void resetForSearch(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            rollbackUserTransaction();
            setStatus(0);
            basicSetModel(actioncontext, createEmptyModelForSearch(actioncontext));
            setDirty(false);
            resetChildren(actioncontext);
            resetTabs(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public void save(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException {
        completeSearchTools(actioncontext, this);
        validate(actioncontext);
        saveChildren(actioncontext);
        if (isInserting()) {
            create(actioncontext);
            if (getMessage() == null)
                setMessage(INFO_MESSAGE, "Creazione eseguita in modo corretto.");
        } else if (isEditing()) {
            update(actioncontext);
            if (getMessage() == null)
                setMessage(INFO_MESSAGE, "Salvataggio eseguito in modo corretto.");
        }
        commitUserTransaction();
        try {
            basicEdit(actioncontext, getModel(), true);
        } catch (BusinessProcessException businessprocessexception) {
            setModel(actioncontext, null);
            setDirty(false);
            throw businessprocessexception;
        }
    }

    public void saveChildren(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException {
        for (Enumeration enumeration = getChildrenController(); enumeration.hasMoreElements(); ) {
            Object obj = enumeration.nextElement();
            if (obj instanceof CRUDController)
                ((CRUDController) obj).save(actioncontext);
        }

    }

    public void setBringBackClone(OggettoBulk oggettobulk) {
        bulkClone = oggettobulk;
    }

    public abstract void update(ActionContext actioncontext)
            throws BusinessProcessException;

    class MainSearchProvider
            implements Serializable, SearchProvider {

        MainSearchProvider() {
        }

        public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
                throws BusinessProcessException {
            return find(actioncontext, compoundfindclause, oggettobulk);
        }
    }
}