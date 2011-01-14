package it.cnr.jada.util.ejb.was35;

import it.cnr.jada.UserTransaction;

// Referenced classes of package it.cnr.jada.util.ejb.was35:
//            UserTransaction

public class UserTransactionFactory extends it.cnr.jada.util.ejb.UserTransactionFactory
{

    public UserTransactionFactory()
    {
    }

    public UserTransaction createUserTransaction(Object obj)
    {
        return new it.cnr.jada.util.ejb.was35.UserTransaction(obj);
    }
}