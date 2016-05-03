package it.cnr.jada.ejb;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.Remote;
import javax.transaction.RollbackException;

@Remote
public interface UserTransactionWrapper{
	int HIGH_TIME = 10000000;
	
    public abstract void begin()
        throws RemoteException;

    public abstract void begin(int i)
        throws RemoteException;

    public abstract void commit()
        throws RemoteException, RollbackException;

    public abstract Object invoke(String s, String s1, Object aobj[])
        throws RemoteException, InvocationTargetException;

    public abstract Object invoke(Object ejbobject, String s, Object aobj[])
        throws RemoteException, InvocationTargetException;

    public abstract void ping()
        throws RemoteException;

    public abstract void rollback()
        throws RemoteException;
    
    public abstract void ejbRemove() 
    	throws RemoteException;
}
