package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PostRemove;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;

@Stateful(name="JADAEJB_TransactionalBulkLoaderIterator")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class TransactionalBulkLoaderIteratorBean extends BaseBulkLoaderIteratorBean implements TransactionalBulkLoaderIterator{
	static final long serialVersionUID = 0x2c7e5503d9bf9553L;
	private Connection connection;

	public TransactionalBulkLoaderIteratorBean(){
	}

	protected Connection getConnection() throws PersistencyException{
		return connection;
	}

	@Override
	@PostActivate
	protected void initialize() throws PersistencyException {
		super.ejbActivate();
		super.initialize();
	}
	
	protected void initializeConnection() throws PersistencyException{
		try{
			if(connection == null)
				connection = EJBCommonServices.getConnection(super.userContext);      
		}catch(SQLException sqlexception){
			throw new PersistencyException(sqlexception);
		}catch(EJBException ejbexception){
			throw new PersistencyException(ejbexception);
		}
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
	}
	
	public void close(){
		super.close();
		closeConnection();
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

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(super.query);
		out.writeInt(super.position);
		out.writeObject(super.bulkClass);
		out.writeObject(super.userContext);
		out.writeInt(super.pageSize);
		out.writeObject(super.fetchPolicyName);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		super.doCount = true;
		super.query = (Query) in.readObject();
		super.position = in.readInt();
		super.bulkClass = (Class<?>) in.readObject();
		super.userContext = (UserContext) in.readObject();
		super.pageSize = in.readInt();
		super.fetchPolicyName = (String) in.readObject();
	}	
}
