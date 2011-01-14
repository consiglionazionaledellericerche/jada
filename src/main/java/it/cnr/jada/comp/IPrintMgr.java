package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

// Referenced classes of package it.cnr.jada.comp:
//            IRicercaMgr, ComponentException

public interface IPrintMgr
    extends IRicercaMgr
{

    public abstract OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk stampaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;
}