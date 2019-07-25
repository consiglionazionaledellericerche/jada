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

/*
 * Created on Oct 12, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.servlet;

/**
 * @author marco
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import it.cnr.jada.util.upload.MultipartRequest;
import it.cnr.jada.util.upload.UploadedFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MultipartWrapper extends HttpServletRequestWrapper {

    MultipartRequest mreq = null;

    public MultipartWrapper(HttpServletRequest req, String dir)
            throws IOException {
        super(req);
        mreq = new MultipartRequest(req, dir);
    }

    // Methods to replace HSR methods
    public Enumeration getParameterNames() {
        return mreq.getParameterNames();
    }

    public String getParameter(String name) {
        return mreq.getParameter(name);
    }

    public String[] getParameterValues(String name) {
        return mreq.getParameterValues(name);
    }

    public Map getParameterMap() {
        Map map = new HashMap();
        Enumeration numerazione = getParameterNames();
        while (numerazione.hasMoreElements()) {
            String name = (String) numerazione.nextElement();
            map.put(name, mreq.getParameterValues(name));
        }
        return map;
    }

    // Methods only in MultipartRequest
    public Enumeration getFileNames() {
        return mreq.getFileNames();
    }

    public String getFilesystemName(String name) {
        return mreq.getFilesystemName(name);
    }

    public String getContentType(String name) {
        return mreq.getContentType(name);
    }

    public UploadedFile getFile(String name) {
        return mreq.getFile(name);
    }
}