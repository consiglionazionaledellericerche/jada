/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.PrimaryKeyChangedException;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.NotDeletableException;

import java.io.Serializable;

/**
 * Componente generica per effettuare operazioni di CRUD su una classe di OggettiBulk.
 * Una CRUDComponent offre i seguenti servizi:
 * <p>
 * * Creazione, modifica e cancellazione su base dati di OggettiBulk persistenti;
 * * Controllo di consistenza transazionale per le operazioni CRUD;
 * * Validazioni standard per le operazioni di creazione e modifica: campi NOT_NULLABLE e lunghezza
 * massima di campi di testo;
 * * Riempimento automatico di attributi "keys" e "options" specificati nei BulkInfo;
 * * Gestione automatica di ricerche semplici (basate sui valori degli attributi persistenti di un OggettoBulk)
 * e ricerche complesse (specificate da alberi logici di clausole).
 * * Gestione automatica di ricerche semplici e complesse per attributi secondari dell'oggetto principale.
 *
 * @author mspasiano
 * <p>
 * Mar 15, 2007 12:36:14 PM
 */
public class CRUDComponent extends RicercaComponent implements IMultipleCRUDMgr, Cloneable, Serializable, Component {
    private static final long serialVersionUID = 1L;

    public CRUDComponent() {
    }

