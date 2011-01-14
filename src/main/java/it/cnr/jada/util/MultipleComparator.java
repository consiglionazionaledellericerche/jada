package it.cnr.jada.util;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

// Referenced classes of package it.cnr.jada.util:
//            Introspector, IntrospectionError

public class MultipleComparator
    implements Serializable, Comparator
{

    public MultipleComparator(Map map)
    {
        orderByClauses = map;
    }

    public int compare(Object obj, Object obj1)
    {
        try
        {
            for(Iterator iterator = orderByClauses.entrySet().iterator(); iterator.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                String s = (String)entry.getKey();
                int i = ((Integer)entry.getValue()).intValue();
                if(i != 0)
                {
                    Comparable comparable = (Comparable)Introspector.getPropertyValue(obj, s);
                    Comparable comparable1 = (Comparable)Introspector.getPropertyValue(obj1, s);
                    if(comparable == null)
                        return comparable1 == null ? 0 : 1;
                    if(comparable1 == null)
                        return -1;
                    int j = comparable.compareTo(comparable1);
                    if(j != 0)
                        return i != 1 ? -j : j;
                }
            }

            return 0;
        }
        catch(IntrospectionException introspectionexception)
        {
            throw new IntrospectionError(introspectionexception);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            throw new IntrospectionError(invocationtargetexception);
        }
    }

    private final Map orderByClauses;
}