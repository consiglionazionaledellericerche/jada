package it.cnr.jada.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.RemotePagedIterator;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.ejb.Remote;

@Remote
public interface BulkLoaderIterator extends RemotePagedIterator, RemoteOrderable{

    public abstract void close()
        throws RemoteException;
    /**
     * Restituisce la dimensione della collezione di elementi.
     */
    public abstract int countElements()
        throws DetailedRuntimeException, RemoteException;
    /**
     * Restituisce il numero di pagine che l'iterator è in grado di scorrere.
     */
    public abstract int countPages()
        throws DetailedRuntimeException, RemoteException;

    public abstract int getOrderBy(String s)
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Restituisce false se il cursore è arrivato oltre l'ultimo elemento della collezione
     */
    public abstract boolean hasMoreElements()
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Interroga il ricevente se possiede ancora pagine da scorrere partendo dalla posizione corrente del cursore
     */
    public abstract boolean hasMorePages()
        throws DetailedRuntimeException, RemoteException;

    public abstract boolean isOrderableBy(String s)
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Muove il cursore alla posizione specificata.
     */
    public abstract void moveTo(int i)
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Muove il cursore del ricevente all'inizio di una pagina
     */
    public abstract void moveToPage(int i)
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Restituisce il prossimo elemento della collezione.
     */
    public abstract Object nextElement()
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Legge l'intero contenuto della pagina corrente; all'uscita il cursore del ricevente è posizionato all'inizio della pagina successiva
     */
    public abstract Object[] nextPage()
        throws DetailedRuntimeException, RemoteException;

    public abstract void open(UserContext usercontext)
        throws ComponentException, RemoteException, DetailedRuntimeException;

    public abstract void refresh()
        throws DetailedRuntimeException, RemoteException;

    public abstract void setOrderBy(String s, int i)
        throws DetailedRuntimeException, RemoteException;
    /**
     * Description copied from interface:
     * Imposta la dimensione della paginatura.
     */
    public abstract void setPageSize(int i)
        throws DetailedRuntimeException, RemoteException;

    public abstract Query getQuery()
	throws DetailedRuntimeException, RemoteException;

    public abstract void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
	throws CreateException, ComponentException;

    public abstract void ejbCreate(UserContext usercontext, Query query1, Class<?> class1, String s)
	throws CreateException, ComponentException;

    public abstract void ejbRemove() 
	throws EJBException;
}