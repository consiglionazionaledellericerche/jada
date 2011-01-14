package it.cnr.jada.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.RemotePagedIterator;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.Remote;

@Remote
public interface TransactionalBulkLoaderIterator extends RemotePagedIterator, RemoteOrderable {

	public abstract void close()
		throws RemoteException;

	public abstract int countElements()
		throws DetailedRuntimeException, RemoteException;

	public abstract int countPages()
		throws DetailedRuntimeException, RemoteException;

	public abstract int getOrderBy(String s)
		throws DetailedRuntimeException, RemoteException;

	public abstract boolean hasMoreElements()
		throws DetailedRuntimeException, RemoteException;

	public abstract boolean hasMorePages()
		throws DetailedRuntimeException, RemoteException;

	public abstract boolean isOrderableBy(String s)
		throws DetailedRuntimeException, RemoteException;

	public abstract void moveTo(int i)
		throws DetailedRuntimeException, RemoteException;

	public abstract void moveToPage(int i)
		throws DetailedRuntimeException, RemoteException;

	public abstract Object nextElement()
		throws DetailedRuntimeException, RemoteException;

	public abstract Object[] nextPage()
		throws DetailedRuntimeException, RemoteException;

	public abstract void open(UserContext usercontext)
		throws ComponentException, RemoteException, DetailedRuntimeException;

	public abstract void refresh()
		throws DetailedRuntimeException, RemoteException;

	public abstract void setOrderBy(String s, int i)
		throws DetailedRuntimeException, RemoteException;

	public abstract void setPageSize(int i)
		throws DetailedRuntimeException, RemoteException;

	public abstract Query getQuery()
		throws DetailedRuntimeException, RemoteException;
	
	public abstract void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
		throws CreateException, ComponentException;

	public abstract void ejbCreate(UserContext usercontext, Query query1, Class<?> class1, String s)
		throws CreateException, ComponentException;

	public abstract void ejbRemove() 
		throws EJBException;
}