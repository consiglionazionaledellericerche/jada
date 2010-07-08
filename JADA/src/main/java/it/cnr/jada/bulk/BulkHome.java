/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.ejb.session.ComponentException;
import it.cnr.jada.util.Introspector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.CriteriaFactory;
import net.bzdyl.ejb3.criteria.projections.Projections;
import net.bzdyl.ejb3.criteria.restrictions.Restrictions;
/**
 * PersistentHome specializzato per classi persistenti derivate da OggettoBulk. 
 * Possiede le seguenti estensioni: gestisce i campi sistemistici di 
 * OggettoBulk: utcr, utuv, duva e dacr; 
 * effettua i controlli di consistenza sulla versione durante l'update; 
 * metodi per chiedere un lock su DB del record corrispondente all'oggetto persistente; 
 * supporto per il caricamento delle "options" e "keys" definite nel BulkInfo dell'OggettoBulk
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class BulkHome<T extends OggettoBulk> implements Serializable{

	private static final long serialVersionUID = -7338290995354855602L;
	protected EntityManager manager;
	protected Class<T> bulkClass;
	
	public BulkHome(EntityManager manager, Class<T> bulkClass) {
		super();
		this.manager = manager;
		this.bulkClass = bulkClass;
	}
	
	public Criteria createCriteria(UserContext userContext){
		return CriteriaFactory.createCriteria(bulkClass.getName());
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByQuery(UserContext userContext, String queryString){
    	Query query = manager.createQuery(queryString);
    	return query.getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public T merge(UserContext userContext, T oggettoBulk){
		return manager.merge(oggettoBulk);
	}	

	@SuppressWarnings("unchecked")
	public T refresh(UserContext userContext, T oggettoBulk){
		manager.refresh(oggettoBulk);
		return oggettoBulk;
	}	
	
	@SuppressWarnings("unchecked")
	public T findByPrimaryKey(UserContext userContext, T oggettoBulk){
    	T bulk = (T)manager.find(oggettoBulk.getClass(), oggettoBulk.getId());
    	if(bulk == null)
    		return null;
    	bulk.setCrudStatus(OggettoBulk.NORMAL);
    	return bulk;
    }
	
    public T update(UserContext userContext, T oggettoBulk) throws ComponentException{
    	/*
    	T oggettoBulkDB = findByPrimaryKey(userContext, oggettoBulk);
    	if (oggettoBulkDB == null)
    		throw new BulkNotFoundException();
    	if (!oggettoBulkDB.getPg_ver_rec().equals(oggettoBulk.getPg_ver_rec()))
    		throw new OutdatedResourceException(oggettoBulk);
    	*/	
    	oggettoBulk.setUtuv(userContext.getUser());
    	oggettoBulk.setDuva(new Timestamp(new Date().getTime()));
    	T obj = manager.merge(oggettoBulk);
    	obj.setCrudStatus(OggettoBulk.NORMAL);
    	return obj;
    }

    public T insert(UserContext userContext, T oggettoBulk) throws ComponentException{
    	oggettoBulk.setUtcr(userContext.getUser());
    	oggettoBulk.setUtuv(userContext.getUser());
    	oggettoBulk.setDuva(new Timestamp(new Date().getTime()));
    	oggettoBulk.setDacr(new Timestamp(new Date().getTime()));
    	manager.persist(oggettoBulk);
    	return findByPrimaryKey(userContext, oggettoBulk);
    }

    public void delete(UserContext userContext, T oggettoBulk) throws ComponentException{
    	oggettoBulk = findByPrimaryKey(userContext, oggettoBulk);
    	manager.remove(oggettoBulk);
    }
    
    public T persist(UserContext userContext, T oggettoBulk) throws ComponentException{
    	if (oggettoBulk.isToBeCreated())
    		return insert(userContext, oggettoBulk);
    	else if (oggettoBulk.isToBeUpdated())
    		return update(userContext, oggettoBulk);
    	else if (oggettoBulk.isToBeDeleted())
    		delete(userContext, oggettoBulk);
    	return oggettoBulk;
    }
    
    @SuppressWarnings("unchecked")
	public List<T> findByCriteria(UserContext userContext, Criteria criteria){
    	Query query = criteria.prepareQuery(manager);
    	return query.getResultList();
    }

    @SuppressWarnings("unchecked")
	public void deleteByCriteria(UserContext userContext, Criteria criteria) throws ComponentException{
    	Query query = criteria.prepareQuery(manager);    	
    	for (Iterator iterator = query.getResultList().iterator(); iterator.hasNext();) {
			T result = (T) iterator.next();
			result.setToBeDeleted();
			persist(userContext, result);
		}
    }
    
    /**
     * Effettua una ricerca lockante del massimo valore di un attributo di un oggetto persistente. 
     **/
    @SuppressWarnings("unchecked")
	public T fetchAndLockMax(UserContext userContext, String propertyName){
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
		criteria.setProjection(Projections.max(propertyName));
		Query query = criteria.prepareQuery(manager);
		T oggettoBulk = (T)query.getSingleResult();
		manager.lock(oggettoBulk, LockModeType.WRITE);
    	return oggettoBulk;
    }

    @SuppressWarnings("unchecked")
	public List<T> find(UserContext userContext, T oggettoBulk){
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
    	Query query = criteria.prepareQuery(manager);
    	return query.getResultList();
    }
    
	// ====================================================================
	// conteggio generico
	// ====================================================================
	public Long count() {
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
		criteria.setProjection(Projections.rowCount());
		Query query = criteria.prepareQuery(manager);
		return (Long) query.getSingleResult();
	}
	// ====================================================================
	// query generica
	// ====================================================================
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
		Query query = criteria.prepareQuery(manager);
		return query.getResultList();
	}	
    
	@SuppressWarnings("unchecked")
	public <K extends OggettoBulk> BulkHome<K> getHomeClass(K oggettoBulk) throws ComponentException{
    	return (BulkHome<K>) getHomeClass(oggettoBulk.getClass());
    }

    @SuppressWarnings("unchecked")
	public <K extends OggettoBulk> BulkHome<K> getHomeClass(Class<K> bulkClass) throws ComponentException{
    	try{
	    	HomeClass homeClass = bulkClass.getAnnotation(HomeClass.class);	    		    	
	    	return (BulkHome<K>) Introspector.newInstance(homeClass.name(), manager, bulkClass);
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
