package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface CRUDComponentSession extends RicercaComponentSession {

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
    	throws ComponentException, RemoteException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
    	throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk[] oggettobulk)
    	throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
    	throws ComponentException, RemoteException;
}
