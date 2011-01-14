package it.cnr.jada.persistency;


// Referenced classes of package it.cnr.jada.persistency:
//            KeyedPersistent

public interface Keyed
    extends KeyedPersistent
{

    public abstract KeyedPersistent getKey();

    public abstract void setKey(KeyedPersistent keyedpersistent);
}