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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

class CacheHttpServletResponse
        implements HttpServletResponse {

    private int status;
    private Hashtable headers;
    private int contentLength;
    private String contentType;
    private Locale locale;
    private Vector cookies;
    private boolean didError;
    private boolean didRedirect;
    private boolean gotStream;
    private boolean gotWriter;
    private HttpServletResponse _flddelegate;
    private CacheServletOutputStream out;
    private PrintWriter writer;

    CacheHttpServletResponse(HttpServletResponse res) {
        _flddelegate = res;
        try {
            out = new CacheServletOutputStream(res.getOutputStream());
        } catch (IOException e) {
            System.out.println("Got IOException constructing cached response: " + e.getMessage());
        }
        internalReset();
    }

    public void addCookie(Cookie cookie) {
        _flddelegate.addCookie(cookie);
        cookies.addElement(cookie);
    }

    public void addDateHeader(String name, long value) {
        internalAddHeader(name, new Long(value));
    }

    public void addHeader(String name, String value) {
        internalAddHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        internalAddHeader(name, new Integer(value));
    }

    public boolean containsHeader(String name) {
        return _flddelegate.containsHeader(name);
    }

    public String encodeRedirectURL(String url) {
        return _flddelegate.encodeRedirectURL(url);
    }

    /**
     * @deprecated Method encodeRedirectUrl is deprecated
     */

    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    public String encodeURL(String url) {
        return _flddelegate.encodeURL(url);
    }

    /**
     * @deprecated Method encodeUrl is deprecated
     */

    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    public void flushBuffer()
            throws IOException {
        _flddelegate.flushBuffer();
    }

    public int getBufferSize() {
        return _flddelegate.getBufferSize();
    }

    public void setBufferSize(int size)
            throws IllegalStateException {
        _flddelegate.setBufferSize(size);
    }

    public String getCharacterEncoding() {
        return _flddelegate.getCharacterEncoding();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String charset) {
        // TODO Auto-generated method stub

    }

    public Locale getLocale() {
        return _flddelegate.getLocale();
    }

    public void setLocale(Locale loc) {
        _flddelegate.setLocale(loc);
        locale = loc;
    }

    public ServletOutputStream getOutputStream()
            throws IOException {
        if (gotWriter) {
            throw new IllegalStateException("Cannot get output stream after getting writer");
        } else {
            gotStream = true;
            return out;
        }
    }

    public PrintWriter getWriter()
            throws UnsupportedEncodingException {
        if (gotStream)
            throw new IllegalStateException("Cannot get writer after getting output stream");
        gotWriter = true;
        if (writer == null) {
            OutputStreamWriter w = new OutputStreamWriter(out, getCharacterEncoding());
            writer = new PrintWriter(w, true);
        }
        return writer;
    }

    private void internalAddHeader(String name, Object value) {
        Vector v = (Vector) headers.get(name);
        if (v == null)
            v = new Vector();
        v.addElement(value);
        headers.put(name, v);
    }

    private void internalReset() {
        status = 200;
        headers = new Hashtable();
        contentLength = -1;
        contentType = null;
        locale = null;
        cookies = new Vector();
        didError = false;
        didRedirect = false;
        gotStream = false;
        gotWriter = false;
        out.getBuffer().reset();
    }

    private void internalSetHeader(String name, Object value) {
        Vector v = new Vector();
        v.addElement(value);
        headers.put(name, v);
    }

    public boolean isCommitted() {
        return _flddelegate.isCommitted();
    }

    public boolean isValid() {
        return !didError && !didRedirect;
    }

    public void reset()
            throws IllegalStateException {
        _flddelegate.reset();
        internalReset();
    }

    public void resetBuffer()
            throws IllegalStateException {
        _flddelegate.resetBuffer();
        contentLength = -1;
        out.getBuffer().reset();
    }

    public void sendError(int sc)
            throws IOException {
        _flddelegate.sendError(sc);
        didError = true;
    }

    public void sendError(int sc, String msg)
            throws IOException {
        _flddelegate.sendError(sc, msg);
        didError = true;
    }

    public void sendRedirect(String location)
            throws IOException {
        _flddelegate.sendRedirect(location);
        didRedirect = true;
    }

    public void setContentLength(int len) {
        _flddelegate.setContentLength(len);
    }

    public void setDateHeader(String name, long date) {
        _flddelegate.setDateHeader(name, date);
        internalSetHeader(name, new Long(date));
    }

    public void setHeader(String name, String value) {
        _flddelegate.setHeader(name, value);
        internalSetHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        _flddelegate.setIntHeader(name, value);
        internalSetHeader(name, new Integer(value));
    }

    public void setStatus(int sc) {
        _flddelegate.setStatus(sc);
        status = sc;
    }

    /**
     * @deprecated Method setStatus is deprecated
     */

    public void setStatus(int sc, String sm) {
        _flddelegate.setStatus(sc, sm);
        status = sc;
    }

    public void writeTo(HttpServletResponse res) {
        res.setStatus(status);
        if (contentType != null)
            res.setContentType(contentType);
        if (locale != null)
            res.setLocale(locale);
        Cookie c;
        for (Enumeration numerazione = cookies.elements(); numerazione.hasMoreElements(); res.addCookie(c))
            c = (Cookie) numerazione.nextElement();

        for (Enumeration numerazione = headers.keys(); numerazione.hasMoreElements(); ) {
            String name = (String) numerazione.nextElement();
            Vector values = (Vector) headers.get(name);
            for (Enumeration enum2 = values.elements(); enum2.hasMoreElements(); ) {
                Object value = enum2.nextElement();
                if (value instanceof String)
                    res.setHeader(name, (String) value);
                if (value instanceof Integer)
                    res.setIntHeader(name, ((Integer) value).intValue());
                if (value instanceof Long)
                    res.setDateHeader(name, ((Long) value).longValue());
            }

        }

        res.setContentLength(out.getBuffer().size());
        try {
            out.getBuffer().writeTo(res.getOutputStream());
        } catch (IOException e) {
            System.out.println("Got IOException writing cached response: " + e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getContentType()
     */
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setContentType(String type) {
        _flddelegate.setContentType(type);
        contentType = type;
    }
}