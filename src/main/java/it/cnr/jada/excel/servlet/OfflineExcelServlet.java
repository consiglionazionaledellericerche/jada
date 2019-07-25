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

package it.cnr.jada.excel.servlet;

import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.persistency.sql.HomeCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 * @version 1.0
 */
public class OfflineExcelServlet extends HttpServlet {

    /**
     * @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpActionContext context = new HttpActionContext(this, request, response);

            if (context.getBusinessProcessRoot(false) == null ||
                    context.getUserContext() == null ||
                    context.getUserContext().getUser() == null) {
                unauthorized(request, response);
                return;
            }

            Long pg_estrazione = new Long(request.getParameter("pg"));

            java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();

            try {
                Excel_spoolerBulk excel_spooler = getExcel_spoolerBulk(conn, context.getUserContext(), pg_estrazione);

                if (excel_spooler == null) {
                    unauthorized(request, response);
                    return;
                }

                String server = excel_spooler.getServer();
                String file = excel_spooler.getNome_file();
                if (context.getBusinessProcess() == null) {
                    unauthorized(request, response);
                } else if (server == null || file == null) {
                    unauthorized(request, response);
                } else {
                    StringBuffer reportServerURL = new StringBuffer(server);
                    reportServerURL.append("/" + file);
                    reportServerURL.append("?user=");
                    reportServerURL.append(java.net.URLEncoder.encode(excel_spooler.getUtcr()));
                    reportServerURL.append("&file=");
                    reportServerURL.append(java.net.URLEncoder.encode(file));
                    reportServerURL.append("&actualuser=");
                    reportServerURL.append(java.net.URLEncoder.encode(context.getUserContext().getUser()));
                    java.net.HttpURLConnection serverConn = (java.net.HttpURLConnection) new java.net.URL(reportServerURL.toString()).openConnection();
                    serverConn.setUseCaches(false);
                    serverConn.connect();
                    java.io.InputStream is = serverConn.getInputStream();
                    if (is == null) {
                        unauthorized(request, response);
                    } else {
                        response.setContentType(serverConn.getContentType());
                        response.setContentLength(serverConn.getContentLength());
                        response.setDateHeader("Expires", 0);
                        javax.servlet.ServletOutputStream os = response.getOutputStream();
                        byte[] buffer = new byte[response.getBufferSize()];
                        int buflength;
                        while ((buflength = is.read(buffer)) > 0) {
                            os.write(buffer, 0, buflength);
                        }
                        is.close();
                        os.flush();
                    }
                }
            } finally {
                conn.close();
            }
        } catch (java.io.FileNotFoundException e) {
            response.setStatus(response.SC_NOT_FOUND);
            response.getWriter().println("File inesistente.");
        } catch (Throwable e) {
            response.sendError(response.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public Excel_spoolerBulk getExcel_spoolerBulk(java.sql.Connection conn, it.cnr.jada.UserContext userContext, Long pg_estrazione) throws ApplicationException {
        try {
            HomeCache homeCache = new HomeCache(conn);
            Excel_spoolerBulk excel_spooler = (Excel_spoolerBulk) homeCache.getHome(Excel_spoolerBulk.class).findByPrimaryKey(new Excel_spoolerBulk(pg_estrazione));
            return excel_spooler;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ApplicationException(e);
        }
    }

    private void unauthorized(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("Impossibile aprire il file Excel.<BR>");
        response.getWriter().println("Consultare il <a href=\"http://contab.cnr.it/manuali/000%20-%2001%20requisiti%20browser.doc\">Manuale della Procedura di Contabilita'</a> e verificare le Impostazioni del Browser.");
        response.flushBuffer();
    }

}
