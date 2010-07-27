/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.ejb.session.BulkLoaderIterator;
import it.cnr.jada.ejb.session.CRUDComponentSessionLocal;
import it.cnr.jada.ejb.session.ComponentException;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public final class EJBCommonServices {

	public static CRUDComponentSessionLocal createCRUDComponentSession(){
		return (CRUDComponentSessionLocal) createRemoteEJB("JADA_CRUDComponentSession");
	}
	
	public static final CRUDComponentSessionLocal createRemoteEJB(String jndiName){
		try {
			return (CRUDComponentSessionLocal)getInitialContext().lookup(jndiName);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	public static final BulkLoaderIterator createRemoteIterator(String jndiName){
		try {
			return (BulkLoaderIterator)getInitialContext().lookup(jndiName);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private static InitialContext getInitialContext() throws NamingException{
		return new InitialContext();
	}
	
	public static final RemoteIterator openRemoteIterator(HttpSession httpsession, UserContext userContext, RemoteIterator remoteIterator){
		try{
			HttpEJBCleaner.register(httpsession, remoteIterator);
			if(remoteIterator instanceof BulkLoaderIterator)
				((BulkLoaderIterator)remoteIterator).open(userContext);
		}catch(ComponentException componentexception){
			throw new DetailedRuntimeException(componentexception);
		}
		return remoteIterator;
	}

}
