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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class HttpMessage {

    URL servlet;
    Hashtable headers;

    public HttpMessage(URL servlet) {
        this.servlet = null;
        headers = null;
        this.servlet = servlet;
    }

    public InputStream sendGetMessage()
            throws IOException {
        return sendGetMessage(null);
    }

    public InputStream sendGetMessage(Properties args)
            throws IOException {
        String argString = "";
        if (args != null)
            argString = "?" + toEncodedString(args);
        URL url = new URL(servlet.toExternalForm() + argString);
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        sendHeaders(con);
        return con.getInputStream();
    }

    private void sendHeaders(URLConnection con) {
        if (headers != null) {
            String name;
            String value;
            for (Enumeration numerazione = headers.keys(); numerazione.hasMoreElements(); con.setRequestProperty(name, value)) {
                name = (String) numerazione.nextElement();
                value = (String) headers.get(name);
            }

        }
    }

    public InputStream sendPostMessage()
            throws IOException {
        return sendPostMessage(null);
    }

    public InputStream sendPostMessage(Serializable obj)
            throws IOException {
        URLConnection con = servlet.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        sendHeaders(con);
        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        out.writeObject(obj);
        out.flush();
        out.close();
        return con.getInputStream();
    }

    public InputStream sendPostMessage(Properties args)
            throws IOException {
        String argString = "";
        if (args != null)
            argString = toEncodedString(args);
        URLConnection con = servlet.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        sendHeaders(con);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(argString);
        out.flush();
        out.close();
        return con.getInputStream();
    }

    public void setAuthorization(String name, String password) {
        String authorization = Base64Encoder.encode(name + ":" + password);
        setHeader("Authorization", "Basic " + authorization);
    }

    public void setCookie(String name, String value) {
        if (headers == null)
            headers = new Hashtable();
        String existingCookies = (String) headers.get("Cookie");
        if (existingCookies == null)
            setHeader("Cookie", name + "=" + value);
        else
            setHeader("Cookie", existingCookies + "; " + name + "=" + value);
    }

    public void setHeader(String name, String value) {
        if (headers == null)
            headers = new Hashtable();
        headers.put(name, value);
    }

    private String toEncodedString(Properties args) {
        StringBuffer buf = new StringBuffer();
        for (Enumeration names = args.propertyNames(); names.hasMoreElements(); ) {
            String name = (String) names.nextElement();
            String value = args.getProperty(name);
            buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
            if (names.hasMoreElements())
                buf.append("&");
        }

        return buf.toString();
    }
}