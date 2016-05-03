package it.cnr.jada.comp;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDConstraintException

public class CRUDTooLargeConstraintException extends CRUDConstraintException
    implements Serializable
{

    public CRUDTooLargeConstraintException(String s, OggettoBulk oggettobulk, int i)
    {
        super(s, oggettobulk);
        maxLength = i;
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    public String getUserMessage()
    {
        StringBuffer stringbuffer = new StringBuffer();
        FieldProperty fieldproperty = getFormFieldProperty();
        stringbuffer.append("Il campo \"");
        stringbuffer.append(fieldproperty != null && fieldproperty.getLabel() != null ? fieldproperty.getLabel() : getPropertyName());
        stringbuffer.append("\" \350 troppo lungo. La massima lunghezza \350 ");
        stringbuffer.append(maxLength);
        stringbuffer.append(" caratteri.");
        return stringbuffer.toString();
    }

    private final int maxLength;
}