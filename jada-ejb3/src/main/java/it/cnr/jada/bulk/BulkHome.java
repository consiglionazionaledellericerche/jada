/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.ejb.session.ComponentException;
import it.cnr.jada.util.Introspector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.CriteriaFactory;
import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.Order;
import net.bzdyl.ejb3.criteria.Projection;
import net.bzdyl.ejb3.criteria.projections.Projections;
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
	
	public Criteria createCriteria(Principal principal){
		return CriteriaFactory.createCriteria(bulkClass.getName());
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByQuery(Principal principal, String queryString){
    	Query query = manager.createQuery(queryString);
    	return query.getResultList();
    }
	
	public T merge(Principal principal, T oggettoBulk){
		return manager.merge(oggettoBulk);
	}	

	public T refresh(Principal principal, T oggettoBulk){
		manager.refresh(oggettoBulk);
		return oggettoBulk;
	}	
	
	@SuppressWarnings("unchecked")
	public T findByPrimaryKey(Principal principal, T oggettoBulk) throws ComponentException{
    	T bulk = (T)manager.find(oggettoBulk.getClass(), oggettoBulk.getId());
    	if(bulk == null)
    		return null;
    	bulk.setCrudStatus(OggettoBulk.NORMAL);
    	return bulk;
    }
	
    public T update(Principal principal, T oggettoBulk) throws ComponentException{
    	oggettoBulk.setUtuv(principal.getName());
    	oggettoBulk.setDuva(new Timestamp(new Date().getTime()));
    	T obj = manager.merge(oggettoBulk);
    	obj.setCrudStatus(OggettoBulk.NORMAL);
    	return obj;
    }

    public T insert(Principal principal, T oggettoBulk) throws ComponentException{
    	oggettoBulk.setUtcr(principal.getName());
    	oggettoBulk.setUtuv(principal.getName());
    	oggettoBulk.setDuva(new Timestamp(new Date().getTime()));
    	oggettoBulk.setDacr(new Timestamp(new Date().getTime()));
    	manager.persist(oggettoBulk);
    	return findByPrimaryKey(principal, oggettoBulk);
    }

    public void delete(Principal principal, T oggettoBulk) throws ComponentException{
    	oggettoBulk = findByPrimaryKey(principal, oggettoBulk);
    	manager.remove(oggettoBulk);
    }
    
    public T persist(Principal principal, T oggettoBulk) throws ComponentException{
    	if (oggettoBulk.isToBeCreated())
    		return insert(principal, oggettoBulk);
    	else if (oggettoBulk.isToBeUpdated())
    		return update(principal, oggettoBulk);
    	else if (oggettoBulk.isToBeDeleted())
    		delete(principal, oggettoBulk);
    	return oggettoBulk;
    }
    
	public Query getQueryByCriterion(Principal principal, Criterion criterion){
		Criteria criteria = selectByCriterion(principal, criterion, new Order[0]);
    	return criteria.prepareQuery(manager);
    }

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Criterion criterion){
		return selectByCriterion(principal, criterion, new Order[0]).prepareQuery(manager).getResultList();
    }
	
	public Criteria selectByCriterion(Principal principal, Criterion criterion){
		return selectByCriterion(principal, criterion, new Order[0]);
    }

	public Criteria selectByProjection(Principal principal, Projection projection){
		return selectByCriterion(principal, null, projection, new Order[0]);
    }

	public Criteria selectByProjection(Principal principal, Projection projection, Criterion criterion){
		return selectByCriterion(principal, criterion, projection, new Order[0]);
    }

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Criterion criterion, Order... order){
		return selectByCriterion(principal, criterion, order).prepareQuery(manager).getResultList();
    }
	
	public Criteria selectByCriterion(Principal principal, Criterion criterion, Order... order){
		return selectByCriterion(principal, criterion, null, order);
    }

	public Criteria selectByCriterion(Principal principal, Criterion criterion, Projection projection, Order... order){
		Criteria criteria = createCriteria(principal);
		if (criterion != null)
			criteria.add(criterion);
		if (projection != null)
			criteria.setProjection(projection);
		if (order != null)
			for (Order o : order) {
				criteria.addOrder(o);
			}
    	return criteria;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteByCriteria(Principal principal, Criteria criteria) throws ComponentException{
    	Query query = criteria.prepareQuery(manager);    	
    	for (Iterator iterator = query.getResultList().iterator(); iterator.hasNext();) {
			T result = (T) iterator.next();
			result.setToBeDeleted();
			persist(principal, result);
		}
    }
    
    /**
     * Effettua una ricerca lockante del massimo valore di un attributo di un oggetto persistente. 
     **/
    @SuppressWarnings("unchecked")
	public T fetchAndLockMax(Principal principal, String propertyName){
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
		criteria.setProjection(Projections.max(propertyName));
		Query query = criteria.prepareQuery(manager);
		T oggettoBulk = (T)query.getSingleResult();
		manager.lock(oggettoBulk, LockModeType.WRITE);
    	return oggettoBulk;
    }

    @SuppressWarnings("unchecked")
	public List<T> find(Principal principal, T oggettoBulk){
		Criteria criteria = CriteriaFactory.createCriteria(bulkClass.getName());
    	Query query = criteria.prepareQuery(manager);
    	return query.getResultList();
    }
    
    public void lock(Principal principal, T oggettoBulk){
    	manager.lock(oggettoBulk, LockModeType.WRITE);
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
	
	public T findById(Principal principal, Class<T> bulkClass, Serializable id) throws ComponentException{
	     T bulk = (T)manager.find(bulkClass, id);
	     if(bulk == null)
	      return null;
	     bulk.setCrudStatus(OggettoBulk.NORMAL);
	     return bulk;
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

	public void initializePrimaryKeyForInsert(Principal principal,
			OggettoBulk oggettobulk) {
		
	}   

	public void initializeKeysAndOptionsInto(Principal principal, T oggettobulk) throws ComponentException{
		
	}
}
