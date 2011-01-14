package it.cnr.jada.blobs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;

public interface IBframeBlobMgr
{

    public abstract void elimina(UserContext usercontext, Bframe_blob_pathBulk abframe_blob_pathbulk[])
        throws ComponentException;

    public abstract RemoteIterator getBlobChildren(UserContext usercontext, Bframe_blob_pathBulk bframe_blob_pathbulk, String s)
        throws ComponentException;

    public abstract Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext usercontext)
        throws ComponentException;
}