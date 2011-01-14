package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

// Referenced classes of package it.cnr.jada.comp:
//            ICRUDMgr, ComponentException

public interface IMultipleCRUDMgr
    extends ICRUDMgr
{

    public abstract OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException;

    public abstract OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException;

    public abstract OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException;
}