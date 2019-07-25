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

package it.cnr.jada.ejb;

import it.cnr.jada.util.PropertyNames;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;

@Stateless(name = "JADAEJB_ServerDate")
public class ServerDateBean implements ServerDate {
    private static final long serialVersionUID = 0x2c7e5503d9bf9553L;

    public Timestamp getServerDate() throws EJBException {
        try {
            Connection conn = EJBCommonServices.getConnection();
            Statement statement = conn.createStatement();
            try {
                ResultSet resultset = statement.executeQuery(PropertyNames.getProperty("query.date"));
                try {
                    if (!resultset.next())
                        throw new EJBException("Errore interno: SELECT SYSDATE FROM DUAL ha restituito 0 records");
                    Timestamp timestamp = resultset.getTimestamp(1);
                    return timestamp;
                } finally {
                    try {
                        resultset.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } finally {
                try {
                    statement.close();
                    conn.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (SQLException sqlexception) {
            throw new EJBException(sqlexception);
        }
    }

    public Timestamp getServerTimestamp() throws EJBException {
        try {
            Connection conn = EJBCommonServices.getConnection();
            Statement statement = conn.createStatement();
            try {
                ResultSet resultset = statement.executeQuery(PropertyNames.getProperty("query.timestamp"));
                try {
                    if (!resultset.next())
                        throw new EJBException("Errore interno: SELECT SYSDATE FROM DUAL ha restituito 0 records");
                    Timestamp timestamp = resultset.getTimestamp(1);
                    return timestamp;
                } finally {
                    try {
                        resultset.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } finally {
                try {
                    statement.close();
                    conn.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (SQLException sqlexception) {
            throw new EJBException(sqlexception);
        }
    }
}