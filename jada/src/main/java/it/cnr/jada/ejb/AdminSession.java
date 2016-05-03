package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface AdminSession{

    public abstract void addSQLTraceUser(String s)
        throws RemoteException;

    public abstract long getActiveBulkLoaderIteratorCounter()
        throws RemoteException;

    public abstract long getActiveComponentCounter()
        throws RemoteException;

    public abstract long getActiveUserTransactionCounter()
        throws RemoteException;

    public abstract String[] getTraceUsers()
        throws RemoteException;

    public abstract byte[] getZippedTrace(UserContext usercontext)
        throws RemoteException;

    public abstract boolean isDumpStackTraceEnabled()
        throws RemoteException;

    public abstract void removeSQLTraceUser(String s)
        throws RemoteException;

	public abstract void loadBulkInfos(Class class1)
		throws RemoteException;	

	public abstract void loadPersistentInfos(Class class1)
	    throws RemoteException;	
	
    public abstract void resetPersistentInfos()
        throws RemoteException;

    public abstract void setDumpStackTraceEnabled(boolean flag)
        throws RemoteException;
}
