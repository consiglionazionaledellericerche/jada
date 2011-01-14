package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Bframe_blob_tipoHome extends BulkHome
{

    public Bframe_blob_tipoHome(Connection conn)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class, conn);
    }

    public Bframe_blob_tipoHome(Connection conn, PersistentCache persistentCache)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class, conn, persistentCache);
    }
}