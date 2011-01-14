package it.cnr.jada.comp;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDException

public class CRUDReferentialIntegrityException extends CRUDException
    implements Serializable
{

    public CRUDReferentialIntegrityException()
    {
    }

    public CRUDReferentialIntegrityException(String s)
    {
        super(s);
    }

    public CRUDReferentialIntegrityException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public CRUDReferentialIntegrityException(String s, Throwable throwable, OggettoBulk oggettobulk)
    {
        super(s, throwable, oggettobulk);
    }

    public CRUDReferentialIntegrityException(Throwable throwable)
    {
        super(throwable);
    }

    public CRUDReferentialIntegrityException(Throwable throwable, OggettoBulk oggettobulk)
    {
        super(throwable, oggettobulk);
    }
}