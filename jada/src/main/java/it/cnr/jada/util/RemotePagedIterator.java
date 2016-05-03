package it.cnr.jada.util;

import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator

public interface RemotePagedIterator
    extends RemoteIterator
{

    public abstract int countPages()
        throws RemoteException;

    public abstract boolean hasMorePages()
        throws RemoteException;

    public abstract void moveToPage(int i)
        throws RemoteException;

    public abstract Object[] nextPage()
        throws RemoteException;

    public abstract void setPageSize(int i)
        throws RemoteException;
}