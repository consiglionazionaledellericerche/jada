package it.cnr.jada.util;

import java.io.Serializable;

class OrderedHashMapEntry
    implements Serializable, Cloneable, java.util.Map.Entry
{

    public OrderedHashMapEntry(Object obj, Object obj1)
    {
        key = obj;
        value = obj1;
    }

    protected Object clone()
    {
        return new OrderedHashMapEntry(key, value);
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof java.util.Map.Entry))
            return false;
        java.util.Map.Entry entry = (java.util.Map.Entry)obj;
        return (key != null ? key.equals(entry.getKey()) : entry.getKey() == null) && (value != null ? value.equals(entry.getValue()) : entry.getValue() == null);
    }

    public Object getKey()
    {
        return key;
    }

    public Object getValue()
    {
        return value;
    }

    public int hashCode()
    {
        return value != null ? value.hashCode() : 0;
    }

    public Object setValue(Object obj)
    {
        Object obj1 = value;
        value = obj;
        return obj1;
    }

    private Object key;
    private Object value;
}