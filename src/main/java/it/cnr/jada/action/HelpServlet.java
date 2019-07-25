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

package it.cnr.jada.action;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

public class HelpServlet extends HttpServlet
        implements Serializable {

    public HelpServlet() {
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
            throws ServletException, IOException {
        try {
            Connection connection = EJBCommonServices.getConnection();
            try {
                LoggableStatement statement = new LoggableStatement(connection, "SELECT HELP_URL FROM " + EJBCommonServices.getDefaultSchema()
                        + "HELP_LKT WHERE PAGE = ?", true, this.getClass());

                statement.setString(1, httpservletrequest.getPathInfo());
                ResultSet resultset = statement.executeQuery();

                LoggableStatement LSstr = new LoggableStatement(connection, "SELECT FLAG_MAN FROM " + EJBCommonServices.getDefaultSchema() + "HELP_LKT WHERE PAGE = ?", true, this.getClass());
                LSstr.setString(1, httpservletrequest.getPathInfo());
                ResultSet RS = LSstr.executeQuery();
                if (RS.next()) {

                    if (resultset.next())
                        if (RS.getString(1).compareTo("Y") == 0)
                            httpservletresponse.sendRedirect(resultset.getString(1));
                        else
                            httpservletresponse.sendRedirect(JSPUtils.buildAbsoluteUrl(httpservletrequest, "", resultset.getString(1)));
                    else
                        httpservletrequest.getRequestDispatcher("/help_not_found.html").forward(httpservletrequest, httpservletresponse);
                }
                try {
                    resultset.close();
                } catch (java.sql.SQLException e) {
                }
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                }
            } finally {
                connection.close();
            }
        } catch (Throwable throwable) {
            throw new ServletException(throwable);
        }
    }
}