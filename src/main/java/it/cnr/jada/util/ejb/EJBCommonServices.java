package it.cnr.jada.util.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.GenericComponentSession;
import it.cnr.jada.ejb.ServerDate;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.ejb.UserTransactionWrapper;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.EventTracer;
import it.cnr.jada.util.Log;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SessionEventTracer;

import java.io.Serializable;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;


public class EJBCommonServices implements Serializable{
	private static Hashtable EJBHomePool = new Hashtable();
	private static Hashtable EJBLocalHomePool = new Hashtable();
	private static Hashtable dataSources = new Hashtable();
	private static Hashtable qConnectionFactorys = new Hashtable();
	private static Hashtable queues = new Hashtable();
	private static EventTracer sqlEventTracer;
	private static EventTracer componentEventTracer;
	private static String dataSourceName;
	private static String earAppName;
	private static final Format clientInfoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm.ss");
	private static final Log logger = Log.getInstance(EJBCommonServices.class);

	private EJBCommonServices(){
		
	}

	public static final void closeRemoteIterator(ActionContext actioncontext, RemoteIterator remoteiterator) throws RemoteException{
		closeRemoteIterator(remoteiterator);
		HttpEJBCleaner.unregister(actioncontext, remoteiterator);
	}
	public static final void closeRemoteIterator(HttpSession session, RemoteIterator remoteiterator) throws RemoteException{
		closeRemoteIterator(remoteiterator);
		HttpEJBCleaner.unregister(session, remoteiterator);
	}

	private static final void closeRemoteIterator(RemoteIterator remoteiterator) throws RemoteException{
		try{
			if(remoteiterator != null){
				remoteiterator.close();
				remoteiterator.ejbRemove();
			}
		}catch(IllegalStateException illegalStateException){
		}catch(javax.ejb.NoSuchEJBException noSuchEJBException){
			logger.info(noSuchEJBException);
		}catch(javax.ejb.ConcurrentAccessException concurrentAccessException){
			logger.info(concurrentAccessException);			
		}catch(Exception removeexception){
			throw new RemoteException("Remove exception", removeexception);
		}
	}
	
	@Deprecated
	public static final Object createEJB(String jndiName, Class sessionClass){
		return createRemoteEJB(jndiName);
	}
	
	public static final Object createEJB(String s){
		return createRemoteEJB(s);
	}
	
	public static final void loadEarAppName(){
		try {
			earAppName = (String)getInitialContext().lookup("java:comp/env/earAppName");
		} catch (NamingException e) {
			earAppName="SIGLA";
		}
	}

	public static final void loadDataSourceName(){
		try {
			dataSourceName = (String)getInitialContext().lookup("java:comp/env/dataSourceName");
		} catch (NamingException e) {
			dataSourceName="jdbc/CIR";
		}
	}

	@SuppressWarnings("unused")
	public static final Object createRemoteEJB(String jndiName){
		List<String> modules = Arrays.asList("jada", "sigla-ejb", "sigla-sdi", "sigla-ws", "sigla-ws-ns");
		Object obj = null;
		try {
			if (earAppName==null)
				loadEarAppName();
			return getInitialContext().lookup(earAppName+"/"+jndiName+"/remote");
		} catch (NamingException e) {
			for (String module : modules) {
				try {
					return createRemoteEJBInternal("/" + module +"/" + jndiName);					
				} catch (NamingException e1) {
					logger.debug("NamingException", e1);
				}
			}
			if (obj == null)
				throw new EJBException(e);
		}
		return obj;		
	}
	
	private static final Object createRemoteEJBInternal(String jndiName) throws NamingException{
		return getInitialContext().lookup("java:global/" + earAppName + jndiName);		
	}

	public static final TransactionalSessionImpl createEJB(it.cnr.jada.UserTransaction usertransaction, String jndiName) throws EJBException, RemoteException{
		GenericComponentSession genericComponentSession = (GenericComponentSession)createEJB(jndiName);
		String s1 = genericComponentSession.getTransactionalInterface();
		try{
			TransactionalSessionImpl transactionalsessionimpl = (TransactionalSessionImpl)Class.forName(s1).newInstance();
			transactionalsessionimpl.setUserTransaction(usertransaction);
			transactionalsessionimpl.setSessionName(jndiName);
			return transactionalsessionimpl;
		}catch(ClassNotFoundException classnotfoundexception){
			throw new EJBException(classnotfoundexception);
		}catch(InstantiationException instantiationexception){
			throw new EJBException(instantiationexception);
		}catch(IllegalAccessException illegalaccessexception){
			throw new EJBException(illegalaccessexception);
		}
	}

