package it.cnr.jada.util;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

public class ArrayRemoteIterator implements RemoteOrderable, RemoteIterator, Serializable{

	private Object array[];
    private int position;

    public ArrayRemoteIterator(Object aobj[])
    {
        array = aobj;
    }

    public void close()
        throws RemoteException
    {
    }

    public int countElements()
    {
        return array.length;
    }

    public int getOrderBy(String s)
        throws RemoteException
    {
        return 0;
    }

    public boolean hasMoreElements()
    {
        return position < array.length;
    }

    public boolean isOrderableBy(String s)
        throws RemoteException
    {
        return false;
    }

    public void moveTo(int i)
    {
        if(i < 0 || i >= array.length)
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
        if(position >= array.length)
            throw new NoSuchElementException();
        else
            return array[position++];
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