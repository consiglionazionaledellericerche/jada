package it.cnr.jada.comp;

import it.cnr.jada.DetailedException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.MTUStuff;
import it.cnr.jada.bulk.MTUWrapper;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.PrimaryKeyChangedException;
import it.cnr.jada.bulk.ROWrapper;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.Introspector;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.Persister;
import it.cnr.jada.persistency.sql.ApplicationPersistencyDiscardedException;
import it.cnr.jada.persistency.sql.ApplicationPersistencyException;
import it.cnr.jada.persistency.sql.BusyRecordException;
import it.cnr.jada.persistency.sql.ColumnMap;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.DuplicateKeyException;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.LockedRecordException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.ReferentialIntegrityException;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.persistency.sql.SQLPersister;
import it.cnr.jada.persistency.sql.ValueTooLargeException;
import it.cnr.jada.util.EmptyRemoteIterator;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.EJBTracer;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.persistence.PersistenceContext;

/**
 * Classe di utilità da usare come superclasse per tutte le componenti applicative che compiono 
 * operazioni di persistenza su OggettiBulk
 */
public class GenericComponent implements Component, Serializable, Cloneable{

    protected HomeCache homeCache;
    protected HomeCache tempHomeCache;
    protected Connection connection;
    
    public GenericComponent(){
    }

    protected final MTUWrapper asMTU(OggettoBulk oggettobulk, String message) throws ComponentException{
        return new MTUWrapper(oggettobulk, new MTUStuff(message));
    }

    protected final ROWrapper asRO(OggettoBulk oggettobulk, String message) throws ComponentException{
        return new ROWrapper(oggettobulk, new MTUStuff(message));
    }
    /**
     * Effettua un controllo di validità dell'OggettoBulk con le informazioni di persistenza. 
     * Viene effettuato un controllo di validità su tutti gli attributi mappati nella defaulColumnMap 
     * dell'OggettoBulk per le seguenti condizioni: 
     * Un attributo nullo è mappato su una colonna NOT_NULLABLE; 
     * viene generata una CRUDNotNullConstraintException 
     * Un attributo di tipo String contiene una stringa troppo lunga per la colonna su cui è mappato; 
     * viene generata una CRUDTooLargeConstraintException. 
     * Un attributo numerico contiene una numero troppo grande per la colonna su cui è mappato; 
     * viene generata una CRUDTooLargeConstraintException.
     */
    protected final void checkSQLConstraints(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
        checkSQLConstraints(usercontext, oggettobulk, true, true);
    }
    /**
     * Effettua un controllo di validità dell'OggettoBulk con le informazioni di persistenza. 
     * Viene effettuato un controllo di validità su tutti gli attributi mappati nella defaulColumnMap 
     * dell'OggettoBulk per le seguenti condizioni: 
     * Un attributo nullo è mappato su una colonna NOT_NULLABLE; 
     * viene generata una CRUDNotNullConstraintException 
     * Un attributo di tipo String contiene una stringa troppo lunga per la colonna su cui è mappato; 
     * viene generata una CRUDTooLargeConstraintException. 
     * Un attributo numerico contiene una numero troppo grande per la colonna su cui è mappato; 
     * viene generata una CRUDTooLargeConstraintException.
     */
    protected final void checkSQLConstraints(UserContext usercontext, OggettoBulk oggettobulk, boolean flag, boolean flag1) throws ComponentException{
        try{
            BulkHome bulkhome = getHome(usercontext, oggettobulk);
            for(Iterator iterator1 = bulkhome.getColumnMap().getColumnMappings().iterator(); iterator1.hasNext();){
                ColumnMapping columnmapping = (ColumnMapping)iterator1.next();
                if(flag && columnmapping.isPrimary() || flag1 && !columnmapping.isPrimary())
                    try{
                        bulkhome.getIntrospector().getPropertyType(it.cnr.jada.bulk.OggettoBulk.class, columnmapping.getPropertyName());
                    }catch(IntrospectionException _ex){
                        if(!columnmapping.isNullable() && bulkhome.getIntrospector().getPropertyValue(oggettobulk, columnmapping.getPropertyName()) == null)
                            throw new CRUDNotNullConstraintException(columnmapping.getPropertyName(), oggettobulk);
                        Class class1 = bulkhome.getIntrospector().getPropertyType(oggettobulk.getClass(), columnmapping.getPropertyName());
                        if(class1 == java.lang.String.class){
                            String s = (String)bulkhome.getIntrospector().getPropertyValue(oggettobulk, columnmapping.getPropertyName());
                            if(s != null && s.length() > columnmapping.getColumnSize())
                                throw new CRUDTooLargeConstraintException(columnmapping.getPropertyName(), oggettobulk, columnmapping.getColumnSize());
                        } else
                        if(java.lang.Number.class.isAssignableFrom(class1)){
                            Number number = (Number)bulkhome.getIntrospector().getPropertyValue(oggettobulk, columnmapping.getPropertyName());
                            if(number != null)
                            {
                                BigDecimal bigdecimal;
                                if(number instanceof Double)
                                    bigdecimal = new BigDecimal(number.doubleValue());
                                else
                                if(number instanceof Float)
                                    bigdecimal = new BigDecimal(number.floatValue());
                                else
                                if(number instanceof BigDecimal)
                                    bigdecimal = (BigDecimal)number;
                                else
                                    bigdecimal = BigDecimal.valueOf(number.longValue());
                                if(bigdecimal.scale() > columnmapping.getColumnScale())
                                    throw new CRUDScaleTooLargeConstraintException(columnmapping.getPropertyName(), oggettobulk, columnmapping.getColumnScale());
                                if(bigdecimal.unscaledValue().abs().toString().length() - bigdecimal.scale() > columnmapping.getColumnSize() - columnmapping.getColumnScale())
                                    throw new CRUDTooLargeConstraintException(columnmapping.getPropertyName(), oggettobulk, columnmapping.getColumnSize());
                            }
                        }
                    }
            }

        }catch(IntrospectionException introspectionexception){
            throw handleException(introspectionexception);
        }
    }

