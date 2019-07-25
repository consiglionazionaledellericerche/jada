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

public class Excel_spooler_param_columnBulk extends Excel_spooler_param_columnBase {
    Excel_spooler_paramBulk excel_spooler_param;

    public Excel_spooler_param_columnBulk() {
        super();
    }

    public Excel_spooler_param_columnBulk(java.lang.Long pg_estrazione, java.lang.Long pg_column, java.lang.String id_key) {
        super(pg_estrazione, pg_column, id_key);
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