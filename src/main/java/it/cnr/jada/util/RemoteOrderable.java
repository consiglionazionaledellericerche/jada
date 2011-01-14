package it.cnr.jada.util;

import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util:
//            OrderConstants

public interface RemoteOrderable
    extends OrderConstants
{

    public abstract int getOrderBy(String s)
        throws RemoteException;

    public abstract boolean isOrderableBy(String s)
        throws RemoteException;

    public abstract void setOrderBy(String s, int i)
        throws RemoteException;
}