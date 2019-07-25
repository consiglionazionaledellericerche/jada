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
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.NotDeletableException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author mspasiano
 * <p>
 * Mar 16, 2007 12:34:43 PM
 */
public class CRUDDetailComponent extends CRUDComponent implements ICRUDDetailMgr, Serializable {

    public CRUDDetailComponent() {
    }

    /**
     * Esegue una operazione di ricerca su un attributo di un OggettoBulk con clausole.
     * Il risultato della ricerca deve fornire un'elenco di OggettoBulk compatibili e contestuali con
     * l'attributo e l'OggettoBulk specificato.
     * Pre-post-conditions:
     * Nome: Clausole non specificate
     * Pre: Viene richiesta la ricerca dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. L'albero delle clausole non e specificato (nullo)
     * Post: Viene generato un albero di clausole usando tutti i valori non nulli degli attributi
     * dell'OggettoBulk specificato come prototipo. L'elenco degli attributi da utilizzare per
     * ottenere le clausole estratto dal BulkInfo dell'OggettoBulk
     * Nome: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. Tutti i controlli superati
     * Pre: Albero delle clausole di ricerca specificato (non nullo)
     * Post: Viene effettuata una ricerca di possibili OggettoBulk da assegnare all'attributo "attributo"
     * dell'OggettoBulk "contesto". La ricerca deve essere effettuata utilizzando le clausole
     * specificate da "clausole".
     */
    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class bulkClass, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            return iterator(usercontext, select(usercontext, compoundfindclause, bulkClass, contesto, attributo), bulkClass, getFetchPolicyName("find", attributo));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di creazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non passa validazione applicativa
     * Pre: l'OggettoBulk non passa i criteri di validita applicativi
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' contiene qualche attributo nullo in corrispondenza
     * di campi NOT_NULLABLE o qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Errore di chiave duplicata
     * Pre: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. Esiste gia un OggettoBulk persistente che possiede
     * la stessa chiave primaria di quello specificato.
     * Post: Viene generata una CRUDDuplicateKeyException con la descrizione dell'errore e con una nuova
     * istanza che rappresenta l'oggetto esistente in base dati che possiede la chiave duplicata.
     * Nome: Vincolo di integrita referenziale
     * Pre: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito della relazione dal
     * nome 'attributo' che lo lega con l'oggetto 'contesto'. 'bulk' non puo essere creato perche
     * viene violato qualche vincolo di integrita referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta la creazione dell'oggetto 'bulk' nell'ambito
     * della relazione dal nome 'attributo' che lo lega con l'oggetto 'contesto'.
     * Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene crato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            return (OggettoBulk) Introspector.invoke(this, "crea", attributo + "ConBulk", usercontext, bulk, contesto);
        } catch (NoSuchMethodException _ex) {
            return creaConBulk(usercontext, bulk);
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Esegue una operazione di eliminazione di un array di OggettoBulk. Per ogni elemento dell'array:
     * Pre-post-conditions:
     * Nome: Non passa validazione di business
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione
     * dal nome 'attributo' che lo lega con l'oggetto 'contesto'. 'bulk' non passa i criteri di
     * validità di business per l'operazione di cancellazione
     * Post: Viene generata una ComponentException con detail la ValidationException che descrive
     * l'errore di validazione.
     * Nome: Oggetto non trovato
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. l'OggettoBulk specificato e stato modificato da altri utenti
     * dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' e bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto non cancellabile
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non e cancellabile per motivi applicativi
     * Post: Viene generata una CRUDNotDeletableException con la descrizione dell'errore
     * Nome: Vincolo di integrita referenziale
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non puo essere cancellato perche viene violato
     * qualche vincolo di integrita referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. Tutti i controlli precedenti superati
     * Post: l'OggettoBulk eliminato fisicamente dalla base di dati e viene chiusa la transazione
     */
    public void eliminaConBulk(UserContext usercontext, OggettoBulk bulks[], OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            Introspector.invoke(this, "elimina", attributo + "ConBulk", usercontext, bulks, contesto);
        } catch (NoSuchMethodException _ex) {
            eliminaConBulk(usercontext, bulks);
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Esegue una operazione di eliminazione di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione di business
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione
     * dal nome 'attributo' che lo lega con l'oggetto 'contesto'. 'bulk' non passa i criteri di
     * validita di business per l'operazione di cancellazione
     * Post: Viene generata una ComponentException con detail la ValidationException che descrive
     * l'errore di validazione.
     * Nome: Oggetto non trovato
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. l'OggettoBulk specificato e stato modificato da altri utenti
     * dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' e bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto non cancellabile
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non e cancellabile per motivi applicativi
     * Post: Viene generata una CRUDNotDeletableException con la descrizione dell'errore
     * Nome: Vincolo di integrita referenziale
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non puo essere cancellato perche viene violato
     * qualche vincolo di integrita referenziale
     * Post: Viene generata una CRUDReferentialIntegrityException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta la eliminazione dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. Tutti i controlli precedenti superati
     * Post: l'OggettoBulk eliminato fisicamente dalla base di dati e viene chiusa la transazione
     */

    public void eliminaConBulk(UserContext usercontext, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            Introspector.invoke(this, "elimina", attributo + "ConBulk", usercontext, contesto);
        } catch (NoSuchMethodException _ex) {
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Esegue una la parte di persistenza di creaConBulk.
     */
    protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException, PersistencyException {
        makeBulkPersistent(usercontext, bulk, false);
        return bulk;
    }

    /**
     * Esegue una la parte di persistenza di eliminaConBulk.
     */
    protected void eseguiEliminaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException, PersistencyException {
        try {
            super.makeBulkPersistent(usercontext, bulk, false);
        } catch (NotDeletableException notdeletableexception) {
            if (notdeletableexception.getPersistent() != bulk)
                throw handleException(notdeletableexception);
            else
                throw new CRUDNotDeletableException("Oggetto non eliminabile", notdeletableexception);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di persistenza di modificaConBulk.
     */
    protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException, PersistencyException {
        makeBulkPersistent(usercontext, bulk, false);
        return bulk;
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile operazione di creazione.
     * Pre-post-conditions:
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta l'inizializzazione per inserimento dell'oggetto 'bulk' nell'ambito della relazione
     * dal nome 'attributo' che lo lega con l'oggetto 'contesto'.
     * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            return (OggettoBulk) Introspector.invoke(this, "inizializza", attributo + "BulkPerInserimento", usercontext, bulk, contesto);
        } catch (NoSuchMethodException _ex) {
            return inizializzaBulkPerInserimento(usercontext, bulk);
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Prepara un' OggettoBulk per la presentazione all'utente per una possibile operazione di modifica.
     * Pre-post-conditions:
     * Nome: Oggetto non esistente
     * Pre: Viene richiesta l'inizializzazione per modifica dell'oggetto 'bulk' nell'ambito della relazione
     * dal nome 'attributo' che lo lega con l'oggetto 'contesto'. 'bulk' non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta l'inizializzazione per modifica dell'oggetto 'bulk' nell'ambito della relazione
     * dal nome 'attributo' che lo lega con l'oggetto 'contesto'. 'bulk' esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato per
     * l'operazione di presentazione e modifica nell'interfaccia visuale.
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            return (OggettoBulk) Introspector.invoke(this, "inizializza", attributo + "BulkPerModifica", usercontext, bulk, contesto);
        } catch (NoSuchMethodException _ex) {
            return bulk;
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Esegue una operazione di modifica di un OggettoBulk.
     * Pre-post-conditions:
     * Nome: Non passa validazione applicativa
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non passa i criteri di validità applicativi per
     * l'operazione di modifica
     * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
     * Nome: Non passa validazione per violazione di vincoli della base di dati
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' contiene qualche attributo nullo in corrispondenza di
     * campi NOT_NULLABLE o qualche attributo stringa troppo lungo per i corrispondenti campi fisici.
     * Post: Viene generata una it.jada.stone.comp.CRUDNotNullConstraintException o una
     * CRUDTooLargeConstraintException con la descrizione dell'errore
     * Nome: Oggetto non trovato
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto scaduto
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' e stato modificato da altri utenti dopo la lettura
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Oggetto occupato
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. 'bulk' e bloccato da qualche altro utente.
     * Post: Viene generata una CRUDException con la descrizione dell'errore
     * Nome: Tutti i controlli superati
     * Pre: Viene richiesta la modifica dell'oggetto 'bulk' nell'ambito della relazione dal nome 'attributo'
     * che lo lega con l'oggetto 'contesto'. Tutti i controlli precedenti superati
     * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
     */
    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            return (OggettoBulk) Introspector.invoke(this, "modifica", attributo + "ConBulk", usercontext, bulk, contesto);
        } catch (NoSuchMethodException nosuchmethodexception) {
            try {
                validaModificaConBulk(usercontext, bulk, contesto, attributo);
                eseguiModificaConBulk(usercontext, bulk, contesto, attributo);
                return bulk;
            } catch (Throwable _ex) {
                throw handleException(nosuchmethodexception);
            }
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Implementazione fisica del metodo cerca(UserContext,CompoundFindClause,OggettoBulk,OggettoBulk,String).
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * L'implementazione standard invoca il metodo selectOptionsByClause sull'Home dell'OggettoBulk specificato
     * come contesto della ricerca.
     */
    protected SQLBuilder select(UserContext usercontext, CompoundFindClause compoundfindclause, Class bulkClass, OggettoBulk contesto, String attribute) throws ComponentException, PersistencyException {
        try {
            try {
                return (SQLBuilder) Introspector.invoke(this, "select", attribute + "ByClause", usercontext, contesto, bulkClass, compoundfindclause);
            } catch (NoSuchMethodException _ex) {
                return getHome(usercontext, contesto).selectOptionsByClause(attribute, contesto, getHome(usercontext, bulkClass), null, compoundfindclause);
            }
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception.getTargetException());
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }

    /**
     * Esegue una la parte di validazione di creaConBulk.
     */
    protected void validaCreaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        if ((bulk instanceof KeyedPersistent) && bulk.isToBeCreated())
            try {
                OggettoBulk oggettobulk2 = (OggettoBulk) getHome(usercontext, bulk).findByPrimaryKey(bulk);
                if (oggettobulk2 != null)
                    throw new CRUDDuplicateKeyException("Errore di chiave duplicata", bulk, oggettobulk2);
            } catch (PersistencyException persistencyexception) {
                throw new ComponentException(persistencyexception);
            }
        validaCreaModificaConBulk(usercontext, bulk);
    }

    /**
     * Esegue una la parte di validazione comune a creaConBulk e modificaConBulk
     */
    protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            validateBulkForPersistency(usercontext, bulk);
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
    protected void validaEliminaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        try {
            validateBulkForPersistency(usercontext, bulk);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una la parte di validazione di modificaConBulk.
     */
    protected void validaModificaConBulk(UserContext usercontext, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException {
        validaCreaModificaConBulk(usercontext, bulk);
    }
}