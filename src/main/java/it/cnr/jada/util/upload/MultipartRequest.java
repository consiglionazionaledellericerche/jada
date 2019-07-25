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
 * Created on Oct 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.upload;

/**
 * @author max
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class MultipartRequest {

    private static final int DEFAULT_MAX_POST_SIZE = 100000000;
    protected Hashtable parameters;
    protected Hashtable files;

    public MultipartRequest(ServletRequest request, String saveDirectory)
            throws IOException {
        this((HttpServletRequest) request, saveDirectory);
    }

    public MultipartRequest(ServletRequest request, String saveDirectory, int maxPostSize)
            throws IOException {
        this((HttpServletRequest) request, saveDirectory, maxPostSize);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory)
            throws IOException {
        this(request, saveDirectory, DEFAULT_MAX_POST_SIZE);
    }

    public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize)
            throws IOException {
        parameters = new Hashtable();
        files = new Hashtable();
        if (request == null)
            throw new IllegalArgumentException("request cannot be null");
        if (saveDirectory == null)
            throw new IllegalArgumentException("saveDirectory cannot be null");
        if (maxPostSize <= 0)
            throw new IllegalArgumentException("maxPostSize must be positive");
        File dir = new File(saveDirectory);
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Not a directory: " + saveDirectory);
        if (!dir.canWrite())
            throw new IllegalArgumentException("Not writable: " + saveDirectory);
        MultipartParser parser = new MultipartParser(request, maxPostSize);
        Part part;
        while ((part = parser.readNextPart()) != null) {
            String name = part.getName();
            if (part.isParam()) {
                ParamPart paramPart = (ParamPart) part;
                String value = paramPart.getStringValue();
                Vector existingValues = (Vector) parameters.get(name);
                if (existingValues == null) {
                    existingValues = new Vector();
                    parameters.put(name, existingValues);
                }
                existingValues.addElement(value);
            } else if (part.isFile()) {
                FilePart filePart = (FilePart) part;
                String fileName = filePart.getFileName();
                if (fileName != null) {
                    filePart.writeTo(dir);
                    files.put(name, new UploadedFile(dir.toString(), fileName, filePart.getContentType()));
                } else {
                    files.put(name, new UploadedFile(null, null, null));
                }
            }
        }
    }

    public String getContentType(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getContentType();
        } catch (Exception _ex) {
            return null;
        }
    }

    public UploadedFile getFile(String name) {
        try {
            return (UploadedFile) files.get(name);
        } catch (Exception _ex) {
            return null;
        }
    }

    public Enumeration getFileNames() {
        return files.keys();
    }

    public String getFilesystemName(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFilesystemName();
        } catch (Exception _ex) {
            return null;
        }
    }

    public String getParameter(String name) {
        try {
            Vector values = (Vector) parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            } else {
                String value = (String) values.elementAt(values.size() - 1);
                return value;
            }
        } catch (Exception _ex) {
            return null;
        }
    }

    public Enumeration getParameterNames() {
        return parameters.keys();
    }

    public String[] getParameterValues(String name) {
        try {
            Vector values = (Vector) parameters.get(name);
            if (values == null || values.size() == 0) {
                return null;
            } else {
                String[] valuesArray = new String[values.size()];
                values.copyInto(valuesArray);
                return valuesArray;
            }
        } catch (Exception _ex) {
            return null;
        }
    }
}