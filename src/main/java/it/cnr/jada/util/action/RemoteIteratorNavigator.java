package it.cnr.jada.util.action;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            Navigator

public class RemoteIteratorNavigator extends Navigator
    implements Serializable
{

    public RemoteIteratorNavigator()
    {
    }

    protected int countElements()
        throws BusinessProcessException
    {
        try
        {
            return iterator.countElements();
        }
        catch(RemoteException remoteexception)
        {
            throw new BusinessProcessException(remoteexception);
        }
    }

    public OggettoBulk[] fetchPageContents(ActionContext actioncontext, int i)
        throws BusinessProcessException
    {
        try
        {
            if(pagedIterator != null)
            {
                pagedIterator.moveToPage(i);
                return (OggettoBulk[])pagedIterator.nextPage();
            }
            int j = i * getPageSize();
            OggettoBulk aoggettobulk[] = new OggettoBulk[Math.min(getPageSize(), getElementsCount() - j)];
            iterator.moveTo(j);
            for(int k = 0; k < getPageSize() && j < getElementsCount(); j++)
            {
                aoggettobulk[k] = (OggettoBulk)iterator.nextElement();
                k++;
            }

            setPageContents(aoggettobulk);
            return aoggettobulk;
        }
        catch(RemoteException remoteexception)
        {
            throw new BusinessProcessException(remoteexception);
        }
    }

    public RemoteIterator getIterator()
    {
        return iterator;
    }

    public int getOrderBy(String s)
    {
        try
        {
            return ((RemoteOrderable)iterator).getOrderBy(s);
        }
        catch(RemoteException remoteexception)
        {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator)
        throws RemoteException, BusinessProcessException
    {
        setIterator(actioncontext, remoteiterator, getPageSize(), getPageFrameSize());
    }

    public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator, int i, int j)
        throws RemoteException, BusinessProcessException
    {
        EJBCommonServices.closeRemoteIterator(actioncontext, iterator);
        iterator = remoteiterator;
        if(remoteiterator instanceof RemotePagedIterator)
            pagedIterator = (RemotePagedIterator)remoteiterator;
        setPageFrameSize(j);
        setPageSize(i);
        reset(actioncontext);
    }

    public void setOrderBy(String s, int i)
    {
        try
        {
            ((RemoteOrderable)iterator).setOrderBy(s, i);
        }
        catch(RemoteException remoteexception)
        {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    public void setPageSize(int i)
        throws BusinessProcessException
    {
        try
        {
            if(pagedIterator != null)
                pagedIterator.setPageSize(i);
        }
        catch(RemoteException remoteexception)
        {
            throw new BusinessProcessException(remoteexception);
        }
        super.setPageSize(i);
    }

    private RemoteIterator iterator;
    private RemotePagedIterator pagedIterator;
}