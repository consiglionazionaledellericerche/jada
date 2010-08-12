package it.cnr.jada.ejb.session;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.List;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.Order;

public interface CRUDComponentSession<T extends OggettoBulk> {

	public T findByPrimaryKey(UserContext userContext, T oggettoBulk)
			throws ComponentException;

	public Long count(UserContext userContext, Class<T> bulkClass)
			throws ComponentException;

	public Long countByCriterion(UserContext userContext, Class<T> bulkClass,
			Criterion criterion) throws ComponentException;

	public List<T> findAll(UserContext userContext, Class<T> bulkClass)
			throws ComponentException;

	public List<T> findByCriterion(UserContext userContext, Class<T> bulkClass,
			Criterion criterion, Integer firstResult, Integer maxResult)
			throws ComponentException;

	public List<T> findByCriterion(UserContext userContext, Class<T> bulkClass) 
		throws ComponentException;
	
	public List<T> findByCriterion(UserContext userContext, Class<T> bulkClass,
			Criterion criterion) throws ComponentException;

	public List<T> findByCriterion(UserContext userContext, Class<T> bulkClass,
			Criterion criterion, Order... order) throws ComponentException;

	public void deleteByCriteria(UserContext userContext, Criteria criteria,
			Class<T> bulkClass) throws ComponentException;

	public T persist(UserContext userContext, T model)
			throws ComponentException;

	public void eliminaConBulk(UserContext userContext, T model)
			throws ComponentException;

	public T creaConBulk(UserContext userContext, T model)
			throws ComponentException;

	public T inizializzaBulkPerModifica(UserContext userContext, T oggettobulk)
			throws ComponentException;

	public T inizializzaBulkPerRicercaLibera(UserContext userContext,
			T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerInserimento(UserContext userContext,
			T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerRicerca(UserContext userContext, T oggettobulk)
			throws ComponentException;

	public T modificaConBulk(UserContext userContext, T oggettobulk)
			throws ComponentException;
	public void initializeForeignKey(UserContext userContext, T oggettobulk) 
		throws ComponentException;
	
	public void initializeForeignKey(UserContext userContext, T oggettobulk, String...attributes) 
		throws ComponentException;

}