	public static final it.cnr.jada.UserTransaction createUserTransaction(ActionContext actioncontext, Object obj) throws EJBException, RemoteException{
		it.cnr.jada.UserTransaction usertransaction = UserTransactionFactory.getFactory().createUserTransaction(obj);
		HttpEJBCleaner.register(actioncontext, usertransaction);
		return usertransaction;
	}

	public static final UserTransactionWrapper createUserTransactionWrapper() throws EJBException, RemoteException{
		UserTransactionWrapper usertransactionwrapper = (UserTransactionWrapper)createEJB("JADAEJB_UserTransactionWrapper");
		usertransactionwrapper.begin(UserTransactionWrapper.HIGH_TIME);
		return usertransactionwrapper;
	}

	public static EventTracer getComponentEventTracer(){
		if(componentEventTracer == null){
			componentEventTracer = new SessionEventTracer(Config.getHandler().getLogPath(), "componenttracelog");
			componentEventTracer.setTraceAllUsers(true);
		}
		return componentEventTracer;
	}

	public static final Connection getConnection() throws SQLException, EJBException{ 
	  return getDatasource().getConnection();
	}

	public static final Connection getConnection(ActionContext actioncontext) throws SQLException, EJBException{
		Connection connection = getConnection();
		connection.setAutoCommit(false);
		return traceUserConnection(actioncontext.getUserContext(), connection);
	}

	public static final Connection getConnection(UserContext usercontext) throws SQLException, EJBException{
		
		return traceUserConnection(usercontext, getConnection());
	}


	public static final DataSource getDatasource() throws SQLException, EJBException{
		if (dataSourceName==null)
			loadDataSourceName();
		try {
			return getDatasource("java:"+dataSourceName);
		} catch (EJBException e) {
			try {
				return getDatasource("java:/"+dataSourceName);
			} catch (EJBException e1) {
				throw e1;				
			}
		}
	}

	public static final DataSource getDatasource(String s) throws EJBException, SQLException {
		DataSource datasource = (DataSource)dataSources.get(s);
		if(datasource == null)
			try{
				InitialContext initialcontext = getInitialContext();
				dataSources.put(s, datasource = (DataSource)initialcontext.lookup(s));
			} catch(NameNotFoundException namingexception){
				dataSources = new Hashtable();
				throw new EJBException(namingexception);
			} catch(NamingException namingexception){
				dataSources = new Hashtable();
				throw new EJBException(namingexception);
			}
		return datasource;
	}

	public static final String getDefaultSchema(){
		return System.getProperty("SIGLA_SCHEMA_DB","").trim();
	}

	private static InitialContext getInitialContext() throws NamingException{
		return new InitialContext();
	}
	public static final QueueConnectionFactory getQueueConnectionFactory() throws NamingException{
		QueueConnectionFactory qConnectionFactory = (QueueConnectionFactory)qConnectionFactorys.get("MyConnectionFactoryRef");
		if(qConnectionFactory == null)
			try{
				InitialContext initialcontext = getInitialContext();
				qConnectionFactorys.put("MyConnectionFactoryRef", qConnectionFactory = (QueueConnectionFactory)initialcontext.lookup("java:comp/env/jms/MyConnectionFactoryRef"));
			}catch(NameNotFoundException namenotfoundexception){
				throw new EJBException(namenotfoundexception);
			}catch(NamingException namingexception){
				qConnectionFactorys = new Hashtable();
				throw new EJBException(namingexception);
			}
		return qConnectionFactory;
	}
	public static final Queue getQueue() throws NamingException{
		Queue queue = (Queue)queues.get("MyQueueRef");
		if(queue == null)
			try{
				InitialContext initialcontext = getInitialContext();
				queues.put("MyQueueRef", queue = (Queue)initialcontext.lookup("java:comp/env/jms/MyQueueRef"));
			}catch(NameNotFoundException namenotfoundexception){
				throw new EJBException(namenotfoundexception);
			}catch(NamingException namingexception){
				queues = new Hashtable();
				throw new EJBException(namingexception);
			}
		return queue;
	}
	
	public static final Timestamp getServerDate() throws EJBException{
		try{
			return ((ServerDate)createEJB("JADAEJB_ServerDate")).getServerDate();
		}catch(RemoteException remoteexception){
			throw new EJBException(remoteexception);
		}
	}

	public static final Timestamp getServerTimestamp() throws EJBException{
		try{
			return ((ServerDate)createEJB("JADAEJB_ServerDate")).getServerTimestamp();
		}catch(RemoteException remoteexception){
			throw new EJBException(remoteexception);
		}
	}

