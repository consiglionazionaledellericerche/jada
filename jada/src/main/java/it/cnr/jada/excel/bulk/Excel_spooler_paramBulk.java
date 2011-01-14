/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Excel_spooler_paramBulk extends Excel_spooler_paramBase {
	Excel_spoolerBulk excel_spooler;
	public Excel_spooler_paramBulk() {
		super();
	}
	public Excel_spooler_paramBulk(java.lang.Long pg_estrazione, java.lang.Long pg_column) {
		super(pg_estrazione, pg_column);
	}
	/**
	 * @return
	 */
	public Excel_spoolerBulk getExcel_spooler() {
		return excel_spooler;
	}

	/**
	 * @param bulk
	 */
	public void setExcel_spooler(Excel_spoolerBulk bulk) {
		excel_spooler = bulk;
	}
	public Long getPg_estrazione() {
		return getExcel_spooler().getPg_estrazione();
	}
	public void setPg_estrazione(Long pg_estrazione) {
		getExcel_spooler().setPg_estrazione(pg_estrazione);
	}


}