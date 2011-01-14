package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            NestedFormController, FormController

public class CompoundPropertyController extends NestedFormController
    implements Serializable
{

    public CompoundPropertyController(String s, Class class1, String s1, FormController formcontroller)
    {
        super(s, formcontroller);
        propertyName = s1;
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
    }

    public BulkInfo getBulkInfo()
    {
        return bulkInfo;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void resync(ActionContext actioncontext)
    {
        try
        {
            setModel(actioncontext, (OggettoBulk)Introspector.getPropertyValue(getParentModel(), propertyName));
            super.resync(actioncontext);
        }
        catch(Exception exception)
        {
            throw new IntrospectionError(exception);
        }
    }

    public void setPropertyName(String s)
    {
        propertyName = s;
    }

    private String propertyName;
    private final BulkInfo bulkInfo;
    private final Class modelClass;
}