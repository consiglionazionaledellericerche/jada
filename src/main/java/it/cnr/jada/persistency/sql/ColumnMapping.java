package it.cnr.jada.persistency.sql;

import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * Oggetto che descrive la mappatura di una PersistentProperty su una colonna DB. 
 * Gli attributi di ColumnMapping sono: 
 * columnName Nome della colonna fisica su cui  mappata la property 
 * propertyName Nome della PersistentProperty su cui mappata la colonna 
 * primary se true la colonna fisica fa parte della chiave primaria della tabella 
 * fetchOnly se true la ColumnMapping viene aggiunta solo alla mappatura delle property; 
 * 		in pratica questa mappatura viene usata solo in lettura e non in scrittura. 
 * converterClassName Nome della classe da utilizzare come converter dal tipo di dati SQL al tipo della PersistentProperty e viceversa 
 * sqlType Il tipo SQL della colonna fisica; una stringa che puo assumere i seguenti valori: 
 * 	BIT 
 * 	TINYINT 
 * 	SMALLINT 
 * 	INTEGER 
 * 	BIGINT 
 * 	FLOAT 
 * 	REAL 
 * 	DOUBLE 
 * 	NUMERIC 
 * 	DECIMAL 
 * 	CHAR 
 * 	VARCHAR 
 * 	LONGVARCHAR 
 * 	DATE 
 * 	TIME 
 * 	TIMESTAMP 
 * 	BINARY 
 * 	VARBINARY 
 * 	LONGVARBINARY 
 * 	NULL 
 * 	OTHER 
 * 	JAVA_OBJECT 
 * 	DISTINCT 
 * 	STRUCT 
 * 	ARRAY 
 * 	BLOB 
 * 	CLOB 
 * 	REF 
 * columnSize Largezza fisica della colonna 
 * columnScale Scale della colonna (solo per le colonne di tipo DECIMAL) 
 * nullable Se false la colonna non puo contenere il valore SQL NULL
 */
public class ColumnMapping implements Serializable{

    private String columnName;
    private String propertyName;
    private int sqlType;
    private SQLConverter converter;
    private static final Map sqlTypeNames;
    private static final Map reverseSqlTypeNames;
    private boolean primary;
    private int columnSize;
    private int columnScale;
    private Boolean nullable;
    private boolean fetchOnly;
    private Boolean orderable;
    private int ordinalPosition;
	public static String SQLTYPE_BIT = "BIT";
	public static String SQLTYPE_TINYINT = "TINYINT";
	public static String SQLTYPE_SMALLINT = "SMALLINT";
	public static String SQLTYPE_INTEGER = "INTEGER";
	public static String SQLTYPE_BIGINT = "BIGINT";
	public static String SQLTYPE_FLOAT = "FLOAT";
	public static String SQLTYPE_REAL = "REAL";
	public static String SQLTYPE_DOUBLE = "DOUBLE";
	public static String SQLTYPE_NUMERIC = "NUMERIC";
	public static String SQLTYPE_DECIMAL = "DECIMAL";
	public static String SQLTYPE_CHAR = "CHAR";
	public static String SQLTYPE_VARCHAR = "VARCHAR";
	public static String SQLTYPE_LONGVARCHAR = "LONGVARCHAR";
	public static String SQLTYPE_DATE = "DATE";
	public static String SQLTYPE_TIME = "TIME";
	public static String SQLTYPE_TIMESTAMP = "TIMESTAMP";
	public static String SQLTYPE_BINARY = "BINARY";
	public static String SQLTYPE_VARBINARY = "VARBINARY";
	public static String SQLTYPE_LONGVARBINARY = "LONGVARBINARY";
	public static String SQLTYPE_NULL = "NULL";
	public static String SQLTYPE_OTHER = "OTHER";
	public static String SQLTYPE_JAVA_OBJECT = "JAVA_OBJECT";
	public static String SQLTYPE_DISTINCT = "DISTINCT";
	public static String SQLTYPE_STRUCT = "STRUCT";
	public static String SQLTYPE_ARRAY = "ARRAY";
	public static String SQLTYPE_BLOB = "BLOB";
	public static String SQLTYPE_CLOB = "CLOB";
	public static String SQLTYPE_REF = "REF";
	
    static 
    {
		sqlTypeNames = new HashMap();
		sqlTypeNames.put(SQLTYPE_BIT, "-7");
		sqlTypeNames.put(SQLTYPE_TINYINT, "-6");
		sqlTypeNames.put(SQLTYPE_SMALLINT, "5");
		sqlTypeNames.put(SQLTYPE_INTEGER, "4");
		sqlTypeNames.put(SQLTYPE_BIGINT, "-5");
		sqlTypeNames.put(SQLTYPE_FLOAT, "6");
		sqlTypeNames.put(SQLTYPE_REAL, "7");
		sqlTypeNames.put(SQLTYPE_DOUBLE, "8");
		sqlTypeNames.put(SQLTYPE_NUMERIC, "2");
		sqlTypeNames.put(SQLTYPE_DECIMAL, "3");
		sqlTypeNames.put(SQLTYPE_CHAR, "1");
		sqlTypeNames.put(SQLTYPE_VARCHAR, "12");
		sqlTypeNames.put(SQLTYPE_LONGVARCHAR, "-1");
		sqlTypeNames.put(SQLTYPE_DATE, "91");
		sqlTypeNames.put(SQLTYPE_TIME, "92");
		sqlTypeNames.put(SQLTYPE_TIMESTAMP, "93");
		sqlTypeNames.put(SQLTYPE_BINARY, "-2");
		sqlTypeNames.put(SQLTYPE_VARBINARY, "-3");
		sqlTypeNames.put(SQLTYPE_LONGVARBINARY, "-4");
		sqlTypeNames.put(SQLTYPE_NULL, "0");
		sqlTypeNames.put(SQLTYPE_OTHER, "1111");
		sqlTypeNames.put(SQLTYPE_JAVA_OBJECT, "2000");
		sqlTypeNames.put(SQLTYPE_DISTINCT, "2001");
		sqlTypeNames.put(SQLTYPE_STRUCT, "2002");
		sqlTypeNames.put(SQLTYPE_ARRAY, "2003");
		sqlTypeNames.put(SQLTYPE_BLOB, "2004");
		sqlTypeNames.put(SQLTYPE_CLOB, "2005");
		sqlTypeNames.put(SQLTYPE_REF, "2006");

        reverseSqlTypeNames = new HashMap(sqlTypeNames.size());
        Object obj;
        for(Iterator iterator = sqlTypeNames.keySet().iterator(); iterator.hasNext(); reverseSqlTypeNames.put(sqlTypeNames.get(obj), obj))
            obj = iterator.next();
    }
    
