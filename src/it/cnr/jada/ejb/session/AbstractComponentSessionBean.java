/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.util.Introspector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
/**
 * Astrazione del manager di persistenza
 * 
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public abstract class AbstractComponentSessionBean {

	protected EntityManager manager;

	protected org.hibernate.Session hibernateSession; 
	
	public org.hibernate.Session getHibernateSession() {
		return hibernateSession;
	}
	public EntityManager getManager() {
		return manager;
	}
	// ====================================================================
	// conteggio generico
	// ====================================================================
	public <T extends OggettoBulk>  Long count(Class<T> bulkClass) throws ComponentException {		
		return getHomeClass(bulkClass).count();
	}
	// ====================================================================
	// query generica
	// ====================================================================
	@SuppressWarnings("unchecked")
	public <T extends OggettoBulk> List<T> findAll(Class<T> bulkClass) throws ComponentException {		
		return getHomeClass(bulkClass).findAll();
	}	

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> List<T> findByQuery(UserContext userContext, String queryString, Class<T> bulkClass)throws ComponentException{
    	return getHomeClass(bulkClass).findByQuery(userContext, queryString);
    }    
	
    public <T extends OggettoBulk> BulkHome<T> getHomeClass(T oggettoBulk) throws ComponentException{
    	return getHomeClass(oggettoBulk.getClass());
    }

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> BulkHome getHomeClass(Class<T> bulkClass) throws ComponentException{
    	try{
	    	HomeClass homeClass = bulkClass.getAnnotation(HomeClass.class);	    		    	
	    	return (BulkHome) Introspector.newInstance(homeClass.name(), getManager(), bulkClass);
    	}catch(SecurityException e){
    		throw new ComponentException(e);
    	} catch (NoSuchMethodException e) {
    		throw new ComponentException(e);
		} catch (IllegalArgumentException e) {
    		throw new ComponentException(e);
		} catch (InstantiationException e) {
    		throw new ComponentException(e);
		} catch (IllegalAccessException e) {
    		throw new ComponentException(e);
		} catch (InvocationTargetException e) {
    		throw new ComponentException(e);
		}
    }    
}
