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
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.servlet.jsp.JspWriter;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.action:
//            FormBP, FormController, FormField, ModelController, 
//            AbstractPrintBP, CRUDController, SearchProvider

public abstract class BulkBP extends FormBP
        implements Serializable, FormController {
    private OggettoBulk model;
    private boolean dirty;
    private Map childrenController;
    private String printbp;
    private boolean editable;
    private int printServerPriority;
    private boolean inputReadonly;

    public BulkBP() {
        childrenController = new HashMap();
    }

    public BulkBP(String s) {
        super(s);
        childrenController = new HashMap();
        editable = s != null && s.indexOf('M') >= 0;
    }

    public void addChildController(FormController formcontroller) {
        childrenController.put(formcontroller.getControllerName(), formcontroller);
    }

    protected boolean basicFillModel(ActionContext actioncontext)
            throws FillException {
        if (getModel() == null) {
            return false;
        } else {
            getModel().setUser(actioncontext.getUserContext().getUser());
            return getModel().fillFromActionContext(actioncontext, "main", getStatus(), getFieldValidationMap());
        }
    }

    protected void basicSetModel(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        inputReadonly = oggettobulk instanceof ROWrapper;
        if (oggettobulk instanceof MTUWrapper) {
            MTUWrapper mtuwrapper = (MTUWrapper) oggettobulk;
            setMessage(mtuwrapper.getMtu().getMessage());
            oggettobulk = mtuwrapper.getBulk();
        }
        if (model == oggettobulk) {
            return;
        } else {
            model = oggettobulk;
            return;
        }
    }

    public void completeSearchTool(ActionContext actioncontext, OggettoBulk oggettobulk, FieldProperty fieldproperty)
            throws BusinessProcessException, ValidationException {
        if (fieldproperty.getInputTypeIndex() == 5 && fieldproperty.isCompleteOnSave())
            try {
                Object obj = fieldproperty.getValueFrom(oggettobulk);
                if ((obj instanceof OggettoBulk) && ((OggettoBulk) obj).getCrudStatus() != OggettoBulk.NORMAL) {
                    OggettoBulk oggettobulk1 = (OggettoBulk) obj;
                    boolean flag = true;
                    for (Enumeration enumeration = oggettobulk1.getBulkInfo().getFindFieldProperties(); enumeration.hasMoreElements(); )
                        if (((FieldProperty) enumeration.nextElement()).getValueFrom(oggettobulk1) != null) {
                            flag = false;
                            break;
                        }

                    if (flag)
                        return;
                    RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk1, oggettobulk, fieldproperty.getProperty());
                    try {
                        int i = remoteiterator.countElements();
                        if (i == 0)
                            throw new ValidationException("La ricerca non ha fornito nessun risultato per il campo " + fieldproperty.getLabel(), fieldproperty.getProperty());
                        if (i == 1)
                            fieldproperty.setValueIn(oggettobulk, remoteiterator.nextElement());
                        else
                            throw new ValidationException("La ricerca ha fornito pi\371 di un risultato per il campo " + fieldproperty.getLabel(), fieldproperty.getProperty());
                    } finally {
                        EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                    }
                }
            } catch (InvocationTargetException invocationtargetexception) {
                handleException(invocationtargetexception);
            } catch (IntrospectionException introspectionexception) {
                handleException(introspectionexception);
            } catch (RemoteException remoteexception) {
                handleException(remoteexception);
            }
    }

    public void completeSearchTool(ActionContext actioncontext, FormController formcontroller, String s)
            throws BusinessProcessException, ValidationException {
        FormField formfield = formcontroller.getFormField(s);
        if (formfield != null)
            completeSearchTool(actioncontext, formfield.getModel(), formfield.getField());
    }

    public void completeSearchTools(ActionContext actioncontext, FormController formcontroller)
            throws BusinessProcessException, ValidationException {
        OggettoBulk oggettobulk = formcontroller.getModel();
        for (Enumeration enumeration = formcontroller.getBulkInfo().getFieldProperties(); enumeration.hasMoreElements(); completeSearchTool(actioncontext, oggettobulk, (FieldProperty) enumeration.nextElement()))
            ;
        FormController formcontroller1;
        for (Enumeration enumeration1 = formcontroller.getChildrenController(); enumeration1.hasMoreElements(); completeSearchTools(actioncontext, formcontroller1))
            formcontroller1 = (FormController) enumeration1.nextElement();

    }

    public boolean fillModel(ActionContext actioncontext)
            throws FillException {
        boolean flag = basicFillModel(actioncontext);
        for (Iterator iterator = childrenController.values().iterator(); iterator.hasNext(); )
            try {
                flag = ((FormController) iterator.next()).fillModel(actioncontext) || flag;
            } catch (ClassCastException _ex) {
            }

        if (flag)
            setDirty(true);
        return flag;
    }

    public abstract RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws BusinessProcessException;

    public BulkInfo getBulkInfo() {
        return getModel().getBulkInfo();
    }

    public FormController getChildController(String s) {
        return (FormController) childrenController.get(s);
    }

    public Enumeration getChildrenController() {
        return Collections.enumeration(childrenController.values());
    }

    public FormController getController() {
        return this;
    }

    public String getControllerName() {
        return "main";
    }

    public FormField getFormField(String s) {
        int i = s.indexOf('.');
        if (i > 0)
            return getChildController(s.substring(0, i)).getFormField(s.substring(i + 1));
        else
            return new FormField(this, getBulkInfo().getFieldProperty(s), getModel());
    }

    public String getInputPrefix() {
        return "main";
    }

    public OggettoBulk getModel() {
        return model;
    }

    public FormController getParentController() {
        return null;
    }

    public String getPrintbp() {
        return printbp;
    }

    public void setPrintbp(String s) {
        printbp = s;
    }

    public int getPrintServerPriority() {
        return printServerPriority;
    }

    public void setPrintServerPriority(int i) {
        printServerPriority = i;
    }

    public SearchProvider getSearchProvider(OggettoBulk oggettobulk, String s) {
        return new ContextSearchProvider(oggettobulk, s);
    }

    public int getStatus() {
        return 2;
    }

    protected void init(Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        setPrintbp(config.getInitParameter("printbp"));
        try {
            setPrintServerPriority(Integer.parseInt(config.getInitParameter("printServerPriority")));
        } catch (NumberFormatException _ex) {
        }
        super.init(config, actioncontext);
    }

    protected void initializePrintBP(ActionContext actioncontext, BusinessProcess businessprocess) {
        if (businessprocess instanceof AbstractPrintBP)
            initializePrintBP(actioncontext, (AbstractPrintBP) businessprocess);
    }

    protected void initializePrintBP(ActionContext actioncontext, AbstractPrintBP abstractprintbp) {
        initializePrintBP(abstractprintbp);
    }

    protected void initializePrintBP(AbstractPrintBP abstractprintbp) {
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean flag) {
        dirty = flag;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean flag) {
        editable = flag;
    }

    public boolean isEditing() {
        return getStatus() == 2;
    }

    public boolean isInputReadonly() {
        return inputReadonly;
    }

    public boolean isInserting() {
        return getStatus() == 1;
    }

    public boolean isSearching() {
        return getStatus() == 0;
    }

    public boolean isViewing() {
        return getStatus() == 5;
    }

    public void resetChildren(ActionContext actioncontext)
            throws BusinessProcessException {
        for (Iterator iterator = childrenController.values().iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            if (obj instanceof CRUDController)
                ((CRUDController) obj).reset(actioncontext);
        }

    }

    public void resync(ActionContext actioncontext)
            throws BusinessProcessException {
        resyncChildren(actioncontext);
    }

    public void resyncChildren(ActionContext actioncontext)
            throws BusinessProcessException {
        for (Iterator iterator = childrenController.values().iterator(); iterator.hasNext(); ((FormController) iterator.next()).resync(actioncontext))
            ;
    }

    public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        basicSetModel(actioncontext, oggettobulk);
        resyncChildren(actioncontext);
    }

    public void validate(ActionContext actioncontext)
            throws ValidationException {
        getModel().validate();
        FormController formcontroller;
        for (Enumeration enumeration = getChildrenController(); enumeration.hasMoreElements(); formcontroller.validate(actioncontext))
            formcontroller = (FormController) enumeration.nextElement();

    }

    public void writeForm(JspWriter jspwriter)
            throws IOException {
        getBulkInfo().writeForm(jspwriter, getModel(), null, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeForm(JspWriter jspwriter, String s)
            throws IOException {
        getBulkInfo().writeForm(jspwriter, getModel(), s, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s)
            throws IOException {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), null, s, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1)
            throws IOException {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j)
            throws IOException {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), i, j, getStatus(), isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s)
            throws IOException {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, isInputReadonly(), this.getParentRoot().isBootstrap() ? "form-control" : "FormInput", null, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1)
            throws IOException {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String cssClass, String s3)
            throws IOException {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag || isInputReadonly(), Optional.ofNullable(cssClass).orElseGet(() -> this.getParentRoot().isBootstrap() ? "form-control" : "FormInput"), s3, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormInputByStatus(JspWriter jspwriter, String s)
            throws IOException {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, getStatus() == 2 || isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s)
            throws IOException {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), null, s, null, this.getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1)
            throws IOException {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, null, this.getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
            throws IOException {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, s2, this.getParentRoot().isBootstrap());
    }

    class ContextSearchProvider
            implements Serializable, SearchProvider {

        private final OggettoBulk context;
        private final String property;
        ContextSearchProvider(OggettoBulk oggettobulk, String s) {
            property = s;
            context = oggettobulk;
        }

        public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
                throws BusinessProcessException {
            return find(actioncontext, compoundfindclause, oggettobulk, context, property);
        }
    }
}