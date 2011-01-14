package it.cnr.jada.util.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.EJBException;

public class TransactionClosedException extends EJBException
    implements Serializable
{

    public TransactionClosedException()
    {
    }

    public TransactionClosedException(String s)
    {
        super(s);
    }

    public TransactionClosedException(String s, Exception exception)
    {
        super(s, exception);
    }
}