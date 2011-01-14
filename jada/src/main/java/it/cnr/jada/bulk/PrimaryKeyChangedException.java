package it.cnr.jada.bulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class PrimaryKeyChangedException extends Exception
    implements Serializable
{

    public PrimaryKeyChangedException()
    {
    }

    public PrimaryKeyChangedException(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    public PrimaryKeyChangedException(String s)
    {
        super(s);
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