	public static EventTracer getSqlEventTracer(){
		if(sqlEventTracer == null)
			sqlEventTracer = new SessionEventTracer(Config.getHandler().getLogPath(), "sqltracelog");
		return sqlEventTracer;
	}

	public static final UserTransaction getUserTransaction() throws EJBException{
		try{
			return (UserTransaction)getInitialContext().lookup("java:comp/UserTransaction");
		}catch(NamingException namingexception){
			throw new EJBException(namingexception);
		}
	}

	private static boolean isCacheInvalid(Object obj){
		try{
			((EJBHome)obj).getEJBMetaData();
		}catch(MarshalException _ex){
			return true;
		}catch(RemoteException _ex){
			return true;
		}
		return false;
	}

	public static final Timestamp getDateRefreshDB() throws EJBException{
		java.sql.Timestamp dateRefresh = null;		
		try{
			Connection connection = getConnection();
			LoggableStatement callablestatement = new LoggableStatement(connection,
					"{? = call " +it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
					+ "IBMUTL001.getDBRefreshDate }",false,EJBCommonServices.class);
			try{
				callablestatement.registerOutParameter( 1, java.sql.Types.DATE);
				callablestatement.executeQuery();
				dateRefresh = callablestatement.getTimestamp(1);
 			}
			catch(SQLException sqlexception1){
				
			}finally{
				callablestatement.close();
				connection.close();
				return dateRefresh;
			}
		}catch(SQLException sqlexception){
			throw new EJBException(sqlexception);
		}		
	}
	public static final void lockTransaction() throws EJBException{
		try{
			Connection connection = getConnection();
			LoggableStatement callablestatement = new LoggableStatement(connection,"{  call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "IBMUTL001.LOCK_TRANSACTION }",false,EJBCommonServices.class);
			try{
				callablestatement.execute();
			}catch(SQLException sqlexception1){
				if(sqlexception1.getErrorCode() != 1)
					throw new EJBException("Can't lock transaction :"+sqlexception1.getMessage());
			}finally{
				callablestatement.close();
				connection.close();
			}
		}catch(SQLException sqlexception){
			throw new EJBException(sqlexception);
		}
	}

	public static final RemoteIterator openRemoteIterator(ActionContext actioncontext, RemoteIterator remoteiterator) throws RemoteException{
		try{
			if(remoteiterator instanceof BulkLoaderIterator) {
				HttpEJBCleaner.register(actioncontext, remoteiterator);				
				((BulkLoaderIterator)remoteiterator).open(actioncontext.getUserContext());
			}
			if(remoteiterator instanceof TransactionalBulkLoaderIterator) {
				if (actioncontext.getUserInfo().getUserTransaction() != null) {
					if (remoteiterator instanceof UserTransactionalBulkLoaderIterator)
						return remoteiterator;
					HttpEJBCleaner.register(actioncontext, remoteiterator);
					remoteiterator = new UserTransactionalBulkLoaderIterator(actioncontext.getUserInfo().getUserTransaction(), remoteiterator); 					
					return remoteiterator;
				} else {
					((TransactionalBulkLoaderIterator)remoteiterator).open(actioncontext.getUserContext());					
				}
			}
		}catch(ComponentException componentexception){
			throw new DetailedRuntimeException(componentexception);
		}
		return remoteiterator;
	}

	private static final Connection traceUserConnection(UserContext usercontext, Connection connection){
		try{
			String user = "LOGIN", sessionId = "LOGIN";
			if (usercontext != null){
				if (usercontext.getUser()!= null)
					user = usercontext.getUser();
				if (usercontext.getSessionId() != null)
					sessionId = usercontext.getSessionId();
			}
			LoggableStatement callablestatement = new LoggableStatement(connection,"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
			+ "IBMUTL001.TRACE_USER_CONNECTION(?,?,?) }",false,EJBCommonServices.class);
			callablestatement.setString(1, user);
			callablestatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			callablestatement.setString(3, sessionId);
			try{
				callablestatement.execute();
			}finally{
				callablestatement.close();
			}
		}catch(SQLException _ex) {
			
		}
		return connection;
	}

	public static final void unlockTransaction() throws EJBException{
		try{
			Connection connection = getConnection();
			LoggableStatement callablestatement =new LoggableStatement(connection,"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
			+ "IBMUTL001.UNLOCK_TRANSACTION }",false,EJBCommonServices.class);
			try{
				callablestatement.execute();
			}finally{
				callablestatement.close();
				connection.close();
			}
		}catch(SQLException _ex){
			throw new EJBException("Can't unlock transaction");
		}
	}

}