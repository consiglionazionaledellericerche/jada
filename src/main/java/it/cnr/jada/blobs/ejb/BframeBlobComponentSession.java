package it.cnr.jada.blobs.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.RicercaComponentSession;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.Remote;
import java.rmi.RemoteException;

@Remote
public interface BframeBlobComponentSession extends RicercaComponentSession {
    public abstract void elimina(UserContext usercontext, Bframe_blob_pathBulk abframe_blob_pathbulk[])
            throws ComponentException, RemoteException;

    public abstract RemoteIterator getBlobChildren(UserContext usercontext, Bframe_blob_pathBulk bframe_blob_pathbulk, String s)
            throws ComponentException, RemoteException;

    public abstract Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext usercontext)
            throws ComponentException, RemoteException;

    void insertBlob(UserContext context) throws ComponentException, Exception;
}