    protected final void deleteBulk(UserContext usercontext, OggettoBulk oggettobulk) throws PersistencyException, ComponentException{
        if(oggettobulk instanceof Persistent)
            getHome(usercontext, oggettobulk).delete((Persistent)oggettobulk, usercontext);
    }
    /**
     * Effettua il caricamento di un attributo "options" di OggettoBulk. 
     * L'implementazione standard semplicemente invoca il corrispondente metodo sull'Home dell'OggettoBulk specificato; 
     * è possibile reimplementarlo per fornire un comportamento specifico per la componente. 
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è ottenuto concatenando il nome 
     * della component con la stringa ".findOptions." e il nome della optionsProperty
     */
    protected final Object findOptions(UserContext usercontext, OggettoBulk oggettobulk, FieldProperty fieldproperty) throws ComponentException{
        try{
            try{
                return it.cnr.jada.util.Introspector.invoke(this, "find", fieldproperty.getOptionsProperty(), usercontext, oggettobulk);
            }catch(NoSuchMethodException _ex){
                Class class1 = it.cnr.jada.util.Introspector.getPropertyType(oggettobulk.getClass(), fieldproperty.getProperty());
                BulkHome bulkhome;
                if(it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1))
                    bulkhome = getHome(usercontext, class1, null, getFetchPolicyName("findOptions", fieldproperty.getOptionsProperty()));
                else
                    bulkhome = null;
                return getHome(usercontext, oggettobulk).findOptions(fieldproperty.getOptionsProperty(), oggettobulk, bulkhome, null);
            }
        }catch(InvocationTargetException invocationtargetexception){
            throw handleException(invocationtargetexception.getTargetException());
        }catch(Exception exception){
            throw handleException(exception);
        }
    }
    /**
     * Restituisce un array di BulkCollection contenenti oggetti bulk dipendenti da un altro OggettoBulk.
     */
    protected BulkCollection[] getBulkListsForPersistency(UserContext usercontext, OggettoBulk oggettobulk){
        return oggettobulk.getBulkLists();
    }
    /**
     * Restituisce un array di OggettoBulk dipendenti da un altro OggettoBulk.
     */
    protected OggettoBulk[] getBulksForPersistency(UserContext usercontext, OggettoBulk oggettobulk){
        return oggettobulk.getBulksForPersistentcy();
    }

    public void commitComponent() throws ComponentException{
        try{
        	if (connection != null)
        		connection.commit();
        }catch(Exception exception){
            throw handleException(exception);
        }
    }

    public void rollbackComponent() throws ComponentException{
        try{
        	if (connection != null)
        		connection.rollback();
        }catch(Exception exception){
            throw handleException(exception);
        }
    }
    
    public Connection getConnection(UserContext usercontext) throws ComponentException{
        try{
        	if (connection == null)
        		connection = EJBCommonServices.getConnection(usercontext);
        	return connection;
        }catch(Exception exception){
            throw handleException(exception);
        }
    }

    public void closeConnection() throws ComponentException{
        try{
	    	if (connection != null){
	    		connection.close();
	    		connection = null;
	    	}
        }catch(Exception exception){
            throw handleException(exception);
        }
    }
    
    /**
     * Restituisce il nome di una FetchPolicy da usare per il caricamento degli oggetti persistenti. 
     * L'implementazione di default restituisce il nome della classe concatenata con l'operazione specificata.
     */
    protected final String getFetchPolicyName(String operation){
        StringBuffer stringbuffer = new StringBuffer(getClass().getName().length() + operation.length() + 1);
        stringbuffer.append(getClass().getName());
        stringbuffer.append('.');
        stringbuffer.append(operation);
        return stringbuffer.toString();
    }
    /**
     * Restituisce il nome di una FetchPolicy da usare per il caricamento degli oggetti persistenti. 
     * L'implementazione di default restituisce il nome della classe concatenata con l'operazione 
     * specificata e il nome dell'attributo.
     */
    protected final String getFetchPolicyName(String operation, String attribute){
        StringBuffer stringbuffer = new StringBuffer(getClass().getName().length() + operation.length() + attribute.length() + 2);
        stringbuffer.append(getClass().getName());
        stringbuffer.append('.');
        stringbuffer.append(operation);
        stringbuffer.append('.');
        stringbuffer.append(attribute);
        return stringbuffer.toString();
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(oggettobulk.getClass());
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, OggettoBulk oggettobulk, String columnMapName) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(oggettobulk.getClass(), columnMapName);
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, OggettoBulk oggettobulk, String columnMapName, String fetchPolicyName) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(oggettobulk.getClass(), columnMapName, fetchPolicyName);
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, Class class1) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(class1);
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, Class class1, String columnMapName) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(class1, columnMapName);
    }
    /**
     * Restituisce l'istanza del BulkHome di una classe di oggetti bulk.
     */
    protected final BulkHome getHome(UserContext usercontext, Class class1, String columnMapName, String fetchPolicyName) throws ComponentException{
        return (BulkHome)getHomeCache(usercontext).getHome(class1, columnMapName, fetchPolicyName);
    }
    /**
     * Restituisce l'istanza dell'HomeCache corrente.
     */
    protected final HomeCache getHomeCache(UserContext usercontext) throws ComponentException{
        if(homeCache == null)
            homeCache = new HomeCache(getConnection(usercontext));
        return homeCache;
    }

    protected final BulkHome getTempHome(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(oggettobulk.getClass());
    }

    protected final BulkHome getTempHome(UserContext usercontext, OggettoBulk oggettobulk, String s)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(oggettobulk.getClass(), s);
    }

    protected final BulkHome getTempHome(UserContext usercontext, OggettoBulk oggettobulk, String s, String s1)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(oggettobulk.getClass(), s, s1);
    }

    protected final BulkHome getTempHome(UserContext usercontext, Class class1)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(class1);
    }

    protected final BulkHome getTempHome(UserContext usercontext, Class class1, String s)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(class1, s);
    }

    protected final BulkHome getTempHome(UserContext usercontext, Class class1, String s, String s1)
        throws ComponentException
    {
        return (BulkHome)getTempHomeCache(usercontext).getHome(class1, s, s1);
    }

    protected final HomeCache getTempHomeCache(UserContext usercontext)
        throws ComponentException
    {
        if(tempHomeCache == null)
            tempHomeCache = new HomeCache(getConnection(usercontext));
        return tempHomeCache;
    }

    protected ComponentException handleException(Throwable throwable)
    {
        try
        {
            throw throwable;
        }
        catch(OutdatedResourceException outdatedresourceexception)
        {
            return new ApplicationException("Risorsa non pi\371 valida", outdatedresourceexception);
        }
        catch(BusyResourceException busyresourceexception)
        {
            return new ApplicationException("Risorsa occupata", busyresourceexception);
        }
        catch(BusyRecordException busyrecordexception)
        {
            return new ApplicationException("Risorsa occupata", busyrecordexception);
        }
        catch(LockedRecordException lockedrecordexception)
        {
            return new CRUDException("Risorsa occupata", lockedrecordexception);
        }
        catch(ValueTooLargeException valuetoolargeexception)
        {
            return new CRUDException("Un campo contiene testo troppo lungo.", valuetoolargeexception);
        }
        catch(PrimaryKeyChangedException primarykeychangedexception)
        {
            return new CRUDException("Non \350 possibile cambiare la chiave primaria.", primarykeychangedexception);
        }
        catch(ReferentialIntegrityException referentialintegrityexception)
        {
            return new CRUDReferentialIntegrityException("Vincolo di integrit\340 referenziale violato.", referentialintegrityexception);
        }
        catch(DuplicateKeyException duplicatekeyexception)
        {
            return new ApplicationException("Errore di chiave duplicata: si sta tentando di inserire un oggetto gi\340 presente.\n\n\n"+duplicatekeyexception.getMessage(), duplicatekeyexception);
        }
        catch(ApplicationPersistencyException applicationpersistencyexception)
        {
            return new ApplicationException(applicationpersistencyexception.getMessage(), applicationpersistencyexception);
        }
        catch(ApplicationPersistencyDiscardedException applicationpersistencydiscardedexception)
        {
            return new ApplicationException(applicationpersistencydiscardedexception.getMessage(), applicationpersistencydiscardedexception);
        }
        catch(ComponentException componentexception)
        {
            if(componentexception.getDetail() != null && componentexception.getClass() == it.cnr.jada.comp.ComponentException.class)
            {
                ComponentException componentexception1 = handleException(componentexception.getDetail());
                if(componentexception1.getClass() == it.cnr.jada.comp.ComponentException.class && (componentexception1.getDetail() == componentexception.getDetail() && componentexception1.getMessage() == null))
                    return componentexception;
                else
                    return componentexception1;
            } else
            {
                return componentexception;
            }
        }
        catch(SQLException sqlexception)
        {
            return handleException(((Throwable) (SQLExceptionHandler.getInstance().handleSQLException(sqlexception))));
        }
        catch(Throwable throwable1)
        {
            return uncaughtException(throwable1);
        }
    }

    protected ComponentException handleSQLException(SQLException sqlexception)
    {
        return handleException(sqlexception);
    }

    public final void initialize()
    {
        homeCache = tempHomeCache = null;
        EJBTracer.getInstance().incrementActiveComponentCounter();
    }

    protected void initializeKeysAndOptionsInto(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException
    {
        if(!(oggettobulk instanceof Persistent))
            return;
        getHome(usercontext, oggettobulk.getClass());
        for(Enumeration enumeration = BulkInfo.getBulkInfo(oggettobulk.getClass()).getFieldProperties(); enumeration.hasMoreElements();)
        {
            FieldProperty fieldproperty = (FieldProperty)enumeration.nextElement();
            try
            {
                if(fieldproperty.getKeysProperty() != null)
                {
                    if(it.cnr.jada.util.Introspector.getPropertyValue(oggettobulk, fieldproperty.getKeysProperty()) == null)
                    {
                        Dictionary dictionary = loadKeys(usercontext, oggettobulk, fieldproperty);
                        if(dictionary != null)
                            it.cnr.jada.util.Introspector.setPropertyValue(oggettobulk, fieldproperty.getKeysProperty(), dictionary);
                    }
                } else
                if(fieldproperty.getOptionsProperty() != null && it.cnr.jada.util.Introspector.getPropertyValue(oggettobulk, fieldproperty.getOptionsProperty()) == null)
                {
                    Object obj = findOptions(usercontext, oggettobulk, fieldproperty);
                    if(obj != null)
                        it.cnr.jada.util.Introspector.setPropertyValue(oggettobulk, fieldproperty.getOptionsProperty(), obj);
                }
            }
            catch(Exception exception)
            {
                throw handleException(exception);
            }
        }

    }

    protected void insertBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws PersistencyException, ComponentException
    {
        insertBulk(usercontext, oggettobulk, true);
    }

    protected void insertBulk(UserContext usercontext, OggettoBulk oggettobulk, boolean flag)
        throws PersistencyException, ComponentException
    {
        if(oggettobulk instanceof Persistent)
        {
            if(oggettobulk instanceof KeyedPersistent)
                getHome(usercontext, oggettobulk).initializePrimaryKeyForInsert(usercontext, oggettobulk);
            checkSQLConstraints(usercontext, oggettobulk, true, flag);
            if(oggettobulk.getUser() == null)
                oggettobulk.setUser(usercontext.getUser());
            getHome(usercontext, oggettobulk).insert((Persistent)oggettobulk, usercontext);
        }
    }

    protected final RemoteIterator iterator(UserContext usercontext, Query query, Class class1, String s) throws ComponentException{
        try{
            if(query == null)
                return new EmptyRemoteIterator();
            if(!usercontext.isTransactional()){
            	BulkLoaderIterator bulkLoaderIterator = (BulkLoaderIterator) EJBCommonServices.createEJB("JADAEJB_BulkLoaderIterator");
            	bulkLoaderIterator.ejbCreate(usercontext, query, class1, s);
            	return bulkLoaderIterator;
            }else{
            	TransactionalBulkLoaderIterator transationalBulkLoaderIterator = (TransactionalBulkLoaderIterator) EJBCommonServices.createEJB("JADAEJB_TransactionalBulkLoaderIterator");
            	transationalBulkLoaderIterator.ejbCreate(usercontext, query, class1, s);
            	return transationalBulkLoaderIterator;
            }
        }catch(EJBException ejbexception){
            throw handleException(ejbexception);
        }catch(CreateException createexception){
            throw handleException(createexception);
        }
    }

    protected Dictionary loadKeys(UserContext usercontext, OggettoBulk oggettobulk, FieldProperty fieldproperty)
        throws ComponentException
    {
        try
        {
            try
            {
                return (Dictionary)it.cnr.jada.util.Introspector.invoke(this, "load", fieldproperty.getKeysProperty(), usercontext, oggettobulk);
            }
            catch(NoSuchMethodException _ex)
            {
                return getHome(usercontext, oggettobulk).loadKeys(fieldproperty.getKeysProperty(), oggettobulk);
            }
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            throw handleException(invocationtargetexception.getTargetException());
        }
        catch(Exception exception)
        {
            throw handleException(exception);
        }
    }

    protected void lockBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws PersistencyException, ComponentException, OutdatedResourceException, BusyResourceException
    {
        getHome(usercontext, oggettobulk).lock(oggettobulk);
    }

    protected void makeBulkListPersistent(UserContext usercontext, BulkCollection bulkcollection)
        throws ComponentException, PersistencyException
    {
        makeBulkListPersistent(usercontext, bulkcollection, true);
    }

    protected void makeBulkListPersistent(UserContext usercontext, BulkCollection bulkcollection, boolean flag)
        throws ComponentException, PersistencyException
    {
        if(bulkcollection == null)
            return;
        for(Iterator iterator1 = bulkcollection.deleteIterator(); iterator1.hasNext(); makeBulkPersistent(usercontext, (OggettoBulk)iterator1.next(), flag));
        for(Iterator iterator2 = bulkcollection.iterator(); iterator2.hasNext(); makeBulkPersistent(usercontext, (OggettoBulk)iterator2.next(), flag));
    }

    protected void makeBulkPersistent(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException
    {
        makeBulkPersistent(usercontext, oggettobulk, true);
    }

    protected void makeBulkPersistent(UserContext usercontext, OggettoBulk oggettobulk, boolean flag)
        throws ComponentException, PersistencyException
    {
    	if (oggettobulk instanceof Persistent){
	        switch(oggettobulk.getCrudStatus())
	        {
	        case 1: // '\001'
	            insertBulk(usercontext, oggettobulk, flag);
	            break;
	
	        case 2: // '\002'
	            updateBulk(usercontext, oggettobulk, flag);
	            break;
	        }
    	}
        OggettoBulk aoggettobulk[] = getBulksForPersistency(usercontext, oggettobulk);
        if(aoggettobulk != null)
        {
            for(int i = 0; i < aoggettobulk.length; i++)
                if(aoggettobulk[i] != null)
                    makeBulkPersistent(usercontext, aoggettobulk[i], flag);

        }
        BulkCollection abulkcollection[] = getBulkListsForPersistency(usercontext, oggettobulk);
        if(abulkcollection != null)
        {
            for(int j = 0; j < abulkcollection.length; j++)
                makeBulkListPersistent(usercontext, abulkcollection[j], flag);

        }
        if(oggettobulk.getCrudStatus() == 3)
            deleteBulk(usercontext, oggettobulk);
    }

    public final void release(UserContext usercontext){
	    release();
    }

    public final void release(){
	    try{
	    	closeConnection();
			if(homeCache != null && homeCache.getConnection() != null)
				homeCache.getConnection().close();
			if(tempHomeCache != null && tempHomeCache.getConnection() != null)
				tempHomeCache.getConnection().close();	    	
	    } catch (ComponentException e) {
	    	throw new RuntimeException(e);
	    } catch (SQLException e) {
	    	throw new RuntimeException(e);
		}
        homeCache = tempHomeCache = null;
        EJBTracer.getInstance().decrementActiveComponentCounter();
    }
    
    protected final void rollbackToSavepoint(UserContext usercontext, String s)
        throws ComponentException, SQLException
    {
        Statement statement = getConnection(usercontext).createStatement();
        try
        {
            statement.execute("ROLLBACK TO SAVEPOINT " + s);
        }
        finally
        {
            statement.close();
        }
    }

    protected final void setSavepoint(UserContext usercontext, String s)
        throws ComponentException, SQLException
    {
        Statement statement = getConnection(usercontext).createStatement();
        try
        {
            statement.execute("SAVEPOINT " + s);
        }
        finally
        {
            statement.close();
        }
    }

    protected final ComponentException uncaughtException(Throwable throwable)
    {
        StringWriter stringwriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringwriter));
        return new ComponentException(stringwriter.getBuffer().toString(), throwable);
    }

    protected void updateBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws PersistencyException, ComponentException
    {
        updateBulk(usercontext, oggettobulk, true);
    }

    protected void updateBulk(UserContext usercontext, OggettoBulk oggettobulk, boolean flag)
        throws PersistencyException, ComponentException
    {
        if(oggettobulk instanceof Persistent)
            try
            {
                if(flag)
                    checkSQLConstraints(usercontext, oggettobulk, false, true);
                lockBulk(usercontext, oggettobulk);
                if(oggettobulk.getUser() == null)
                    oggettobulk.setUser(usercontext.getUser());
                getHome(usercontext, oggettobulk).update((Persistent)oggettobulk, usercontext);
            }
            catch(OutdatedResourceException outdatedresourceexception)
            {
                throw handleException(outdatedresourceexception);
            }
            catch(BusyResourceException busyresourceexception)
            {
                throw handleException(busyresourceexception);
            }
    }

    protected void validateBulkForCheck(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException
    {
        lockBulk(usercontext, oggettobulk);
    }

    protected void validateBulkForDelete(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException, ObjectNotFoundException
    {
        lockBulk(usercontext, oggettobulk);
    }

    protected void validateBulkForInsert(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException
    {
        checkSQLConstraints(usercontext, oggettobulk, false, true);
    }

    protected void validateBulkForPersistency(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException, PrimaryKeyChangedException
    {
    	if (oggettobulk instanceof Persistent){
	        switch(oggettobulk.getCrudStatus())
	        {
	        case 1: // '\001'
	            validateBulkForInsert(usercontext, oggettobulk);
	            break;
	
	        case 2: // '\002'
	            validateBulkForUpdate(usercontext, oggettobulk);
	            break;
	
	        case 3: // '\003'
	            validateBulkForDelete(usercontext, oggettobulk);
	            break;
	
	        case 4: // '\004'
	            validateBulkForCheck(usercontext, oggettobulk);
	            break;
	        }
    	}
        OggettoBulk aoggettobulk[] = getBulksForPersistency(usercontext, oggettobulk);
        if(aoggettobulk != null)
        {
            for(int i = 0; i < aoggettobulk.length; i++)
                if(aoggettobulk[i] != null)
                    validateBulkForPersistency(usercontext, aoggettobulk[i]);

        }
        BulkCollection abulkcollection[] = getBulkListsForPersistency(usercontext, oggettobulk);
        if(abulkcollection != null)
        {
            for(int j = 0; j < abulkcollection.length; j++)
                validateBulkListForPersistency(usercontext, abulkcollection[j]);

        }
    }

    protected void validateBulkForUpdate(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException, ObjectNotFoundException, PrimaryKeyChangedException
    {
        if(oggettobulk instanceof Keyed)
        {
            KeyedPersistent keyedpersistent = ((Keyed)oggettobulk).getKey();
            if(keyedpersistent != null && !oggettobulk.equalsByPrimaryKey(keyedpersistent))
                throw new PrimaryKeyChangedException(oggettobulk);
        }
        checkSQLConstraints(usercontext, oggettobulk, false, true);
    }

    protected void validateBulkListForPersistency(UserContext usercontext, BulkCollection bulkcollection)
        throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException, PrimaryKeyChangedException
    {
        if(bulkcollection == null)
            return;
        for(Iterator iterator1 = bulkcollection.deleteIterator(); iterator1.hasNext(); validateBulkForPersistency(usercontext, (OggettoBulk)iterator1.next()));
        for(Iterator iterator2 = bulkcollection.iterator(); iterator2.hasNext(); validateBulkForPersistency(usercontext, (OggettoBulk)iterator2.next()));
    }
}