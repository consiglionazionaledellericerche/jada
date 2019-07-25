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

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLConverter<T> {

    Class<T> getTargetJavaType(int i, boolean flag);

    void javaToSql(LoggableStatement preparedstatement, Object obj, int i, int j)
            throws SQLException;

    Object sqlToJava(ResultSet resultset, String s)
            throws SQLException;

    Object javaToSql(Object obj);

    Object sqlToJava(Object obj);

    String columnName(String columnName);
}