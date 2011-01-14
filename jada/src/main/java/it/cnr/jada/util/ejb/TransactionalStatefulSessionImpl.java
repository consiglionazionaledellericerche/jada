package it.cnr.jada.util.ejb;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.ejb.UserTransactionWrapper;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import javax.ejb.*;

public abstract class TransactionalStatefulSessionImpl
    implements EJBObject, Serializable
{

    public TransactionalStatefulSessionImpl()
    {
    }

    public EJBHome getEJBHome()
        throws RemoteException
    {
        return ejbObject.getEJBHome();
    }

    public EJBObject getEjbObject()
    {
        return ejbObject;
    }

    public Handle getHandle()
        throws RemoteException
    {
        return ejbObject.getHandle();
    }

    public Object getPrimaryKey()
        throws RemoteException
    {
        return ejbObject.getPrimaryKey();
    }

    public UserTransaction getUserTransaction()
    {
        return userTransaction;
    }

    public UserTransactionWrapper getUserTransactionWrapper()
    {
        return userTransactionWrapper;
    }

    public Object invoke(String s, Object aobj[])
        throws InvocationTargetException, RemoteException
    {
        return userTransaction.invoke(this, s, aobj);
    }

    public boolean isIdentical(EJBObject ejbobject)
        throws RemoteException
    {
        return ejbObject.isIdentical(ejbobject);
    }

    public void remove()
        throws RemoteException, RemoveException
    {
    }

    public void setEjbObject(EJBObject ejbobject)
    {
        ejbObject = ejbobject;
    }

    public void setUserTransaction(UserTransaction usertransaction)
    {
        userTransaction = usertransaction;
    }

    public void setUserTransactionWrapper(UserTransactionWrapper usertransactionwrapper)
    {
        userTransactionWrapper = usertransactionwrapper;
    }

    private UserTransaction userTransaction;
    private UserTransactionWrapper userTransactionWrapper;
    protected EJBObject ejbObject;
}