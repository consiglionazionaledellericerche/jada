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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NotOpenedRemoteIteratorException;
import it.cnr.jada.persistency.IntrospectionError;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBTracer;
import it.cnr.jada.util.ejb.TransactionClosedException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class BaseBulkLoaderIteratorBean {
    protected Query query;
    protected int recordCount;
    protected int position;
    protected Class<?> bulkClass;
    protected transient SQLBroker broker;
    protected transient HomeCache homeCache;
    protected UserContext userContext;
    protected int pageSize;
    protected String fetchPolicyName;
    protected boolean removed;
    protected boolean doCount;
    private String uid;

    public BaseBulkLoaderIteratorBean() {
        pageSize = 1;
        removed = true;
        doCount = true;
    }

    public void close() {
        try {
            reset();
        } catch (PersistencyException _ex) {

        }
    }

    public int countElements() throws DetailedRuntimeException, EJBException {
        try {
            testOpen();
            return recordCount;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public int countPages() throws DetailedRuntimeException, EJBException {
        try {
            testOpen();
            return ((countElements() + pageSize) - 1) / pageSize;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public void ejbActivate() throws EJBException {
        try {
            open(userContext);
        } catch (ComponentException e) {
            throw new DetailedRuntimeException(e);
        }
    }

    public void ejbCreate(UserContext usercontext, Query query1, Class<?> class1) throws CreateException, ComponentException {
        ejbCreate(usercontext, query1, class1, null);
    }

    public void ejbCreate(UserContext usercontext, Query query1, Class<?> class1, String s) throws CreateException, ComponentException {
        bulkClass = class1;
        query = query1;
        userContext = usercontext;
        fetchPolicyName = s;
        try {
            initialize();
        } catch (PersistencyException _ex) {
            throw new CreateException();
        }
        removed = false;
        this.uid = UUID.randomUUID().toString();
        EJBTracer.getInstance().addToTacers(uid, this, usercontext);
        EJBTracer.getInstance().incrementActiveBulkLoaderIteratorCounter();
    }

    public void ejbRemove() throws EJBException {
        if (!removed) {
            EJBTracer.getInstance().removeToTracers(this.uid);
            EJBTracer.getInstance().decrementActiveBulkLoaderIteratorCounter();
        }
        removed = true;
    }

    protected Object fetchRow() throws PersistencyException {
        it.cnr.jada.persistency.Persistent persistent = broker.fetch(bulkClass);
        homeCache.fetchAll(userContext, homeCache.getHome(persistent));
        persistent = homeCache.getHome(persistent).completeBulkRowByRow(userContext, persistent);
        return persistent;
    }

    protected Object fetchRow(int i) throws PersistencyException {
        try {
            if (i == broker.getResultSet().getRow()) {
                if (!broker.next())
                    return null;
            } else {
                if (!broker.getResultSet().absolute(i + 1))
                    return null;
            }
            return fetchRow();
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    protected abstract Connection getConnection()
            throws PersistencyException;

    public int getOrderBy(String s) throws DetailedRuntimeException, EJBException {
        try {
            return query.getOrderBy(s);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    protected final Error handleError(Error error) throws DetailedRuntimeException {
        throw new DetailedRuntimeException(error);
    }

    protected final RuntimeException handleRuntimeException(RuntimeException runtimeexception) throws DetailedRuntimeException {
        throw new DetailedRuntimeException(runtimeexception);
    }

    public boolean hasMoreElements() throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            return position < recordCount;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public boolean hasMorePages() throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            return ((position + pageSize) - 1) / pageSize < ((recordCount + pageSize) - 1) / pageSize;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    protected void initialize() throws PersistencyException {
    }

    protected abstract void initializeConnection()
            throws PersistencyException;

    public boolean isOrderableBy(String s) throws DetailedRuntimeException {
        try {
            return query.isOrderableByProperty(s);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public void moveTo(int i) throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            if (i <= 0) {
                position = 0;
            } else {
                if (i >= recordCount)
                    position = recordCount - 1;
                else
                    position = i;
            }
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw runtimeexception;
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public void moveToPage(int i) throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            if (i <= 0) {
                moveTo(0);
            } else {
                int j = countPages();
                if (i >= j)
                    i = Math.max(0, j - 1);
                moveTo(i * pageSize);
            }
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public Object nextElement() throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            Object obj = fetchRow(position++);
            homeCache.clearPersistentCache();
            return obj;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        }
    }

    public Object[] nextPage() throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            int i = ((position + pageSize) - 1) / pageSize;
            if (i >= countPages())
                return new OggettoBulk[0];
            moveToPage(i);
            int j = Math.min(pageSize, recordCount - position);
            OggettoBulk[] aoggettobulk = new OggettoBulk[j];
            for (int k = 0; k < j; k++)
                aoggettobulk[k] = (OggettoBulk) fetchRow(position++);

            homeCache.clearPersistentCache();
            return aoggettobulk;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    public void open(UserContext usercontext) throws ComponentException, EJBException, DetailedRuntimeException {
        try {
            initializeConnection();
            if (homeCache == null)
                homeCache = new HomeCache(getConnection());
        } catch (PersistencyException persistencyexception) {
            throw new ComponentException(persistencyexception);
        }
    }

    protected void predictCount() {
        try {
            ResultSet resultset = broker.getResultSet();
            resultset.getRow();
        } catch (SQLException _ex) {
        }
    }

    private void prepareSearchResult() throws PersistencyException {
        try {
            reset();
            it.cnr.jada.persistency.sql.PersistentHome persistenthome = homeCache.getHome(bulkClass);
            if (doCount)
                recordCount = query.executeCountQuery(getConnection());
            LoggableStatement preparedstatement = query.prepareStatement(persistenthome.getConnection(), 1004, 1007);
            broker = persistenthome.createBroker(preparedstatement, preparedstatement.executeQuery(), query.getColumnMap(), persistenthome.getIntrospector().getPersistentInfo(bulkClass).getFetchPolicy(fetchPolicyName));
            broker.setAutomaticClose(false);
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public void refresh() throws DetailedRuntimeException {
        try {
            reset();
            testOpen();
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        }
    }

    private void reset() throws PersistencyException {
        if (broker != null)
            broker.close();
        broker = null;
    }

    public void setOrderBy(String s, int i) throws DetailedRuntimeException {
        try {
            query.setOrderBy(s, i);
            reset();
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        }
    }

    public void setPageSize(int i) throws DetailedRuntimeException, EJBException {
        try {
            testConnection();
            pageSize = i;
        } catch (NotOpenedRemoteIteratorException notopenedremoteiteratorexception) {
            throw new DetailedRuntimeException(notopenedremoteiteratorexception);
        } catch (PersistencyException persistencyexception) {
            throw new DetailedRuntimeException(persistencyexception);
        } catch (RuntimeException runtimeexception) {
            throw handleRuntimeException(runtimeexception);
        } catch (Error error) {
            throw handleError(error);
        }
    }

    protected void testConnection() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
        try {
            if (homeCache == null)
                throw new NotOpenedRemoteIteratorException(query.toString());
            if (getConnection().isClosed())
                throw new TransactionClosedException();
            if (broker == null)
                prepareSearchResult();
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    protected void testOpen() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
        if (homeCache == null)
            throw new NotOpenedRemoteIteratorException(query.toString());
        if (broker == null)
            prepareSearchResult();
    }

    /**
     * @return
     */
    public Query getQuery() {
        return query;
    }

}