    /**
     * Esegue una operazione di creazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione applicativa
     * Pre: l'OggettoBulk non passa i criteri di validità applicativi
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE o
     * qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Errore di chiave duplicata
     * Pre: Esiste già un OggettoBulk persistente che possiede la stessa chiave primaria di quello specificato.
     * Post: Viene generata una CRUDDuplicateKeyException con la descrizione dell'errore e con una nuova
     * istanza che rappresenta l'oggetto esistente in base dati che possiede la chiave duplicata.
     * Nome: Vincolo di integrità referenziale Pre: l'OggettoBulk specificato non può essere creato perchè
     * viene violato qualche vincolo di integrità referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene creato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException {
        try {
            for (int i = 0; i < aoggettobulk.length; i++)
                validaCreaConBulk(usercontext, aoggettobulk[i]);

            for (int j = 0; j < aoggettobulk.length; j++)
                aoggettobulk[j] = eseguiCreaConBulk(usercontext, aoggettobulk[j]);

            return aoggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di creazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione applicativa
     * Pre: l'OggettoBulk non passa i criteri di validità applicativi
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE o
     * qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Errore di chiave duplicata
     * Pre: Esiste già un OggettoBulk persistente che possiede la stessa chiave primaria di quello specificato.
     * Post: Viene generata una CRUDDuplicateKeyException con la descrizione dell'errore e con una nuova
     * istanza che rappresenta l'oggetto esistente in base dati che possiede la chiave duplicata.
     * Nome: Vincolo di integrità referenziale Pre: l'OggettoBulk specificato non può essere creato perchè
     * viene violato qualche vincolo di integrità referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene crato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            validaCreaConBulk(usercontext, oggettobulk);
            return eseguiCreaConBulk(usercontext, oggettobulk);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di eliminazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione di business
     * Pre: l'OggettoBulk non passa i criteri di validità di business per l'operazione di cancellazione
     * Post: Viene generata una ComponentException con detail la ValidationException che descrive l'errore di validazione.
     * Nome: Oggetto non trovato
     * Pre: l'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto non cancellabile
     * Pre: l'OggettoBulk non è cancellabile per motivi applicativi
     * Post: Viene generata una CRUDNotDeletableException con la descrizione dell'errore
     * Nome: Vincolo di integrità referenziale
     * Pre: l'OggettoBulk specificato non può essere cancellato perchè viene violato+ qualche vincolo di integrità referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk eliminato fisicamente dalla base di dati e viene chiusa la transazione
     */
    public void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException {
        try {
            for (int i = 0; i < aoggettobulk.length; i++)
                validaEliminaConBulk(usercontext, aoggettobulk[i]);

            for (int j = 0; j < aoggettobulk.length; j++)
                eseguiEliminaConBulk(usercontext, aoggettobulk[j]);

        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di eliminazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione di business
     * Pre: l'OggettoBulk non passa i criteri di validità di business per l'operazione di cancellazione
     * Post: Viene generata una ComponentException con detail la ValidationException che descrive l'errore di validazione.
     * Nome: Oggetto non trovato
     * Pre: l'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto non cancellabile
     * Pre: l'OggettoBulk non è cancellabile per motivi applicativi
     * Post: Viene generata una CRUDNotDeletableException con la descrizione dell'errore
     * Nome: Vincolo di integrità referenziale
     * Pre: l'OggettoBulk specificato non può essere cancellato perchè viene violato+ qualche vincolo di integrità referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk eliminato fisicamente dalla base di dati e viene chiusa la transazione
     */
    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            validaEliminaConBulk(usercontext, oggettobulk);
            eseguiEliminaConBulk(usercontext, oggettobulk);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di persistenza di creaConBulk.
     */
    protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        makeBulkPersistent(usercontext, oggettobulk, false);
        return oggettobulk;
    }

    /**
     * Esegue una la parte di persistenza di creaConBulk.
     */
    protected void eseguiEliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        try {
            super.makeBulkPersistent(usercontext, oggettobulk, false);
        } catch (NotDeletableException notdeletableexception) {
            if (notdeletableexception.getPersistent() != oggettobulk)
                throw handleException(notdeletableexception);
            else
                throw new CRUDNotDeletableException("Oggetto non eliminabile", notdeletableexception);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di persistenza di creaConBulk.
     */
    protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        makeBulkPersistent(usercontext, oggettobulk, false);
        return oggettobulk;
    }

    /**
     * Gestione standard delle eccezioni per una operazione CRUD.
     * La gestione standard prevede una gestione specifica per:
     * I metodi che utilizzano questo servizio devono dichiarare la clausola throws it.jada.stone.comp.ComponentException
     * Es. di codice per la gestione delle eccezioni:
     * try {
     * // codice ...
     * } catch(MiaEccezione e) {
     * // gestione di eccezioni personalizzata
     * } catch(Throwable e) {
     * throw handleException(e,bulk);
     * } finally {
     * // blocco finally se necessario
     * }
     */
    protected ComponentException handleException(OggettoBulk oggettobulk, Throwable throwable) {
        return handleException(throwable);
    }

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException {
        try {
            initializeKeysAndOptionsInto(usercontext, oggettobulk);
            return oggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile operazione di creazione.
     * Pre-post-conditions:
     * Nome: Tutti i controlli superati
     * Pre:
     * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per
     * una operazione di creazione
     */
    public OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException {
        for (int i = 0; i < aoggettobulk.length; i++)
            aoggettobulk[i] = inizializzaBulkPerModifica(usercontext, aoggettobulk[i]);
        return aoggettobulk;
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile operazione di modifica.
     * Pre-post-conditions:
     * Nome: Oggetto non esistente
     * Pre: L'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * Nome: Tutti i controlli superati
     * Pre: L'OggettoBulk specificato esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
     * per l'operazione di presentazione e modifica nell'interfaccia visuale.
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è ottenuto concatenando
     * il nome della component con la stringa ".edit"
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            it.cnr.jada.bulk.BulkHome bulkhome = getHome(usercontext, oggettobulk.getClass(), null, getFetchPolicyName("edit"));
            oggettobulk = (OggettoBulk) bulkhome.findByPrimaryKey(usercontext, oggettobulk);
            if (oggettobulk == null) {
                throw new CRUDException("Risorsa non pi\371 valida: \350 stata cancellata dall'ultimo caricamento.", null, oggettobulk);
            } else {
                initializeKeysAndOptionsInto(usercontext, oggettobulk);
                getHomeCache(usercontext).fetchAll(usercontext, bulkhome);
                return oggettobulk;
            }
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile operazione di modifica.
     * Pre-post-conditions:
     * Nome: Oggetto non esistente
     * Pre: L'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * Nome: Tutti i controlli superati
     * Pre: L'OggettoBulk specificato esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
     * per l'operazione di presentazione e modifica nell'interfaccia visuale.
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è ottenuto concatenando
     * il nome della component con la stringa ".edit"
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            initializeKeysAndOptionsInto(usercontext, oggettobulk);
            return oggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile operazione di ricerca libera.
     * Nome: Tutti i controlli superati
     * Pre:
     * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
     * per l'utilizzo come prototipo in in una operazione di ricerca libera
     */
    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            initializeKeysAndOptionsInto(usercontext, oggettobulk);
            return oggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di modifica di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione applicativa
     * Pre: l'OggettoBulk non passa i criteri di validità applicativi per l'operazione di modifica
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE
     * o qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Oggetto non trovato
     * Pre: l'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[]) throws ComponentException {
        try {
            for (int i = 0; i < aoggettobulk.length; i++)
                validaModificaConBulk(usercontext, aoggettobulk[i]);

            for (int j = 0; j < aoggettobulk.length; j++)
                aoggettobulk[j] = eseguiModificaConBulk(usercontext, aoggettobulk[j]);

            return aoggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di modifica di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione applicativa
     * Pre: l'OggettoBulk non passa i criteri di validità applicativi per l'operazione di modifica
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE
     * o qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Oggetto non trovato
     * Pre: l'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            validaModificaConBulk(usercontext, oggettobulk);
            return eseguiModificaConBulk(usercontext, oggettobulk);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di validazione di creaConBulk.
     */
    protected void validaCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        if ((oggettobulk instanceof KeyedPersistent) && oggettobulk.isToBeCreated())
            try {
                OggettoBulk oggettobulk1 = (OggettoBulk) getHome(usercontext, oggettobulk).findByPrimaryKey(oggettobulk);
                if (oggettobulk1 != null)
                    throw new CRUDDuplicateKeyException("Errore di chiave duplicata", oggettobulk, oggettobulk1);
            } catch (PersistencyException persistencyexception) {
                throw new ComponentException(persistencyexception);
            }
        validaCreaModificaConBulk(usercontext, oggettobulk);
    }

    /**
     * Esegue una la parte di validazione comune a creaConBulk e modificaConBulk
     */
    protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            validateBulkForPersistency(usercontext, oggettobulk);
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
     * Esegue una la parte di validazione di eliminaConBulk.
     */
    protected void validaEliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            validateBulkForPersistency(usercontext, oggettobulk);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di validazione di modificaConBulk.
     */
    protected void validaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        validaCreaModificaConBulk(usercontext, oggettobulk);
    }
}