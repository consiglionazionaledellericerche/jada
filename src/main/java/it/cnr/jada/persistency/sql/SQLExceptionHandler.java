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

// Referenced classes of package it.cnr.jada.persistency.sql:
//            DefaultSQLExceptionHandler, PersistencySQLException

public class SQLExceptionHandler
        implements Serializable {

    private static SQLExceptionHandler instance = new DefaultSQLExceptionHandler();

    protected SQLExceptionHandler() {
    }

    public static SQLExceptionHandler getInstance() {
        return instance;
    }

    public static void setInstance(SQLExceptionHandler sqlexceptionhandler) {
        instance = sqlexceptionhandler;
    }

    public final PersistencyException handleSQLException(SQLException sqlexception) {
        return handleSQLException(sqlexception, null);
    }

    public PersistencyException handleSQLException(SQLException sqlexception, Persistent persistent) {
        return new PersistencySQLException(sqlexception, persistent);
    }

}