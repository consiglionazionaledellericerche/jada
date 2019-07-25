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

package it.cnr.jada.util.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class EJBCommonServices implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EJBCommonServices.class);
    private static Hashtable dataSources = new Hashtable();
    private static EventTracer sqlEventTracer;
    private static EventTracer componentEventTracer;
    private static String dataSourceName;
    private static String earAppName;

    private EJBCommonServices() {
    }

    public static final void closeRemoteIterator(ActionContext actioncontext, RemoteIterator remoteiterator) throws RemoteException {
        closeRemoteIterator(remoteiterator);
        if (remoteiterator != null)
            HttpEJBCleaner.unregister(actioncontext, remoteiterator);
    }

    public static final void closeRemoteIterator(HttpSession session, RemoteIterator remoteiterator) throws RemoteException {
        closeRemoteIterator(remoteiterator);
        if (remoteiterator != null)
            HttpEJBCleaner.unregister(session, remoteiterator);
    }

    private static final void closeRemoteIterator(RemoteIterator remoteiterator) throws RemoteException {
        try {
            if (remoteiterator != null) {
                remoteiterator.close();
                remoteiterator.ejbRemove();
            }
        } catch (IllegalStateException illegalStateException) {
        } catch (javax.ejb.NoSuchEJBException noSuchEJBException) {
            logger.info("NoSuchEJBException");
        } catch (javax.ejb.ConcurrentAccessException concurrentAccessException) {
            logger.info("ConcurrentAccessException");
        } catch (Exception removeexception) {
            throw new RemoteException("Remove exception", removeexception);
        }
    }

    @Deprecated
    public static final Object createEJB(String jndiName, Class sessionClass) {
        return createRemoteEJB(jndiName);
    }

    public static final Object createEJB(String s) {
        return createRemoteEJB(s);
    }

    public static final void loadEarAppName() {
        try {
            earAppName = (String) getInitialContext().lookup("java:comp/env/earAppName");
        } catch (NamingException e) {
            earAppName = "SIGLA";
        }
    }

    public static final void loadDataSourceName() {
        try {
            dataSourceName = (String) getInitialContext().lookup("java:comp/env/dataSourceName");
        } catch (NamingException e) {
            dataSourceName = "jdbc/CIR";
        }
    }

    @SuppressWarnings("unused")
    public static final Object createRemoteEJB(String jndiName) {
        List<String> modules = Arrays.asList("jada", "sigla-ejb", "sigla-sdi", "sigla-ws", "sigla-ws-ns");
        Object obj = null;
        try {
            if (earAppName == null)
                loadEarAppName();
            return getInitialContext().lookup("java:module/" + jndiName);
        } catch (NamingException e) {
            for (String module : modules) {
                try {
                    return createRemoteEJBInternal("/" + module + "/" + jndiName);
                } catch (NamingException e1) {
                    logger.debug("NamingException", e1);
                }
            }
            if (obj == null)
                throw new EJBException(e);
        }
        return obj;
    }

    private static final Object createRemoteEJBInternal(String jndiName) throws NamingException {
        return getInitialContext().lookup("java:global/" + earAppName + jndiName);
    }

    public static final TransactionalSessionImpl createEJB(it.cnr.jada.UserTransaction usertransaction, String jndiName) throws EJBException, RemoteException {
        GenericComponentSession genericComponentSession = (GenericComponentSession) createEJB(jndiName);
        String s1 = genericComponentSession.getTransactionalInterface();
        try {
            TransactionalSessionImpl transactionalsessionimpl = (TransactionalSessionImpl) Class.forName(s1).newInstance();
            transactionalsessionimpl.setUserTransaction(usertransaction);
            transactionalsessionimpl.setSessionName(jndiName);
            return transactionalsessionimpl;
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new EJBException(classnotfoundexception);
        } catch (InstantiationException instantiationexception) {
            throw new EJBException(instantiationexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new EJBException(illegalaccessexception);
        }
    }

    public static final it.cnr.jada.UserTransaction createUserTransaction(ActionContext actioncontext, Object obj) throws EJBException, RemoteException {
        it.cnr.jada.UserTransaction usertransaction = UserTransactionFactory.getFactory().createUserTransaction(obj);
        HttpEJBCleaner.register(actioncontext, usertransaction);
        return usertransaction;
    }

    public static final UserTransactionWrapper createUserTransactionWrapper() throws EJBException, RemoteException {
        UserTransactionWrapper usertransactionwrapper = (UserTransactionWrapper) createEJB("JADAEJB_UserTransactionWrapper");
        usertransactionwrapper.begin(UserTransactionWrapper.HIGH_TIME);
        return usertransactionwrapper;
    }

    public static EventTracer getComponentEventTracer() {
        if (componentEventTracer == null) {
            componentEventTracer = new SessionEventTracer(Config.getHandler().getLogPath(), "componenttracelog");
            componentEventTracer.setTraceAllUsers(true);
        }
        return componentEventTracer;
    }

    public static final Connection getConnection() throws SQLException, EJBException {
        return getDatasource().getConnection();
    }

    public static final Connection getConnection(ActionContext actioncontext) throws SQLException, EJBException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        return traceUserConnection(actioncontext.getUserContext(), connection);
    }

    public static final Connection getConnection(UserContext usercontext) throws SQLException, EJBException {

        return traceUserConnection(usercontext, getConnection());
    }


    public static final DataSource getDatasource() throws SQLException, EJBException {
        if (dataSourceName == null)
            loadDataSourceName();
        try {
            return getDatasource("java:" + dataSourceName);
        } catch (EJBException e) {
            try {
                return getDatasource("java:/" + dataSourceName);
            } catch (EJBException e1) {
                throw e1;
            }
        }
    }

    public static final DataSource getDatasource(String s) throws EJBException, SQLException {
        DataSource datasource = (DataSource) dataSources.get(s);
        if (datasource == null)
            try {
                InitialContext initialcontext = getInitialContext();
                dataSources.put(s, datasource = (DataSource) initialcontext.lookup(s));
            } catch (NameNotFoundException namingexception) {
                dataSources = new Hashtable();
                throw new EJBException(namingexception);
            } catch (NamingException namingexception) {
                dataSources = new Hashtable();
                throw new EJBException(namingexception);
            }
        return datasource;
    }

    public static final String getDefaultSchema() {
        return System.getProperty("SIGLA_SCHEMA_DB", "").trim();
    }

    private static InitialContext getInitialContext() throws NamingException {
        return new InitialContext();
    }

    public static final Timestamp getServerDate() throws EJBException {
        try {
            return ((ServerDate) createEJB("JADAEJB_ServerDate")).getServerDate();
        } catch (RemoteException remoteexception) {
            throw new EJBException(remoteexception);
        }
    }

    public static final Timestamp getServerTimestamp() throws EJBException {
        try {
            return ((ServerDate) createEJB("JADAEJB_ServerDate")).getServerTimestamp();
        } catch (RemoteException remoteexception) {
            throw new EJBException(remoteexception);
        }
    }

    public static EventTracer getSqlEventTracer() {
        if (sqlEventTracer == null)
            sqlEventTracer = new SessionEventTracer(Config.getHandler().getLogPath(), "sqltracelog");
        return sqlEventTracer;
    }

    public static final UserTransaction getUserTransaction() throws EJBException {
        try {
            return (UserTransaction) getInitialContext().lookup("java:comp/UserTransaction");
        } catch (NamingException namingexception) {
            throw new EJBException(namingexception);
        }
    }

    private static boolean isCacheInvalid(Object obj) {
        try {
            ((EJBHome) obj).getEJBMetaData();
        } catch (MarshalException _ex) {
            return true;
        } catch (RemoteException _ex) {
            return true;
        }
        return false;
    }

    public static final Timestamp getDateRefreshDB() throws EJBException {
        java.sql.Timestamp dateRefresh = null;
        try {
            Connection connection = getConnection();
            LoggableStatement callablestatement = new LoggableStatement(connection, PropertyNames.getProperty("package.getdbrefreshdate"), false, EJBCommonServices.class);
            try {
                callablestatement.registerOutParameter(1, Types.TIMESTAMP);
                callablestatement.execute();
                dateRefresh = callablestatement.getTimestamp(1);
            } catch (SQLException sqlexception1) {
                logger.error("Cannot get getDateRefreshDB ", sqlexception1);
            } finally {
                callablestatement.close();
                connection.close();
                return dateRefresh;
            }
        } catch (SQLException sqlexception) {
            throw new EJBException(sqlexception);
        }
    }

    public static final void lockTransaction() throws EJBException {
        try {
            Connection connection = getConnection();
            LoggableStatement callablestatement = new LoggableStatement(connection, PropertyNames.getProperty("package.lock.transaction"), false, EJBCommonServices.class);
            try {
                callablestatement.execute();
            } catch (SQLException sqlexception1) {
                if (sqlexception1.getErrorCode() != 1)
                    throw new EJBException("Can't lock transaction :" + sqlexception1.getMessage());
            } finally {
                callablestatement.close();
                connection.close();
            }
        } catch (SQLException sqlexception) {
            throw new EJBException(sqlexception);
        }
    }

    public static final RemoteIterator openRemoteIterator(ActionContext actioncontext, RemoteIterator remoteiterator) throws RemoteException {
        try {
            if (remoteiterator instanceof BulkLoaderIterator) {
                HttpEJBCleaner.register(actioncontext, remoteiterator);
                ((BulkLoaderIterator) remoteiterator).open(actioncontext.getUserContext(false));
            }
            if (remoteiterator instanceof TransactionalBulkLoaderIterator) {
                if (actioncontext.getUserInfo().getUserTransaction() != null) {
                    if (remoteiterator instanceof UserTransactionalBulkLoaderIterator)
                        return remoteiterator;
                    HttpEJBCleaner.register(actioncontext, remoteiterator);
                    remoteiterator = new UserTransactionalBulkLoaderIterator(actioncontext.getUserInfo().getUserTransaction(), remoteiterator);
                    return remoteiterator;
                } else {
                    ((TransactionalBulkLoaderIterator) remoteiterator).open(actioncontext.getUserContext());
                }
            }
        } catch (ComponentException componentexception) {
            throw new DetailedRuntimeException(componentexception);
        }
        return remoteiterator;
    }

    private static final Connection traceUserConnection(UserContext usercontext, Connection connection) {
        try {
            String user = "LOGIN", sessionId = "LOGIN";
            if (usercontext != null) {
                if (usercontext.getUser() != null)
                    user = usercontext.getUser();
                if (usercontext.getSessionId() != null)
                    sessionId = usercontext.getSessionId();
            }
            LoggableStatement callablestatement = new LoggableStatement(connection, PropertyNames.getProperty("package.trace.user.connection"), false, EJBCommonServices.class);
            callablestatement.setString(1, user);
            callablestatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            callablestatement.setString(3, sessionId);
            try {
                callablestatement.execute();
            } finally {
                callablestatement.close();
            }
        } catch (SQLException _ex) {

        }
        return connection;
    }

    public static final void unlockTransaction() throws EJBException {
        try {
            Connection connection = getConnection();
            LoggableStatement callablestatement = new LoggableStatement(connection, PropertyNames.getProperty("package.unlock.transaction"), false, EJBCommonServices.class);
            try {
                callablestatement.execute();
            } finally {
                callablestatement.close();
                connection.close();
            }
        } catch (SQLException _ex) {
            throw new EJBException("Can't unlock transaction");
        }
    }

}