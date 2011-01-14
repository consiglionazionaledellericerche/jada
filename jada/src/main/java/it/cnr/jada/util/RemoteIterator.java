package it.cnr.jada.util;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface RemoteIterator
    extends Serializable
{

    public abstract void close()
        throws RemoteException;

    public abstract int countElements()
        throws RemoteException;

    public abstract boolean hasMoreElements()
        throws RemoteException;

    public abstract void moveTo(int i)
        throws RemoteException;

    public abstract Object nextElement()
        throws RemoteException;

    public abstract void refresh()
        throws RemoteException;
    
    public abstract void ejbRemove()
    	throws RemoteException;
    
}