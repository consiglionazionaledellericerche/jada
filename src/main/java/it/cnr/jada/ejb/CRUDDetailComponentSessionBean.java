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

package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDDetailComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;

public class CRUDDetailComponentSessionBean extends CRUDComponentSessionBean implements CRUDDetailComponentSession {

    public CRUDDetailComponentSessionBean() {
    }

    public static CRUDComponentSessionBean newInstance() throws EJBException {
        return new CRUDDetailComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new CRUDDetailComponent();
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class class1, OggettoBulk oggettobulk, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            RemoteIterator remoteiterator = ((CRUDDetailComponent) componentObj).cerca(usercontext, compoundfindclause, class1, oggettobulk, s);
            component_invocation_succes(usercontext, componentObj);
            return remoteiterator;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            OggettoBulk oggettobulk2 = ((CRUDDetailComponent) componentObj).creaConBulk(usercontext, oggettobulk, oggettobulk1, s);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk2;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[], OggettoBulk oggettobulk, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            ((CRUDDetailComponent) componentObj).eliminaConBulk(usercontext, aoggettobulk, oggettobulk, s);
            component_invocation_succes(usercontext, componentObj);
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            ((CRUDDetailComponent) componentObj).eliminaConBulk(usercontext, oggettobulk, s);
            component_invocation_succes(usercontext, componentObj);
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            OggettoBulk oggettobulk2 = ((CRUDDetailComponent) componentObj).inizializzaBulkPerInserimento(usercontext, oggettobulk, oggettobulk1, s);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk2;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            OggettoBulk oggettobulk2 = ((CRUDDetailComponent) componentObj).inizializzaBulkPerModifica(usercontext, oggettobulk, oggettobulk1, s);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk2;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            OggettoBulk oggettobulk2 = ((CRUDDetailComponent) componentObj).modificaConBulk(usercontext, oggettobulk, oggettobulk1, s);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk2;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(usercontext, componentObj, error);
        }
    }
}