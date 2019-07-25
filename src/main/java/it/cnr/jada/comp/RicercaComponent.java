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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Una componente con responsabilit  di ricerca. L'interfaccia pubblica possiede due metodi cerca che implementano
 * la ricerca di un OggettoBulk (senza vincoli e contestuale ad un altro OggettoBulk)
 */
public class RicercaComponent extends GenericComponent implements Serializable, IRicercaMgr {

    public RicercaComponent() {
    }

    /**
     * Esegue una operazione di ricerca di un OggettoBulk con clausole.
     * Pre-post-conditions:
     * Nome: Clausole non specificate
     * Pre: L'albero delle clausole non   specficato (nullo)
     * Post: Viene generato un albero di clausole usando tutti i valori non nulli degli attributi dell'OggettoBulk
     * specificato come prototipo. L'elenco degli attributi da utilizzare per ottenere le clausole   estratto
     * dal BulkInfo dell'OggettoBulk
     * Nome: Tutti i controlli superati
     * Pre: Albero delle clausole di ricerca specificato (non nullo)
     * Post: Viene effettuata una ricerca di OggettoBulk compatibili con il bulk specificato.
     * La ricerca deve essere effettuata utilizzando le clausole specificate da "clausole".
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome   ottenuto concatenando il
     * nome della component con la stringa ".find"
     */
    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException {
        try {
            return iterator(usercontext, select(usercontext, compoundfindclause, oggettobulk), oggettobulk.getClass(), getFetchPolicyName("find"));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Esegue una operazione di ricerca su un attributo di un OggettoBulk con clausole.
     * Il risultato della ricerca deve fornire un'elenco di OggettoBulk compatibili e contestuali con
     * l'attributo e l'OggettoBulk specificato.
     * Pre-post-conditions:
     * Nome: Clausole non specificate
     * Pre: L'albero delle clausole non   specficato (nullo)
     * Post: Viene generato un albero di clausole usando tutti i valori non nulli degli attributi dell'OggettoBulk
     * specificato come prototipo. L'elenco degli attributi da utilizzare per ottenere le clausole   estratto
     * dal BulkInfo dell'OggettoBulk
     * Nome: Tutti i controlli superati
     * Pre: Albero delle clausole di ricerca specificato (non nullo)
     * Post: Viene effettuata una ricerca di possibili OggettoBulk da assegnare all'attributo "attributo"
     * dell'OggettoBulk "contesto". La ricerca deve essere effettuata utilizzando le clausole specificate
     * da "clausole". L'operazione di lettura viene effettuata con una FetchPolicy il cui nome   ottenuto
     * concatenando il nome della component con la stringa ".find." e il nome dell'attributo
     */
    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String attributo) throws ComponentException {
        try {
            return iterator(usercontext, select(usercontext, compoundfindclause, oggettobulk, oggettobulk1, attributo), oggettobulk != null ? oggettobulk.getClass() : Introspector.getPropertyType(oggettobulk1.getClass(), attributo), getFetchPolicyName("find", attributo));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    /**
     * Implementazione fisica del metodo cerca(UserContext,CompoundFindClause,OggettoBulk).
     * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole specificate.
     * L'implementazione standard invoca il metodo selectByClause sull'Home dell'OggettoBulk specificato
     * come prototipo.
     */
    protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        if (compoundfindclause == null) {
            if (oggettobulk != null)
                compoundfindclause = oggettobulk.buildFindClauses(null);
        } else {
            compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
        }
        return getHome(usercontext, oggettobulk).selectByClause(usercontext, compoundfindclause);
    }

    /**
     * Implementazione fisica del metodo cerca(UserContext,CompoundFindClause,OggettoBulk).
     * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole specificate.
     * L'implementazione standard invoca il metodo selectByClause sull'Home dell'OggettoBulk specificato
     * come prototipo.
     */
    protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String homeMethodName) throws ComponentException, PersistencyException {
        try {
            if (compoundfindclause == null) {
                if (oggettobulk != null)
                    compoundfindclause = oggettobulk.buildFindClauses(null);
            } else {
                compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
            }
            try {
                return (Query) Introspector.invoke(getHome(usercontext, oggettobulk), homeMethodName, usercontext, oggettobulk, compoundfindclause);
            } catch (NoSuchMethodException ex) {
                return getHome(usercontext, oggettobulk).selectByClause(usercontext, compoundfindclause);
            }
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception.getTargetException());
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
    protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk bulk, OggettoBulk contesto, String attributo) throws ComponentException, PersistencyException {
        try {
            if (compoundfindclause == null) {
                if (bulk != null)
                    compoundfindclause = bulk.buildFindClauses(null);
            } else {
                compoundfindclause = CompoundFindClause.and(compoundfindclause, bulk.buildFindClauses(Boolean.FALSE));
            }
            try {
                return (Query) Introspector.invoke(this, "select", attributo + "ByClause", usercontext, contesto, bulk, compoundfindclause);
            } catch (NoSuchMethodException _ex) {
                try {
                    return (Query) Introspector.invoke(getHome(usercontext, contesto), "select", attributo + "ByClause", usercontext, contesto, getHome(usercontext, bulk), bulk, compoundfindclause);
                } catch (NoSuchMethodException ex) {
                    return getHome(usercontext, contesto).selectOptionsByClause(attributo, contesto, getHome(usercontext, bulk), bulk, compoundfindclause);
                }
            }
        } catch (InvocationTargetException invocationtargetexception) {
            throw handleException(invocationtargetexception.getTargetException());
        } catch (IllegalAccessException illegalaccessexception) {
            throw handleException(illegalaccessexception);
        }
    }


    public Persistent findByPrimaryKey(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            return getHome(usercontext, oggettobulk).findByPrimaryKey((Persistent) oggettobulk);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public <T extends OggettoBulk, U extends OggettoBulk> List<U> find(UserContext usercontext, Class<T> contesto, String methodName, Object... parameters) throws ComponentException {
        try {
            return Optional.ofNullable(Introspector.invoke(getHome(usercontext, contesto), methodName, parameters))
                    .filter(List.class::isInstance)
                    .map(List.class::cast)
                    .orElse(Collections.emptyList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue una operazione di ricerca di un OggettoBulk con clausole.
     *
     * @param homeMethodName Specificare il metodo della home da richiamare altrimenti verrà eseguito il fallback su select
     *                       Pre-post-conditions:
     *                       Nome: Clausole non specificate
     *                       Pre: L'albero delle clausole non   specficato (nullo)
     *                       Post: Viene generato un albero di clausole usando tutti i valori non nulli degli attributi dell'OggettoBulk
     *                       specificato come prototipo. L'elenco degli attributi da utilizzare per ottenere le clausole   estratto
     *                       dal BulkInfo dell'OggettoBulk
     *                       Nome: Tutti i controlli superati
     *                       Pre: Albero delle clausole di ricerca specificato (non nullo)
     *                       Post: Viene effettuata una ricerca di OggettoBulk compatibili con il bulk specificato.
     *                       La ricerca deve essere effettuata utilizzando le clausole specificate da "clausole".
     *                       L'operazione di lettura viene effettuata con una FetchPolicy il cui nome   ottenuto concatenando il
     *                       nome della component con la stringa ".find"
     */
    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String homeMethodName) throws ComponentException {
        try {
            return iterator(usercontext, select(usercontext, compoundfindclause, oggettobulk, homeMethodName), oggettobulk.getClass(), getFetchPolicyName("find"));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

}