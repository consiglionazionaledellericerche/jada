package it.cnr.jada.util.ejb;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

import it.cnr.jada.ejb.UserTransactionWrapper;

public abstract class TransactionalSessionImpl implements java.io.Serializable {
	private transient it.cnr.jada.UserTransaction userTransaction;
	protected String sessionName;
	public TransactionalSessionImpl() {
	}
	/**
	 * getHandle method comment.
	 */
	public javax.ejb.Handle getHandle() throws java.rmi.RemoteException {
		return null;
	}
	/**
	 * getPrimaryKey method comment.
	 */
	public java.lang.Object getPrimaryKey() throws java.rmi.RemoteException {
		return null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/12/2001 18:16:12)
	 * @return javax.ejb.EJBObject
	 */
	public Object getSession() throws java.rmi.RemoteException {
		try {
			return EJBCommonServices.createEJB(userTransaction, sessionName);
		} catch(javax.ejb.EJBException e) {
			throw new java.rmi.RemoteException("Can't create EJB",e);
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/14/2002 2:49:02 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getSessionName() {
		return sessionName;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/14/2002 3:43:54 PM)
	 * @return it.cnr.jada.util.ejb.UserTransaction
	 */
	public it.cnr.jada.UserTransaction getUserTransaction() {
		return userTransaction;
	}
	public Object invoke(String method, java.lang.Object[] params) throws java.lang.reflect.InvocationTargetException, java.rmi.RemoteException {
		return userTransaction.invoke(sessionName,method,params);
	}
	/**
	 * isIdentical method comment.
	 */
	public boolean isIdentical(EJBObject ejb) throws java.rmi.RemoteException {
		return false;
	}
	
	public void remove() throws java.rmi.RemoteException, javax.ejb.RemoveException {
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/14/2002 2:49:02 PM)
	 * @param newSessionName java.lang.String
	 */
	public void setSessionName(java.lang.String newSessionName) {
		sessionName = newSessionName;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/14/2002 3:43:54 PM)
	 * @param newUserTransaction it.cnr.jada.util.ejb.UserTransaction
	 */
	public void setUserTransaction(it.cnr.jada.UserTransaction newUserTransaction) {
		userTransaction = newUserTransaction;
	}

	public String getTransactionalInterface(){
    	String name = this.getClass().getName();
    	return name;
    }
	
	public void ejbRemove() throws javax.ejb.EJBException{
		try {
			invoke("ejbRemove", null);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (InvocationTargetException e) {
			throw new EJBException(e);
		}
	}
}
