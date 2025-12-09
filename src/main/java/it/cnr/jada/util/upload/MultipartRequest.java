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

import it.cnr.jada.DetailedRuntimeException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MultipartRequest {

    private static final int DEFAULT_MAX_POST_SIZE = 100000000;
    protected Hashtable parameters;
    protected Hashtable<String, List<UploadedFile>> files;

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
                UploadedFile uploadedFile = Optional.ofNullable(fileName)
                    .map(s -> {
                        try {
                            filePart.writeTo(dir);
                        } catch (IOException e) {
                            throw new DetailedRuntimeException(e);
                        }
                        return new UploadedFile(dir.toString(), fileName, filePart.getFilePath(), filePart.getContentType());
                    }).orElseGet(() -> new UploadedFile(null, null, null, null));
                if (fileName != null) {
                    List existingValues = (List) files.get(name);
                    if (existingValues == null) {
                        existingValues = new ArrayList();
                        files.put(name, existingValues);
                    }
                    existingValues.add(uploadedFile);
                }
            }
        }
    }


    public List<UploadedFile> getFiles(String name) {
        return Optional.ofNullable(files)
                    .flatMap(h -> Optional.ofNullable(h.get(name)))
                    .orElse(Collections.emptyList());
    }

    public String getContentType(String name) {
        return getFiles(name)
                .stream()
                .findFirst()
                .map(UploadedFile::getContentType)
                .orElse(null);
    }

    public UploadedFile getFile(String name) {
        return getFiles(name)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Enumeration getFileNames() {
        return files.keys();
    }

    public String getFilesystemName(String name) {
        return getFiles(name)
                .stream()
                .findFirst()
                .map(UploadedFile::getFilesystemName)
                .orElse(null);
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