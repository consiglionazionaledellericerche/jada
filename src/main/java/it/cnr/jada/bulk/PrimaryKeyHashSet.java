package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey, PrimaryKeyIterator

public class PrimaryKeyHashSet extends HashSet
    implements Serializable
{

    public PrimaryKeyHashSet()
    {
    }

    public PrimaryKeyHashSet(int i)
    {
        super(i);
    }

    public PrimaryKeyHashSet(int i, float f)
    {
        super(i, f);
    }

    public PrimaryKeyHashSet(Collection collection)
    {
        super(collection);
    }

    public boolean add(Object obj)
    {
        return super.add(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public boolean contains(Object obj)
    {
        return super.contains(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public Iterator iterator()
    {
        return new PrimaryKeyIterator(super.iterator());
    }

    public boolean remove(Object obj)
    {
        return super.remove(BulkPrimaryKey.getPrimaryKey(obj));
    }
}