package it.cnr.jada.util.ejb;


import it.cnr.jada.UserContext;
import it.cnr.jada.ejb.BaseBulkLoaderIteratorBean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EJBTracer {

    private static EJBTracer instance;
    private long activeComponentCounter;
    private long activeUserTransactionCounter;
    private long activeBulkLoaderIteratorCounter;
    private Map<String, IteratorTracer> tracers;

    private EJBTracer() {
        activeComponentCounter = 0L;
        activeUserTransactionCounter = 0L;
        activeBulkLoaderIteratorCounter = 0L;
        tracers = new HashMap<>();
    }

    public static final EJBTracer getInstance() {
        if (instance == null)
            instance = new EJBTracer();
        return instance;
    }

    public synchronized void decrementActiveBulkLoaderIteratorCounter() {
        activeBulkLoaderIteratorCounter--;
    }

    public synchronized void decrementActiveComponentCounter() {
        activeComponentCounter--;
    }

    public synchronized void decrementActiveUserTransactionCounter() {
        activeUserTransactionCounter--;
    }

    public synchronized long getActiveBulkLoaderIteratorCounter() {
        return activeBulkLoaderIteratorCounter;
    }

    public synchronized long getActiveComponentCounter() {
        return activeComponentCounter;
    }

    public synchronized long getActiveUserTransactionCounter() {
        return activeUserTransactionCounter;
    }

    public synchronized void incrementActiveBulkLoaderIteratorCounter() {
        activeBulkLoaderIteratorCounter++;
    }

    public synchronized void incrementActiveComponentCounter() {
        activeComponentCounter++;
    }

    public synchronized void incrementActiveUserTransactionCounter() {
        activeUserTransactionCounter++;
    }

    public void addToTacers(String uid, BaseBulkLoaderIteratorBean baseBulkLoaderIteratorBean, UserContext userContext) {
        tracers.put(uid, new IteratorTracer(LocalDateTime.now(), baseBulkLoaderIteratorBean.getQuery().toString(), userContext));
    }

    public void removeToTracers(String uid) {
        tracers.remove(uid);
    }

    public Map<String, IteratorTracer> getTracers() {
        return tracers;
    }

    public class IteratorTracer {
        private final LocalDateTime creationDate;
        private final String query;
        private final UserContext userContext;

        public IteratorTracer(LocalDateTime creationDate, String query, UserContext userContext) {
            this.creationDate = creationDate;
            this.query = query;
            this.userContext = userContext;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public String getQuery() {
            return query;
        }

        public UserContext getUserContext() {
            return userContext;
        }
    }
}