package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import java.util.List;

public class RicercaComponentSessionBean extends GenericComponentSessionBean implements RicercaComponentSession{
    protected Component componentObj;

    public RicercaComponentSessionBean(){
    }

    @PostConstruct
	public void ejbCreate() throws CreateException{
        componentObj = new RicercaComponent();
    }
    
    @Remove
    public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

    public static RicercaComponentSessionBean newInstance() throws EJBException{
        return new RicercaComponentSessionBean();
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            RemoteIterator remoteiterator = ((RicercaComponent)componentObj).cerca(usercontext, compoundfindclause, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return remoteiterator;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            RemoteIterator remoteiterator = ((RicercaComponent)componentObj).cerca(usercontext, compoundfindclause, oggettobulk, oggettobulk1, s);
            component_invocation_succes(usercontext, componentObj);
            return remoteiterator;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public Persistent findByPrimaryKey(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, EJBException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Persistent persistent = ((RicercaComponent)componentObj).findByPrimaryKey(userContext, oggettoBulk);
            component_invocation_succes(userContext, componentObj);
            return persistent;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
    }

    public  <T extends OggettoBulk, U extends OggettoBulk> List<U> find(UserContext userContext, Class<T> contesto, String methodName, Object... parameters) throws ComponentException, EJBException{
        pre_component_invocation(userContext, componentObj);
        try{
            List<U> result = ((RicercaComponent)componentObj).find(userContext, contesto, methodName, parameters);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
    }
}