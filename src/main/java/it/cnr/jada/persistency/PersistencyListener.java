package it.cnr.jada.persistency;

import it.cnr.jada.UserContext;


// Referenced classes of package it.cnr.jada.persistency:
//            Persister

public interface PersistencyListener
{

    public abstract void deletedUsing(Persister persister, UserContext userContext);

    public abstract void deletingUsing(Persister persister, UserContext userContext);

    public abstract void insertedUsing(Persister persister, UserContext userContext);

    public abstract void insertingUsing(Persister persister, UserContext userContext);

    public abstract void updatedUsing(Persister persister, UserContext userContext);

    public abstract void updatingUsing(Persister persister, UserContext userContext);
}