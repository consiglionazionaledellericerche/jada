/*
 * Copyright 2008-2009 Italian National Research Council
 * 	All rights reserved
 */
package it.cnr.jada.ejb.session;


import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.bulk.annotation.JadaOneToMany;
import it.cnr.jada.criterion.CriterionList;
import it.cnr.jada.util.Introspector;

import java.beans.IntrospectionException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

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

	protected void validateBulkForPersistency(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, PrimaryKeyChangedException {
		switch (oggettobulk.getCrudStatus()) {
			case 1: // '\001'
				validateBulkForInsert(principal, oggettobulk);
				break;
	
			case 2: // '\002'
				validateBulkForUpdate(principal, oggettobulk);
				break;
	
			case 3: // '\003'
				validateBulkForDelete(principal, oggettobulk);
				break;
	
			case 4: // '\004'
				validateBulkForCheck(principal, oggettobulk);
				break;
		}
	}

	@SuppressWarnings("unchecked")
	protected void makeBulkPersistent(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException {
		switch (oggettobulk.getCrudStatus()) {
		case 1: // '\001'
			insertBulk(principal, oggettobulk);
			break;

		case 2: // '\002'
			updateBulk(principal, oggettobulk);
			break;
		case 3: // '\003'
			deleteBulk(principal, oggettobulk);
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
					if (attributo.getType().equals(List.class)){
						List<OggettoBulk> result = (List<OggettoBulk>)value;
						for (OggettoBulk oggettoBulk2 : result) {
							makeForeignBulkPersistent(principal, oggettoBulk2);
						}
					}else if (attributo.getType().equals(Set.class)){
						Set<OggettoBulk> result = (Set<OggettoBulk>)value;
						for (OggettoBulk oggettoBulk2 : result) {
							makeForeignBulkPersistent(principal, oggettoBulk2);
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
	
	private <N extends OggettoBulk> void makeForeignBulkPersistent(Principal principal,
			N oggettobulk) throws ComponentException,
			PersistencyException {
		switch (oggettobulk.getCrudStatus()) {
		case 1: // '\001'
			insertForeignBulk(principal, oggettobulk);
			break;

		case 2: // '\002'
			updateForeignBulk(principal, oggettobulk);
			break;
		case 3: // '\003'
			deleteForeignBulk(principal, oggettobulk);
			break;

		}
	}

	private <N extends OggettoBulk> void insertForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				principal, oggettobulk);
		getHomeClass(oggettobulk).insert(principal, oggettobulk);
	}
	
	protected void insertBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				principal, oggettobulk);
		getHomeClass(oggettobulk).insert(principal, oggettobulk);
	}

	private <N extends OggettoBulk> void updateForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).update(principal, oggettobulk);
	}
	
	protected void updateBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).update(principal, oggettobulk);
	}

	private <N extends OggettoBulk> void deleteForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(principal, oggettobulk);
	}
	
	protected void deleteBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(principal, oggettobulk);
	}
	
	protected void validateBulkForCheck(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
		lockBulk(principal, oggettobulk);
	}

	protected void validateBulkForDelete(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, ObjectNotFoundException {
		lockBulk(principal, oggettobulk);
	}

	protected void validateBulkForInsert(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
	}

	protected void validateBulkForUpdate(Principal principal,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, ObjectNotFoundException,
			PrimaryKeyChangedException {
	}

	protected void lockBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException,
			OutdatedResourceException, BusyResourceException {
		getHomeClass(oggettobulk).lock(principal, oggettobulk);
	}

	public void initializeForeignKey(Principal principal, T oggettobulk) throws ComponentException {
		initializeForeignKey(principal, oggettobulk, new String[0]);
	}	
	
	@SuppressWarnings("unchecked")
	public void initializeForeignKey(Principal principal, T oggettobulk, String...attributes) throws ComponentException {
    	List<Field> attributi = Arrays.asList(oggettobulk.getClass().getDeclaredFields());
		for (Field attributo : attributi) {
			if (attributes != null && !Arrays.asList(attributes).isEmpty() &&
					!Arrays.asList(attributes).contains(attributo.getName()))
				continue;
			JadaOneToMany jadaOneToMany = attributo.getAnnotation(JadaOneToMany.class);
			if (jadaOneToMany != null){
				BulkHome home = getHomeClass(jadaOneToMany.targetEntity());
				CriterionList criterionList = new CriterionList(Restrictions.eq(jadaOneToMany.mappedBy(), oggettobulk));
				List result = home.selectByCriterion(principal, criterionList).prepareQuery(getManager()).getResultList();
				try {
					if (attributo.getType().equals(List.class))
						Introspector.setPropertyValue(oggettobulk, attributo.getName(), result);
					else if (attributo.getType().equals(Set.class))
						Introspector.setPropertyValue(oggettobulk, attributo.getName(), new HashSet(result));
				} catch (IntrospectionException e) {
					handleException(e);
				} catch (InvocationTargetException e) {
					handleException(e);
				}
			}
		}
	}

	protected void initializeKeysAndOptionsInto(Principal principal, T oggettobulk)
    		throws ComponentException{
		getHomeClass(oggettobulk).initializeKeysAndOptionsInto(principal, oggettobulk);
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
