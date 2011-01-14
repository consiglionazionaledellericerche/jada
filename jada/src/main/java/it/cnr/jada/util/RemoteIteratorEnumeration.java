package it.cnr.jada.util;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator, RemotePagedIterator

public class RemoteIteratorEnumeration
    implements Serializable, Enumeration
{

    public RemoteIteratorEnumeration(RemoteIterator remoteiterator)
    {
        iterator = remoteiterator;
        try
        {
            remoteiterator.moveTo(0);
            fetchPage();
        }
        catch(RemoteException _ex) { }
    }

    private void fetchPage()
        throws RemoteException
    {
        if((iterator instanceof RemotePagedIterator) && (page == null || position >= page.length))
        {
            if(((RemotePagedIterator)iterator).hasMorePages())
                page = ((RemotePagedIterator)iterator).nextPage();
            else
                page = new OggettoBulk[0];
            position = 0;
        }
    }

    public boolean hasMoreElements()
    {
        try
        {
            if(page == null)
                return iterator.hasMoreElements();
            return position < page.length;
        }
        catch(RemoteException _ex)
        {
            throw new NoSuchElementException();
        }
    }

    public Object nextElement()
    {
        try
        {
            if(page == null)
            {
                return iterator.nextElement();
            } else
            {
                Object obj = page[position++];
                fetchPage();
                return obj;
            }
        }
        catch(RemoteException _ex)
        {
            throw new NoSuchElementException();
        }
    }

    private final RemoteIterator iterator;
    private Object page[];
    private int position;
}