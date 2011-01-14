package it.cnr.jada.util.action;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;
import javax.servlet.jsp.JspWriter;

// Referenced classes of package it.cnr.jada.util.action:
//            FormBP, FormController, FormField, ModelController, 
//            AbstractPrintBP, CRUDController, SearchProvider

public abstract class BulkBP extends FormBP
    implements Serializable, FormController
{
    class ContextSearchProvider
        implements Serializable, SearchProvider
    {

        public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
            throws BusinessProcessException
        {
            return find(actioncontext, compoundfindclause, oggettobulk, context, property);
        }

        private final OggettoBulk context;
        private final String property;

        ContextSearchProvider(OggettoBulk oggettobulk, String s)
        {
            property = s;
            context = oggettobulk;
        }
    }


    public BulkBP()
    {
        childrenController = new HashMap();
    }

    public BulkBP(String s)
    {
        super(s);
        childrenController = new HashMap();
        editable = s != null && s.indexOf('M') >= 0;
    }

    public void addChildController(FormController formcontroller)
    {
        childrenController.put(formcontroller.getControllerName(), formcontroller);
    }

    protected boolean basicFillModel(ActionContext actioncontext)
        throws FillException
    {
        if(getModel() == null)
        {
            return false;
        } else
        {
            getModel().setUser(actioncontext.getUserContext().getUser());
            return getModel().fillFromActionContext(actioncontext, "main", getStatus(), getFieldValidationMap());
        }
    }

    protected void basicSetModel(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        inputReadonly = oggettobulk instanceof ROWrapper;
        if(oggettobulk instanceof MTUWrapper)
        {
            MTUWrapper mtuwrapper = (MTUWrapper)oggettobulk;
            setMessage(mtuwrapper.getMtu().getMessage());
            oggettobulk = mtuwrapper.getBulk();
        }
        if(model == oggettobulk)
        {
            return;
        } else
        {
            model = oggettobulk;
            return;
        }
    }

    public void completeSearchTool(ActionContext actioncontext, OggettoBulk oggettobulk, FieldProperty fieldproperty)
        throws BusinessProcessException, ValidationException
    {
        if(fieldproperty.getInputTypeIndex() == 5 && fieldproperty.isCompleteOnSave())
            try
            {
                Object obj = fieldproperty.getValueFrom(oggettobulk);
                if((obj instanceof OggettoBulk) && ((OggettoBulk)obj).getCrudStatus() != 5)
                {
                    OggettoBulk oggettobulk1 = (OggettoBulk)obj;
                    boolean flag = true;
                    for(Enumeration enumeration = oggettobulk1.getBulkInfo().getFindFieldProperties(); enumeration.hasMoreElements();)
                        if(((FieldProperty)enumeration.nextElement()).getValueFrom(oggettobulk1) != null)
                        {
                            flag = false;
                            break;
                        }

                    if(flag)
                        return;
                    RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk1, oggettobulk, fieldproperty.getProperty());
                    try
                    {
                        int i = remoteiterator.countElements();
                        if(i == 0)
                            throw new ValidationException("La ricerca non ha fornito nessun risultato per il campo " + fieldproperty.getLabel(), fieldproperty.getProperty());
                        if(i == 1)
                            fieldproperty.setValueIn(oggettobulk, (OggettoBulk)remoteiterator.nextElement());
                        else
                            throw new ValidationException("La ricerca ha fornito pi\371 di un risultato per il campo " + fieldproperty.getLabel(), fieldproperty.getProperty());
                    }
                    finally
                    {
                        EJBCommonServices.closeRemoteIterator(remoteiterator);
                    }
                }
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                handleException(invocationtargetexception);
            }
            catch(IntrospectionException introspectionexception)
            {
                handleException(introspectionexception);
            }
            catch(RemoteException remoteexception)
            {
                handleException(remoteexception);
            }
    }

    public void completeSearchTool(ActionContext actioncontext, FormController formcontroller, String s)
        throws BusinessProcessException, ValidationException
    {
        FormField formfield = formcontroller.getFormField(s);
        if(formfield != null)
            completeSearchTool(actioncontext, formfield.getModel(), formfield.getField());
    }

    public void completeSearchTools(ActionContext actioncontext, FormController formcontroller)
        throws BusinessProcessException, ValidationException
    {
        OggettoBulk oggettobulk = formcontroller.getModel();
        for(Enumeration enumeration = formcontroller.getBulkInfo().getFieldProperties(); enumeration.hasMoreElements(); completeSearchTool(actioncontext, oggettobulk, (FieldProperty)enumeration.nextElement()));
        FormController formcontroller1;
        for(Enumeration enumeration1 = formcontroller.getChildrenController(); enumeration1.hasMoreElements(); completeSearchTools(actioncontext, formcontroller1))
            formcontroller1 = (FormController)enumeration1.nextElement();

    }

    public boolean fillModel(ActionContext actioncontext)
        throws FillException
    {
        boolean flag = basicFillModel(actioncontext);
        for(Iterator iterator = childrenController.values().iterator(); iterator.hasNext();)
            try
            {
                flag = ((FormController)iterator.next()).fillModel(actioncontext) || flag;
            }
            catch(ClassCastException _ex) { }

        if(flag)
            setDirty(true);
        return flag;
    }

    public abstract RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws BusinessProcessException;

    public BulkInfo getBulkInfo()
    {
        return getModel().getBulkInfo();
    }

    public FormController getChildController(String s)
    {
        return (FormController)childrenController.get(s);
    }

    public Enumeration getChildrenController()
    {
        return Collections.enumeration(childrenController.values());
    }

    public FormController getController()
    {
        return this;
    }

    public String getControllerName()
    {
        return "main";
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
        return "main";
    }

    public OggettoBulk getModel()
    {
        return model;
    }

    public FormController getParentController()
    {
        return null;
    }

    public String getPrintbp()
    {
        return printbp;
    }

    public int getPrintServerPriority()
    {
        return printServerPriority;
    }

    public SearchProvider getSearchProvider(OggettoBulk oggettobulk, String s)
    {
        return new ContextSearchProvider(oggettobulk, s);
    }

    public int getStatus()
    {
        return 2;
    }

    protected void init(Config config, ActionContext actioncontext)
        throws BusinessProcessException
    {
        setPrintbp(config.getInitParameter("printbp"));
        try
        {
            setPrintServerPriority(Integer.parseInt(config.getInitParameter("printServerPriority")));
        }
        catch(NumberFormatException _ex) { }
        super.init(config, actioncontext);
    }

    protected void initializePrintBP(ActionContext actioncontext, BusinessProcess businessprocess)
    {
        if(businessprocess instanceof AbstractPrintBP)
            initializePrintBP(actioncontext, (AbstractPrintBP)businessprocess);
    }

    protected void initializePrintBP(ActionContext actioncontext, AbstractPrintBP abstractprintbp)
    {
        initializePrintBP(abstractprintbp);
    }

    protected void initializePrintBP(AbstractPrintBP abstractprintbp)
    {
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public boolean isEditing()
    {
        return getStatus() == 2;
    }

    public boolean isInputReadonly()
    {
        return inputReadonly;
    }

    public boolean isInserting()
    {
        return getStatus() == 1;
    }

    public boolean isSearching()
    {
        return getStatus() == 0;
    }

    public boolean isViewing()
    {
        return getStatus() == 5;
    }

    public void resetChildren(ActionContext actioncontext)
        throws BusinessProcessException
    {
        for(Iterator iterator = childrenController.values().iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if(obj instanceof CRUDController)
                ((CRUDController)obj).reset(actioncontext);
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
        for(Iterator iterator = childrenController.values().iterator(); iterator.hasNext(); ((FormController)iterator.next()).resync(actioncontext));
    }

    public void setDirty(boolean flag)
    {
        dirty = flag;
    }

    public void setEditable(boolean flag)
    {
        editable = flag;
    }

    public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        basicSetModel(actioncontext, oggettobulk);
        resyncChildren(actioncontext);
    }

    public void setPrintbp(String s)
    {
        printbp = s;
    }

    public void setPrintServerPriority(int i)
    {
        printServerPriority = i;
    }

    public void validate(ActionContext actioncontext)
        throws ValidationException
    {
        getModel().validate();
        FormController formcontroller;
        for(Enumeration enumeration = getChildrenController(); enumeration.hasMoreElements(); formcontroller.validate(actioncontext))
            formcontroller = (FormController)enumeration.nextElement();

    }

    public void writeForm(JspWriter jspwriter)
        throws IOException
    {
        getBulkInfo().writeForm(jspwriter, getModel(), null, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap());
    }

    public void writeForm(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeForm(jspwriter, getModel(), s, null, null, getInputPrefix(), getStatus(), isInputReadonly(), getFieldValidationMap());
    }

    public void writeFormField(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), null, s, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), 1, 1, getStatus(), isInputReadonly(), getFieldValidationMap());
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j)
        throws IOException
    {
        getBulkInfo().writeFormField(this, jspwriter, getModel(), s, s1, getInputPrefix(), i, j, getStatus(), isInputReadonly(), getFieldValidationMap());
    }

    public void writeFormInput(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap());
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag || isInputReadonly(), s2, s3, getInputPrefix(), getStatus(), getFieldValidationMap());
    }

    public void writeFormInputByStatus(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormInput(jspwriter, getModel(), null, s, getStatus() == 2 || isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap());
    }

    public void writeFormLabel(JspWriter jspwriter, String s)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), null, s, null);
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, null);
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
        throws IOException
    {
        getBulkInfo().writeFormLabel(this, jspwriter, getModel(), s, s1, s2);
    }

    private OggettoBulk model;
    private boolean dirty;
    private Map childrenController;
    private String printbp;
    private boolean editable;
    private int printServerPriority;
    private boolean inputReadonly;
}