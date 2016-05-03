package it.cnr.jada.util;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.util:
//            OrderedHashMapEntry

public class OrderedHashMap extends AbstractMap
    implements Cloneable, Serializable
{
    public class EntryIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        public Object next()
        {
            OrderedHashMapEntry orderedhashmapentry = (OrderedHashMapEntry)iterator.next();
            key = orderedhashmapentry.getKey();
            return orderedhashmapentry;
        }

        public void remove()
        {
            iterator.remove();
            decrementIndexesFrom(((Integer)map.remove(key)).intValue() + 1);
        }

        private Iterator iterator;
        private Object key;

        public EntryIterator()
        {
            iterator = entries.iterator();
        }
    }


    public OrderedHashMap()
    {
        this(((Map) (new HashMap())));
    }

    public OrderedHashMap(Map map1)
    {
        entries = new ArrayList();
        map = map1;
    }

    private void decrementIndexesFrom(int i)
    {
        for(Iterator iterator1 = map.entrySet().iterator(); iterator1.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
            int j = ((Integer)entry.getValue()).intValue();
            if(i <= j)
                entry.setValue(new Integer(--j));
        }

    }

    public Set entrySet()
    {
        if(entrySet == null)
            entrySet = new AbstractSet() {

                public Iterator iterator()
                {
                    return new EntryIterator();
                }

                public boolean contains(Object obj)
                {
                    return entries.contains(obj);
                }

                public boolean remove(Object obj)
                {
                    if(entries.remove(obj))
                    {
                        Integer integer = (Integer)map.remove(((OrderedHashMapEntry)obj).getKey());
                        decrementIndexesFrom(integer.intValue() + 1);
                        return true;
                    } else
                    {
                        return false;
                    }
                }

                public int size()
                {
                    return entries.size();
                }

                public void clear()
                {
                    entries.clear();
                    map.clear();
                }

            };
        return entrySet;
    }

    public Object get(int i)
    {
        return ((OrderedHashMapEntry)entries).getValue();
    }

    public int indexOf(Object obj)
    {
        Integer integer = (Integer)map.get(obj);
        if(integer == null)
            return -1;
        else
            return integer.intValue();
    }

    public Iterator iterator()
    {
        return values().iterator();
    }

    public Set keySet()
    {
        if(keySet == null)
            keySet = new AbstractSet() {

                public Iterator iterator()
                {
                    return new EntryIterator() {

                        public Object next()
                        {
                            return ((OrderedHashMapEntry)super.next()).getKey();
                        }

                    };
                }

                public int size()
                {
                    return this.size();
                }

                public boolean contains(Object obj)
                {
                    return containsKey(obj);
                }

                public boolean remove(Object obj)
                {
                    return OrderedHashMap.this.remove(obj) != null;
                }

                public void clear()
                {
                    this.clear();
                }

            };
        return keySet;
    }

    private Integer newIndex()
    {
        return new Integer(entries.size());
    }

    public Object put(Object obj, Object obj1)
    {
        Integer integer;
        if((integer = (Integer)map.get(obj)) == null)
        {
            integer = newIndex();
            map.put(obj, integer);
            entries.add(new OrderedHashMapEntry(obj, obj1));
            return null;
        } else
        {
            OrderedHashMapEntry orderedhashmapentry = (OrderedHashMapEntry)entries.get(integer.intValue());
            return orderedhashmapentry.setValue(obj1);
        }
    }

    public Object remove(int i)
    {
        OrderedHashMapEntry orderedhashmapentry = (OrderedHashMapEntry)entries.get(i);
        map.remove(orderedhashmapentry.getKey());
        return orderedhashmapentry.getValue();
    }

    public Object remove(Object obj)
    {
        Integer integer;
        if((integer = (Integer)map.get(obj)) == null)
        {
            return null;
        } else
        {
            Integer integer1 = (Integer)map.remove(obj);
            OrderedHashMapEntry orderedhashmapentry = (OrderedHashMapEntry)entries.remove(integer1.intValue());
            decrementIndexesFrom(integer1.intValue() + 1);
            return orderedhashmapentry.getValue();
        }
    }

    public Collection values()
    {
        if(values == null)
            values = new AbstractCollection() {

                public Iterator iterator()
                {
                    return new EntryIterator() {

                        public Object next()
                        {
                            return ((OrderedHashMapEntry)super.next()).getValue();
                        }

                    };
                }

                public int size()
                {
                    return this.size();
                }

                public boolean contains(Object obj)
                {
                    return containsValue(obj);
                }

            };
        return values;
    }

    private final List entries;
    private final Map map;
    private transient Set entrySet;
    private transient Collection values;
    private transient Set keySet;



}