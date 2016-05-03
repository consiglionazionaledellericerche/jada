package it.cnr.jada.persistency;

import java.util.Map;

// Referenced classes of package it.cnr.jada.persistency:
//            IntrospectionException, FetchPolicy

public interface PersistentInfo
{

    public abstract Map getFetchPolicies();

    public abstract FetchPolicy getFetchPolicy(String s);

    public abstract Class getKeyClass();

    public abstract Map getNotInOidPersistentProperties();

    public abstract Map getOidPersistentProperties();

    public abstract Class getPersistentClass();

    public abstract Map getPersistentProperties();

    public abstract void initialize()
        throws IntrospectionException;
}