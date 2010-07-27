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
import it.cnr.jada.util.Introspector;

import java.io.PrintWriter;
import java.io.StringWriter;
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
public abstract class AbstractComponentSessionBean<T extends OggettoBulk> {

	protected EntityManager manager;

	public EntityManager getManager() {
		return manager;
	}

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
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> bulkClass)
			throws ComponentException {
		return getHomeClass(bulkClass).findAll();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByQuery(UserContext userContext,
			String queryString, Class<T> bulkClass) throws ComponentException {
		return getHomeClass(bulkClass).findByQuery(userContext, queryString);
	}

	@SuppressWarnings("unchecked")
	public BulkHome<T> getHomeClass(T oggettoBulk)
			throws ComponentException {
		return getHomeClass((Class<T>)oggettoBulk.getClass());
	}

	@SuppressWarnings("unchecked")
	public BulkHome<T> getHomeClass(Class<T> bulkClass)
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
	}

	protected void insertBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				userContext, oggettobulk);
		getHomeClass(oggettobulk).insert(userContext, oggettobulk);
	}

	protected void updateBulk(UserContext userContext, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).update(userContext, oggettobulk);
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
    
	protected void initializeKeysAndOptionsInto(UserContext usercontext, T oggettobulk)
    		throws ComponentException{
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
