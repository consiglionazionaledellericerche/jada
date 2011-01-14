package it.cnr.jada.util.ejb;


public class EJBTracer
{

    private EJBTracer()
    {
        activeComponentCounter = 0L;
        activeUserTransactionCounter = 0L;
        activeBulkLoaderIteratorCounter = 0L;
    }

    public synchronized void decrementActiveBulkLoaderIteratorCounter()
    {
        activeBulkLoaderIteratorCounter--;
    }

    public synchronized void decrementActiveComponentCounter()
    {
        activeComponentCounter--;
    }

    public synchronized void decrementActiveUserTransactionCounter()
    {
        activeUserTransactionCounter--;
    }

    public synchronized long getActiveBulkLoaderIteratorCounter()
    {
        return activeBulkLoaderIteratorCounter;
    }

    public synchronized long getActiveComponentCounter()
    {
        return activeComponentCounter;
    }

    public synchronized long getActiveUserTransactionCounter()
    {
        return activeUserTransactionCounter;
    }

    public static final EJBTracer getInstance()
    {
        if(instance == null)
            instance = new EJBTracer();
        return instance;
    }

    public synchronized void incrementActiveBulkLoaderIteratorCounter()
    {
        activeBulkLoaderIteratorCounter++;
    }

    public synchronized void incrementActiveComponentCounter()
    {
        activeComponentCounter++;
    }

    public synchronized void incrementActiveUserTransactionCounter()
    {
        activeUserTransactionCounter++;
    }

    private long activeComponentCounter;
    private long activeUserTransactionCounter;
    private long activeBulkLoaderIteratorCounter;
    private static EJBTracer instance;
}