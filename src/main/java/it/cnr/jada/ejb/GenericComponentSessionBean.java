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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.Component;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Stateless
public abstract class GenericComponentSessionBean implements GenericComponentSession {
    @Resource
    private SessionContext context;

    public GenericComponentSessionBean() {
    }

    protected final void component_invocation_failure(UserContext usercontext, Component component) throws EJBException {
        if (usercontext != null && usercontext.isTransactional()) {
            try {
                Statement statement = component.getConnection(usercontext).createStatement();
                statement.execute("ROLLBACK TO SAVEPOINT " + getSavepointName());
                statement.close();
            } catch (Throwable _ex) {
            }
        } else {
            context.setRollbackOnly();
        }
        component.release(usercontext);
    }

    protected final void component_invocation_succes(UserContext usercontext, Component component) throws EJBException {
        if (usercontext == null || !usercontext.isTransactional()) {
            try {
                EJBCommonServices.unlockTransaction();
            } catch (EJBException ejbexception) {
                throw new EJBException("Can't unlock transaction", ejbexception);
            }
        }
        component.release(usercontext);
    }

    private String getSavepointName() {
        return "SP" + Math.abs(getClass().getName().hashCode());
    }

    protected final void pre_component_invocation(UserContext usercontext, Component component) throws EJBException {
        if (usercontext != null)
            if (usercontext.isTransactional()) {
                try {
                    Statement statement = component.getConnection(usercontext).createStatement();
                    statement.execute("SAVEPOINT " + getSavepointName());
                    statement.close();
                } catch (EJBException ejbexception) {
                    throw new EJBException("Can't initialize SAVEPOINT", ejbexception);
                } catch (SQLException sqlexception) {
                    throw new EJBException("Can't initialize SAVEPOINT", sqlexception);
                } catch (ComponentException e) {
                    throw new EJBException("Can't initialize SAVEPOINT", e);
                }
            } else {
                try {
                    EJBCommonServices.lockTransaction();
                } catch (EJBException ejbexception1) {
                    throw new EJBException("Can't lock transaction", ejbexception1);
                }
            }
        component.initialize();
    }

    protected final Error uncaughtError(UserContext usercontext, Component component, Error error) throws EJBException, Error {
        component_invocation_failure(usercontext, component);
        if (Optional.ofNullable(usercontext).map(u -> u.isTransactional()).orElse(false))
            throw new DetailedRuntimeException(error);
        else
            return error;
    }

    protected final RuntimeException uncaughtRuntimeException(UserContext usercontext, Component component, RuntimeException runtimeexception) throws RuntimeException {
        component_invocation_failure(usercontext, component);
        if (Optional.ofNullable(usercontext).map(u -> u.isTransactional()).orElse(false))
            return new DetailedRuntimeException(runtimeexception);
        else
            throw runtimeexception;
    }

    @Remove
    public abstract void ejbRemove() throws javax.ejb.EJBException;

    public String getTransactionalInterface() {
        String name = this.getClass().getName();
        name = name.substring(0, name.lastIndexOf("Bean"));
        int i = name.lastIndexOf('.');
        name = name.substring(0, i + 1) + "Transactional" + name.substring(i + 1);
        return name;
    }
}