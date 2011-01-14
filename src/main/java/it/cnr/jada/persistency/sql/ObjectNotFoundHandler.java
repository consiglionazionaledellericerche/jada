package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.ObjectNotFoundException;

public interface ObjectNotFoundHandler
{

    public abstract void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception)
        throws ObjectNotFoundException;
}