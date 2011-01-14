package it.cnr.jada.blobs.bulk;

import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blobHome

public class Bframe_blob_pathHome extends Bframe_blobHome
{

    protected Bframe_blob_pathHome(Class clazz, Connection connection)
    {
        super(clazz, connection);
    }

    protected Bframe_blob_pathHome(Class clazz, Connection connection, PersistentCache persistentCache)
    {
        super(clazz, connection, persistentCache);
    }

    public Bframe_blob_pathHome(Connection conn)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, conn);
    }

    public Bframe_blob_pathHome(Connection conn, PersistentCache persistentCache)
    {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, conn, persistentCache);
    }
}