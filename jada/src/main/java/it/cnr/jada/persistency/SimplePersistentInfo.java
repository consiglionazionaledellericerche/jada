package it.cnr.jada.persistency;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistentInfo, IntrospectionException, Introspector, SimpleFetchPolicy, 
//            PersistentProperty, FetchNonePolicy, FetchPolicy, FetchAllPolicy, 
//            IntrospectionError

public class SimplePersistentInfo
    implements Serializable, PersistentInfo
{

    public SimplePersistentInfo()
    {
        persistentProperties = new HashMap();
        oidPersistentProperties = new HashMap();
        notInOidPersistentProperties = new HashMap();
        fetchPolicies = new HashMap();
    }

    public SimplePersistentInfo(Class class1, Introspector introspector1)
        throws IntrospectionException
    {
        persistentProperties = new HashMap();
        oidPersistentProperties = new HashMap();
        notInOidPersistentProperties = new HashMap();
        fetchPolicies = new HashMap();
        if(!it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1))
            throw new RuntimeException("Tentativo di costruire un SQLPersistentInfo per una classe che non \350 Persistent");
        persistentClass = class1;
        introspector = introspector1;
        if(class1 != java.lang.Object.class && it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1.getSuperclass()))
        {
            PersistentInfo persistentinfo = introspector1.getPersistentInfo(class1.getSuperclass());
            if(persistentinfo != null)
                initializeFrom(persistentinfo);
        }
    }

    public void addFetchPolicy(SimpleFetchPolicy simplefetchpolicy)
    {
        fetchPolicies.put(simplefetchpolicy.getName(), simplefetchpolicy);
    }

    public void addPersistentProperty(PersistentProperty persistentproperty)
    {
        persistentProperties.put(persistentproperty.getName(), persistentproperty);
        if(persistentproperty.isPartOfOid())
            oidPersistentProperties.put(persistentproperty.getName(), persistentproperty);
        else
            notInOidPersistentProperties.put(persistentproperty.getName(), persistentproperty);
    }

    public void clear()
    {
        persistentProperties.clear();
        oidPersistentProperties.clear();
        notInOidPersistentProperties.clear();
    }

    public Map getFetchPolicies()
    {
        return Collections.unmodifiableMap(fetchPolicies);
    }

    public FetchPolicy getFetchPolicy(String s)
    {
        if("none".equalsIgnoreCase(s))
            return FetchNonePolicy.FETCHNONE;
        FetchPolicy fetchpolicy = (FetchPolicy)fetchPolicies.get(s);
        if(fetchpolicy == null)
            return FetchAllPolicy.FETCHALL;
        else
            return fetchpolicy;
    }

    public Introspector getIntrospector()
    {
        return introspector;
    }

    public Class getKeyClass()
    {
        return keyClass;
    }

    public String getKeyClassName()
    {
        if(keyClass == null)
            return null;
        else
            return keyClass.getName();
    }

    public final Map getNotInOidPersistentProperties()
    {
        return notInOidPersistentProperties;
    }

    public final Map getOidPersistentProperties()
    {
        return oidPersistentProperties;
    }

    public final Class getPersistentClass()
    {
        return persistentClass;
    }

    public final Map getPersistentProperties()
    {
        return persistentProperties;
    }

    public void initialize()
        throws IntrospectionException
    {
    }

    protected void initializeFrom(PersistentInfo persistentinfo)
    {
        persistentProperties.putAll(persistentinfo.getPersistentProperties());
        oidPersistentProperties.putAll(persistentinfo.getOidPersistentProperties());
        notInOidPersistentProperties.putAll(persistentinfo.getNotInOidPersistentProperties());
        fetchPolicies.putAll(persistentinfo.getFetchPolicies());
        keyClass = persistentinfo.getKeyClass();
    }

    public final void removePersistentProperty(String s)
    {
        oidPersistentProperties.remove(s);
        notInOidPersistentProperties.remove(s);
        persistentProperties.remove(s);
    }

    public void setIntrospector(Introspector introspector1)
    {
        introspector = introspector1;
    }

    public void setKeyClass(Class class1)
    {
        keyClass = class1;
    }

    public void setKeyClassName(String s)
    {
        try
        {
            keyClass = getClass().getClassLoader().loadClass(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new IntrospectionError("Key class not found", classnotfoundexception);
        }
    }

    public void setPersistentClass(Class class1)
    {
        persistentClass = class1;
    }

    private Map persistentProperties;
    private Map oidPersistentProperties;
    private Map notInOidPersistentProperties;
    private Class persistentClass;
    private Introspector introspector;
    private Map fetchPolicies;
    private Class keyClass;
}