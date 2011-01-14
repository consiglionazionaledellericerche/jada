package it.cnr.jada.persistency;


// Referenced classes of package it.cnr.jada.persistency:
//            IntrospectionException, FetchException, Broker

public interface FetchListener
{

    public abstract void fetchedFrom(Broker broker)
        throws IntrospectionException, FetchException;
}