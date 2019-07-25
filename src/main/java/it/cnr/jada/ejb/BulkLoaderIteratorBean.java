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
import it.cnr.jada.comp.NotOpenedRemoteIteratorException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.TransactionClosedException;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;


@Stateful(name = "JADAEJB_BulkLoaderIterator")
@TransactionManagement(TransactionManagementType.BEAN)
public class BulkLoaderIteratorBean extends BaseBulkLoaderIteratorBean implements BulkLoaderIterator {
    static final long serialVersionUID = 0x2c7e5503d9bf9553L;
    @Resource
    javax.transaction.UserTransaction usertransaction;
    private transient Connection connection;

    public BulkLoaderIteratorBean() {
    }

    public void close() {
        super.close();
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (super.homeCache != null && super.homeCache.getConnection() != null)
                super.homeCache.getConnection().close();
            if (connection != null)
                connection.close();
        } catch (SQLException _ex) {
        }
        connection = null;
        super.homeCache = null;
        super.broker = null;
        try {
            if (usertransaction.getStatus() == Status.STATUS_ACTIVE)
                usertransaction.commit();
        } catch (SecurityException e) {
            throw new EJBException(e);
        } catch (IllegalStateException e) {
            throw new EJBException(e);
        } catch (SystemException e) {
            throw new EJBException(e);
        } catch (RollbackException e) {
            throw new EJBException(e);
        } catch (HeuristicMixedException e) {
            throw new EJBException(e);
        } catch (HeuristicRollbackException e) {
            throw new EJBException(e);
        }
    }

    @PrePassivate
    public void ejbPassivate() {
        closeConnection();
    }

    @Remove
    public void ejbRemove() {
        closeConnection();
        super.ejbRemove();
    }

    @Override
    protected void initialize() throws PersistencyException {
        super.ejbActivate();
        super.initialize();
    }

    protected Connection getConnection() {
        return connection;
    }

    protected void initializeConnection() throws PersistencyException {
        try {
            if (usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
                usertransaction.begin();
            }
        } catch (NotSupportedException notsupportedexception) {
            throw new EJBException("Can't begin transaction", notsupportedexception);
        } catch (SystemException systemexception) {
            throw new EJBException("Can't begin transaction", systemexception);
        } catch (EJBException ejbexception) {
            throw new EJBException("Can't begin transaction", ejbexception);
        }
        try {
            if (connection == null)
                connection = EJBCommonServices.getConnection(super.userContext);
        } catch (SQLException sqlexception) {
            throw new PersistencyException(sqlexception);
        } catch (EJBException ejbexception) {
            throw new PersistencyException(ejbexception);
        }
    }

    protected void testConnection() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
        super.testConnection();
        try {
            if (usertransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                throw new TransactionClosedException();
            }
        } catch (SystemException systemexception) {
            throw new EJBException("Can't begin transaction", systemexception);
        }
    }

    protected void testOpen() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
        super.testOpen();
        try {
            if (usertransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                throw new TransactionClosedException();
            }

        } catch (SystemException systemexception) {
            throw new EJBException("Can't begin transaction", systemexception);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(super.query);
        out.writeInt(super.position);
        out.writeObject(super.bulkClass);
        out.writeObject(super.userContext);
        out.writeInt(super.pageSize);
        out.writeObject(super.fetchPolicyName);
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.doCount = true;
        super.query = (Query) in.readObject();
        super.position = in.readInt();
        super.bulkClass = (Class<?>) in.readObject();
        super.userContext = (UserContext) in.readObject();
        super.pageSize = in.readInt();
        super.fetchPolicyName = (String) in.readObject();
        in.defaultReadObject();
    }
}