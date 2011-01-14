package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

// Referenced classes of package it.cnr.jada.comp:
//            ICRUDMgr, ComponentException

public interface ICRUDDetailMgr
    extends ICRUDMgr
{

    public abstract RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class class1, OggettoBulk oggettobulk, String s)
        throws ComponentException;

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[], OggettoBulk oggettobulk, String s)
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk, String s)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException;

    public abstract OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException;
}