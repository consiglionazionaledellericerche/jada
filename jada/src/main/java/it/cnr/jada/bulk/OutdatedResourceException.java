package it.cnr.jada.bulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class OutdatedResourceException extends Exception
    implements Serializable
{

    public OutdatedResourceException()
    {
    }

    public OutdatedResourceException(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    public OutdatedResourceException(String s)
    {
        super(s);
    }

    public OutdatedResourceException(String s, OggettoBulk oggettobulk)
    {
        super(s);
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