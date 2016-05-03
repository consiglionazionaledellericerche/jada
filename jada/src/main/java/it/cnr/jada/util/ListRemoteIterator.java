package it.cnr.jada.util;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteOrderable, RemoteIterator

public class ListRemoteIterator implements RemoteOrderable, RemoteIterator, Serializable{

    private List list;
    private int position;

    public ListRemoteIterator(List list1)
    {
        list = list1;
    }

    public void close()
        throws RemoteException
    {
    }

    public int countElements()
    {
        return list.size();
    }

    public int getOrderBy(String s)
        throws RemoteException
    {
        return 0;
    }

    public boolean hasMoreElements()
    {
        return position < list.size();
    }

    public boolean isOrderableBy(String s)
        throws RemoteException
    {
        return false;
    }

    public void moveTo(int i)
    {
        if(i < 0 || i >= list.size())
        {
            throw new NoSuchElementException();
        } else
        {
            position = i;
            return;
        }
    }

    public Object nextElement()
    {
        if(position >= list.size())
            throw new NoSuchElementException();
        else
            return list.get(position++);
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
		// TODO Auto-generated method stub
		
	}
}