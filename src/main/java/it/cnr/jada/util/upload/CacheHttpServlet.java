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

package it.cnr.jada.util.upload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class CacheHttpServlet extends HttpServlet {

    CacheHttpServletResponse cacheResponse;
    long cacheLastMod;
    String cacheQueryString;
    String cachePathInfo;
    String cacheServletPath;
    Object lock;
    public CacheHttpServlet() {
        cacheLastMod = -1L;
        cacheQueryString = null;
        cachePathInfo = null;
        cacheServletPath = null;
        lock = new Object();
    }

    private boolean equal(String s1, String s2) {
        if (s1 == null && s2 == null)
            return true;
        if (s1 == null || s2 == null)
            return false;
        else
            return s1.equals(s2);
    }

    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("GET")) {
            super.service(req, res);
            return;
        }
        long servletLastMod = getLastModified(req);
        if (servletLastMod == -1L) {
            super.service(req, res);
            return;
        }
        if ((servletLastMod / 1000L) * 1000L <= req.getDateHeader("If-Modified-Since")) {
            res.setStatus(304);
            return;
        }
        CacheHttpServletResponse localResponseCopy = null;
        synchronized (lock) {
            if (servletLastMod <= cacheLastMod && cacheResponse.isValid() && equal(cacheQueryString, req.getQueryString()) && equal(cachePathInfo, req.getPathInfo()) && equal(cacheServletPath, req.getServletPath()))
                localResponseCopy = cacheResponse;
        }
        if (localResponseCopy != null) {
            localResponseCopy.writeTo(res);
            return;
        }
        localResponseCopy = new CacheHttpServletResponse(res);
        super.service(req, localResponseCopy);
        synchronized (lock) {
            cacheResponse = localResponseCopy;
            cacheLastMod = servletLastMod;
            cacheQueryString = req.getQueryString();
            cachePathInfo = req.getPathInfo();
            cacheServletPath = req.getServletPath();
        }
    }
}