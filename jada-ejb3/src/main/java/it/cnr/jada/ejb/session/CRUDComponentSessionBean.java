/*
 * Copyright 2008-2009 Italian National Research Council
 * 	All rights reserved
 */
package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import javax.persistence.Query;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.Order;
import net.bzdyl.ejb3.criteria.projections.Projections;

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
public abstract class CRUDComponentSessionBean<T extends OggettoBulk> extends
		AbstractComponentSessionBean<T> implements CRUDComponentSession<T> {
	/**
	 * Default constructor.
	 */
	public CRUDComponentSessionBean() {
	}

	public void deleteByCriteria(Principal principal, Criteria criteria,
			Class<T> bulkClass) throws ComponentException {
		getHomeClass(bulkClass).deleteByCriteria(principal, criteria);
	}

	/**
	 * Metodi di Ricerca
	 */
	public T findByPrimaryKey(Principal principal, T oggettoBulk)
			throws ComponentException {
		return getHomeClass(oggettoBulk).findByPrimaryKey(principal,
				oggettoBulk);
	}

	public Long count(Principal principal,Class<T> bulkClass) throws ComponentException {
		Criteria criteria = select(principal, bulkClass, null);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.prepareQuery(getManager()).getSingleResult();
	}

	public Long countByCriterion(Principal principal, Class<T> bulkClass, Criterion criterion)
			throws ComponentException {
		Criteria criteria = select(principal, bulkClass, criterion);
		criteria.setProjection(Projections.rowCount());
		if (criterion != null)
			criteria.add(criterion);
		return (Long) criteria.prepareQuery(getManager()).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Principal principal, Class<T> bulkClass)
			throws ComponentException {
		return select(principal, bulkClass, null).prepareQuery(getManager()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Class<T> bulkClass, Criterion criterion,
			Integer firstResult, Integer maxResult) throws ComponentException {
		Query query = select(principal, bulkClass, criterion).prepareQuery(getManager());
		if (firstResult != null)
			query.setFirstResult(firstResult);
		if (maxResult != null)
			query.setMaxResults(maxResult);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Class<T> bulkClass) throws ComponentException {
		Criteria criteria = select(principal, bulkClass, null);
		return criteria.prepareQuery(getManager()).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion) throws ComponentException {
		Criteria criteria = select(principal, bulkClass, criterion);
		return criteria.prepareQuery(getManager()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(Principal principal, Class<T> bulkClass,
			Criterion criterion, Order... order) throws ComponentException {
		Criteria criteria = select(principal, bulkClass, criterion, order);
		return criteria.prepareQuery(getManager()).getResultList();
	}
	
	public T findById(Principal principal, Class<T> bulkClass, Serializable id) throws ComponentException {
		return getHomeClass(bulkClass).findById(principal, bulkClass, id);
	}
	
	protected Criteria select(Principal principal, Class<T> bulkClass,
			Criterion criterion) throws ComponentException {
		return select(principal, bulkClass, criterion, new Order[0]);
	}
	
	protected Criteria select(Principal principal, Class<T> bulkClass,
			Criterion criterion, Order... order) throws ComponentException {
		return getHomeClass(bulkClass)
				.selectByCriterion(principal, criterion, order);
	}

	public T persist(Principal principal, T model)
			throws ComponentException {
		if (model.isToBeCreated())
			return creaConBulk(principal, model);
		else if (model.isToBeUpdated())
			return modificaConBulk(principal, model);
		else if (model.isToBeDeleted())
			eliminaConBulk(principal, model);
		return model;
	}

	/**
	 * Esegue una operazione di creazione di un T. Pre-post-conditions: Nome:
	 * Non passa validazione applicativa Pre: l'T non passa i criteri di
	 * validità applicativi Post: Viene generata CRUDValidationException che
	 * descrive l'errore di validazione. Nome: Non passa validazione per
	 * violazione di vincoli della base di dati Pre: l'T contiene qualche
	 * attributo nullo in corrispondenza di campi NOT_NULLABLE o qualche
	 * attributo stringa troppo lungo per i corrispondenti campi fisici. Post:
	 * Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o
	 * una CRUDTooLargeConstraintException con la descrizione dell'errore Nome:
	 * Errore di chiave duplicata Pre: Esiste già un T persistente che possiede
	 * la stessa chiave primaria di quello specificato. Post: Viene generata una
	 * CRUDDuplicateKeyException con la descrizione dell'errore e con una nuova
	 * istanza che rappresenta l'oggetto esistente in base dati che possiede la
	 * chiave duplicata. Nome: Vincolo di integrità referenziale Pre: l'T
	 * specificato non può essere creato perchè viene violato qualche vincolo di
	 * integrità referenziale Post: Viene generata una
	 * CRUDReferentialIntegrityException con la descrizione dell'errore Nome:
	 * Tutti i controlli superati Pre: Tutti i controlli precedenti superati
	 * Post: l'T viene crato fisicamente nella base dati e viene chiusa la
	 * transazione
	 */
	public T creaConBulk(Principal principal, T model)
			throws ComponentException {
		try {
			validaCreaConBulk(principal, model);
			return eseguiCreaConBulk(principal, model);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di persistenza di creaConBulk.
	 */
	protected T eseguiCreaConBulk(Principal principal, T oggettobulk)
			throws ComponentException, PersistencyException {
		makeBulkPersistent(principal, oggettobulk);
		return oggettobulk;
	}

	/**
	 * Esegue una la parte di validazione di creaConBulk.
	 */
	protected void validaCreaConBulk(Principal principal, T oggettobulk)
			throws ComponentException {
		if (oggettobulk.isToBeCreated() && oggettobulk.getId() != null) {
			T oggettobulk1 = (T) getHomeClass(oggettobulk).findByPrimaryKey(
					principal, oggettobulk);
			if (oggettobulk1 != null)
				throw new CRUDDuplicateKeyException(
						"Errore di chiave duplicata", oggettobulk, oggettobulk1);
		}
		validaCreaModificaConBulk(principal, oggettobulk);
	}

	/**
	 * Esegue una la parte di validazione comune a creaConBulk e modificaConBulk
	 */
	protected void validaCreaModificaConBulk(Principal principal,
			T oggettobulk) throws ComponentException {
		try {
			validateBulkForPersistency(principal, oggettobulk);
		} catch (OutdatedResourceException outdatedresourceexception) {
			throw handleException(outdatedresourceexception);
		} catch (BusyResourceException busyresourceexception) {
			throw handleException(busyresourceexception);
		} catch (PersistencyException persistencyexception) {
			throw handleException(persistencyexception);
		} catch (PrimaryKeyChangedException primarykeychangedexception) {
			throw handleException(primarykeychangedexception);
		}
	}

	/**
	 * Esegue una operazione di eliminazione di un T. Pre-post-conditions: Nome:
	 * Non passa validazione di business Pre: l'T non passa i criteri di
	 * validità di business per l'operazione di cancellazione Post: Viene
	 * generata una ComponentException con detail la ValidationException che
	 * descrive l'errore di validazione. Nome: Oggetto non trovato Pre: l'T
	 * specificato non esiste. Post: Viene generata una CRUDException con la
	 * descrizione dell'errore Nome: Oggetto scaduto Pre: l'T specificato è
	 * stato modificato da altri utenti dopo la lettura Post: Viene generata una
	 * CRUDException con la descrizione dell'errore Nome: Oggetto occupato Pre:
	 * l'T specificato è bloccato da qualche altro utente. Post: Viene generata
	 * una CRUDException con la descrizione dell'errore Nome: Oggetto non
	 * cancellabile Pre: l'T non è cancellabile per motivi applicativi Post:
	 * Viene generata una CRUDNotDeletableException con la descrizione
	 * dell'errore Nome: Vincolo di integrità referenziale Pre: l'T specificato
	 * non può essere cancellato perchè viene violato+ qualche vincolo di
	 * integrità referenziale Post: Viene generata una
	 * CRUDReferentialIntegrityException con la descrizione dell'errore Nome:
	 * Tutti i controlli superati Pre: Tutti i controlli precedenti superati
	 * Post: l'T eliminato fisicamente dalla base di dati e viene chiusa la
	 * transazione
	 */
	public void eliminaConBulk(Principal principal, T model)
			throws ComponentException {
		try {
			validaEliminaConBulk(principal, model);
			eseguiEliminaConBulk(principal, model);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di validazione di eliminaConBulk.
	 */
	protected void validaEliminaConBulk(Principal principal, T oggettobulk)
			throws ComponentException {
		try {
			validateBulkForPersistency(principal, oggettobulk);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di persistenza di eliminaConBulk.
	 */
	protected void eseguiEliminaConBulk(Principal principal, T oggettobulk)
			throws ComponentException, PersistencyException {
		try {
			super.makeBulkPersistent(principal, oggettobulk);
		} catch (NotDeletableException notdeletableexception) {
			if (notdeletableexception.getPersistent() != oggettobulk)
				throw handleException(notdeletableexception);
			else
				throw new CRUDNotDeletableException("Oggetto non eliminabile",
						notdeletableexception);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	public T inizializzaBulkPerInserimento(Principal principal,
			T oggettobulk) throws ComponentException {
		try {
			initializeKeysAndOptionsInto(principal, oggettobulk);
			return oggettobulk;
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Prepara un T per la presentazione all'utente per una possibile operazione
	 * di modifica. Pre-post-conditions: Nome: Oggetto non esistente Pre: L'T
	 * specificato non esiste. Post: Viene generata una CRUDException con la
	 * descrizione dell'errore. Nome: Tutti i controlli superati Pre: L'T
	 * specificato esiste. Post: Viene riletto l'T, inizializzato con tutti gli
	 * oggetti collegati e preparato per l'operazione di presentazione e
	 * modifica nell'interfaccia visuale. L'operazione di lettura viene
	 * effettuata con una FetchPolicy il cui nome è ottenuto concatenando il
	 * nome della component con la stringa ".edit"
	 */
	public T inizializzaBulkPerModifica(Principal principal, T oggettobulk)
			throws ComponentException {
		try {
			oggettobulk = getHomeClass(oggettobulk).findByPrimaryKey(
					principal, oggettobulk);
			if (oggettobulk == null) {
				throw new CRUDException(
						"Risorsa non pi\371 valida: \350 stata cancellata dall'ultimo caricamento.",
						null, oggettobulk);
			} else {
				initializeKeysAndOptionsInto(principal, oggettobulk);
				initializeForeignKey(principal, oggettobulk);
				return oggettobulk;
			}
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	/**
	 * Prepara un T per la presentazione all'utente per una possibile operazione
	 * di modifica. Pre-post-conditions: Nome: Oggetto non esistente Pre: L'T
	 * specificato non esiste. Post: Viene generata una CRUDException con la
	 * descrizione dell'errore. Nome: Tutti i controlli superati Pre: L'T
	 * specificato esiste. Post: Viene riletto l'T, inizializzato con tutti gli
	 * oggetti collegati e preparato per l'operazione di presentazione e
	 * modifica nell'interfaccia visuale. L'operazione di lettura viene
	 * effettuata con una FetchPolicy il cui nome è ottenuto concatenando il
	 * nome della component con la stringa ".edit"
	 */
	public T inizializzaBulkPerRicerca(Principal principal, T oggettobulk)
			throws ComponentException {
		try {
			initializeKeysAndOptionsInto(principal, oggettobulk);
			return oggettobulk;
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Prepara un T per la presentazione all'utente per una possibile operazione
	 * di ricerca libera. Nome: Tutti i controlli superati Pre: Post: l'T viene
	 * inizializzato con tutti gli oggetti collegati e preparato per l'utilizzo
	 * come prototipo in in una operazione di ricerca libera
	 */
	public T inizializzaBulkPerRicercaLibera(Principal principal,
			T oggettobulk) throws ComponentException {
		try {
			initializeKeysAndOptionsInto(principal, oggettobulk);
			return oggettobulk;
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una operazione di modifica di un T. Pre-post-conditions: Nome: Non
	 * passa validazione applicativa Pre: l'T non passa i criteri di validità
	 * applicativi per l'operazione di modifica Post: Viene generata
	 * CRUDValidationException che descrive l'errore di validazione. Nome: Non
	 * passa validazione per violazione di vincoli della base di dati Pre: l'T
	 * contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE
	 * o qualche attributo stringa troppo lungo per i corrispondenti campi
	 * fisici. Post: Viene generata una
	 * it.jada.stone.comp.CRUDNotNullConstraintException o una
	 * CRUDTooLargeConstraintException con la descrizione dell'errore Nome:
	 * Oggetto non trovato Pre: l'T specificato non esiste. Post: Viene generata
	 * una CRUDException con la descrizione dell'errore Nome: Oggetto scaduto
	 * Pre: l'T specificato è stato modificato da altri utenti dopo la lettura
	 * Post: Viene generata una CRUDException con la descrizione dell'errore
	 * Nome: Oggetto occupato Pre: l'T specificato è bloccato da qualche altro
	 * utente. Post: Viene generata una CRUDException con la descrizione
	 * dell'errore Nome: Tutti i controlli superati Pre: Tutti i controlli
	 * precedenti superati Post: l'T viene modificato fisicamente nella base
	 * dati e viene chiusa la transazione
	 */
	public T modificaConBulk(Principal principal, T oggettobulk)
			throws ComponentException {
		try {
			validaModificaConBulk(principal, oggettobulk);
			return eseguiModificaConBulk(principal, oggettobulk);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di validazione di modificaConBulk.
	 */
	protected void validaModificaConBulk(Principal principal, T oggettobulk)
			throws ComponentException {
		validaCreaModificaConBulk(principal, oggettobulk);
	}

	/**
	 * Esegue una la parte di persistenza di creaConBulk.
	 */
	protected T eseguiModificaConBulk(Principal principal, T oggettobulk)
			throws ComponentException, PersistencyException {
		makeBulkPersistent(principal, oggettobulk);
		return oggettobulk;
	}

}
