/*
 * Copyright 2008-2009 Italian National Research Council
 * 	All rights reserved
 */
package it.cnr.jada.ejb.session;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;

import java.util.List;

import javax.persistence.Query;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.Criterion;
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
		AbstractComponentSessionBean<T> implements
		CRUDComponentSession<T>{
	/**
	 * Default constructor.
	 */
	public CRUDComponentSessionBean() {
	}

	public T findByPrimaryKey(UserContext userContext, T oggettoBulk)
			throws ComponentException {
		return getHomeClass(oggettoBulk).findByPrimaryKey(userContext,
				oggettoBulk);
	}

	public void deleteByCriteria(UserContext userContext, Criteria criteria,
			Class<T> bulkClass) throws ComponentException {
		getHomeClass(bulkClass).deleteByCriteria(userContext, criteria);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(UserContext userContext, Criteria criteria)
			throws ComponentException {
		return criteria.prepareQuery(getManager()).getResultList();
	}

	public Long countByCriteria(UserContext userContext, Criteria criteria)
			throws ComponentException {
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.prepareQuery(getManager()).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(UserContext userContext, Criteria criteria,
			Integer firstResult, Integer maxResult) throws ComponentException {
		Query query = criteria.prepareQuery(getManager());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriterion(UserContext userContext,
			Criterion criterion, Class<T> bulkClass) throws ComponentException {
		Criteria criteria = select(userContext, criterion, bulkClass);
		return criteria.prepareQuery(getManager()).getResultList();
	}

	/**
	 * Implementazione fisica del metodo
	 * cerca(UserContext,CompoundFindClause,T). Costruisce l'istruzione SQL
	 * corrispondente ad una ricerca con le clausole specificate.
	 * L'implementazione standard invoca il metodo findByCriteria sull'Home
	 * dell'T specificato come prototipo.
	 */
	protected Criteria select(UserContext userContext, Criterion criterion,
			T oggettoBulk) throws ComponentException {
		return getHomeClass(oggettoBulk).selectByCriterion(userContext,
				criterion);
	}

	protected Criteria select(UserContext userContext, Criterion criterion,
			Class<T> bulkClass) throws ComponentException {
		return getHomeClass(bulkClass)
				.selectByCriterion(userContext, criterion);
	}

	public T persist(UserContext userContext, T model)
			throws ComponentException {
		if (model.isToBeCreated())
			return creaConBulk(userContext, model);
		else if (model.isToBeUpdated())
			return modificaConBulk(userContext, model);
		else if (model.isToBeDeleted())
			eliminaConBulk(userContext, model);
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
	public T creaConBulk(UserContext userContext, T model)
			throws ComponentException {
		try {
			validaCreaConBulk(userContext, model);
			return eseguiCreaConBulk(userContext, model);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di persistenza di creaConBulk.
	 */
	protected T eseguiCreaConBulk(UserContext usercontext, T oggettobulk)
			throws ComponentException, PersistencyException {
		makeBulkPersistent(usercontext, oggettobulk);
		return oggettobulk;
	}

	/**
	 * Esegue una la parte di validazione di creaConBulk.
	 */
	protected void validaCreaConBulk(UserContext userContext, T oggettobulk)
			throws ComponentException {
		if (oggettobulk.isToBeCreated() && oggettobulk.getId() != null) {
			T oggettobulk1 = (T) getHomeClass(oggettobulk).findByPrimaryKey(
					userContext, oggettobulk);
			if (oggettobulk1 != null)
				throw new CRUDDuplicateKeyException(
						"Errore di chiave duplicata", oggettobulk, oggettobulk1);
		}
		validaCreaModificaConBulk(userContext, oggettobulk);
	}

	/**
	 * Esegue una la parte di validazione comune a creaConBulk e modificaConBulk
	 */
	protected void validaCreaModificaConBulk(UserContext userContext,
			T oggettobulk) throws ComponentException {
		try {
			validateBulkForPersistency(userContext, oggettobulk);
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
	public void eliminaConBulk(UserContext userContext, T model)
			throws ComponentException {
		try {
			validaEliminaConBulk(userContext, model);
			eseguiEliminaConBulk(userContext, model);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di validazione di eliminaConBulk.
	 */
	protected void validaEliminaConBulk(UserContext usercontext, T oggettobulk)
			throws ComponentException {
		try {
			validateBulkForPersistency(usercontext, oggettobulk);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di persistenza di eliminaConBulk.
	 */
	protected void eseguiEliminaConBulk(UserContext usercontext, T oggettobulk)
			throws ComponentException, PersistencyException {
		try {
			super.makeBulkPersistent(usercontext, oggettobulk);
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

	public T inizializzaBulkPerInserimento(UserContext userContext,
			T oggettobulk) throws ComponentException {
		try {
			initializeKeysAndOptionsInto(userContext, oggettobulk);
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
	public T inizializzaBulkPerModifica(UserContext userContext, T oggettobulk)
			throws ComponentException {
		try {
			oggettobulk = getHomeClass(oggettobulk).findByPrimaryKey(
					userContext, oggettobulk);
			if (oggettobulk == null) {
				throw new CRUDException(
						"Risorsa non pi\371 valida: \350 stata cancellata dall'ultimo caricamento.",
						null, oggettobulk);
			} else {
				initializeKeysAndOptionsInto(userContext, oggettobulk);
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
	public T inizializzaBulkPerRicerca(UserContext userContext, T oggettobulk)
			throws ComponentException {
		try {
			initializeKeysAndOptionsInto(userContext, oggettobulk);
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
	public T inizializzaBulkPerRicercaLibera(UserContext userContext,
			T oggettobulk) throws ComponentException {
		try {
			initializeKeysAndOptionsInto(userContext, oggettobulk);
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
	public T modificaConBulk(UserContext userContext, T oggettobulk)
			throws ComponentException {
		try {
			validaModificaConBulk(userContext, oggettobulk);
			return eseguiModificaConBulk(userContext, oggettobulk);
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	/**
	 * Esegue una la parte di validazione di modificaConBulk.
	 */
	protected void validaModificaConBulk(UserContext usercontext, T oggettobulk)
			throws ComponentException {
		validaCreaModificaConBulk(usercontext, oggettobulk);
	}

	/**
	 * Esegue una la parte di persistenza di creaConBulk.
	 */
	protected T eseguiModificaConBulk(UserContext userContext, T oggettobulk)
			throws ComponentException, PersistencyException {
		makeBulkPersistent(userContext, oggettobulk);
		return oggettobulk;
	}

}
