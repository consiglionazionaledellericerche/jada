package it.cnr.jada.util.ejb;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.UserTransaction;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.RemoteIterator;

public class UserTransactionalBulkLoaderIterator implements TransactionalBulkLoaderIterator {
	private static final long serialVersionUID = 1L;
	private UserTransaction userTransaction;
	private RemoteIterator remoteiterator;

	public UserTransactionalBulkLoaderIterator(it.cnr.jada.UserTransaction userTransaction, RemoteIterator remoteiterator) {
		this.userTransaction = userTransaction;
		this.remoteiterator = remoteiterator;
	}

	@Override
	public void close() throws RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "close");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int countElements() throws DetailedRuntimeException, RemoteException {
		try {			
			return (int) userTransaction.invoke(remoteiterator, "countElements");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int countPages() throws DetailedRuntimeException, RemoteException {
		try {
			return (int) userTransaction.invoke(remoteiterator, "countPages");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getOrderBy(String s) throws DetailedRuntimeException,
			RemoteException {
		try {
			return (int) userTransaction.invoke(remoteiterator, "getOrderBy", new Object[]{s});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasMoreElements() throws DetailedRuntimeException,
			RemoteException {
		try {
			return (boolean) userTransaction.invoke(remoteiterator, "hasMoreElements");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasMorePages() throws DetailedRuntimeException,
			RemoteException {
		try {
			return (boolean) userTransaction.invoke(remoteiterator, "hasMorePages");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isOrderableBy(String s) throws DetailedRuntimeException,
			RemoteException {
		try {
			return (boolean) userTransaction.invoke(remoteiterator, "isOrderableBy", new Object[]{s});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void moveTo(int i) throws DetailedRuntimeException, RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "moveTo", new Object[]{i});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void moveToPage(int i) throws DetailedRuntimeException,
			RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "moveToPage", new Object[]{i});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}				
	}

	@Override
	public Object nextElement() throws DetailedRuntimeException,
			RemoteException {
		try {
			return userTransaction.invoke(remoteiterator, "nextElement");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] nextPage() throws DetailedRuntimeException, RemoteException {
		try {
			return (Object[]) userTransaction.invoke(remoteiterator, "nextPage");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void open(UserContext usercontext) throws ComponentException,
			RemoteException, DetailedRuntimeException {
		try {
			userTransaction.invoke(remoteiterator, "open", new Object[]{usercontext});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}						
	}

	@Override
	public void refresh() throws DetailedRuntimeException, RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "refresh");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}								
	}

	@Override
	public void setOrderBy(String s, int i) throws DetailedRuntimeException,
			RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "setOrderBy", new Object[]{s, i});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}								
	}

	@Override
	public void setPageSize(int i) throws DetailedRuntimeException,
			RemoteException {
		try {
			userTransaction.invoke(remoteiterator, "setPageSize", new Object[]{i});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}										
	}

	@Override
	public Query getQuery() throws DetailedRuntimeException, RemoteException {
		try {
			return (Query) userTransaction.invoke(remoteiterator, "getQuery");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
			throws CreateException, ComponentException {
		try {
			userTransaction.invoke(remoteiterator, "ejbCreate", new Object[]{usercontext, query1, class1});
		} catch (InvocationTargetException | RemoteException e) {
			throw new RuntimeException(e);
		}												
	}

	@Override
	public void ejbCreate(UserContext usercontext, Query query1,
			Class<?> class1, String s) throws CreateException,
			ComponentException {
		try {
			userTransaction.invoke(remoteiterator, "ejbCreate", new Object[]{usercontext, query1, class1, s});
		} catch (InvocationTargetException | RemoteException e) {
			throw new RuntimeException(e);
		}														
	}

	@Override
	public void ejbRemove() throws EJBException {
		try {
			userTransaction.invoke(remoteiterator, "ejbRemove");
		} catch (InvocationTargetException | RemoteException e) {
			throw new RuntimeException(e);
		}
	}

}
