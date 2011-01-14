package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey, PrimaryKeyMapEntrySet, PrimaryKeyMapKeySet

public class PrimaryKeyHashMap extends HashMap
    implements Serializable
{

    public PrimaryKeyHashMap()
    {
    }

    public PrimaryKeyHashMap(int i)
    {
        super(i);
    }

    public PrimaryKeyHashMap(int i, float f)
    {
        super(i, f);
    }

    public PrimaryKeyHashMap(Map map)
    {
        super(map);
    }

    public boolean containsKey(Object obj)
    {
        return super.containsKey(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public Set entrySet()
    {
        if(entrySet == null)
            return entrySet = new PrimaryKeyMapEntrySet(super.entrySet());
        else
            return entrySet;
    }

    public Object get(Object obj)
    {
    	//Aggiunto il 21/10/2004 da Marco in seguito alla
    	//segnalazione di NullPointerException 
    	Object o = super.get(BulkPrimaryKey.getPrimaryKey(obj));
    	if(o==null)
          return super.get(obj);
        else
          return o;  
    }

    public Set keySet()
    {
        if(keySet == null)
            return keySet = new PrimaryKeyMapKeySet(super.keySet());
        else
            return keySet;
    }

    public Object put(Object obj, Object obj1)
    {
        return super.put(BulkPrimaryKey.getPrimaryKey(obj), obj1);
    }

    public Object remove(Object obj)
    {
        return super.remove(BulkPrimaryKey.getPrimaryKey(obj));
    }

    private transient Set keySet;
    private transient Set entrySet;
}