/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.persistency.Keyed;
public class Excel_spooler_param_columnBase extends Excel_spooler_param_columnKey implements Keyed {
//    VALUE VARCHAR(300) NOT NULL
	private java.lang.String value;
 
	public Excel_spooler_param_columnBase() {
		super();
	}
	public Excel_spooler_param_columnBase(java.lang.Long pg_estrazione, java.lang.Long pg_column, java.lang.String key) {
		super(pg_estrazione, pg_column,key);
	}
	
	/**
	 * @return
	 */
	public java.lang.String getValue() {
		return value;
	}
	
	/**
	 * @param string
	 */
	public void setValue(java.lang.String string) {
		value = string;
	}

}