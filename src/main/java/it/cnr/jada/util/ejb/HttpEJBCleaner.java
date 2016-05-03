package it.cnr.jada.util.ejb;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.util.Log;

import java.io.Serializable;
import java.util.*;
import javax.ejb.*;
import javax.servlet.http.*;

public class HttpEJBCleaner implements Serializable {
	private final Map objects = new Hashtable();
	private static final Log log = Log.getInstance(HttpEJBCleaner.class);

	private HttpEJBCleaner(){
	}

	private static HttpEJBCleaner bindToHttpSession(HttpSession httpsession){
		if(httpsession == null)
			return null;
		HttpEJBCleaner httpejbcleaner = (HttpEJBCleaner)httpsession.getAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner");
		if(httpejbcleaner == null)
			httpsession.setAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner", httpejbcleaner = new HttpEJBCleaner());
		return httpejbcleaner;
	}

	public static void register(ActionContext actioncontext, Object obj){
		if(actioncontext instanceof HttpActionContext)
			register(((HttpActionContext)actioncontext).getSession(false), obj);
	}

	public static void unregister(ActionContext actioncontext, Object obj){
		if(actioncontext instanceof HttpActionContext)
			unregister(((HttpActionContext)actioncontext).getSession(false), obj);
	}

	public void register(Object obj){
		try{
			Object handle = obj; 
			objects.put(obj,handle);
		}catch(Throwable _ex) {
		}
	}

	public void unregister(Object obj){
		try{
			objects.remove(obj);
		}catch(Throwable _ex) {
		}
	}

	public static void register(HttpSession httpsession, Object obj){
		HttpEJBCleaner httpejbcleaner = bindToHttpSession(httpsession);
		if(httpejbcleaner != null)
			httpejbcleaner.register(obj);
	}

	public static void unregister(HttpSession httpsession, Object obj){
		HttpEJBCleaner httpejbcleaner = bindToHttpSession(httpsession);
		if(httpejbcleaner != null)
			httpejbcleaner.unregister(obj);
	}
	
	public void remove(Object obj){
		try{
			for(; obj instanceof Handle; obj = ((Handle)obj).getEJBObject());
			if(obj instanceof BulkLoaderIterator){            
				((BulkLoaderIterator)obj).ejbRemove();
			} else if (obj instanceof TransactionalBulkLoaderIterator){
				((TransactionalBulkLoaderIterator)obj).ejbRemove();
			} else if(obj instanceof it.cnr.jada.UserTransaction){
				((it.cnr.jada.UserTransaction)obj).remove();
			}                
		}catch(javax.ejb.NoSuchEJBException ex){	
		}catch(Exception _ex) {
			log.warn(_ex);
		}
	}

	public void remove(){
		try{
			for(Iterator iterator = objects.values().iterator(); iterator.hasNext(); remove(iterator.next()));
		}finally{
			objects.clear();
		}
		log.debug("ActiveBulkLoaderIterator: "+EJBTracer.getInstance().getActiveBulkLoaderIteratorCounter());
		log.debug("ActiveComponent: "+EJBTracer.getInstance().getActiveComponentCounter());
		log.debug("ActiveUserTransaction: "+EJBTracer.getInstance().getActiveUserTransactionCounter());
	}
}