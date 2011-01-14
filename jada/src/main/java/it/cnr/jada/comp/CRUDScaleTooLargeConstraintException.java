package it.cnr.jada.comp;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDTooLargeConstraintException, CRUDConstraintException

public class CRUDScaleTooLargeConstraintException extends CRUDTooLargeConstraintException
{

    public CRUDScaleTooLargeConstraintException(String s, OggettoBulk oggettobulk, int i)
    {
        super(s, oggettobulk, i);
    }

    public String getUserMessage()
    {
        StringBuffer stringbuffer = new StringBuffer();
        FieldProperty fieldproperty = getFormFieldProperty();
        stringbuffer.append("Il campo \"");
        stringbuffer.append(fieldproperty != null && fieldproperty.getLabel() != null ? fieldproperty.getLabel() : getPropertyName());
        stringbuffer.append("\" ha troppe cifre decimali. La massima lunghezza \350 ");
        stringbuffer.append(getMaxLength());
        stringbuffer.append(" cifre decimali.");
        return stringbuffer.toString();
    }
}