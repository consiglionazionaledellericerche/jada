package it.cnr.jada.persistency;


// Referenced classes of package it.cnr.jada.persistency:
//            IntrospectionException, KeyedPersistent, PersistentInfo

public interface Introspector
{

    public abstract String getOid(KeyedPersistent keyedpersistent)
        throws IntrospectionException;

    public abstract PersistentInfo getPersistentInfo(Class class1)
        throws IntrospectionException;

    public abstract Class getPropertyType(Class class1, String s)
        throws IntrospectionException;

    public abstract Object getPropertyValue(Object obj, String s)
        throws IntrospectionException;

    public abstract void setPropertyValue(Object obj, String s, Object obj1)
        throws IntrospectionException;
}