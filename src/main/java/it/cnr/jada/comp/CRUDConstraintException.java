package it.cnr.jada.comp;

import it.cnr.jada.bulk.*;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDValidationException, CRUDException

public abstract class CRUDConstraintException extends CRUDValidationException
    implements Serializable
{

    public CRUDConstraintException(String s, OggettoBulk oggettobulk)
    {
        super(s, null, oggettobulk);
        propertyName = s;
    }

    public FieldProperty getFormFieldProperty()
    {
        String s = propertyName;
        do
        {
            FieldProperty fieldproperty = getBulk().getBulkInfo().getFieldPropertyByProperty(s);
            if(fieldproperty != null)
                return fieldproperty;
            int i = s.lastIndexOf('.');
            if(i < 1)
                return null;
            s = s.substring(0, i);
        } while(true);
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public abstract String getUserMessage();

    private final String propertyName;
}