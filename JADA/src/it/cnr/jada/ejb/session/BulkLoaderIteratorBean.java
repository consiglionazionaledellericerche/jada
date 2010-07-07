/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.session.remote.BulkLoaderIterator;
import it.cnr.jada.util.OrderConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
import net.bzdyl.ejb3.criteria.QueryContext;
import net.bzdyl.ejb3.criteria.QueryParameterValue;
import net.bzdyl.ejb3.criteria.impl.QueryBuilder;

import org.hibernate.ScrollableResults;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Stateful(name="JADA_BulkLoaderIterator")
@TransactionManagement(TransactionManagementType.BEAN)
public class BulkLoaderIteratorBean extends AbstractComponentSessionBean implements BulkLoaderIterator{
	private static final long serialVersionUID = -2431501664741158683L;
	
	@Resource 
	protected UserTransaction usertransaction;
	
	protected Query query;
	protected Criteria criteria;
	private Criteria criteriaCount;	
	protected Long recordCount;
	protected int position;
	protected UserContext userContext;
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
			throw new EJBException("Can't begin transaction", notsupportedexception);
		}catch(SystemException systemexception){
			throw new EJBException("Can't begin transaction", systemexception);
		}catch(EJBException ejbexception){
			throw new EJBException("Can't begin transaction", ejbexception);
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

	public void create(UserContext usercontext, Criteria criteria) throws ComponentException{
		this.criteria = criteria;
		try {
			this.criteriaCount = (Criteria) criteria.clone();
		} catch (CloneNotSupportedException e) {
			throw new ComponentException(e);
		} 
		this.userContext = usercontext;
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
			QueryBuilder queryBuilder =  new QueryBuilder((QueryContext) criteriaCount, getManager() );			
			queryBuilder.collectJoinElements();
			queryBuilder.collectWhereElements();
	        String queryString = queryBuilder.buildQueryString();
			org.hibernate.Query hibernateQuery = getHibernateSession().createQuery(queryString);
	        Map<String, QueryParameterValue> parameters = queryBuilder.getQueryContext().getParameters();
	        for ( String parameterName : parameters.keySet() ){
	            QueryParameterValue parameterValue = parameters.get( parameterName );
	            hibernateQuery.setParameter(parameterName, parameterValue.getValue());
	        }
			ScrollableResults scroll = hibernateQuery.scroll();
			scroll.last();			
			recordCount = Long.valueOf(scroll.getRowNumber()+1);
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
			criteria.removeOrder(Order.remove(propertyName));
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

	public void open(UserContext usercontext) throws ComponentException, DetailedRuntimeException{
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
			throw new EJBException(e);
		}catch (IllegalStateException e){
			throw new EJBException(e);
		}catch (SystemException e){
			throw new EJBException(e);
		}catch (RollbackException e){
			throw new EJBException(e);
		}catch (HeuristicMixedException e){
			throw new EJBException(e);
		}catch (HeuristicRollbackException e){
			throw new EJBException(e);
		}		
	}
	
	@Remove
	public void remove() {
		closeUserTransaction();
	}
}