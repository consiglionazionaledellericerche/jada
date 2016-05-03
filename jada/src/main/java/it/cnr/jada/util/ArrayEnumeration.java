package it.cnr.jada.util;

import java.util.Enumeration;

public final class ArrayEnumeration
    implements Enumeration
{

    public ArrayEnumeration(Object aobj[])
    {
        array = aobj;
    }

    public final boolean hasMoreElements()
    {
        return index < array.length;
    }

    public final Object nextElement()
    {
        return array[index++];
    }

    private Object array[];
    int index;
}