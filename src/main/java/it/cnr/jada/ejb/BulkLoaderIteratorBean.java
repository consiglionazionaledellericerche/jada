package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.NotOpenedRemoteIteratorException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.TransactionClosedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;


@Stateful(name="JADAEJB_BulkLoaderIterator")
@TransactionManagement(TransactionManagementType.BEAN)
public class BulkLoaderIteratorBean extends BaseBulkLoaderIteratorBean implements BulkLoaderIterator{
	static final long serialVersionUID = 0x2c7e5503d9bf9553L;
	private transient Connection connection;
	@Resource javax.transaction.UserTransaction usertransaction;
	
	public BulkLoaderIteratorBean(){
	}

	public void close(){
		super.close();
		closeConnection();
	}

	private void closeConnection(){
		try{
			if(super.homeCache != null && super.homeCache.getConnection() != null)
				super.homeCache.getConnection().close();
			if (connection != null)
				connection.close();
		}catch(SQLException _ex) { 
		}
		connection = null;
		super.homeCache = null;
		super.broker = null;
		try{
			if(usertransaction.getStatus() == Status.STATUS_ACTIVE)
			  usertransaction.commit();  
		}catch (SecurityException e){
			throw new EJBException(e);
		}catch (IllegalStateException e){
			throw new EJBException(e);
		}catch (SystemException e){
			throw new EJBException(e);
		}catch (RollbackException e){
			throw new EJBException(e);
		}catch (HeuristicMixedException e){
			throw new EJBException(e);
		}catch (HeuristicRollbackException e){
			throw new EJBException(e);
		}
	}
	
	@PrePassivate
	public void ejbPassivate(){
		closeConnection();
	}

	@Remove
	public void ejbRemove(){
		closeConnection();
		super.ejbRemove();
	}

	@Override
	protected void initialize() throws PersistencyException {
		super.ejbActivate();
		super.initialize();
	}
	
	protected Connection getConnection(){
		return connection;
	}

	protected void initializeConnection() throws PersistencyException{
		try{
			if(usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION){
				usertransaction.begin();
			}
		}catch(NotSupportedException notsupportedexception){
			throw new EJBException("Can't begin transaction", notsupportedexception);
		}catch(SystemException systemexception){
			throw new EJBException("Can't begin transaction", systemexception);
		}catch(EJBException ejbexception){
			throw new EJBException("Can't begin transaction", ejbexception);
		}   
		try{
			if(connection == null)
				connection = EJBCommonServices.getConnection(super.userContext);      
		}catch(SQLException sqlexception){
			throw new PersistencyException(sqlexception);
		}catch(EJBException ejbexception){
			throw new PersistencyException(ejbexception);
		}
	}
	protected void testConnection() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
		super.testConnection();
		try{
			if(usertransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK){
				throw new TransactionClosedException();
			}				
		}catch(SystemException systemexception){
			throw new EJBException("Can't begin transaction", systemexception);
		}
	}
	protected void testOpen() throws EJBException, PersistencyException, NotOpenedRemoteIteratorException {
		super.testOpen();
		try{
			if(usertransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK){
				throw new TransactionClosedException();
			}
				
		}catch(SystemException systemexception){
			throw new EJBException("Can't begin transaction", systemexception);
		}
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(super.query);
		out.writeInt(super.position);
		out.writeObject(super.bulkClass);
		out.writeObject(super.userContext);
		out.writeInt(super.pageSize);
		out.writeObject(super.fetchPolicyName);
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
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