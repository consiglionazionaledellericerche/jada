package it.cnr.jada.persistency.sql;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.BeanIntrospector;

import java.io.Serializable;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLPersister, ObjectNotFoundHandler, SQLBuilder, HomeCache, 
//            ColumnMap, SQLExceptionHandler, SQLPersistentInfo, CompoundFindClause

public class PersistentHome extends SQLPersister
    implements Serializable, ObjectNotFoundHandler
{

    protected PersistentHome(Class class1, Connection connection)
    {
        super(BeanIntrospector.getSQLInstance(), class1, connection);
        persistentClass = class1;
    }

    protected PersistentHome(Class class1, Connection connection, PersistentCache persistentcache)
    {
        super(BeanIntrospector.getSQLInstance(), class1, connection, persistentcache);
        persistentClass = class1;
    }

    public SQLBuilder buildSQLByClause(CompoundFindClause compoundfindclause)
        throws PersistencyException
    {
        try
        {
            return new SQLBuilder(getPersistentClass(), compoundfindclause);
        }
        catch(IntrospectionException introspectionexception)
        {
            throw new PersistencyException(introspectionexception);
        }
    }

    public List fetchAll(Broker broker)
        throws PersistencyException
    {
        LinkedList linkedlist = new LinkedList();
        for(; broker.next(); linkedlist.add(broker.fetch(persistentClass)));
        return linkedlist;
    }

    public List fetchAll(SQLBuilder sqlbuilder)
        throws PersistencyException
    {
        return fetchAll(((Broker) (createBroker(sqlbuilder))));
    }

    public List find(Persistent persistent)
        throws PersistencyException
    {
        return fetchAll(select(persistent));
    }

    public List find(Persistent persistent, String as[], boolean flag)
        throws PersistencyException, IntrospectionException
    {
        return fetchAll(select(persistent, as, flag));
    }

    public List find(Persistent persistent, boolean flag)
        throws PersistencyException
    {
        return fetchAll(select(persistent, flag));
    }

    public List findAll()
        throws PersistencyException
    {
        return fetchAll(createSQLBuilder());
    }

    public List findByClause(CompoundFindClause compoundfindclause)
        throws PersistencyException
    {
        return fetchAll(selectByClause(compoundfindclause));
    }
	/**
	 * Metodo che permette la valorizzazione di attributi del persistent
	 * riga per riga
	 * 
	 * @param userContext
	 * @param persistent
	 * @return Persistent (OggettoBulk modificato)
	 * @throws PersistencyException
	 */
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent)
		throws PersistencyException
	{
		return persistent;
	}
    public Persistent findByPrimaryKey(UserContext userContext,Persistent persistent)throws PersistencyException{
    	return findByPrimaryKey(persistent);
    }
    public Persistent findByPrimaryKey(Persistent persistent)
        throws PersistencyException
    {
        if(!persistentClass.isAssignableFrom(persistent.getClass()))
            return findByPrimaryKey(persistent, persistentClass, false);
        else
            return super.findByPrimaryKey(persistent);
    }
    public Persistent findByPrimaryKey(UserContext userContext,Object obj)throws PersistencyException{
    	return findByPrimaryKey(obj);
    }

    public Persistent findByPrimaryKey(Object obj)
        throws PersistencyException
    {
        return findByPrimaryKey(obj, persistentClass, false);
    }

    public Persistent findByPrimaryKey(Object obj, boolean flag)
        throws PersistencyException
    {
        return findByPrimaryKey(obj, persistentClass, flag);
    }

    public HomeCache getHomeCache()
    {
        return homeCache;
    }

    public final Class getPersistentClass()
    {
        return persistentClass;
    }

    public Blob getSQLBlob(KeyedPersistent keyedpersistent, String s)
        throws PersistencyException
    {
        try
        {
        	 ColumnMap columnmap = getHomeCache().getHome(keyedpersistent).getColumnMap();
             String s1 = columnmap.getSelectForBlobSQL(s);
             LoggableStatement statement = new LoggableStatement(getConnection(),s1,true,this.getClass());
             try
             {
                 statement.clearParameters();
                 setParametersUsing(statement, keyedpersistent, columnmap.getPrimaryColumnNames(), 1);
                 ResultSet resultset = statement.executeQuery(s1);
                //ResultSet resultset = LoggableStatement.executeQuery(preparedstatement);
                try
                {
                    if(!resultset.next())
                        return null;
                    Blob blob1 = resultset.getBlob(1);
                    if(resultset.next())
                        throw new FindException("SELECT statement return more than one row");
                    Blob blob = blob1;
                    return blob;
                }
                finally
                {
					try{resultset.close();}catch( java.sql.SQLException e ){};
                }
            }
            finally
            {
				try{statement.close();}catch( java.sql.SQLException e ){};
            }
        }
        catch(SQLException sqlexception)
        {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    public void setSQLClob(KeyedPersistent keyedpersistent, String columnName, String value)
            throws PersistencyException
    {
        try
        {
            ColumnMap columnmap = getHomeCache().getHome(keyedpersistent).getColumnMap();
            String updateForLobSQL = columnmap.getUpdateForLobSQL(columnName);
            LoggableStatement statement = new LoggableStatement(getConnection(),updateForLobSQL,true,this.getClass());
            try
            {
                statement.clearParameters();
                statement.setObject(1, value);
                setParametersUsing(statement, keyedpersistent, columnmap.getPrimaryColumnNames(), 2);
                int i = statement.executeUpdate();
                if(i == 0)
                    throw new ObjectNotFoundException("UPDATE statment affected 0 rows.");
                if(i > 1)
                    throw new DeleteException("UPDATE statement affected more than 1 rows.");
            }
            finally
            {
                try{statement.close();}catch( java.sql.SQLException e ){};
            }
        }
        catch(SQLException sqlexception)
        {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    public Clob getSQLClob(KeyedPersistent keyedpersistent, String s)
        throws PersistencyException
    {
        try
        {
            ColumnMap columnmap = getHomeCache().getHome(keyedpersistent).getColumnMap();
            String s1 = columnmap.getSelectForBlobSQL(s);
            LoggableStatement statement = new LoggableStatement(getConnection(),s1,true,this.getClass());
            try
            {
                statement.clearParameters();
                setParametersUsing(statement, keyedpersistent, columnmap.getPrimaryColumnNames(), 1);
                ResultSet resultset = statement.executeQuery(s1);
                //ResultSet resultset = LoggableStatement.executeQuery(preparedstatement);
                try
                {
                    if(!resultset.next())
                        return null;
                    Clob clob1 = resultset.getClob(1);
                    if(resultset.next())
                        throw new FindException("SELECT statement return more than one row");
                    Clob clob = clob1;
                    return clob;
                }
                finally
                {
					try{resultset.close();}catch( java.sql.SQLException e ){};
                }
            }
            finally
            {
				try{statement.close();}catch( java.sql.SQLException e ){};
            }
        }
        catch(SQLException sqlexception)
        {
            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
        }
    }

    public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception)
        throws ObjectNotFoundException
    {
        throw objectnotfoundexception;
    }

    public SQLBuilder select(Persistent persistent)
        throws PersistencyException
    {
        return select(persistent, false);
    }

    public SQLBuilder select(Persistent persistent, String as[], boolean flag)
        throws PersistencyException, IntrospectionException
    {
        SQLBuilder sqlbuilder = createSQLBuilder();
        sqlbuilder.addClausesUsing(persistent, as, flag);
        return sqlbuilder;
    }

    public SQLBuilder select(Persistent persistent, boolean flag)
        throws PersistencyException
    {
        SQLBuilder sqlbuilder = createSQLBuilder();
        sqlbuilder.addClausesUsing(persistent, flag);
        return sqlbuilder;
    }

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{		
		return selectByClause(compoundfindclause);
	}

    public SQLBuilder selectByClause(CompoundFindClause compoundfindclause)
        throws PersistencyException
    {
        SQLBuilder sqlbuilder = createSQLBuilder();
        sqlbuilder.addClause(compoundfindclause);
        return sqlbuilder;
    }

    public void setColumnMap(String s)
    {
        try
        {
            setColumnMap(((SQLPersistentInfo)getIntrospector().getPersistentInfo(persistentClass)).getColumnMap(s));
        }
        catch(IntrospectionException introspectionexception)
        {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public void setFetchPolicy(String s)
    {
        try
        {
            setFetchPolicy(((SQLPersistentInfo)getIntrospector().getPersistentInfo(persistentClass)).getFetchPolicy(s));
        }
        catch(IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    void setHomeCache(HomeCache homecache)
    {
        homeCache = homecache;
    }

    private final Class persistentClass;
    private HomeCache homeCache;
}