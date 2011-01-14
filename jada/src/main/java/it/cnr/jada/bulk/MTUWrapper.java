package it.cnr.jada.bulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk, MTUStuff

public class MTUWrapper extends OggettoBulk
    implements Serializable, Cloneable
{

    public MTUWrapper()
    {
    }

    public MTUWrapper(OggettoBulk oggettobulk, MTUStuff mtustuff)
    {
        setBulk(oggettobulk);
        setMtu(mtustuff);
    }

    public OggettoBulk getBulk()
    {
        return bulk;
    }

    public MTUStuff getMtu()
    {
        return mtu;
    }

    public void setBulk(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    public void setMtu(MTUStuff mtustuff)
    {
        mtu = mtustuff;
    }

    protected OggettoBulk bulk;
    protected MTUStuff mtu;
}