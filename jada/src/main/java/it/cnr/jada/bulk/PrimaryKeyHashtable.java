package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey, PrimaryKeyMapEntrySet, PrimaryKeyMapKeySet

public class PrimaryKeyHashtable extends Hashtable
    implements Serializable
{
    private class KeysEnumeration
        implements Serializable, Enumeration
    {

        public Object nextElement()
        {
            return BulkPrimaryKey.getBulk(e.nextElement());
        }

        public boolean hasMoreElements()
        {
            return e.hasMoreElements();
        }

        private Enumeration e;

        KeysEnumeration(Enumeration enumeration)
        {
            e = enumeration;
        }
    }


    public PrimaryKeyHashtable()
    {
    }

    public PrimaryKeyHashtable(int i)
    {
        super(i);
    }

    public PrimaryKeyHashtable(int i, float f)
    {
        super(i, f);
    }

    public PrimaryKeyHashtable(Map map)
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
		//Aggiunto il 02/11/2004 da Marco in seguito alla
		//segnalazione di NullPointerException
		Object o = super.get(BulkPrimaryKey.getPrimaryKey(obj));
		if(o==null)
		  return super.get(obj);
		else
		  return o;  
    }

    public synchronized Enumeration keys()
    {
        return new KeysEnumeration(super.keys());
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