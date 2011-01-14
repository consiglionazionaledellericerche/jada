package it.cnr.jada.comp;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDConstraintException

public class CRUDNotNullConstraintException extends CRUDConstraintException
    implements Serializable
{

    public CRUDNotNullConstraintException(String s, OggettoBulk oggettobulk)
    {
        super(s, oggettobulk);
    }

    public String getUserMessage()
    {
        StringBuffer stringbuffer = new StringBuffer();
        FieldProperty fieldproperty = getFormFieldProperty();
        stringbuffer.append("Il campo \"");
        stringbuffer.append(fieldproperty != null && fieldproperty.getLabel() != null ? fieldproperty.getLabel() : getPropertyName());
        stringbuffer.append("\" non pu\362 essere vuoto.");
        return stringbuffer.toString();
    }
}