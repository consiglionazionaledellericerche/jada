package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            PrimaryKeyIterator

class PrimaryKeyMapKeySet extends AbstractSet
    implements Serializable
{

    public PrimaryKeyMapKeySet(Set set1)
    {
        set = set1;
    }

    public void clear()
    {
        set.clear();
    }

    public boolean contains(Object obj)
    {
        return set.contains(obj);
    }

    public Iterator iterator()
    {
        return new PrimaryKeyIterator(set.iterator());
    }

    public boolean remove(Object obj)
    {
        return set.remove(obj);
    }

    public int size()
    {
        return set.size();
    }

    private final Set set;
}