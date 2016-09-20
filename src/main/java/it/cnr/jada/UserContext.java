package it.cnr.jada;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public interface UserContext
    extends Serializable, Cloneable
{

    public abstract String getSessionId();

    public abstract String getUser();

    public abstract boolean isTransactional();

    public abstract void setTransactional(boolean flag);

    public abstract void writeTo(PrintWriter printwriter);
    
    public abstract Dictionary getHiddenColumns();

    public abstract Hashtable<String, Serializable> getAttributes();    
}