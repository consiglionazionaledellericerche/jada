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
import java.sql.ResultSet;
import java.sql.SQLException;

public class CHARToBooleanConverter implements SQLConverter<java.lang.Boolean>, Serializable {
    private static final long serialVersionUID = 1L;

    public CHARToBooleanConverter() {
        super();
    }

    public Class<java.lang.Boolean> getTargetJavaType(int i, boolean flag) {
        return java.lang.Boolean.class;
    }

    public void javaToSql(LoggableStatement preparedstatement, Object obj,
                          int i, int j) throws SQLException {
        if (obj == null)
            preparedstatement.setNull(i, j);
        else
            preparedstatement.setString(i, ((Boolean) obj).booleanValue() ? "Y"
                    : "N");
    }

    public Object sqlToJava(ResultSet resultset, String s) throws SQLException {
        String s1 = resultset.getString(s);
        if (s1 == null || s1.length() == 0) {
            return null;
        } else {
            char c = Character.toUpperCase(s1.charAt(0));
            return new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == '1');
        }
    }

    public Object javaToSql(Object obj) {
        return ((Boolean) obj).booleanValue() ? "Y" : "N";
    }

    public Object sqlToJava(Object obj) {
        String s1 = (String) obj;
        if (s1 == null || s1.length() == 0) {
            return null;
        } else {
            char c = Character.toUpperCase(s1.charAt(0));
            return new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == '1');
        }
    }

    public String columnName(String columnName) {
        return columnName;
    }
}