package it.cnr.jada.bulk;

import java.io.*;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

class BulkPrimaryKey
    implements Serializable
{

    private BulkPrimaryKey(Object obj)
    {
        bulk = (OggettoBulk)obj;
    }

    public boolean equals(Object obj)
    {
        return bulk.equalsByPrimaryKey(getBulk(obj));
    }

    public final OggettoBulk getBulk()
    {
        return bulk;
    }

    public static Object getBulk(Object obj)
    {
        if(obj instanceof BulkPrimaryKey)
            return ((BulkPrimaryKey)obj).getBulk();
        else
            return obj;
    }

    public static Object getPrimaryKey(Object obj)
    {
        if(obj instanceof OggettoBulk)
            return new BulkPrimaryKey(obj);
        else
            return obj;
    }

    public int hashCode()
    {
        return bulk.primaryKeyHashCode();
    }

    private synchronized void readObject(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException
    {
        bulk = (OggettoBulk)objectinputstream.readObject();
    }

    private synchronized void writeObject(ObjectOutputStream objectoutputstream)
        throws IOException
    {
        objectoutputstream.writeObject(bulk);
    }

    private OggettoBulk bulk;
}