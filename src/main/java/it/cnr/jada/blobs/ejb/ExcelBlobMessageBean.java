package it.cnr.jada.blobs.ejb;
/**
 * Bean implementation class for Enterprise Bean: ExcelBlobMessageBean
 */
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Dictionary;

import javax.ejb.*;
import javax.jms .*;

public class ExcelBlobMessageBean implements javax.jms.MessageListener{
	private javax.ejb.MessageDrivenContext fMessageDrivenCtx;
	BframeBlobComponentSession bframeBlobComponentSession = (BframeBlobComponentSession) EJBCommonServices.createEJB("BFRAMEBLOBS_EJB_BframeBlobComponentSession");
	/**
	 * getMessageDrivenContext
	 */
	public javax.ejb.MessageDrivenContext getMessageDrivenContext()
	{
		return fMessageDrivenCtx;
	}
	/**
	 * setMessageDrivenContext
	 */
	public void setMessageDrivenContext(javax.ejb.MessageDrivenContext ctx)
	{
		fMessageDrivenCtx = ctx;
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate()
	{
	}
	/**
	 * onMessage
	 */
	public void onMessage(javax.jms.Message msg)
	{
		try
		{
			if (msg instanceof ObjectMessage) {
			  ObjectMessage obj = (ObjectMessage) msg;
			  try {
				java.util.Vector parametri = (java.util.Vector)obj.getObject();			  	
				bframeBlobComponentSession.caricaFileExcel(
				  (UserContext)parametri.get(0),
				  (String) parametri.get(1),
				  (Dictionary) parametri.get(2),
				  (File) parametri.get(3),
				  (RemoteIterator) parametri.get(4),
				  (String) parametri.get(5));
			}
			  catch(JMSException e) 
			{
				e.printStackTrace();
			} 
			  catch (DetailedRuntimeException e)
			{
				e.printStackTrace();
			} catch (ComponentException e)
			{
				e.printStackTrace();
			} catch (PersistencyException e)
			{
				e.printStackTrace();
			}
			}    	            
		}
		catch (EJBException e1)
		{
			e1.printStackTrace();
		}
		catch (RemoteException e1)
		{
			e1.printStackTrace();
		}    	
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove()
	{
	}
}
