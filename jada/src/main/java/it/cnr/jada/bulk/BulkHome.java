package it.cnr.jada.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.FindException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.ColumnMap;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.persistency.sql.SQLPersistentInfo;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Dictionary;

// Referenced classes of package it.cnr.jada.bulk:
//            BusyResourceException, OggettoBulk, OutdatedResourceException

public class BulkHome extends PersistentHome implements Serializable {

	protected BulkHome(Class class1, Connection connection) {
		super(class1, connection);
	}

	protected BulkHome(Class class1, Connection connection,
			PersistentCache persistentcache) {
		super(class1, connection, persistentcache);
	}

	private ResultSet executeLockedQuery(LoggableStatement preparedStatement)
			throws SQLException, BusyResourceException, PersistencyException {
		try {
			for (int i = 0; i < 3; i++) {
				try {
					return preparedStatement.executeQuery();
				} catch (SQLException sqlexception1) {
					if (sqlexception1.getErrorCode() != 54)
						throw sqlexception1;
				}
				Thread.sleep(2000L);
			}

			throw new BusyResourceException();
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		} catch (InterruptedException interruptedexception) {
			throw new PersistencyException(
					"Sleep interrotto durante ciclo di attesa per ottenere lock su record",
					interruptedexception);
		}
	}

	public OggettoBulk fetch(Broker broker) throws PersistencyException {
		return (OggettoBulk) broker.fetch(getPersistentClass());
	}

