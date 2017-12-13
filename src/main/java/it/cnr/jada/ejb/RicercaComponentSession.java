package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Remote;

public interface RicercaComponentSession extends GenericComponentSession {

    public abstract RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    public abstract RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws ComponentException, RemoteException;

    public abstract Persistent findByPrimaryKey(UserContext userContext, OggettoBulk oggettoBulk)
            throws ComponentException, RemoteException;

    public abstract <T extends OggettoBulk, U extends OggettoBulk> List<U> find(UserContext userContext, Class<T> contesto, String methodName, Object... parameters)
            throws ComponentException, RemoteException;
}
