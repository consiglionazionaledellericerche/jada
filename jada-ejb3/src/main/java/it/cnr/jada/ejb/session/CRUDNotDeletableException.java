package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDException

public class CRUDNotDeletableException extends CRUDException
    implements Serializable
{

    public CRUDNotDeletableException()
    {
    }

    public CRUDNotDeletableException(String s)
    {
        super(s);
    }

    public CRUDNotDeletableException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public CRUDNotDeletableException(String s, Throwable throwable, OggettoBulk oggettobulk)
    {
        super(s, throwable, oggettobulk);
    }

    public CRUDNotDeletableException(Throwable throwable)
    {
        super(throwable);
    }

    public CRUDNotDeletableException(Throwable throwable, OggettoBulk oggettobulk)
    {
        super(throwable, oggettobulk);
    }
}