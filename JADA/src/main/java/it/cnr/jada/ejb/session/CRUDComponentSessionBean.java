/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.session.remote.CRUDComponentSession;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import net.bzdyl.ejb3.criteria.Criteria;
/**
 * Componente generica per effettuare operazioni di CRUD su una classe di OggettiBulk. 
 * Una CRUDComponent offre i seguenti servizi:
 * 
 *     * Creazione, modifica e cancellazione su base dati di OggettiBulk persistenti;
 *     * Controllo di consistenza transazionale per le operazioni CRUD;
 *     * Validazioni standard per le operazioni di creazione e modifica: campi NOT_NULLABLE e lunghezza 
 *       massima di campi di testo;
 *     * Gestione automatica di ricerche semplici (basate sui valori degli attributi persistenti di un OggettoBulk) 
 *     	 e ricerche complesse (specificate da alberi logici di clausole).
 *     * Gestione automatica di ricerche semplici e complesse per attributi secondari dell'oggetto principale.  
 *
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Stateless(name="JADA_CRUDComponentSession")
public class CRUDComponentSessionBean extends AbstractComponentSessionBean  implements CRUDComponentSession {	
    /**
     * Default constructor. 
     */
    public CRUDComponentSessionBean() {
    }

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> T findByPrimaryKey(UserContext userContext, T oggettoBulk) throws ComponentException{
    	return (T) getHomeClass(oggettoBulk).findByPrimaryKey(userContext, oggettoBulk);
    }

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> T merge(UserContext userContext, T oggettoBulk) throws ComponentException{
    	return (T) getHomeClass(oggettoBulk).merge(userContext, oggettoBulk);
    }

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> T refresh(UserContext userContext, T oggettoBulk) throws ComponentException{
    	return (T) getHomeClass(oggettoBulk).refresh(userContext, oggettoBulk);
    }
    
    public <T extends OggettoBulk> List<T> persist(UserContext userContext, List<T> oggettiBulk) throws ComponentException{
    	List<T> results = new ArrayList<T>();
    	for (Iterator iterator = oggettiBulk.iterator(); iterator.hasNext();) {
			T oggettoBulk = (T) iterator.next();
			results.add(persist(userContext, oggettoBulk));
		}
    	return results;
    }
    
    public <T extends OggettoBulk> T persist(UserContext userContext, T oggettoBulk) throws ComponentException{
		return (T) getHomeClass(oggettoBulk).persist(userContext, oggettoBulk);
    }
    
    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> List<T> findByCriteria(UserContext userContext, Criteria criteria, Class<T> bulkClass) throws ComponentException{
    	return getHomeClass(bulkClass).findByCriteria(userContext, criteria);
    }

    @SuppressWarnings("unchecked")
	public <T extends OggettoBulk> void deleteByCriteria(UserContext userContext, Criteria criteria, Class<T> bulkClass) throws ComponentException{
    	getHomeClass(bulkClass).deleteByCriteria(userContext, criteria);
    }

	public <T extends OggettoBulk> List<T> cerca(UserContext userContext, Criteria compoundfindclause, T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends OggettoBulk> List<T> cerca(UserContext userContext, Criteria compoundfindclause, T oggettobulk, OggettoBulk contesto,String property) throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends OggettoBulk> T creaConBulk(UserContext userContext, T model) throws ComponentException {
		// TODO Auto-generated method stub
		return model;
	}

	public void eliminaConBulk(UserContext userContext, OggettoBulk model) throws ComponentException {
		// TODO Auto-generated method stub
		
	}

	public <T extends OggettoBulk> T inizializzaBulkPerInserimento(UserContext userContext, T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return oggettobulk;
	}

	public <T extends OggettoBulk> T inizializzaBulkPerModifica(UserContext userContext, T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return oggettobulk;
	}

	public <T extends OggettoBulk> T inizializzaBulkPerRicerca(UserContext userContext, T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return oggettobulk;
	}

	public <T extends OggettoBulk> T inizializzaBulkPerRicercaLibera(UserContext userContext, T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return oggettobulk;
	}

	public <T extends OggettoBulk> T modificaConBulk(UserContext userContext,T oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		return oggettobulk;
	}
}
