/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;


import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderConstants;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Order;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public abstract class BulkLoaderIteratorBean<T extends OggettoBulk> extends AbstractComponentSessionBean<T> implements BulkLoaderIterator{
	private static final long serialVersionUID = -2431501664741158683L;
	
	@Resource 
	protected UserTransaction usertransaction;
	
	protected Query query;
	protected Criteria criteria;
	protected Long recordCount;
	protected int position;
	protected Principal principal;
	protected int pageSize;
	protected boolean removed;
	protected boolean doCount;
	
	public BulkLoaderIteratorBean(){
		pageSize = 0;
		removed = true;
		doCount = true;
	}

	private void initializeTransaction(){
		try{
			if(usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION){
				usertransaction.begin();
			}
		}catch(NotSupportedException notsupportedexception){
			throw new RuntimeException("Can't begin transaction", notsupportedexception);
		}catch(SystemException systemexception){
			throw new RuntimeException("Can't begin transaction", systemexception);
		}
	}
	
	public void close(){
		try{
			reset();
		}catch(PersistencyException _ex) { 
		}
	}

	public int countElements() throws DetailedRuntimeException{
		try{
			testOpen();
			return recordCount.intValue();
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	public int countPages() throws DetailedRuntimeException{
		try{
			testOpen();
			return ((countElements() + pageSize) - 1) / pageSize;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	public void create(Principal principal, Criteria criteria) throws ComponentException{
		this.criteria = criteria;
		this.principal = principal;
		removed = false;
	}

	protected Object fetchRow() throws PersistencyException{
		query.setFirstResult(position);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	protected Object fetchRow(int i) throws PersistencyException{		
		return fetchRow();
	}

	protected final Error handleError(Error error) throws DetailedRuntimeException{
		throw new DetailedRuntimeException(error);
	}

	protected final RuntimeException handleRuntimeException(RuntimeException runtimeexception) throws DetailedRuntimeException{
		throw new DetailedRuntimeException(runtimeexception);
	}

	public boolean hasMoreElements() throws DetailedRuntimeException{
		try{
			testConnection();
			return position < recordCount;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	public boolean hasMorePages() throws DetailedRuntimeException{
		try{
			testConnection();
			return ((position + pageSize) - 1) / pageSize < ((recordCount + pageSize) - 1) / pageSize;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	public void moveTo(int i)throws DetailedRuntimeException{
		try{
			testConnection();
			if(i <= 0){
				position = 0;
			}else{
				if(i >= recordCount)
					position = recordCount.intValue() - 1;
				else
					position = i;
			}
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw runtimeexception;
		}catch(Error error){
			throw handleError(error);
		}
	}

	public void moveToPage(int i)throws DetailedRuntimeException{
		try{
			testConnection();
			if(i <= 0){
				moveTo(0);
			}else{
				int j = countPages();
				if(i >= j)
					i = Math.max(0, j - 1);
				moveTo(i * pageSize);
			}
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	public Object nextElement()throws DetailedRuntimeException{
		try{
			testConnection();
			Object obj = fetchRow(position++);
			return obj;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}
	}

	@SuppressWarnings("unchecked")
	public List<OggettoBulk> nextPage()throws DetailedRuntimeException{
		try{
			testConnection();
			int i = ((position + pageSize) - 1) / pageSize;
			if(i >= countPages())
				return new ArrayList<OggettoBulk>();
			moveToPage(i);
			int j = Math.min(pageSize, recordCount.intValue() - position);
			query.setFirstResult(position);
			query.setMaxResults(j);
			List<OggettoBulk> bulks = query.getResultList();
			position = position + j;
			return bulks;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	protected void predictCount(){
		if(doCount){
			recordCount = Long.valueOf(criteria.prepareQuery(getManager()).getResultList().size());
			doCount=false;
		}
	}

	private void prepareSearchResult() throws PersistencyException{
		reset();
		predictCount();
		query = criteria.prepareQuery(getManager());
	}

	public void refresh() throws DetailedRuntimeException{
		try{
			reset();
			testOpen();
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}
	}

	private void reset() throws PersistencyException{
		if(query != null)
			query = null;		
	}

	public void setOrderBy(String propertyName, OrderConstants sortDirection) throws DetailedRuntimeException{
		try{
			//criteria.removeOrder(Order.remove(propertyName));
			if (sortDirection.equals(OrderConstants.ORDER_ASC))
				criteria.addOrder(Order.asc(propertyName));
			else if (sortDirection.equals(OrderConstants.ORDER_DESC))
				criteria.addOrder(Order.desc(propertyName));
			reset();
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}
	}
	
	public void setPageSize(int i)throws DetailedRuntimeException {
		try{
			testConnection();
			pageSize = i;
		}catch(NotOpenedRemoteIteratorException notopenedremoteiteratorexception){
			throw new DetailedRuntimeException(notopenedremoteiteratorexception);
		}catch(PersistencyException persistencyexception){
			throw new DetailedRuntimeException(persistencyexception);
		}catch(RuntimeException runtimeexception){
			throw handleRuntimeException(runtimeexception);
		}catch(Error error){
			throw handleError(error);
		}
	}

	protected void testConnection() throws PersistencyException, NotOpenedRemoteIteratorException{
		testOpen();
	}

	protected void testOpen() throws PersistencyException, NotOpenedRemoteIteratorException{
		if(query == null)
			prepareSearchResult();
	}

	public void open(Principal principal) throws ComponentException, DetailedRuntimeException{
		initializeTransaction();
	}
	
	@SuppressWarnings("unused")
	private void passivate() throws PersistencyException{
		closeUserTransaction();
		reset();
	}

	private void closeUserTransaction(){
		try{
			if(usertransaction.getStatus() == Status.STATUS_ACTIVE)
			  usertransaction.commit();
		}catch (SecurityException e){
			throw new RuntimeException(e);
		}catch (IllegalStateException e){
			throw new RuntimeException(e);
		}catch (SystemException e){
			throw new RuntimeException(e);
		}catch (RollbackException e){
			throw new RuntimeException(e);
		}catch (HeuristicMixedException e){
			throw new RuntimeException(e);
		}catch (HeuristicRollbackException e){
			throw new RuntimeException(e);
		}		
	}
	
	public void remove() {
		closeUserTransaction();
	}
}