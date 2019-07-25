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

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Vector;

public class MultipartParser {

    private ServletInputStream in;
    private String boundary;
    private FilePart lastFilePart;
    private byte[] buf;

    public MultipartParser(HttpServletRequest req, int maxSize)
            throws IOException {
        this(req, maxSize, true, true);
    }

    public MultipartParser(HttpServletRequest req, int maxSize, boolean buffer, boolean limitLength)
            throws IOException {
        buf = new byte[8192];
        String type = null;
        String type1 = req.getHeader("Content-Type");
        String type2 = req.getContentType();
        if (type1 == null && type2 != null)
            type = type2;
        else if (type2 == null && type1 != null)
            type = type1;
        else if (type1 != null && type2 != null)
            type = type1.length() <= type2.length() ? type2 : type1;
        if (type == null || !type.toLowerCase().startsWith("multipart/form-data"))
            throw new IOException("Posted content type isn't multipart/form-data");
        int length = req.getContentLength();
        if (length > maxSize)
            throw new IOException("Posted content length of " + length + " exceeds limit of " + maxSize);
        String boundary = extractBoundary(type);
        if (boundary == null)
            throw new IOException("Separation boundary was not specified");
        ServletInputStream in = req.getInputStream();
        if (buffer)
            in = new BufferedServletInputStream(in);
        if (limitLength)
            in = new LimitedServletInputStream(in, length);
        this.in = in;
        this.boundary = boundary;
        String line = readLine();
        if (line == null)
            throw new IOException("Corrupt form data: premature ending");
        if (!line.startsWith(boundary))
            throw new IOException("Corrupt form data: no leading boundary: " + line + " != " + boundary);
        else
            return;
    }

    private String extractBoundary(String line) {
        int index = line.lastIndexOf("boundary=");
        if (index == -1)
            return null;
        String boundary = line.substring(index + 9);
        if (boundary.charAt(0) == '"') {
            index = boundary.lastIndexOf('"');
            boundary = boundary.substring(1, index);
        }
        boundary = "--" + boundary;
        return boundary;
    }

    private String extractContentType(String line)
            throws IOException {
        String contentType = null;
        String origline = line;
        line = origline.toLowerCase();
        if (line.startsWith("content-type")) {
            int start = line.indexOf(" ");
            if (start == -1)
                throw new IOException("Content type corrupt: " + origline);
            contentType = line.substring(start + 1);
        } else if (line.length() != 0)
            throw new IOException("Malformed line after disposition: " + origline);
        return contentType;
    }

    private String[] extractDispositionInfo(String line)
            throws IOException {
        String[] retval = new String[4];
        String origline = line;
        line = origline.toLowerCase();
        int start = line.indexOf("content-disposition: ");
        int end = line.indexOf(";");
        if (start == -1 || end == -1)
            throw new IOException("Content disposition corrupt: " + origline);
        String disposition = line.substring(start + 21, end);
        if (!disposition.equals("form-data"))
            throw new IOException("Invalid content disposition: " + disposition);
        start = line.indexOf("name=\"", end);
        end = line.indexOf("\"", start + 7);
        if (start == -1 || end == -1)
            throw new IOException("Content disposition corrupt: " + origline);
        String name = origline.substring(start + 6, end);
        String filename = null;
        String origname = null;
        start = line.indexOf("filename=\"", end + 2);
        end = line.indexOf("\"", start + 10);
        if (start != -1 && end != -1) {
            filename = origline.substring(start + 10, end);
            origname = filename;
            int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (slash > -1)
                filename = filename.substring(slash + 1);
        }
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        retval[3] = origname;
        return retval;
    }

    private String readLine()
            throws IOException {
        StringBuffer sbuf = new StringBuffer();
        int result;
        do {
            result = in.readLine(buf, 0, buf.length);
            if (result != -1)
                sbuf.append(new String(buf, 0, result, StandardCharsets.UTF_8));
        } while (result == buf.length);
        if (sbuf.length() == 0)
            return null;
        int len = sbuf.length();
        if (sbuf.charAt(len - 2) == '\r')
            sbuf.setLength(len - 2);
        else
            sbuf.setLength(len - 1);
        return sbuf.toString();
    }

    public Part readNextPart()
            throws IOException {
        if (lastFilePart != null) {
            lastFilePart.getInputStream().close();
            lastFilePart = null;
        }
        Vector headers = new Vector();
        String line = readLine();
        if (line == null)
            return null;
        if (line.length() == 0)
            return null;
        headers.addElement(line);
        for (; (line = readLine()) != null && line.length() > 0; headers.addElement(line)) ;
        if (line == null)
            return null;
        String name = null;
        String filename = null;
        String origname = null;
        String contentType = "text/plain";
        for (Enumeration numerazione = headers.elements(); numerazione.hasMoreElements(); ) {
            String headerline = (String) numerazione.nextElement();
            if (headerline.toLowerCase().startsWith("content-disposition:")) {
                String[] dispInfo = extractDispositionInfo(headerline);
                name = dispInfo[1];
                filename = dispInfo[2];
                origname = dispInfo[3];
            } else if (headerline.toLowerCase().startsWith("content-type:")) {
                String type = extractContentType(headerline);
                if (type != null)
                    contentType = type;
            }
        }

        if (filename == null)
            return new ParamPart(name, in, boundary);
        if (filename.equals(""))
            filename = null;
        lastFilePart = new FilePart(name, in, boundary, contentType, filename, origname);
        return lastFilePart;
    }
}