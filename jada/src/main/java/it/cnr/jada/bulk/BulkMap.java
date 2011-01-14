package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkCollection

public class BulkMap extends AbstractMap
    implements Serializable, BulkCollection
{

    public BulkMap()
    {
        map = new HashMap();
    }

    public BulkMap(Map map1)
    {
        map = new HashMap(map1);
    }

    public void clear()
    {
        if(deleteMap == null)
            deleteMap = new HashMap();
        deleteMap.putAll(map);
        map.clear();
    }

    public boolean containsKey(Object obj)
    {
        return map.containsKey(obj);
    }

    public boolean containsValue(Object obj)
    {
        return map.containsValue(obj);
    }

    public Iterator deleteIterator()
    {
        if(deleteMap == null)
            return Collections.EMPTY_LIST.iterator();
        else
            return deleteMap.values().iterator();
    }

    public Set entrySet()
    {
        return map.entrySet();
    }

    public Object get(Object obj)
    {
        return map.get(obj);
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public Iterator iterator()
    {
        return map.values().iterator();
    }

    public Set keySet()
    {
        return map.keySet();
    }

    public Object put(Object obj, Object obj1)
    {
        if(deleteMap != null)
            deleteMap.remove(obj);
        return map.put(obj, obj1);
    }

    public void putAll(Map map1)
    {
        map.putAll(map1);
    }

    public Object remove(Object obj)
    {
        Object obj1 = map.remove(obj);
        if(obj1 != null)
        {
            if(deleteMap == null)
                deleteMap = new HashMap();
            deleteMap.put(obj, obj1);
        }
        return obj1;
    }

    public int size()
    {
        return map.size();
    }

    public Collection values()
    {
        return map.values();
    }

    private HashMap map;
    private HashMap deleteMap;
}