package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey

class PrimaryKeyMapEntrySet extends AbstractSet
    implements Serializable
{
    class PrimaryKeySetIterator
        implements Iterator, java.util.Map.Entry, Serializable
    {

        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        public Object next()
        {
            entry = (java.util.Map.Entry)iterator.next();
            return this;
        }

        public void remove()
        {
            iterator.remove();
        }

        public Object getKey()
        {
			if(entry.getKey() instanceof BulkPrimaryKey)
			  return entry.getKey();
            return BulkPrimaryKey.getBulk(entry.getKey());
        }

        public Object getValue()
        {
            return entry.getValue();
        }

        public Object setValue(Object obj)
        {
            return entry.setValue(obj);
        }

        public int hashCode()
        {
            return entry.hashCode();
        }

        private Iterator iterator;
        private java.util.Map.Entry entry;

        public PrimaryKeySetIterator(Iterator iterator1)
        {
            iterator = iterator1;
        }
    }

    class PrimaryKeyMapEntry
        implements java.util.Map.Entry, Serializable
    {

        public Object getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        public Object setValue(Object obj)
        {
            return obj;
        }

        Object key;
        Object value;

        public PrimaryKeyMapEntry(java.util.Map.Entry entry)
        {
            key = BulkPrimaryKey.getPrimaryKey(entry.getKey());
            value = entry.getValue();
        }
    }


    public PrimaryKeyMapEntrySet(Set set1)
    {
        set = set1;
    }

    public void clear()
    {
        set.clear();
    }

    public boolean contains(Object obj)
    {
        if(!(obj instanceof java.util.Map.Entry))
            return false;
        else
            return set.contains(new PrimaryKeyMapEntry((java.util.Map.Entry)obj));
    }

    public Iterator iterator()
    {
        return new PrimaryKeySetIterator(set.iterator());
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