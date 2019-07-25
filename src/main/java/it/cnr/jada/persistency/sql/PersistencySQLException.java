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

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;
import java.sql.SQLException;

public class PersistencySQLException extends PersistencyException
        implements Serializable {

    public PersistencySQLException(String s, SQLException sqlexception) {
        super(s, sqlexception);
    }

    public PersistencySQLException(String s, SQLException sqlexception, Persistent persistent) {
        super(s, sqlexception, persistent);
    }

    public PersistencySQLException(SQLException sqlexception) {
        super(sqlexception);
    }

    public PersistencySQLException(SQLException sqlexception, Persistent persistent) {
        super(sqlexception, persistent);
    }

    public SQLException getSQLException() {
        return (SQLException) getDetail();
    }
}