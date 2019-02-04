package it.cnr.jada.persistency.sql;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.BeanIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLPersister, PersistentHome, ObjectNotFoundHandler, SQLPersistentInfo

public class HomeCache
        implements Serializable {

    private final PersistentCache persistentCache = new PersistentCache();
    private Connection connection;
    private Map homes;
    private static final Logger logger = LoggerFactory.getLogger(HomeCache.class);

    public HomeCache(Connection connection1) {
        homes = new HashMap();
        connection = connection1;
    }

    public void clearPersistentCache() {
        persistentCache.clear();
    }

    public void fetchAll(it.cnr.jada.UserContext userContext) throws PersistencyException {
        fetchAll(userContext, null);
    }

    private void fetchAll()
            throws PersistencyException {
        fetchAll((ObjectNotFoundHandler) null);
    }

    public void fetchAll(UserContext userContext, ObjectNotFoundHandler objectnotfoundhandler) throws PersistencyException {
        try {
            while (!persistentCache.isFetchQueueEmpty()) {
                for (Iterator iterator = persistentCache.getFetchQueue(); iterator.hasNext(); ) {
                    java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                    Persistent persistent = (Persistent) entry.getKey();
                    PersistentHome persistenthome = getHome(persistent.getClass());
                    FetchPolicy fetchpolicy = (FetchPolicy) entry.getValue();
                    persistenthome.setFetchPolicy(fetchpolicy);
                    if (persistenthome.findByPrimaryKey(userContext, persistent) == null) {
                        persistentCache.removeFromFetchQueue(persistenthome.getIntrospector(), persistent, fetchpolicy);
                        ObjectNotFoundException objectnotfoundexception = new ObjectNotFoundException("Object not found during fetchAll: " + persistent, persistent);
                        if (objectnotfoundhandler != null)
                            persistenthome.handleObjectNotFoundException(objectnotfoundexception);
                    }
                }

            }
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    private void fetchAll(ObjectNotFoundHandler objectnotfoundhandler) throws PersistencyException {
        fetchAll(null, objectnotfoundhandler);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection1) {
        clearPersistentCache();
        connection = connection1;
    }

    public Iterator getFetchQueue() {
        return persistentCache.getFetchQueue();
    }

    public PersistentHome getHome(Persistent persistent) {
        return getHome(persistent.getClass(), null);
    }

    public PersistentHome getHome(Class class1) {
        return getHome(class1, null);
    }

    public PersistentHome getHome(Class class1, String s) {
        return getHome(class1, s, null);
    }

    public PersistentHome getHome(Class class1, String s, String s1) {
        Object obj = (Map) homes.get(class1);
        if (obj == null)
            homes.put(class1, obj = new HashMap());
        Object obj1 = (Map) ((Map) (obj)).get(s);
        if (obj1 == null)
            ((Map) (obj)).put(s, obj1 = new HashMap());
        PersistentHome persistenthome = (PersistentHome) ((Map) (obj1)).get(s1);
        if (persistenthome == null)
            try {
                BeanIntrospector beanintrospector = BeanIntrospector.getSQLInstance();
                Class class2 = ((SQLPersistentInfo) beanintrospector.getPersistentInfo(class1)).getHomeClass();
                if (class2 == null)
                    persistenthome = new PersistentHome(class1, connection, persistentCache);
                else
                    persistenthome = (PersistentHome) class2.getConstructor(new Class[]{
                            java.sql.Connection.class, it.cnr.jada.persistency.PersistentCache.class
                    }).newInstance(new Object[]{
                            connection, persistentCache
                    });
                ((Map) (obj1)).put(s1, persistenthome);
                persistenthome.setHomeCache(this);
                if (s != null)
                    persistenthome.setColumnMap(s);
                if (s1 != null)
                    persistenthome.setFetchPolicy(s1);
            } catch (Exception exception) {
                logger.error("Can't instantiate home", exception);
                throw new IntrospectionError("Can't instantiate home", exception);
            }
        return persistenthome;
    }

    public final PersistentCache getPersistentCache() {
        return persistentCache;
    }
}