	public Persistent fetchAndLock(Query query) throws PersistencyException,
			BusyResourceException, OutdatedResourceException {
		try {
			LoggableStatement preparedstatement = query
					.prepareStatement(getConnection());
			try {
				SQLBroker sqlbroker = createBroker(preparedstatement,
						executeLockedQuery(preparedstatement));
				if (!sqlbroker.next())
					return null;
				Persistent persistent1 = sqlbroker.fetch(getPersistentClass());
				if (sqlbroker.next()) {
					sqlbroker.close();
					throw new FindException(
							"SELECT statement return more than one row");
				}
				Persistent persistent = persistent1;
				return persistent;
			} finally {
				try {
					preparedstatement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		}
	}

	public Object fetchAndLockMax(SQLBuilder sqlbuilder, Object obj, String s,
			Object obj1) throws PersistencyException, BusyResourceException {
		try {
			ColumnMapping columnmapping = sqlbuilder.getColumnMap()
					.getMappingForProperty(s);
			if (columnmapping == null)
				throw new PersistencyException("Property not mapped");
			String as[] = sqlbuilder.getColumnMap().getPrimaryColumnNames();
			ColumnMap columnmap = ((SQLPersistentInfo) getIntrospector()
					.getPersistentInfo(obj.getClass())).getDefaultColumnMap();
			SQLBuilder sqlbuilder1 = createSQLBuilder();
			sqlbuilder1.setForUpdate(true);
			sqlbuilder1.setHeader("SELECT " + columnmapping.getColumnName());
			sqlbuilder1.addSQLClause("AND", columnmapping.getColumnName(),
					8192, sqlbuilder);
			for (int i = 0; i < as.length; i++) {
				ColumnMapping columnmapping1 = columnmap
						.getMappingForColumn(as[i]);
				if (columnmapping1 != null
						&& !columnmapping1.getPropertyName().equals(s))
					sqlbuilder1.addClause("AND", columnmapping1
							.getPropertyName(), 8192, getIntrospector()
							.getPropertyValue(obj,
									columnmapping1.getPropertyName()));
			}

			sqlbuilder = sqlbuilder1;
			LoggableStatement loggablestatement = sqlbuilder.prepareStatement(getConnection());
			ResultSet resultset = executeLockedQuery(loggablestatement);
			SQLBroker sqlbroker = createBroker(loggablestatement, resultset);
			if (sqlbroker.next()) {
				Object obj2 = sqlbroker.fetchPropertyValue(s, getIntrospector()
						.getPropertyType(getPersistentClass(), s));
				sqlbroker.close();
				if (obj2 != null)
					return obj2;
			}
			return obj1;
		} catch (IntrospectionException introspectionexception) {
			throw new PersistencyException(introspectionexception);
		} catch (SQLException sqlexception) {
			throw new PersistencyException(sqlexception);
		}
	}

	public Object fetchMax(SQLBuilder sqlbuilder, String s, Object obj)
			throws PersistencyException {
		try {
			LoggableStatement loggablestatement = sqlbuilder
					.prepareStatement(getConnection());
			ResultSet resultset = loggablestatement.executeQuery();
			SQLBroker sqlbroker = createBroker(loggablestatement, resultset);
			if (sqlbroker.next()) {
				Object obj1 = sqlbroker.fetchPropertyValue(s, getIntrospector()
						.getPropertyType(getPersistentClass(), s));
				sqlbroker.close();
				if (obj1 != null)
					return obj1;
			}
			return obj;
		} catch (IntrospectionException introspectionexception) {
			throw new PersistencyException(introspectionexception);
		} catch (SQLException sqlexception) {
			throw new PersistencyException(sqlexception);
		}
	}

	public BigInteger fetchNextSequenceValue(UserContext usercontext, String s)
			throws PersistencyException {
		try {
			Statement statement = getConnection().createStatement();
			try {
				ResultSet resultset = statement.executeQuery("SELECT "
						+ EJBCommonServices.getDefaultSchema() + s
						+ ".NEXTVAL FROM DUAL");
				try {
					resultset.next();
					BigInteger biginteger = resultset.getBigDecimal(1)
							.toBigInteger();
					return biginteger;
				} finally {
					try {
						resultset.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} finally {
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		}
	}

	public Persistent findAndLock(OggettoBulk oggettobulk)
			throws PersistencyException, OutdatedResourceException,
			BusyResourceException {
		try {
			LoggableStatement preparestatement = getLoggableSelectForUpdateStatement();
			preparestatement.clearParameters();
			setParametersUsing(preparestatement, oggettobulk, getColumnMap()
					.getPrimaryColumnNames(), 1);
			try {
				ResultSet resultset = executeLockedQuery(preparestatement);
				SQLBroker sqlbroker = createBroker(preparestatement, resultset);
				if (!sqlbroker.next())
					throw new ObjectNotFoundException(
							"Impossibile trovare il record da lockare");
				Persistent persistent1 = sqlbroker.fetch(getPersistentClass());
				if (sqlbroker.next()) {
					sqlbroker.close();
					throw new FindException(
							"SELECT statement return more than one row");
				}
				Persistent persistent = persistent1;
				return persistent;
			} finally {
				try {
					preparestatement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (BusyResourceException busyresourceexception) {
			busyresourceexception.setBulk(oggettobulk);
			throw busyresourceexception;
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception, (Persistent) oggettobulk);
		}
	}

	public Object findAndLockMax(Object obj, String s)
			throws PersistencyException, BusyResourceException {
		return findMax(obj, s, null, true);
	}

	public Object findAndLockMax(Object obj, String s, Object obj1)
			throws PersistencyException, BusyResourceException {
		return findMax(obj, s, obj1, true);
	}

	public Object findMax(Object obj, String s) throws PersistencyException {
		try {
			return findMax(obj, s, null, false, null);
		} catch (BusyResourceException busyresourceexception) {
			throw new PersistencyException(busyresourceexception);
		}
	}

	public Object findMax(Object obj, String s, Object obj1)
			throws PersistencyException {
		try {
			return findMax(obj, s, obj1, false, null);
		} catch (BusyResourceException busyresourceexception) {
			throw new PersistencyException(busyresourceexception);
		}
	}

	public Object findMax(Object obj, String s, Object obj1, boolean flag)
			throws PersistencyException, BusyResourceException {
			return findMax(obj, s, obj1, flag, null);
	}

	public Object findMax(Object obj, String s, Object obj1, boolean flag,
			FindClause findClause) throws PersistencyException,
			BusyResourceException {
		try {
			SQLBuilder sqlbuilder = createSQLBuilder();
			ColumnMapping columnmapping = sqlbuilder.getColumnMap()
					.getMappingForProperty(s);
			if (columnmapping == null)
				throw new PersistencyException("Property not mapped");
			sqlbuilder.setHeader("SELECT MAX(" + columnmapping.getColumnName()
					+ ") AS " + columnmapping.getColumnName());
			String as[] = sqlbuilder.getColumnMap().getPrimaryColumnNames();
			ColumnMap columnmap = ((SQLPersistentInfo) getIntrospector()
					.getPersistentInfo(obj.getClass())).getDefaultColumnMap();
			for (int i = 0; i < as.length; i++) {
				ColumnMapping columnmapping1 = columnmap
						.getMappingForColumn(as[i]);
				if (columnmapping1 != null
						&& !columnmapping1.getPropertyName().equals(s))
					sqlbuilder.addClause("AND", columnmapping1
							.getPropertyName(), 8192, getIntrospector()
							.getPropertyValue(obj,
									columnmapping1.getPropertyName()));
			}

			if (flag) {
				SQLBuilder sqlbuilder1 = createSQLBuilder();
				sqlbuilder1.setForUpdate(true);
				sqlbuilder1
						.setHeader("SELECT " + columnmapping.getColumnName());
				sqlbuilder1.addSQLClause("AND", columnmapping.getColumnName(),
						8192, sqlbuilder);
				for (int j = 0; j < as.length; j++) {
					ColumnMapping columnmapping2 = columnmap
							.getMappingForColumn(as[j]);
					if (columnmapping2 != null
							&& !columnmapping2.getPropertyName().equals(s))
						sqlbuilder1.addClause("AND", columnmapping2
								.getPropertyName(), 8192, getIntrospector()
								.getPropertyValue(obj,
										columnmapping2.getPropertyName()));
				}
				sqlbuilder = sqlbuilder1;
			}
			if (findClause != null)
				sqlbuilder.addClause(findClause);
			LoggableStatement loggablestatement = sqlbuilder
					.prepareStatement(getConnection());
			ResultSet resultset = executeLockedQuery(loggablestatement);
			SQLBroker sqlbroker = createBroker(loggablestatement, resultset);
			if (sqlbroker.next()) {
				Object obj2 = sqlbroker.fetchPropertyValue(s, getIntrospector()
						.getPropertyType(getPersistentClass(), s));
				sqlbroker.close();
				if (obj2 != null)
					return obj2;
			}
			return obj1;
		} catch (IntrospectionException introspectionexception) {
			throw new PersistencyException(introspectionexception);
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		}
	}

	public Object findOptions(String s, OggettoBulk oggettobulk,
			BulkHome bulkhome, OggettoBulk oggettobulk1)
			throws InvocationTargetException, IllegalAccessException,
			PersistencyException {
		try {
			return it.cnr.jada.util.Introspector.invoke(this, "find", s,
					oggettobulk, bulkhome, oggettobulk1);
		} catch (NoSuchMethodException _ex) {
			SQLBuilder sqlbuilder = selectOptions(s, oggettobulk, bulkhome,
					oggettobulk1);
			if (sqlbuilder == null)
				return null;
			else
				return bulkhome.fetchAll(sqlbuilder);
		}
	}

	public Object findOptionsByClause(String s, OggettoBulk oggettobulk,
			BulkHome bulkhome, OggettoBulk oggettobulk1,
			CompoundFindClause compoundfindclause)
			throws InvocationTargetException, IllegalAccessException,
			PersistencyException {
		try {
			return it.cnr.jada.util.Introspector.invoke(this, "find", s
					+ "ByClause", oggettobulk, bulkhome, oggettobulk1,
					compoundfindclause);
		} catch (NoSuchMethodException _ex) {
			SQLBuilder sqlbuilder = selectOptionsByClause(s, oggettobulk,
					bulkhome, oggettobulk1, compoundfindclause);
			if (sqlbuilder == null)
				return null;
			else
				return bulkhome.fetchAll(sqlbuilder);
		}
	}

	public Timestamp getServerDate() throws PersistencyException {
		try {
			Statement statement = getConnection().createStatement();
			try {
				ResultSet resultset = statement
						.executeQuery("SELECT TRUNC(SYSDATE) FROM DUAL");
				try {
					if (!resultset.next())
						throw new PersistencyException(
								"Tentativo di recuperare timestamp ORACLE fallito: nessun record restituito");
					Timestamp timestamp = resultset.getTimestamp(1);
					return timestamp;
				} finally {
					try {
						resultset.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} finally {
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		}
	}

	public Timestamp getServerTimestamp() throws PersistencyException {
		try {
			Statement statement = getConnection().createStatement();
			try {
				ResultSet resultset = statement
						.executeQuery("SELECT SYSDATE FROM DUAL");
				try {
					if (!resultset.next())
						throw new PersistencyException(
								"Tentativo di recuperare timestamp ORACLE fallito: nessun record restituito");
					Timestamp timestamp = resultset.getTimestamp(1);
					return timestamp;
				} finally {
					try {
						resultset.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} finally {
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception);
		}
	}

	public void initializePrimaryKeyForInsert(UserContext usercontext,
			OggettoBulk oggettobulk) throws PersistencyException,
			ComponentException {
	}
	
	public void initializeBulkForInsert(UserContext usercontext,
			OggettoBulk oggettobulk) throws PersistencyException,
			ComponentException{		
	}

	public void insert(Persistent persistent, UserContext userContext) throws PersistencyException {
		OggettoBulk oggettobulk = (OggettoBulk) persistent;
		oggettobulk.setPg_ver_rec(new Long(1L));
		oggettobulk.setUtuv(oggettobulk.getUser());
		oggettobulk.setUtcr(oggettobulk.getUser());
		Timestamp timestamp = getServerTimestamp();
		oggettobulk.setDacr(timestamp);
		oggettobulk.setDuva(timestamp);
		super.insert(persistent, userContext);
	}

	public Dictionary loadKeys(String s, OggettoBulk oggettobulk)
			throws InvocationTargetException, IllegalAccessException {
		try {
			return (Dictionary) it.cnr.jada.util.Introspector.invoke(this,
					"load", s, oggettobulk);
		} catch (NoSuchMethodException _ex) {
			return null;
		} catch (ClassCastException _ex) {
			throw new IntrospectionError("Il metodo "
					+ it.cnr.jada.util.Introspector.buildMetodName("load", s)
					+ " non restituisce un Dictionary");
		}
	}

	public void lock(OggettoBulk oggettobulk) throws PersistencyException,
			OutdatedResourceException, BusyResourceException {
		try {
			LoggableStatement loggablestatement = getLoggableSelectForUpdateStatement();
			loggablestatement.clearParameters();
			setParametersUsing(loggablestatement, oggettobulk, getColumnMap()
					.getPrimaryColumnNames(), 1);
			try {
				ResultSet resultset = executeLockedQuery(loggablestatement);
				if (!resultset.next())
					throw new OutdatedResourceException(oggettobulk);
				if (oggettobulk.getPg_ver_rec().longValue() != resultset
						.getLong("PG_VER_REC"))
					throw new OutdatedResourceException(oggettobulk);
				if (resultset.next())
					throw new FindException(
							"SELECT statement return more than one row");
				try {
					resultset.close();
				} catch (java.sql.SQLException e) {
				}
				;
				return;
			} finally {
				try {
					loggablestatement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (BusyResourceException busyresourceexception) {
			busyresourceexception.setBulk(oggettobulk);
			throw busyresourceexception;
		} catch (SQLException sqlexception) {
			throw SQLExceptionHandler.getInstance().handleSQLException(
					sqlexception, (Persistent) oggettobulk);
		}
	}

	public SQLBuilder selectMax(Object obj, String s)
			throws PersistencyException {
		try {
			SQLBuilder sqlbuilder = createSQLBuilder();
			ColumnMapping columnmapping = sqlbuilder.getColumnMap()
					.getMappingForProperty(s);
			if (columnmapping == null)
				throw new PersistencyException("Property not mapped");
			sqlbuilder.setHeader("SELECT MAX(" + columnmapping.getColumnName()
					+ ") AS " + columnmapping.getColumnName());
			String as[] = sqlbuilder.getColumnMap().getPrimaryColumnNames();
			ColumnMap columnmap = ((SQLPersistentInfo) getIntrospector()
					.getPersistentInfo(obj.getClass())).getDefaultColumnMap();
			for (int i = 0; i < as.length; i++) {
				ColumnMapping columnmapping1 = columnmap
						.getMappingForColumn(as[i]);
				if (columnmapping1 != null
						&& !columnmapping1.getPropertyName().equals(s))
					sqlbuilder.addClause("AND", columnmapping1
							.getPropertyName(), 8192, getIntrospector()
							.getPropertyValue(obj,
									columnmapping1.getPropertyName()));
			}

			return sqlbuilder;
		} catch (IntrospectionException introspectionexception) {
			throw new PersistencyException(introspectionexception);
		}
	}

	public SQLBuilder selectOptions(String s, OggettoBulk oggettobulk,
			BulkHome bulkhome, OggettoBulk oggettobulk1)
			throws InvocationTargetException, IllegalAccessException,
			PersistencyException {
		try {
			return (SQLBuilder) it.cnr.jada.util.Introspector.invoke(this,
					"select", s, oggettobulk, bulkhome, oggettobulk1);
		} catch (NoSuchMethodException _ex) {
		}
		if (bulkhome == null)
			return null;
		else
			return bulkhome.select((Persistent) oggettobulk1, false);
	}

	public SQLBuilder selectOptionsByClause(String s, OggettoBulk oggettobulk,
			BulkHome bulkhome, OggettoBulk oggettobulk1,
			CompoundFindClause compoundfindclause)
			throws InvocationTargetException, IllegalAccessException,
			PersistencyException {
		try {
			return (SQLBuilder) it.cnr.jada.util.Introspector.invoke(this,
					"select", s + "ByClause", oggettobulk, bulkhome,
					oggettobulk1, compoundfindclause);
		} catch (NoSuchMethodException _ex) {
		}
		if (bulkhome == null)
			return null;
		else
			return bulkhome.selectByClause(compoundfindclause);
	}

	public void update(Persistent persistent, UserContext userContext)
        throws PersistencyException
    {
        OggettoBulk oggettobulk = (OggettoBulk)persistent;
        oggettobulk.setPg_ver_rec(new Long(oggettobulk.getPg_ver_rec().longValue() + 1L));
        if(oggettobulk.getUser() != null)
            oggettobulk.setUtuv(oggettobulk.getUser());
        oggettobulk.setDuva(getServerTimestamp());
        super.update(persistent, userContext);
    }

	public static final int BUSY_RECORD_SLEEP = 2000;
	public static final int BUSY_RECORD_ATTEMPTS = 3;
}