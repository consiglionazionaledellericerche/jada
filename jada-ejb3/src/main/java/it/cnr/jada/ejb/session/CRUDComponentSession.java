package it.cnr.jada.ejb.session;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.List;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Criterion;

public interface CRUDComponentSession<T extends OggettoBulk> {
	public T findByPrimaryKey(UserContext userContext, T oggettoBulk) throws ComponentException;

	public List<T> findByCriteria(UserContext userContext, Criteria criteria, Integer firstResult, Integer maxResult) throws ComponentException;
	
	public  List<T> findByCriteria(UserContext userContext, Criteria criteria) throws ComponentException;
	
	public  Long countByCriteria(UserContext userContext, Criteria criteria) throws ComponentException;
	
	public List<T> findByCriterion(UserContext userContext, Criterion criterion, Class<T> bulkClass) throws ComponentException;

	public void deleteByCriteria(UserContext userContext, Criteria criteria, Class<T> bulkClass) throws ComponentException;
	
	public List<T> findAll(Class<T> clazz) throws ComponentException;
	
	public Long count(Class<T> clazz) throws ComponentException;

	public List<T> findByQuery(UserContext userContext, String queryString, Class<T> bulkClass) throws ComponentException;

	public  T persist(UserContext userContext, T model) throws ComponentException;
	
	public void eliminaConBulk(UserContext userContext, T model) throws ComponentException;

	public T creaConBulk(UserContext userContext, T model) throws ComponentException;

	public T inizializzaBulkPerModifica(UserContext userContext, T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerRicercaLibera(UserContext userContext, T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerInserimento(UserContext userContext, T oggettobulk) throws ComponentException;

	public T inizializzaBulkPerRicerca(UserContext userContext, T oggettobulk) throws ComponentException;

	public T modificaConBulk(UserContext userContext, T oggettobulk) throws ComponentException;

}
