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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLExceptionHandler, ValueTooLargeException, BusyRecordException, ReferentialIntegrityException, 
//            ApplicationWarningPersistencyException, ApplicationPersistencyException, DuplicateKeyException, NotDeletableException

public class DefaultSQLExceptionHandler extends SQLExceptionHandler
        implements Serializable {

    protected DefaultSQLExceptionHandler() {
    }

    public PersistencyException handleSQLException(SQLException sqlexception, Persistent persistent) {
        switch (sqlexception.getErrorCode()) {
            case 0: // '\0'
                if (sqlexception.getMessage().equals("Overflow Exception"))
                    return new ValueTooLargeException(sqlexception, persistent);
                else
                    return super.handleSQLException(sqlexception, persistent);

            case 54: // '6'
                return new BusyRecordException(sqlexception, persistent);

            case 2292:
            case 20010:
                return new ReferentialIntegrityException(sqlexception, persistent);

            case 1401:
                return new ValueTooLargeException(sqlexception, persistent);

            case 20020:
            case 20025:
                String s = sqlexception.getMessage();
                if (s.startsWith("ORA-" + sqlexception.getErrorCode() + ": ")) {
                    StringBuffer stringbuffer = new StringBuffer();
                    BufferedReader bufferedreader = new BufferedReader(new StringReader(sqlexception.getMessage()));
                    String s1 = null;
                    boolean flag = true;
                    try {
                        while ((s1 = bufferedreader.readLine()) != null) {
                            if (flag) {
                                stringbuffer.append(s1.substring(11));
                                flag = false;
                                continue;
                            }
                            if (s1.startsWith("ORA-"))
                                break;
                            stringbuffer.append(s1);
                        }
                    } catch (IOException _ex) {
                    }
                    s = stringbuffer.toString();
                }
                switch (sqlexception.getErrorCode()) {
                    case 20025:
                        return new ApplicationWarningPersistencyException(s, sqlexception, persistent);

                    case 20020:
                    default:
                        return new ApplicationPersistencyException(s, sqlexception, persistent);
                }

            case 1: // '\001'
                return new DuplicateKeyException(sqlexception, persistent);

            case 20012:
                return new NotDeletableException(sqlexception, persistent);
            case 4068:
                return new ApplicationPersistencyDiscardedException("Si e' verificato un problema momentaneo della procedura.\nRiprovare l'operazione.");
        }
        return super.handleSQLException(sqlexception, persistent);
    }
}