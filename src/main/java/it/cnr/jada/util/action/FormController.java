package it.cnr.jada.util.action;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.*;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.jsp.JspWriter;

// Referenced classes of package it.cnr.jada.util.action:
//            ModelController, FormField

public interface FormController
    extends ModelController
{

    public abstract void addChildController(FormController formcontroller);

    public abstract boolean fillModel(ActionContext actioncontext)
        throws FillException;

    public abstract BulkInfo getBulkInfo();

    public abstract FormController getChildController(String s);

    public abstract Enumeration getChildrenController();

    public abstract String getControllerName();

    public abstract FieldValidationMap getFieldValidationMap();

    public abstract FormField getFormField(String s);

    public abstract String getInputPrefix();

    public abstract FormController getParentController();

    public abstract UserTransaction getUserTransaction();

    public abstract void resync(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract void validate(ActionContext actioncontext)
        throws ValidationException;

    public abstract void writeForm(JspWriter jspwriter)
        throws IOException;

    public abstract void writeForm(JspWriter jspwriter, String s)
        throws IOException;

    public abstract void writeFormField(JspWriter jspwriter, String s)
        throws IOException;

    public abstract void writeFormField(JspWriter jspwriter, String s, String s1)
        throws IOException;

    public abstract void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j)
        throws IOException;

    public abstract void writeFormInput(JspWriter jspwriter, String s)
        throws IOException;

    public abstract void writeFormInput(JspWriter jspwriter, String s, String s1)
        throws IOException;

    public abstract void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3)
        throws IOException;

    public abstract void writeFormInputByStatus(JspWriter jspwriter, String s)
        throws IOException;

    public abstract void writeFormLabel(JspWriter jspwriter, String s)
        throws IOException;

    public abstract void writeFormLabel(JspWriter jspwriter, String s, String s1)
        throws IOException;

    public abstract void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
        throws IOException;

    public static final int UNDEFINED = -1;
    public static final int SEARCH = 0;
    public static final int INSERT = 1;
    public static final int EDIT = 2;
    public static final int FREESEARCH = 4;
    public static final int VIEW = 5;
}