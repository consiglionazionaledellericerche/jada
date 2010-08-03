/*
 * Copyright 2008-2009 Italian National Research Council
 * 	All rights reserved
 */
package it.cnr.jada.ejb.session;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.bulk.annotation.JadaOneToMany;
import it.cnr.jada.util.Introspector;

import java.beans.IntrospectionException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.restrictions.Restrictions;

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
public abstract class AbstractComponentSessionBean<T extends OggettoBulk> {

	public abstract EntityManager getManager();

	// ====================================================================
	// conteggio generico
	// ====================================================================
	public Long count(Class<T> bulkClass)
			throws ComponentException {
		return getHomeClass(bulkClass).count();
	}

	// ====================================================================
	// query generica
	// ====================================================================
	public List<T> findAll(Class<T> bulkClass)
			throws ComponentException {
		return getHomeClass(bulkClass).findAll();
	}

	public List<T> findByQuery(UserContext userContext,
			String queryString, Class<T> bulkClass) throws ComponentException {
		return getHomeClass(bulkClass).findByQuery(userContext, queryString);
	}

	@SuppressWarnings("unchecked")
	public <N extends OggettoBulk> BulkHome<N> getHomeClass(N oggettoBulk)
			throws ComponentException {
		return getHomeClass((Class<N>)oggettoBulk.getClass());
	}

	@SuppressWarnings("unchecked")
	public <N extends OggettoBulk> BulkHome<N> getHomeClass(Class<N> bulkClass)
			throws ComponentException {
		try {
			HomeClass homeClass = bulkClass.getAnnotation(HomeClass.class);
			return (BulkHome) Introspector.newInstance(homeClass.name(),
					getManager(), bulkClass);
		} catch (SecurityException e) {
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

	protected void validateBulkForPersistency(UserContext userContext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, PrimaryKeyChangedException {
		switch (oggettobulk.getCrudStatus()) {
			case 1: // '\001'
				validateBulkForInsert(userContext, oggettobulk);
				break;
	
			case 2: // '\002'
				validateBulkForUpdate(userContext, oggettobulk);
				break;
	
			case 3: // '\003'
				validateBulkForDelete(userContext, oggettobulk);
				break;
	
			case 4: // '\004'
				validateBulkForCheck(userContext, oggettobulk);
				break;
		}
	}

	@SuppressWarnings("unchecked")
	protected void makeBulkPersistent(UserContext usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException {
		switch (oggettobulk.getCrudStatus()) {
		case 1: // '\001'
			insertBulk(usercontext, oggettobulk);
			break;

		case 2: // '\002'
			updateBulk(usercontext, oggettobulk);
			break;
		case 3: // '\003'
			deleteBulk(usercontext, oggettobulk);
			break;

		}
    	List<Field> attributi = Arrays.asList(oggettobulk.getClass().getDeclaredFields());
		for (Field attributo : attributi) {
			JadaOneToMany jadaOneToMany = attributo.getAnnotation(JadaOneToMany.class);
			if (jadaOneToMany != null){
				try {
					Object value = Introspector.getPropertyValue(oggettobulk, attributo.getName());
					if (value == null)
						continue;
					if (attributo.getDeclaringClass().equals(List.class)){
						List<OggettoBulk> result = (List<OggettoBulk>)value;
						for (OggettoBulk oggettoBulk2 : result) {
							makeForeignBulkPersistent(usercontext, oggettoBulk2);
						}
					}else if (attributo.getDeclaringClass().equals(Set.class)){
						Set<OggettoBulk> result = (Set<OggettoBulk>)value;
						for (OggettoBulk oggettoBulk2 : result) {
							makeForeignBulkPersistent(usercontext, oggettoBulk2);
						}
					}
				} catch (IntrospectionException e) {
					handleException(e);
				} catch (InvocationTargetException e) {
					handleException(e);					
				}
			}
		}
		
	}
	
	private <N extends OggettoBulk> void makeForeignBulkPersistent(UserContext usercontext,
			N oggettobulk) throws ComponentException,
			PersistencyException {
		switch (oggettobulk.getCrudStatus()) {
		case 1: // '\001'
			insertForeignBulk(usercontext, oggettobulk);
			break;

		case 2: // '\002'
			updateForeignBulk(usercontext, oggettobulk);
			break;
		case 3: // '\003'
			deleteForeignBulk(usercontext, oggettobulk);
			break;

		}
	}

	private <N extends OggettoBulk> void insertForeignBulk(UserContext userContext, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				userContext, oggettobulk);
		getHomeClass(oggettobulk).insert(userContext, oggettobulk);
	}
	
	protected void insertBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				userContext, oggettobulk);
		getHomeClass(oggettobulk).insert(userContext, oggettobulk);
	}

	private <N extends OggettoBulk> void updateForeignBulk(UserContext userContext, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).update(userContext, oggettobulk);
	}
	
	protected void updateBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).update(userContext, oggettobulk);
	}

	private <N extends OggettoBulk> void deleteForeignBulk(UserContext userContext, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(userContext, oggettobulk);
	}
	
	protected void deleteBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(userContext, oggettobulk);
	}
	
	protected void validateBulkForCheck(UserContext usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
		lockBulk(usercontext, oggettobulk);
	}

	protected void validateBulkForDelete(UserContext usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, ObjectNotFoundException {
		lockBulk(usercontext, oggettobulk);
	}

	protected void validateBulkForInsert(UserContext usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
	}

	protected void validateBulkForUpdate(UserContext usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, ObjectNotFoundException,
			PrimaryKeyChangedException {
	}

	protected void lockBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException,
			OutdatedResourceException, BusyResourceException {
		getHomeClass(oggettobulk).lock(userContext, oggettobulk);
	}
    
	@SuppressWarnings("unchecked")
	protected void initializeKeysAndOptionsInto(UserContext userContext, T oggettobulk)
    		throws ComponentException{
    	List<Field> attributi = Arrays.asList(oggettobulk.getClass().getDeclaredFields());
		for (Field attributo : attributi) {
			JadaOneToMany jadaOneToMany = attributo.getAnnotation(JadaOneToMany.class);
			if (jadaOneToMany != null){
				BulkHome home = getHomeClass(jadaOneToMany.targetEntity());
				Criteria criteria = home.createCriteria(userContext);
				criteria.add(Restrictions.eq(jadaOneToMany.mappedBy(), oggettobulk));
				List result = home.findByCriteria(userContext, criteria);
				try {
					if (attributo.getDeclaringClass().equals(List.class))
						Introspector.setPropertyValue(oggettobulk, attributo.getName(), result);
					else if (attributo.getDeclaringClass().equals(Set.class))
						Introspector.setPropertyValue(oggettobulk, attributo.getName(), new HashSet(result));
				} catch (IntrospectionException e) {
					handleException(e);
				} catch (InvocationTargetException e) {
					handleException(e);
				}
			}
		}
    }
    
	protected ComponentException handleException(Throwable throwable) {
		try {
			throw throwable;
		} catch (OutdatedResourceException outdatedresourceexception) {
			return new ApplicationException("Risorsa non pi\371 valida",
					outdatedresourceexception);
		} catch (Throwable throwable1) {
			return uncaughtException(throwable1);
		}
	}

	protected final ComponentException uncaughtException(Throwable throwable) {
		StringWriter stringwriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringwriter));
		return new ComponentException(stringwriter.getBuffer().toString(),
				throwable);
	}

}
