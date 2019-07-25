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

public class Excel_spooler_paramBase extends Excel_spooler_paramKey implements Keyed {
    //    COLUMN_NAME VARCHAR(300) NOT NULL
    private java.lang.String column_name;

    //    COLUMN_TYPE VARCHAR(100)
    private java.lang.String column_type;

    //    COLUMN_LABEL VARCHAR(100) NOT NULL
    private java.lang.String column_label;

    //    HEADER_LABEL VARCHAR(300)
    private java.lang.String header_label;

    public Excel_spooler_paramBase() {
        super();
    }

    public Excel_spooler_paramBase(java.lang.Long pg_estrazione, java.lang.Long pg_column) {
        super(pg_estrazione, pg_column);
    }

    public java.lang.String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(java.lang.String column_name) {
        this.column_name = column_name;
    }

    public java.lang.String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(java.lang.String column_type) {
        this.column_type = column_type;
    }

    public java.lang.String getColumn_label() {
        return column_label;
    }

    public void setColumn_label(java.lang.String column_label) {
        this.column_label = column_label;
    }

    public java.lang.String getHeader_label() {
        return header_label;
    }

    public void setHeader_label(java.lang.String header_label) {
        this.header_label = header_label;
    }
}