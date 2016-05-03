package it.cnr.jada.persistency;

import it.cnr.jada.UserContext;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, PersistencyListener, Introspector, Persistent

public abstract class Persister
    implements Serializable
{

    public Persister(Introspector introspector1)
    {
        introspector = introspector1;
    }

    public void delete(Persistent persistent, UserContext userContext)
        throws PersistencyException
    {
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).deletingUsing(this, userContext);
        doDelete(persistent);
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).deletedUsing(this, userContext);
    }

    protected abstract void doDelete(Persistent persistent)
        throws PersistencyException;

    protected abstract void doInsert(Persistent persistent)
        throws PersistencyException;

    protected abstract void doUpdate(Persistent persistent)
        throws PersistencyException;

    public Introspector getIntrospector()
    {
        return introspector;
    }

    public void insert(Persistent persistent, UserContext userContext)
        throws PersistencyException
    {
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).insertingUsing(this, userContext);
        doInsert(persistent);
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).insertedUsing(this, userContext);
    }

    public void setIntrospector(Introspector introspector1)
    {
        introspector = introspector1;
    }

    public void update(Persistent persistent, UserContext userContext)
        throws PersistencyException
    {
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).updatingUsing(this, userContext);
        doUpdate(persistent);
        if(persistent instanceof PersistencyListener)
            ((PersistencyListener)persistent).updatedUsing(this, userContext);
    }

    private Introspector introspector;
}