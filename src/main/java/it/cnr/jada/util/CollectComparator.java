package it.cnr.jada.util;

import java.io.Serializable;
import java.util.Comparator;

public class CollectComparator
    implements Serializable, Comparator
{

    public CollectComparator(String s, Comparator comparator1)
    {
        property = s;
        comparator = comparator1;
    }

    public int compare(Object obj, Object obj1)
    {
        return 0;
    }

    public final Comparator getComparator()
    {
        return comparator;
    }

    public final String getProperty()
    {
        return property;
    }

    private final String property;
    private final Comparator comparator;
}