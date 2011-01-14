package it.cnr.jada.comp;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDException

public class CRUDValidationException extends CRUDException
    implements Serializable
{

    public CRUDValidationException()
    {
    }

    public CRUDValidationException(String s)
    {
        super(s);
    }

    public CRUDValidationException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public CRUDValidationException(String s, Throwable throwable, OggettoBulk oggettobulk)
    {
        super(s, throwable, oggettobulk);
    }

    public CRUDValidationException(Throwable throwable)
    {
        super(throwable);
    }

    public CRUDValidationException(Throwable throwable, OggettoBulk oggettobulk)
    {
        super(throwable, oggettobulk);
    }
}