package it.cnr.jada;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.transaction.RollbackException;

public interface UserTransaction extends Serializable{

    public abstract void commit()
        throws RemoteException, EJBException, RollbackException;

    public abstract boolean equals(Object obj);

    public abstract Object getOwner()
        throws RemoteException;

    public abstract Object invoke(String s, String s1, Object aobj[])
        throws InvocationTargetException, RemoteException;

    public abstract Object invoke(Object ejbobject, String s, Object aobj[])
        throws InvocationTargetException, RemoteException;

    public abstract void remove()
        throws RemoteException, RemoveException;

    public abstract void rollback()
        throws RemoteException, EJBException;
}
