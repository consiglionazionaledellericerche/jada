package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.Order;
import net.bzdyl.ejb3.criteria.Projection;
import net.bzdyl.ejb3.criteria.projections.AggregateProjection;
import net.bzdyl.ejb3.criteria.projections.DistinctProjection;

public interface CRUDComponentSession<T extends OggettoBulk> {

	public T findByPrimaryKey(Principal principal, T oggettoBulk)
			throws ComponentException;

	public Long count(Principal principal, Class<T> bulkClass)
			throws ComponentException;

	public Long countByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion) throws ComponentException;

	public List<T> findAll(Principal principal, Class<T> bulkClass)
			throws ComponentException;

	public List<T> findByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion, Integer firstResult, Integer maxResult)
			throws ComponentException;

	public List<T> findByCriterion(Principal principal, Class<T> bulkClass) 
		throws ComponentException;
	
	public List<T> findByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion) throws ComponentException;

	public List<T> findByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion, Order... order) throws ComponentException;
	
	public Object findByAggregateProjection(Principal principal, Class<T> bulkClass,
			AggregateProjection aggregateProjection) throws ComponentException;

	public Object findByAggregateProjection(Principal principal, Class<T> bulkClass,
			AggregateProjection aggregateProjection, Criterion criterion) throws ComponentException;

	public List<Object> findByDistinctProjection(Principal principal, Class<T> bulkClass,
			DistinctProjection distinctProjection) throws ComponentException;

	public List<Object> findByDistinctProjection(Principal principal, Class<T> bulkClass,
			DistinctProjection distinctProjection, Criterion criterion) throws ComponentException;

	public List<Object> findByProjection(Principal principal, Class<T> bulkClass,
			Projection projection, Criterion criterion) throws ComponentException;

	public List<Object> findByProjection(Principal principal, Class<T> bulkClass,
			Projection projection, Criterion criterion, Order... order) throws ComponentException;

	public T findById(Principal principal, Class<T> bulkClass, Serializable id) throws ComponentException;
	
	public void deleteByCriteria(Principal principal, Criteria criteria,
			Class<T> bulkClass) throws ComponentException;

	public T persist(Principal principal, T model)
			throws ComponentException;

	public void eliminaConBulk(Principal principal, T model)
			throws ComponentException;

	public T creaConBulk(Principal principal, T model)
			throws ComponentException;

	public T inizializzaBulkPerModifica(Principal principal, T oggettobulk)
			throws ComponentException;

	public T inizializzaBulkPerRicercaLibera(Principal principal,
			T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerInserimento(Principal principal,
			T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerRicerca(Principal principal, T oggettobulk)
			throws ComponentException;

	public T modificaConBulk(Principal principal, T oggettobulk)
			throws ComponentException;
	public void initializeForeignKey(Principal principal, T oggettobulk) 
		throws ComponentException;
	
	public void initializeForeignKey(Principal principal, T oggettobulk, String...attributes) 
		throws ComponentException;

}
