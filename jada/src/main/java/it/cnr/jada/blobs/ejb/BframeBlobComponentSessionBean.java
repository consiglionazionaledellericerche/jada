package it.cnr.jada.blobs.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Excel_blobBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.blobs.comp.BframeBlobComponent;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Dictionary;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;
@Stateless(name="BFRAMEBLOBS_EJB_BframeBlobComponentSession")
public class BframeBlobComponentSessionBean extends it.cnr.jada.ejb.RicercaComponentSessionBean implements BframeBlobComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new BframeBlobComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.RicercaComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new BframeBlobComponentSessionBean();
	}
	public void elimina(UserContext param0, Bframe_blob_pathBulk param1[])
		throws ComponentException, javax.ejb.EJBException
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			((BframeBlobComponent)componentObj).elimina(param0, param1);
			component_invocation_succes(param0, componentObj);
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}

	public RemoteIterator getBlobChildren(UserContext param0, Bframe_blob_pathBulk param1, String param2)
		throws ComponentException, javax.ejb.EJBException
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			RemoteIterator result = ((BframeBlobComponent)componentObj).getBlobChildren(param0, param1, param2);
			component_invocation_succes(param0, componentObj);
			return result;
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}

	public java.io.File estraiFileExcel(UserContext param0, String param1)
		throws ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			java.io.File result = ((BframeBlobComponent)componentObj).estraiFileExcel(param0, param1);
			component_invocation_succes(param0, componentObj);
			return result;
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}
	public void caricaFileExcel(UserContext param0, String param1,Dictionary param2,File param3,RemoteIterator param4,String param5)
		throws ComponentException, javax.ejb.EJBException,it.cnr.jada.persistency.PersistencyException
	{
		//pre_component_invocation(param0, componentObj);
		try
		{
			((BframeBlobComponent)componentObj).caricaFileExcel(param0, param1, param2, param3, param4, param5);
			//component_invocation_succes(param0, componentObj);
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}
	public Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext param0)
		throws ComponentException, javax.ejb.EJBException
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			Selezione_blob_tipoVBulk result = ((BframeBlobComponent)componentObj).getSelezione_blob_tipo(param0);
			component_invocation_succes(param0, componentObj);
			return result;
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}
	public void InsertBlob(UserContext param0) throws ComponentException,  Exception
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			((BframeBlobComponent)componentObj).InsertBlob(param0);
			component_invocation_succes(param0, componentObj);
			
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}
	public void DeleteAllExcelBlob(UserContext param0,Excel_blobBulk[] param1)throws ComponentException,RemoteException
	{
		pre_component_invocation(param0, componentObj);
		try
		{
			((BframeBlobComponent)componentObj).DeleteAllExcelBlob(param0,param1);
			component_invocation_succes(param0, componentObj);
			
		}
		catch(NoRollbackException e)
		{
			component_invocation_succes(param0, componentObj);
			throw e;
		}
		catch(ComponentException e)
		{
			component_invocation_failure(param0, componentObj);
			throw e;
		}
		catch(RuntimeException e)
		{
			throw uncaughtRuntimeException(param0, componentObj, e);
		}
		catch(Error e)
		{
			throw uncaughtError(param0, componentObj, e);
		}
	}
}