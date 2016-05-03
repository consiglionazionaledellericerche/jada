package it.cnr.jada.util;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator, RemoteOrderable

public class EmptyRemoteIterator
    implements Serializable, RemoteIterator, RemoteOrderable
{

    public EmptyRemoteIterator()
    {
    }

    public void close()
        throws RemoteException
    {
    }

    public int countElements()
        throws RemoteException
    {
        return 0;
    }

    public int getOrderBy(String s)
        throws RemoteException
    {
        return 0;
    }

    public boolean hasMoreElements()
        throws RemoteException
    {
        return false;
    }

    public boolean isOrderableBy(String s)
        throws RemoteException
    {
        return false;
    }

    public void moveTo(int i)
        throws RemoteException
    {
    }

    public Object nextElement()
        throws RemoteException
    {
        return null;
    }

    public void refresh()
        throws RemoteException
    {
    }

    public void setOrderBy(String s, int i)
        throws RemoteException
    {
    }

	public void ejbRemove() throws RemoteException {
		
	}
}