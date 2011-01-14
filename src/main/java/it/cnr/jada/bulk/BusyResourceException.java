package it.cnr.jada.bulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class BusyResourceException extends Exception
    implements Serializable
{

    public BusyResourceException()
    {
    }

    public BusyResourceException(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    public BusyResourceException(String s)
    {
        super(s);
    }

    public BusyResourceException(String s, OggettoBulk oggettobulk)
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