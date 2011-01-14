package it.cnr.jada.util.ejb;

import it.cnr.jada.UserTransaction;

import java.io.Serializable;
import java.rmi.RemoteException;

public class UserTransactionTimeoutException extends RemoteException
    implements Serializable
{

    public UserTransactionTimeoutException(String s)
    {
        super(s);
    }

    public UserTransactionTimeoutException(String s, UserTransaction usertransaction)
    {
        super(s);
        userTransaction = usertransaction;
    }

    public UserTransaction getUserTransaction()
    {
        return userTransaction;
    }

    public void setUserTransaction(UserTransaction usertransaction)
    {
        userTransaction = usertransaction;
    }

    private UserTransaction userTransaction;
}