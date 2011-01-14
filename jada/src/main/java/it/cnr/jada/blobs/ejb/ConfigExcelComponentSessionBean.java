package it.cnr.jada.blobs.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Excel_blobBulk;
import it.cnr.jada.blobs.comp.ConfigExcelComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="BFRAMEBLOBS_EJB_ConfigExcelComponentSession")
public class ConfigExcelComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConfigExcelComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.jada.blobs.comp.ConfigExcelComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new ConfigExcelComponentSessionBean();
}
public Excel_blobBulk InsertBlob(UserContext param0, Excel_blobBulk param1,java.io.File param2)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		Excel_blobBulk result=((ConfigExcelComponent)componentObj).InsertBlob(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public Excel_blobBulk completaOggetto(UserContext param0, Excel_blobBulk param1,String param2) throws ComponentException, PersistencyException,java.rmi.RemoteException{
	pre_component_invocation(param0,componentObj);
	try {
		Excel_blobBulk result = ((ConfigExcelComponent)componentObj).completaOggetto(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
