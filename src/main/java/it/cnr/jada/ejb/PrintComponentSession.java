package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

import javax.ejb.Remote;

public interface PrintComponentSession extends RicercaComponentSession{

    public abstract OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk stampaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, RemoteException;
}