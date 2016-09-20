package it.cnr.jada.excel.ejb;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.comp.ExcelComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;

/**
 * Bean implementation class for Enterprise Bean: BFRAMEEXCEL_EJB_BframeExcelComponentSession
 */
@Stateless(name="BFRAMEEXCEL_EJB_BframeExcelComponentSession")
public class BframeExcelComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements BframeExcelComponentSession {
	private ExcelComponent componentObj;
	public void ejbActivate() throws EJBException {
	}
	public void ejbPassivate() throws EJBException {
	}
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ExcelComponent();
	}
	public static BframeExcelComponentSessionBean newInstance() throws EJBException {
		return new BframeExcelComponentSessionBean();
	}
	public Excel_spoolerBulk addQueue(it.cnr.jada.UserContext param0,  it.cnr.jada.util.OrderedHashtable param1,   it.cnr.jada.util.OrderedHashtable param2,   String param3,  java.util.Dictionary param4,  String param5,   it.cnr.jada.persistency.sql.ColumnMap param6,   it.cnr.jada.bulk.OggettoBulk param7)throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Excel_spoolerBulk result=componentObj.addQueue(param0,param1,param2,param3,param4,param5,param6,param7);
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
	public void deleteJobs(it.cnr.jada.UserContext param0,it.cnr.jada.excel.bulk.Excel_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			componentObj.deleteJobs(param0,param1);
			component_invocation_succes(param0,componentObj);
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
	public it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = componentObj.queryJobs(param0);
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
	public void cancellaSchedulazione(UserContext param0, Long long1,
			String resource) throws ComponentException {
		pre_component_invocation(param0,componentObj);
		try {
			componentObj.cancellaSchedulazione(param0, long1, resource);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public Excel_spoolerBulk findExcelSpooler(UserContext param0, Long pg)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			Excel_spoolerBulk result = componentObj.findExcelSpooler(param0,pg);
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
	public void modifyQueue(UserContext param0, Excel_spoolerBulk param1)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			componentObj.modifyQueue(param0,param1);
			component_invocation_succes(param0,componentObj);
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
