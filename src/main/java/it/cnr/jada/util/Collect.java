package it.cnr.jada.util;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.util:
//            Sortable, Introspector, IntrospectionError

public class Collect extends AbstractList
    implements Serializable, Sortable
{
    class CollectComparator
        implements Serializable, Comparator
    {

        public int compare(Object obj, Object obj1)
        {
            return comparator.compare(selectElement(obj), selectElement(obj1));
        }

        private final Comparator comparator;

        private CollectComparator(Comparator comparator1)
        {
            comparator = comparator1;
        }

        CollectComparator(Comparator comparator1, CollectComparator collectcomparator)
        {
            this(comparator1);
        }
    }


    public Collect(List list1, String s)
    {
        list = list1;
        selectProperty = s;
    }

    public Object get(int i)
    {
        return selectElement(list.get(i));
    }

    public Object remove(int i)
    {
        return selectElement(list.remove(i));
    }

    private Object selectElement(Object obj)
    {
        if(obj == null)
            return null;
        try
        {
            return Introspector.getPropertyValue(obj, selectProperty);
        }
        catch(Exception exception)
        {
            throw new IntrospectionError(exception);
        }
    }

    public void setList(List list1)
    {
        list = list1;
    }

    public int size()
    {
        return list.size();
    }

    public void sort(Comparator comparator)
    {
        Collections.sort(list, new CollectComparator(comparator, null));
    }

    protected List list;
    protected String selectProperty;

}