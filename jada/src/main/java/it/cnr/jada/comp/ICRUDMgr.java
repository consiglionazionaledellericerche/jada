package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

// Referenced classes of package it.cnr.jada.comp:
//            IRicercaMgr, ComponentException

public interface ICRUDMgr
    extends IRicercaMgr
{

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;
}