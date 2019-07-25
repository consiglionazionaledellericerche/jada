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

package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLPersistentInfo, Query, SQLExceptionHandler, SQLBroker, 
//            SQLBuilder, ColumnMap, ColumnMapping, SQLParameter

public class SQLPersister extends Persister
        implements Serializable {

    private final Connection connection;
    private ColumnMap columnMap;
    private PersistentCache persistentCache;
    private FetchPolicy fetchPolicy;

    public SQLPersister(Introspector introspector, ColumnMap columnmap, Connection connection1) {
        this(introspector, columnmap, connection1, null);
    }

    public SQLPersister(Introspector introspector, ColumnMap columnmap, Connection connection1, PersistentCache persistentcache) {
        super(introspector);
        fetchPolicy = FetchAllPolicy.FETCHALL;
        setColumnMap(columnmap);
        connection = connection1;
        if (persistentcache == null)
            persistentcache = new PersistentCache();
        persistentCache = persistentcache;
    }

    public SQLPersister(Introspector introspector, Class class1, Connection connection1) {
        this(introspector, class1, connection1, null);
    }

    public SQLPersister(Introspector introspector, Class class1, Connection connection1, PersistentCache persistentcache) {
        super(introspector);
        fetchPolicy = FetchAllPolicy.FETCHALL;
        try {
            setColumnMap(((SQLPersistentInfo) introspector.getPersistentInfo(class1)).getDefaultColumnMap());
            connection = connection1;
            if (persistentcache == null)
                persistentcache = new PersistentCache();
            persistentCache = persistentcache;
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public void clearCache() {
        persistentCache.clear();
    }

    public SQLBroker createBroker(Query query)
            throws PersistencyException {
        return createBroker(query, fetchPolicy);
    }

    public SQLBroker createBroker(Query query, FetchPolicy fetchpolicy)
            throws PersistencyException {
        try {
            LoggableStatement preparedstatement = query.prepareStatement(getConnection());
            return createBroker(preparedstatement, preparedstatement.executeQuery(), query.getColumnMap(), fetchpolicy);
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    public SQLBroker createBroker(LoggableStatement statement, ResultSet resultset) {
        return createBroker(statement, resultset, getColumnMap(), fetchPolicy);
    }

    public SQLBroker createBroker(LoggableStatement statement, ResultSet resultset, ColumnMap columnmap, FetchPolicy fetchpolicy) {
        return new SQLBroker(columnmap, getIntrospector(), persistentCache, statement, resultset, fetchpolicy);
    }

    public SQLBuilder createSQLBuilder() {
        return new SQLBuilder(columnMap);
    }

    protected void doDelete(Persistent persistent)
            throws PersistencyException {
        try {
            LoggableStatement loggablestatement = getLoggableDeleteStatement();
            try {
                setParametersUsing(loggablestatement, persistent, columnMap.getPrimaryColumnNames(), 1);
                int i = loggablestatement.executeUpdate();
                if (i == 0)
                    throw new ObjectNotFoundException("DELETE statment affected 0 rows.");
                if (i > 1)
                    throw new DeleteException("DELETE statement affected more than 1 rows.");
            } finally {
                loggablestatement.close();
            }
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception, persistent);
        }
    }

    protected void doInsert(Persistent persistent)
            throws PersistencyException {
        try {
            LoggableStatement loggablestatement = getLoggableInsertStatement();
            try {
                setParametersUsing(loggablestatement, persistent, columnMap.getColumnNames(), 1);
                loggablestatement.execute();
            } finally {
                loggablestatement.close();
            }
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception, persistent);
        }
    }

    protected void doUpdate(Persistent persistent)
            throws PersistencyException {
        try {
            LoggableStatement loggablestatement = getLoggableUpdateStatement();
            try {
                int i = 1;
                i = setParametersUsing(loggablestatement, persistent, columnMap.getNonPrimaryColumnNames(), i);
                setParametersUsing(loggablestatement, persistent, columnMap.getPrimaryColumnNames(), i);
                if (loggablestatement.executeUpdate() != 1)
                    throw new UpdateException("UPDATE statement affected 0 or more than 1 rows.");
            } finally {
                loggablestatement.close();
            }
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception, persistent);
        }
    }

    public Persistent findByPrimaryKey(Persistent persistent)
            throws PersistencyException {
        return findByPrimaryKey(persistent, persistent.getClass(), false);
    }

    protected Persistent findByPrimaryKey(Object obj, Class class1, boolean flag)
            throws PersistencyException {
        try {
            LoggableStatement loggablestatement;
            if (flag)
                loggablestatement = getLoggableSelectForUpdateStatement();
            else
                loggablestatement = getLoggableSelectStatement();
            loggablestatement.clearParameters();
            setParametersUsing(loggablestatement, obj, columnMap.getPrimaryColumnNames(), 1);
            SQLBroker sqlbroker = createBroker(loggablestatement, loggablestatement.executeQuery());
            if (!sqlbroker.next())
                return null;
            Persistent persistent = sqlbroker.fetch(class1);
            if (sqlbroker.next()) {
                sqlbroker.close();
                throw new FindException("SELECT statement return more than one row");
            } else {
                return persistent;
            }
        } catch (SQLException sqlexception) {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    public ColumnMap getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(ColumnMap columnmap) {
        columnMap = columnmap;
    }

    public Connection getConnection() {
        return connection;
    }

    protected PreparedStatement getDeleteStatement()
            throws SQLException {
        return getConnection().prepareStatement(columnMap.getDefaultDeleteSQL());
    }

    protected LoggableStatement getLoggableDeleteStatement() throws SQLException {
        return new LoggableStatement(getConnection(), columnMap.getDefaultDeleteSQL(), true, this.getClass());
    }

    public FetchPolicy getFetchPolicy() {
        return fetchPolicy;
    }

    public void setFetchPolicy(FetchPolicy fetchpolicy) {
        fetchPolicy = fetchpolicy;
    }

    protected PreparedStatement getInsertStatement()
            throws SQLException {
        return getConnection().prepareStatement(columnMap.getDefaultInsertSQL());
    }

    protected LoggableStatement getLoggableInsertStatement() throws SQLException {
        return new LoggableStatement(getConnection(), columnMap.getDefaultInsertSQL(), true, this.getClass());
    }

    public PersistentCache getPersistentCache() {
        return persistentCache;
    }

    public void setPersistentCache(PersistentCache persistentcache) {
        persistentCache = persistentcache;
    }

    protected PreparedStatement getSelectForUpdateStatement()
            throws SQLException {
        return getConnection().prepareStatement(columnMap.getDefaultSelectForUpdateSQL());
    }

    protected PreparedStatement getSelectStatement()
            throws SQLException {
        return getConnection().prepareStatement(columnMap.getDefaultSelectSQL());
    }

    protected LoggableStatement getLoggableSelectForUpdateStatement()
            throws SQLException {
        return new LoggableStatement(getConnection(), columnMap.getDefaultSelectForUpdateSQL(), true, this.getClass());
    }

    protected LoggableStatement getLoggableSelectStatement()
            throws SQLException {
        return new LoggableStatement(getConnection(), columnMap.getDefaultSelectSQL(), true, this.getClass());
    }

    protected PreparedStatement getUpdateStatement()
            throws SQLException {
        return getConnection().prepareStatement(columnMap.getDefaultUpdateSQL());
    }

    protected LoggableStatement getLoggableUpdateStatement()
            throws SQLException {
        return new LoggableStatement(getConnection(), columnMap.getDefaultUpdateSQL(), true, this.getClass());
    }

    protected int setParametersUsing(LoggableStatement preparedstatement, Object obj, String as[], int i)
            throws SQLException {
        try {
            for (int j = 0; j < as.length; ) {
                ColumnMapping columnmapping = columnMap.getMappingForColumn(as[j]);
                Object obj1 = getIntrospector().getPropertyValue(obj, columnmapping.getPropertyName());
                SQLParameter.setParameterInPreparedStatement(i, preparedstatement, obj1, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter());
                j++;
                i++;
            }

            return i;
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }
}