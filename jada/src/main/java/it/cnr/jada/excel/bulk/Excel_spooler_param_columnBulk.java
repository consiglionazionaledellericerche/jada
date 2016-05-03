/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Excel_spooler_param_columnBulk extends Excel_spooler_param_columnBase {
	Excel_spooler_paramBulk excel_spooler_param;
	public Excel_spooler_param_columnBulk() {
		super();
	}
	public Excel_spooler_param_columnBulk(java.lang.Long pg_estrazione, java.lang.Long pg_column,java.lang.String id_key) {
		super(pg_estrazione, pg_column,id_key);
	}
	public Long getPg_estrazione() {
		return getExcel_spooler_param().getPg_estrazione();
	}
	public void setPg_estrazione(Long pg_estrazione) {
		getExcel_spooler_param().setPg_estrazione(pg_estrazione);
	}
	public Long getPg_column() {
		return getExcel_spooler_param().getPg_column();
	}
	public void setPg_column(Long pg_column) {
		getExcel_spooler_param().setPg_column(pg_column);
	}
	public Excel_spooler_paramBulk getExcel_spooler_param() {
		return excel_spooler_param;
	}
	public void setExcel_spooler_param(Excel_spooler_paramBulk bulk) {
		excel_spooler_param = bulk;
	}

}