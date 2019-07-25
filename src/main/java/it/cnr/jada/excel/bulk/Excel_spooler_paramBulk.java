/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by Generator 1.0
 * Date 23/01/2006
 */
package it.cnr.jada.excel.bulk;

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