package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;

import javax.ejb.Remote;

public interface CRUDDetailComponentSession extends CRUDComponentSession{

    public abstract RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class class1, OggettoBulk oggettobulk, String s)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException, RemoteException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[], OggettoBulk oggettobulk, String s)
        throws ComponentException, RemoteException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk, String s)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException, RemoteException;

    public abstract OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws ComponentException, RemoteException;
}