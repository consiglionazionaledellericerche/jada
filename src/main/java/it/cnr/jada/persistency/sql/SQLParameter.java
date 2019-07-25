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

package it.cnr.jada.persistency.sql;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLConverter

class SQLParameter
        implements Serializable {

    private Object value;
    private int sqlType;
    private int scale;
    private SQLConverter converter;

    public SQLParameter(Object obj, int i, int j, SQLConverter sqlconverter) {
        value = obj;
        sqlType = i;
        scale = j;
        converter = sqlconverter;
    }

    public static final void setParameterInPreparedStatement(int i, LoggableStatement preparedstatement, Object obj, int j, int k, SQLConverter sqlconverter)
            throws SQLException {
        if (sqlconverter != null)
            sqlconverter.javaToSql(preparedstatement, obj, i, j);
        else if (obj == null)
            preparedstatement.setNull(i, j);
        else if (j == 1111)
            preparedstatement.setObject(i, obj);
        else if (j == 3 || j == 2 && (obj instanceof Number) && !(obj instanceof BigDecimal))
            preparedstatement.setObject(i, obj);
        else
            preparedstatement.setObject(i, obj, j, k);
    }

    public SQLConverter getConverter() {
        return converter;
    }

    public void setConverter(SQLConverter sqlconverter) {
        converter = sqlconverter;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int i) {
        scale = i;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int i) {
        sqlType = i;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object obj) {
        value = obj;
    }

    public void setInPreparedStatement(int i, LoggableStatement preparedstatement)
            throws SQLException {
        setParameterInPreparedStatement(i, preparedstatement, value, sqlType, scale, converter);
    }

    public String toString() {
        return value != null ? value.toString() : "";
    }
}