package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

import javax.ejb.Remote;

public interface MultipleCRUDComponentSession extends CRUDComponentSession{

    public abstract OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException, RemoteException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException, RemoteException;

    public abstract OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException, RemoteException;

    public abstract OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws ComponentException, RemoteException;
}