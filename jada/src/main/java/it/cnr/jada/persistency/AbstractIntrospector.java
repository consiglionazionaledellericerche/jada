package it.cnr.jada.persistency;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package it.cnr.jada.persistency:
//            Introspector, IntrospectionException, PersistentInfo, KeyedPersistent

public abstract class AbstractIntrospector
    implements Serializable, Introspector
{

    protected AbstractIntrospector(Class class1)
        throws IntrospectionException
    {
        try
        {
            persistentInfoConstructor = class1.getConstructor(new Class[] {
                java.lang.Class.class, it.cnr.jada.persistency.Introspector.class
            });
        }
        catch(NoSuchMethodException _ex)
        {
            throw new IntrospectionException("Impossibile creare un'istanza di " + class1 + ",manca un costruttore adeguato");
        }
    }

    protected void cachePersistentInfo(Class class1, PersistentInfo persistentinfo)
        throws IntrospectionException
    {
        persistentInfos.put(class1, persistentinfo);
    }

    protected PersistentInfo getCachedPersistentInfo(Class class1)
        throws IntrospectionException
    {
        return (PersistentInfo)persistentInfos.get(class1);
    }

    public PersistentInfo getPersistentInfo(Class class1)
        throws IntrospectionException
    {
    	
        PersistentInfo persistentinfo = getCachedPersistentInfo(class1);
        if(persistentinfo == null && it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1))
            synchronized(this)
            {
                persistentinfo = getCachedPersistentInfo(class1);
                if(persistentinfo == null)
                    try
                    {
                        persistentinfo = (PersistentInfo)persistentInfoConstructor.newInstance(new Object[] {
                            class1, this
                        });
                        cachePersistentInfo(class1, persistentinfo);
                        persistentinfo.initialize();
                    }
                    catch(InstantiationException instantiationexception)
                    {
                        throw new IntrospectionException("Impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), instantiationexception);
                    }
                    catch(IllegalAccessException illegalaccessexception)
                    {
                        throw new IntrospectionException("IllegalAccessException: impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), illegalaccessexception);
                    }
                    catch(InvocationTargetException invocationtargetexception)
                    {
                        throw new IntrospectionException("Impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), invocationtargetexception.getTargetException());
                    }
                    catch(ClassCastException _ex)
                    {
                        throw new IntrospectionException(persistentInfoConstructor.getDeclaringClass() + "non implementa PersistentInfo");
                    }
            }
        return persistentinfo;
    }

    public synchronized void resetPersistentInfos()
    {
        persistentInfos.clear();
    }

    public synchronized void resetPersistentInfos(Class class1)
    {
        persistentInfos.remove(class1);
    }

    public abstract String getOid(KeyedPersistent keyedpersistent)
        throws IntrospectionException;

    public abstract Class getPropertyType(Class class1, String s)
        throws IntrospectionException;

    public abstract Object getPropertyValue(Object obj, String s)
        throws IntrospectionException;

    public abstract void setPropertyValue(Object obj, String s, Object obj1)
        throws IntrospectionException;

    private final Map persistentInfos = new HashMap();
    private final Constructor persistentInfoConstructor;
}