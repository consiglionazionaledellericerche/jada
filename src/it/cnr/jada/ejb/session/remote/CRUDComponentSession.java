/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session.remote;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.session.ComponentException;

import java.util.List;
import javax.ejb.Remote;
import net.bzdyl.ejb3.criteria.Criteria;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Remote
public interface CRUDComponentSession {
	public <T extends OggettoBulk> T findByPrimaryKey(UserContext userContext, T oggettoBulk) throws ComponentException;

	public <T extends OggettoBulk> T merge(UserContext userContext, T oggettoBulk) throws ComponentException;

	public <T extends OggettoBulk> T refresh(UserContext userContext, T oggettoBulk) throws ComponentException;
	
	public <T extends OggettoBulk> List<T> findByCriteria(UserContext userContext, Criteria criteria, Class<T> bulkClass) throws ComponentException;

	public <T extends OggettoBulk> void deleteByCriteria(UserContext userContext, Criteria criteria, Class<T> bulkClass) throws ComponentException;
	
	public <T extends OggettoBulk> List<T> findAll(Class<T> clazz) throws ComponentException;
	
	public <T extends OggettoBulk>  Long count(Class<T> clazz) throws ComponentException;
	
	public <T extends OggettoBulk> T persist(UserContext userContext, T oggettoBulk) throws ComponentException;

	public <T extends OggettoBulk> List<T> persist(UserContext userContext, List<T> oggettiBulk) throws ComponentException;
	
	public <T extends OggettoBulk> List<T> findByQuery(UserContext userContext, String queryString, Class<T> bulkClass) throws ComponentException;

	public void eliminaConBulk(UserContext userContext, OggettoBulk model) throws ComponentException;

	public <T extends OggettoBulk> T creaConBulk(UserContext userContext, T model) throws ComponentException;

	public <T extends OggettoBulk> List<T> cerca(UserContext userContext, Criteria compoundfindclause, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> T inizializzaBulkPerModifica(UserContext userContext, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> T inizializzaBulkPerRicercaLibera(UserContext userContext, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> T inizializzaBulkPerInserimento(UserContext userContext, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> T inizializzaBulkPerRicerca(UserContext userContext, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> T modificaConBulk(UserContext userContext, T oggettobulk) throws ComponentException;

	public <T extends OggettoBulk> List<T> cerca(UserContext userContext, Criteria compoundfindclause, T oggettobulk, OggettoBulk contesto, String property) throws ComponentException;

}
