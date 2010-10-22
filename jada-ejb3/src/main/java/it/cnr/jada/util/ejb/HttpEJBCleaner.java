/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.ejb;

import it.cnr.jada.ejb.session.BulkLoaderIterator;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class HttpEJBCleaner implements Serializable {
	private static final long serialVersionUID = -8016487788590193347L;
	private final Map<Object, Object> objects = new Hashtable<Object, Object>();

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
			if(obj instanceof BulkLoaderIterator){            
				((BulkLoaderIterator)obj).remove();
			}                
		}catch(Throwable _ex) {
			_ex.printStackTrace();
		}
	}

	public void remove(){
		try{
			for(Iterator<Object> iterator = objects.values().iterator(); iterator.hasNext(); remove(iterator.next()));
		}finally{
			objects.clear();
		}
	}
}