    public ColumnMapping() {
        ordinalPosition = 0;
    }

    void fillNullsFrom(ColumnMapping columnmapping){
        if(columnmapping == null)
            return;
        if(columnScale == 0)
            columnScale = columnmapping.columnScale;
        if(columnSize == 0)
            columnSize = columnmapping.columnSize;
        if(nullable == null)
            nullable = columnmapping.nullable;
        if(!primary)
            primary = columnmapping.primary;
        if(sqlType == 0)
            sqlType = columnmapping.sqlType;
        if(orderable == null)
            orderable = columnmapping.orderable;
    }

    public String getColumnName(){
        return columnName;
    }

    public int getColumnScale(){
        return columnScale;
    }

    public int getColumnSize(){
        return columnSize;
    }

    public SQLConverter getConverter(){
        return converter;
    }

    public String getConverterClassName(){
        if(converter == null)
            return null;
        else
            return converter.getClass().getName();
    }

    public int getOrdinalPosition(){
        return ordinalPosition;
    }

    public String getPropertyName(){
        return propertyName;
    }

    public int getSqlType(){
        return sqlType;
    }

    public String getSqlTypeName(){
        return getSqlTypeName(sqlType);
    }

    public static String getSqlTypeName(int i){
        return (String)reverseSqlTypeNames.get(String.valueOf(i));
    }

    public boolean isFetchOnly(){
        return fetchOnly;
    }

    public boolean isNullable(){
        return nullable != Boolean.FALSE;
    }

    public boolean isOrderable(){
        return orderable != Boolean.FALSE;
    }

    public boolean isPrimary(){
        return primary;
    }

    public static int parseSqlTypeName(String s) throws ParseException{
        return Integer.parseInt((String)sqlTypeNames.get(s.toUpperCase()));
    }

    public void setColumnName(String s){
        columnName = s;
    }

    public void setColumnScale(int i){
        columnScale = i;
    }

    public void setColumnSize(int i){
        columnSize = i;
    }

    public void setConverter(SQLConverter sqlconverter){
        converter = sqlconverter;
    }

    public void setConverterClassName(String s){
        try{
            setConverter((SQLConverter)Class.forName(s).newInstance());
        }catch(ClassNotFoundException _ex){
            throw new RuntimeException("Non \350 possibile trovare la classe " + s);
        }catch(InstantiationException _ex){
            throw new RuntimeException("Non \350 possibile istanziare il SQLConverter " + s);
        }catch(ClassCastException _ex){
            throw new RuntimeException("La classe " + s + " non \350 un SQLConverter");
        }catch(IllegalAccessException _ex){
            throw new RuntimeException("IllegalAccessException durante l'istanziazione del SQLConveter" + s);
        }
    }

    public void setFetchOnly(boolean flag){
        fetchOnly = flag;
    }

    public void setNullable(boolean flag){
        nullable = flag ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setOrderable(boolean flag){
        orderable = !primary && !flag ? Boolean.FALSE : Boolean.TRUE;
    }

    public void setOrdinalPosition(int i){
        ordinalPosition = i;
    }

    public void setPrimary(boolean flag){
        primary = flag;
    }

    public void setPropertyName(String s){
        propertyName = s;
    }

    public void setSqlType(int i){
        sqlType = i;
    }

    public void setSqlTypeName(String s){
        try{
            setSqlType(parseSqlTypeName(s));
        }catch(ParseException _ex){
            throw new RuntimeException(s + " non \350 un tipo SQL conosciuto");
        }
    }

    public String toString(){
        return getColumnName() + " -> " + getPropertyName();
    }

    public void writeTo(XmlWriter xmlwriter) throws IOException{
        xmlwriter.openTag("columnMapping");
        xmlwriter.printAttribute("columnName", getColumnName(), null);
        xmlwriter.printAttribute("propertyName", getPropertyName(), null);
        xmlwriter.printAttribute("sqlTypeName", getSqlTypeName(), null);
        xmlwriter.printAttribute("columnSize", getColumnSize(), 0);
        xmlwriter.printAttribute("columnScale", getColumnScale(), 0);
        xmlwriter.printAttribute("nullable", isNullable(), true);
        xmlwriter.printAttribute("primary", isPrimary(), false);
        xmlwriter.printAttribute("converterClassName", getConverterClassName(), null);
        xmlwriter.closeLastTag();
    }
    
}