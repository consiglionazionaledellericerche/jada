package it.cnr.jada.action;

import it.cnr.jada.UserTransaction;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.*;

// Referenced classes of package it.cnr.jada.action:
//            BusinessProcessException

class NestedUserTransaction
    implements Serializable, UserTransaction
{

    private NestedUserTransaction(UserTransaction usertransaction)
    {
        parent = usertransaction;
    }

    public void commit()
        throws EJBException, RemoteException
    {
    }

    public static UserTransaction createNestedUserTransaction(UserTransaction usertransaction)
        throws BusinessProcessException
    {
        if(usertransaction == null || (usertransaction instanceof NestedUserTransaction))
            return usertransaction;
        else
            return new NestedUserTransaction(usertransaction);
    }

    public Object getOwner()
        throws RemoteException
    {
        return parent.getOwner();
    }

    public Object invoke(String s, String s1, Object aobj[])
        throws InvocationTargetException, RemoteException
    {
        return parent.invoke(s, s1, aobj);
    }

    public Object invoke(Object ejbobject, String s, Object aobj[])
        throws InvocationTargetException, RemoteException
    {
        return parent.invoke(ejbobject, s, aobj);
    }

    public void remove()
        throws RemoteException, RemoveException
    {
    }

    public void rollback()
        throws EJBException, RemoteException
    {
    }

    private final UserTransaction parent;
}