package it.cnr.jada.comp;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ApplicationException

public class CRUDException extends ApplicationException
    implements Serializable
{

    public CRUDException()
    {
    }

    public CRUDException(String s)
    {
        super(s);
    }

    public CRUDException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public CRUDException(String s, Throwable throwable, OggettoBulk oggettobulk)
    {
        super(s, throwable);
        bulk = oggettobulk;
    }

    public CRUDException(Throwable throwable)
    {
        super(throwable);
    }

    public CRUDException(Throwable throwable, OggettoBulk oggettobulk)
    {
        super(throwable);
        bulk = oggettobulk;
    }

    public OggettoBulk getBulk()
    {
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    private OggettoBulk bulk;
}