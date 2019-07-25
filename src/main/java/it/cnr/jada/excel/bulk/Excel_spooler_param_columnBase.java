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

import it.cnr.jada.persistency.Keyed;

public class Excel_spooler_param_columnBase extends Excel_spooler_param_columnKey implements Keyed {
    //    VALUE VARCHAR(300) NOT NULL
    private java.lang.String value;

    public Excel_spooler_param_columnBase() {
        super();
    }

    public Excel_spooler_param_columnBase(java.lang.Long pg_estrazione, java.lang.Long pg_column, java.lang.String key) {
        super(pg_estrazione, pg_column, key);
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