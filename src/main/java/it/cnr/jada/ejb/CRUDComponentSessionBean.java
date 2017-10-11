package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.SessionBean;
import javax.ejb.Stateless;

@Stateless(name="JADAEJB_CRUDComponentSession")
public class CRUDComponentSessionBean extends RicercaComponentSessionBean implements CRUDComponentSession{

    public CRUDComponentSessionBean(){
    }
    @PostConstruct
	public void ejbCreate() {
        componentObj = new CRUDComponent();
    }
    public static CRUDComponentSessionBean newInstance() throws EJBException{
        return new CRUDComponentSessionBean();
    }
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            RemoteIterator remoteiterator = ((CRUDComponent)componentObj).cerca(usercontext, compoundfindclause, oggettobulk);
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
            RemoteIterator remoteiterator = ((CRUDComponent)componentObj).cerca(usercontext, compoundfindclause, oggettobulk, oggettobulk1, s);
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
        	Persistent persistent = ((CRUDComponent)componentObj).findByPrimaryKey(userContext, oggettoBulk);
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
    
    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).creaConBulk(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk aoggettobulk1[] = ((CRUDComponent)componentObj).creaConBulk(usercontext, aoggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return aoggettobulk1;
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

    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((CRUDComponent)componentObj).eliminaConBulk(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
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

    public void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((CRUDComponent)componentObj).eliminaConBulk(usercontext, aoggettobulk);
            component_invocation_succes(usercontext, componentObj);
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

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).inizializzaBulkPerInserimento(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).inizializzaBulkPerModifica(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk aoggettobulk1[] = ((CRUDComponent)componentObj).inizializzaBulkPerModifica(usercontext, aoggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return aoggettobulk1;
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

    public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).inizializzaBulkPerRicerca(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).inizializzaBulkPerRicercaLibera(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk oggettobulk1 = ((CRUDComponent)componentObj).modificaConBulk(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
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

    public OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            OggettoBulk aoggettobulk1[] = ((CRUDComponent)componentObj).modificaConBulk(usercontext, aoggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return aoggettobulk1;
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

    public boolean isLockedBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            ((CRUDComponent)componentObj).lockBulk(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return false;
        } catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        } catch (PersistencyException|OutdatedResourceException|BusyResourceException e) {
           return true;
        }
    }
}