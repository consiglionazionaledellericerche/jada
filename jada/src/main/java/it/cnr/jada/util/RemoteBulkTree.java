package it.cnr.jada.util;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;

import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator

public interface RemoteBulkTree
{

    public abstract RemoteIterator getChildren(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws RemoteException;

    public abstract OggettoBulk getParent(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws RemoteException;

    public abstract boolean isLeaf(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws RemoteException;
}