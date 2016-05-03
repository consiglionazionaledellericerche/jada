package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Bframe_blobHome extends BulkHome
{

    protected Bframe_blobHome(Class clazz, Connection connection)
    {
        super(clazz, connection);
    }

    protected Bframe_blobHome(Class clazz, Connection connection, PersistentCache persistentCache)
    {
        super(clazz, connection, persistentCache);
    }

    public Bframe_blobHome(Connection conn)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blobBulk.class, conn);
    }

    public Bframe_blobHome(Connection conn, PersistentCache persistentCache)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blobBulk.class, conn, persistentCache);
    }
}