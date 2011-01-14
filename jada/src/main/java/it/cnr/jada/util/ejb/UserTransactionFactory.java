package it.cnr.jada.util.ejb;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.util.PlatformObjectFactory;

public abstract class UserTransactionFactory
{

    public UserTransactionFactory()
    {
    }

    public abstract UserTransaction createUserTransaction(Object obj);

    public static final UserTransactionFactory getFactory()
    {
        return factory;
    }

    private static final UserTransactionFactory factory;

    static 
    {
        factory = (UserTransactionFactory)PlatformObjectFactory.createInstance(it.cnr.jada.util.ejb.UserTransactionFactory.class, "was35");
    }
}