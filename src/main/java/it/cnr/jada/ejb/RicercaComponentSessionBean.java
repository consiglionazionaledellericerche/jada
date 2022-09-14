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
import it.cnr.jada.comp.Component;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.comp.RicercaComponent;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import java.util.List;

public class RicercaComponentSessionBean extends GenericComponentSessionBean implements RicercaComponentSession {
    protected Component componentObj;

    public RicercaComponentSessionBean() {
    }

    public static RicercaComponentSessionBean newInstance() throws EJBException {
        return new RicercaComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() throws CreateException {
        componentObj = new RicercaComponent();
    }

    @Remove
    public void ejbRemove() throws javax.ejb.EJBException {
        componentObj.release();
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            RemoteIterator remoteiterator = ((RicercaComponent) componentObj).cerca(usercontext, compoundfindclause, oggettobulk);
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

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            RemoteIterator remoteiterator = ((RicercaComponent) componentObj).cerca(usercontext, compoundfindclause, oggettobulk, oggettobulk1, s);
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

    public Persistent findByPrimaryKey(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            Persistent persistent = ((RicercaComponent) componentObj).findByPrimaryKey(userContext, oggettoBulk);
            component_invocation_succes(userContext, componentObj);
            return persistent;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(userContext, componentObj, error);
        }
    }

    public <T extends OggettoBulk, U extends OggettoBulk> List<U> find(UserContext userContext, Class<T> contesto, String methodName, Object... parameters) throws ComponentException, EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            List<U> result = ((RicercaComponent) componentObj).find(userContext, contesto, methodName, parameters);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (NoRollbackException norollbackexception) {
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        } catch (ComponentException componentexception) {
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        } catch (RuntimeException runtimeexception) {
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        } catch (Error error) {
            throw uncaughtError(userContext, componentObj, error);
        }
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String homeMethodName) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            RemoteIterator remoteiterator = ((RicercaComponent) componentObj).cerca(usercontext, compoundfindclause, oggettobulk, homeMethodName);
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

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String homeMethodName, Object... parameters) throws ComponentException, EJBException {
        pre_component_invocation(usercontext, componentObj);
        try {
            RemoteIterator remoteiterator = ((RicercaComponent) componentObj).cerca(usercontext, compoundfindclause, oggettobulk, homeMethodName, parameters);
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
}