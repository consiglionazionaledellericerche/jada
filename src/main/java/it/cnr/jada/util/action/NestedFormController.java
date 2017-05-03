package it.cnr.jada.util.action;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.servlet.jsp.JspWriter;

// Referenced classes of package it.cnr.jada.util.action:
//            FormController, FormField, ModelController, CRUDController

public abstract class NestedFormController
    implements Serializable, FormController
{

    private OggettoBulk model;
    private final String name;
    private FormController parent;
    private Map children;
    private String inputPrefix;
    private boolean readonly;
    private boolean dirty;
    private final FieldValidationMap fieldValidationMap = new FieldValidationMap();

    public NestedFormController(String s, FormController formcontroller)
    {
        children = new HashMap();
        readonly = false;
        inputPrefix = name = s;
        addTo(formcontroller);
    }

    public void addChildController(FormController formcontroller)
    {
        children.put(formcontroller.getControllerName(), formcontroller);
    }

    protected void addTo(FormController formcontroller)
    {
        setParentController(formcontroller);
        if(formcontroller instanceof FormController)
            inputPrefix = ((FormController)formcontroller).getInputPrefix() + "." + getControllerName();
        formcontroller.addChildController(this);
    }

    protected boolean basicFillModel(ActionContext actioncontext)
        throws FillException
    {
        if(getModel() != null)
        {
            getModel().setUser(actioncontext.getUserInfo().getUserid());
            return getModel().fillFromActionContext(actioncontext, getInputPrefix(), 2, getFieldValidationMap());
        } else
        {
            return false;
        }
    }

    protected void clearFilter(ActionContext actioncontext)
    {
    }

    public boolean fillModel(ActionContext actioncontext)
        throws FillException
    {
        boolean flag = basicFillModel(actioncontext);
        for(Iterator iterator = children.values().iterator(); iterator.hasNext();)
            try
            {
                flag = ((FormController)iterator.next()).fillModel(actioncontext) || flag;
            }
            catch(ClassCastException _ex) { }

        if(flag)
            setDirty(true);
        return flag;
    }

    public abstract BulkInfo getBulkInfo();

    public FormController getChildController(String s)
    {
        return (FormController)children.get(s);
    }

    public Enumeration getChildrenController()
    {
        return Collections.enumeration(children.values());
    }

    public String getControllerName()
    {
        return name;
    }

    public final FieldValidationMap getFieldValidationMap()
    {
        return ((FormController)parent).getFieldValidationMap();
    }

    public FormField getFormField(String s)
    {
        int i = s.indexOf('.');
        if(i > 0)
            return ((FormController)getChildController(s.substring(0, i))).getFormField(s.substring(i + 1));
        else
            return new FormField(this, getBulkInfo().getFieldProperty(s), getModel());
    }

    public String getInputPrefix()
    {
        return inputPrefix;
    }

    public OggettoBulk getModel()
    {
        return model;
    }

    public FormController getParentController()
    {
        return parent;
    }

    public OggettoBulk getParentModel()
    {
        return parent.getModel();
    }

    public int getStatus()
    {
        return parent.getStatus();
    }

    public UserTransaction getUserTransaction()
    {
        return parent.getUserTransaction();
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public boolean isInputReadonly()
    {
        return isReadonly() || getParentController().isInputReadonly();
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public void reset(ActionContext actioncontext)
    {
        resetChildren(actioncontext);
    }

    public void resetChildren(ActionContext actioncontext)
    {
        for(Iterator iterator = children.values().iterator(); iterator.hasNext();)
        {
            FormController formcontroller = (FormController)iterator.next();
            if(formcontroller instanceof CRUDController)
                ((CRUDController)formcontroller).reset(actioncontext);
        }

    }

    public void resync(ActionContext actioncontext)
        throws BusinessProcessException
    {
        resyncChildren(actioncontext);
    }

    public void resyncChildren(ActionContext actioncontext)
        throws BusinessProcessException
    {
        for(Iterator iterator = children.values().iterator(); iterator.hasNext(); ((FormController)iterator.next()).resync(actioncontext));
    }

    public void setDirty(boolean flag)
    {
        dirty = flag;
        if(dirty)
            parent.setDirty(true);
    }

    protected void setModel(ActionContext actioncontext, OggettoBulk oggettobulk)
    {
        setDirty(false);
        model = oggettobulk;
    }

    protected void setParentController(FormController formcontroller)
    {
        parent = formcontroller;
    }

    public void setReadonly(boolean flag)
    {
        readonly = flag;
    }

    public void validate(ActionContext actioncontext)
        throws ValidationException
    {
        if(getModel() != null)
            validate(actioncontext, getModel());
        FormController formcontroller;
        for(Enumeration enumeration = getChildrenController(); enumeration.hasMoreElements(); formcontroller.validate(actioncontext))
            formcontroller = (FormController)enumeration.nextElement();

    }

    protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws ValidationException
    {
        oggettobulk.validate(getParentModel());
    }

    public void writeForm(JspWriter jspwriter)
        throws IOException
    {
        getBulkInfo().writeForm(jspwriter, getModel(), null, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeForm(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeForm(this, jspwriter, getModel(), s, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), null, s, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), i, j, getStatus(), isInputReadonly(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag || isInputReadonly(), s2, s3, getInputPrefix(), getStatus(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormInputByStatus(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), null, s, null, ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, null, ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, s2, ((BusinessProcess)this.getParentController()).getParentRoot().isBootstrap());
    }
}