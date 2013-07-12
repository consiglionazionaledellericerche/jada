/*
 * Copyright 2008-2009 Italian National Research Council
 * 	All rights reserved
 */
package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.BulkCollection;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;

import net.bzdyl.ejb3.criteria.Order;
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
	private void makeBulkChildPersist(Principal usercontext,
			T oggettobulk, T obj) throws ComponentException, PersistencyException{
    	List<Field> attributi = Arrays.asList(oggettobulk.getClass().getDeclaredFields());
		for (Field attributo : attributi) {
			JadaOneToMany jadaOneToMany = attributo.getAnnotation(JadaOneToMany.class);
			if (jadaOneToMany != null){
				try {
					Object value = Introspector.getPropertyValue(oggettobulk, attributo.getName());
					if (value == null || !(value instanceof Collection))
						continue;
					Collection<OggettoBulk> result = (Collection<OggettoBulk>)value;
					Object persistentResult = getFieldTypeNewInstance(attributo);
					if (persistentResult!=null && persistentResult instanceof Collection){
						for (OggettoBulk oggettoBulk2 : result)
							((Collection<OggettoBulk>)persistentResult).add(makeForeignBulkPersistent(usercontext, oggettoBulk2));
						if (result instanceof BulkCollection){
							for (Iterator<OggettoBulk> iterator = ((BulkCollection)result).deleteIterator(); iterator.hasNext();) {
								OggettoBulk oggettoBulk2 = (OggettoBulk) iterator.next();
								((Collection<OggettoBulk>)persistentResult).add(makeForeignBulkPersistent(usercontext, oggettoBulk2));
							}
						}
					}
					if (obj != null)
						Introspector.setPropertyValue(obj, attributo.getName(), persistentResult);
				} catch (IntrospectionException e) {
					handleException(e);
				} catch (InvocationTargetException e) {
					handleException(e);					
				}
			}
		}
	}
	
	protected T makeBulkPersistent(Principal usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException {
		T obj = null;
		switch (oggettobulk.getCrudStatus()) {
			case 1: // '\001'
				obj = insertBulk(usercontext, oggettobulk);
				makeBulkChildPersist(usercontext, oggettobulk, obj);
				break;
			case 2: // '\002'
				obj = updateBulk(usercontext, oggettobulk);
				makeBulkChildPersist(usercontext, oggettobulk, obj);				
				break;
			case 3: // '\003'
				makeBulkChildPersist(usercontext, oggettobulk, obj);
				deleteBulk(usercontext, oggettobulk);
				break;

		}
		return obj;
	}
	
	private <N extends OggettoBulk> N makeForeignBulkPersistent(Principal usercontext,
			N oggettobulk) throws ComponentException,
			PersistencyException {
		N obj = null;
		switch (oggettobulk.getCrudStatus()) {
			case 1: // '\001'
				obj = insertForeignBulk(usercontext, oggettobulk);
				break;
	
			case 2: // '\002'
				obj = updateForeignBulk(usercontext, oggettobulk);
				break;
			case 3: // '\003'
				deleteForeignBulk(usercontext, oggettobulk);
				break;
		}
		return obj;
	}

	private <N extends OggettoBulk> N insertForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				principal, oggettobulk);
		return getHomeClass(oggettobulk).insert(principal, oggettobulk);
	}
	
	protected T insertBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).initializePrimaryKeyForInsert(
				principal, oggettobulk);
		return getHomeClass(oggettobulk).insert(principal, oggettobulk);
	}

	private <N extends OggettoBulk> N updateForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		return getHomeClass(oggettobulk).update(principal, oggettobulk);
	}
	
	protected T updateBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		return getHomeClass(oggettobulk).update(principal, oggettobulk);
	}

	private <N extends OggettoBulk> void deleteForeignBulk(Principal principal, N oggettobulk)
		throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(principal, oggettobulk);
	}
	
	protected void deleteBulk(Principal principal, T oggettobulk)
			throws PersistencyException, ComponentException {
		getHomeClass(oggettobulk).delete(principal, oggettobulk);
	}
	
	protected void validateBulkForCheck(Principal usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
	}

	protected void validateBulkForDelete(Principal usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException, ObjectNotFoundException {
	}

	protected void validateBulkForInsert(Principal usercontext,
			T oggettobulk) throws ComponentException,
			PersistencyException, OutdatedResourceException,
			BusyResourceException {
	}

	protected void validateBulkForUpdate(Principal usercontext,
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
				List<Order> listOrder = new ArrayList<Order>();
				if (jadaOneToMany.orderBy()!=null) {
					for (int i = 0; i < jadaOneToMany.orderBy().length; i++) {
						String string = jadaOneToMany.orderBy()[i];
						StringTokenizer token = new StringTokenizer(string, " ");
						if (token.hasMoreTokens()) {
							Order order;
							String field = token.nextToken(), ascDesc = null;
							if (token.hasMoreTokens())
								ascDesc = token.nextToken();
							if (ascDesc != null && ascDesc.equals("desc"))
								order = Order.desc(field);
							else
								order = Order.asc(field);
							listOrder.add(order);
						}
					}
				}
				List result = home.selectByCriterion(principal, criterionList, listOrder.toArray(new Order[listOrder.size()])).prepareQuery(getManager()).getResultList();
				Object result2 = getFieldTypeNewInstance(attributo);
				if (result2 !=null && result2 instanceof Collection){
					((Collection<OggettoBulk>)result2).addAll(result);
					try{
						Introspector.setPropertyValue(oggettobulk, attributo.getName(), result2);
					} catch (IntrospectionException e) {
						handleException(e);
					} catch (InvocationTargetException e) {
						handleException(e);
					}
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

    private Object getFieldTypeNewInstance(Field attributo)	throws ComponentException{
		if (attributo.getType().isInterface()) {
			if (attributo.getType().equals(List.class))
				return new ArrayList<OggettoBulk>();
			else if (attributo.getType().equals(Set.class))
				return new HashSet<OggettoBulk>();
		} else {
	    	try{
				return attributo.getType().newInstance();
			} catch (InstantiationException e) {
				handleException(e);					
			} catch (IllegalAccessException e) {
				handleException(e);					
			}
		}
		return null;
    }
}