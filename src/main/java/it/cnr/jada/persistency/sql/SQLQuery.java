package it.cnr.jada.persistency.sql;

import it.cnr.jada.util.Config;
import it.cnr.jada.util.PropertyNames;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class SQLQuery implements Query, Serializable{
	private static String formDate = "yyyyMMdd";
	private static String formTime = "yyyyMMdd HHmmss";
	private static String formTimeOracle = "yyyyMMdd HH24miss";
	
	private static SimpleDateFormat formatterDate = new SimpleDateFormat(formDate,Config.getHandler().getLocale());
	private static SimpleDateFormat formatterTime = new SimpleDateFormat(formTime,Config.getHandler().getLocale());

    protected final Vector parameters;
    protected ColumnMap columnMap;
    protected String statement;
    protected Vector updateParameters;

    public SQLQuery(){
        parameters = new Vector();
        updateParameters = null;
    }

    public SQLQuery(ColumnMap columnmap){
        parameters = new Vector();
        updateParameters = null;
        columnMap = columnmap;
    }
    /**
     * Aggiunge il valore un parametro al ricevente. 
     * Questo metodo va usato per clausole parametriche che non sono state create tramite uno dei metodi 
     * addSQLClause o addClause. 
     * L'ordine in cui si specificano i parametri deve essere conforme a quello in cui sono 
     * definiti nello statement.
     */
    public void addParameter(int startIndex, Object value, int fieldType, int scale){
        if(parameters.size() < startIndex)
            parameters.setSize(startIndex);
        parameters.addElement(new SQLParameter(value, fieldType, scale, null));
    }

    /**
     * Aggiunge il valore un parametro al ricevente. 
     * Questo metodo va usato per clausole parametriche che non sono state create tramite uno dei metodi 
     * addSQLClause o addClause. 
     * L'ordine in cui si specificano i parametri deve essere conforme a quello in cui sono 
     * definiti nello statement.
     */
    public void addParameter(Object value, int fieldType, int scale){
        parameters.addElement(new SQLParameter(value, fieldType, scale, null));
    }

    /**
     * Imposta il valore dei parametri delle clausole in un statement creato utilizzando buildParameterSQL.
     */
    public int bindParameters(LoggableStatement preparedstatement) throws SQLException{
        return bindParameters(preparedstatement, 1);
    }

    /**
     * Imposta il valore dei parametri delle clausole in un statement creato utilizzando buildParameterSQL.
     */
    public int bindParameters(LoggableStatement preparedstatement, int index) throws SQLException{
        for(Enumeration enumeration = parameters.elements(); enumeration.hasMoreElements();){
            SQLParameter sqlparameter = (SQLParameter)enumeration.nextElement();
            if(sqlparameter != null)
                sqlparameter.setInPreparedStatement(index, preparedstatement);
            index++;
        }
        return index;
    }

    public int executeCountQuery(Connection connection) throws SQLException{
    	LoggableStatement loggablestatement = loggableCountStatement(connection);
        try{
            ResultSet resultset = loggablestatement.executeQuery();
            if(!resultset.next())
                throw new SQLException("L'esecuzione di una SELECT COUNT non ha ritornato nessun record.");
            int j = resultset.getInt(1);
			try{resultset.close();}catch( java.sql.SQLException e ){};
            int i = j;
            return i;
        }finally{
			try{
				loggablestatement.close();
			}catch( java.sql.SQLException e ){
			};
        }
    }
    /**
     * Esegue una query per verificare l'esistenza di almeno un record nel risultato della query 
     * definita dal ricevente. N.B.: Non funziona per query contenenti clausole ORDER BY o FOR UPDATE 
     * (e probabilmente altre)
     */
    public boolean executeExistsQuery(Connection connection) throws SQLException{
    	LoggableStatement loggablestatement = loggableExistsStatement(connection);
        try{
            ResultSet resultset = loggablestatement.executeQuery();
            boolean flag = resultset.next();
            return flag;
        }finally{
        	loggablestatement.close();
        }
    }

    public ColumnMap getColumnMap(){
        return columnMap;
    }

    public String getExistsStatement(){
        return getStatement();
    }

    public int getOrderBy(String property){
        return 0;
    }

    public String getStatement(){
        return statement;
    }

    public String getStatement(boolean withoutOrderBy){
        return statement;
    }
    
    public boolean isOrderableByProperty(String property){
        return false;
    }
    /**
     * Costruisce un PreparedStatement che contiene una query di conteggio del numero di record risultanti 
     * dalla query definita dal ricevente
     */
    public LoggableStatement prepareCountStatement(Connection connection) throws SQLException{
        StringBuffer stringbuffer = new StringBuffer("SELECT COUNT(*) FROM (");
        stringbuffer.append(getStatement(true));
        stringbuffer.append(PropertyNames.getProperty("query.count.end"));
    	LoggableStatement loggablestatement = new LoggableStatement(connection,stringbuffer.toString(),true,this.getClass());
        bindParameters(loggablestatement);
        return loggablestatement;
    }
    /**
     * Costruisce un PreparedStatement che contiene una query di conteggio del numero di record risultanti 
     * dalla query definita dal ricevente
     */
    public LoggableStatement prepareExistsStatement(Connection connection) throws SQLException{
        StringBuffer stringbuffer = new StringBuffer(PropertyNames.getProperty("query.exists"));
        String s = getStatement();
        int i = s.toUpperCase().lastIndexOf("ORDER BY");
        if(i >= 0)
            s = s.substring(0, i);
        stringbuffer.append(s);
        stringbuffer.append(" )");
    	LoggableStatement loggablestatement = new LoggableStatement(connection,stringbuffer.toString(),true,this.getClass());
        bindParameters(loggablestatement);
        return loggablestatement;
    }
    /**
     * Crea un PreparedStatement che contiene la statemenent SQL generato e i valori dei parametri delle clausole.
     */
    public LoggableStatement prepareStatement(Connection connection) throws SQLException{
    	LoggableStatement loggablestatement = new LoggableStatement(connection,getStatement(),true,this.getClass());
        bindParameters(loggablestatement);
        return loggablestatement;
    }
    /**
     * Crea un PreparedStatement che contiene la statemenent SQL generato e i valori dei parametri delle clausole.
     */
    public LoggableStatement prepareStatement(Connection connection, int resultSetType, int resulSetConcurrency) throws SQLException{
    	LoggableStatement loggablestatement = new LoggableStatement(connection,getStatement(),true,this.getClass(),resultSetType, resulSetConcurrency);
        bindParameters(loggablestatement);
        return loggablestatement;
    }

    public void setColumnMap(ColumnMap columnmap){
        columnMap = columnmap;
    }

    public void setOrderBy(String property, int clause){
    }

    public void setStatement(String statement){
        this.statement = statement;
    }

    public String toString(){
        StringTokenizer stringtokenizer = new StringTokenizer(getStatement(), "?", true);
        StringBuffer stringbuffer = new StringBuffer();
        toString(stringtokenizer, parameters, stringbuffer);
        for(; stringtokenizer.hasMoreTokens(); stringbuffer.append(stringtokenizer.nextToken()));
        return stringbuffer.toString();
    }

    protected StringBuffer toString(StringTokenizer stringtokenizer, Vector vector, StringBuffer stringbuffer){
        if(vector != null){
            for(int i = 0; stringtokenizer.hasMoreTokens() && i < vector.size();){
                String s = stringtokenizer.nextToken();
                if(s.equals("?")){
                    SQLParameter sqlparameter = (SQLParameter)vector.get(i++);
                    if(sqlparameter == null)
                        stringbuffer.append(s);
                    else{
						Object obj = sqlparameter.getValue();
						if (sqlparameter.getConverter() != null)
						  obj = sqlparameter.getConverter().javaToSql(obj);
						try {
							if((sqlparameter.getSqlType() == ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_CHAR) || 
									sqlparameter.getSqlType() == ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_VARCHAR) || 
									sqlparameter.getSqlType() == ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_LONGVARCHAR) || 
									sqlparameter.getSqlType() ==ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_OTHER)) && 
									!(obj instanceof Timestamp) && !(obj instanceof java.sql.Date)){
								if (obj instanceof String){
									obj = ((String)obj).replace("'","''");
									stringbuffer.append("'" + obj + "'");
								}
								else if (obj instanceof Number)
								  stringbuffer.append(obj);	
							}else if (sqlparameter.getSqlType() == ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_TIMESTAMP) || 
									  obj instanceof Timestamp){
								String result = formatterTime.format(obj);
								stringbuffer.append("TO_DATE('" + result + "','"+formTimeOracle+"')");
							}else if (sqlparameter.getSqlType() == ColumnMapping.parseSqlTypeName(ColumnMapping.SQLTYPE_DATE) || 
									  obj instanceof java.sql.Date){
								String result = formatterDate.format(obj);
								stringbuffer.append("TO_DATE('" + result + "','"+formDate+"')");
						    }
							else
								stringbuffer.append(obj);
						} catch (ParseException e) {
						}                    	
                    }
                }else{
                    stringbuffer.append(s);
                }
            }
        }
        return stringbuffer;
    }

    public SQLUnion union(SQLQuery sqlquery, boolean flag){
        SQLUnion sqlunion = new SQLUnion(columnMap);
        sqlunion.addUnion(this, flag);
        sqlunion.addUnion(sqlquery, flag);
        return sqlunion;
    }
    /**
     * Costruisce un PreparedStatement che contiene una query di conteggio del numero di record risultanti 
     * dalla query definita dal ricevente
     */
    public LoggableStatement loggableCountStatement(Connection connection) throws SQLException{
        StringBuffer stringbuffer = new StringBuffer("SELECT COUNT(*) FROM (");
        stringbuffer.append(getStatement(true));
        stringbuffer.append(PropertyNames.getProperty("query.count.end"));
        LoggableStatement preparedstatement = new LoggableStatement(connection,stringbuffer.toString(),true,this.getClass());
        bindParameters(preparedstatement);
        return preparedstatement;
    }
    /**
     * Costruisce un PreparedStatement che contiene una query di conteggio del numero di record risultanti 
     * dalla query definita dal ricevente
     */
    public LoggableStatement loggableExistsStatement(Connection connection) throws SQLException{
        StringBuffer stringbuffer = new StringBuffer(PropertyNames.getProperty("query.exists"));
        String s = getStatement();
        int i = s.toUpperCase().lastIndexOf("ORDER BY");
        if(i >= 0)
            s = s.substring(0, i);
        stringbuffer.append(s);
        stringbuffer.append(" )");
        LoggableStatement preparedstatement = new LoggableStatement(connection,stringbuffer.toString(),true,this.getClass());
        bindParameters(preparedstatement);
        return preparedstatement;
    }
}