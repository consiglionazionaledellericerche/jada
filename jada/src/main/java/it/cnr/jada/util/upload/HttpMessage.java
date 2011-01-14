package it.cnr.jada.util.upload;
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpMessage
{

    public HttpMessage(URL servlet)
    {
        this.servlet = null;
        headers = null;
        this.servlet = servlet;
    }

    public InputStream sendGetMessage()
        throws IOException
    {
        return sendGetMessage(null);
    }

    public InputStream sendGetMessage(Properties args)
        throws IOException
    {
        String argString = "";
        if(args != null)
            argString = "?" + toEncodedString(args);
        URL url = new URL(servlet.toExternalForm() + argString);
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        sendHeaders(con);
        return con.getInputStream();
    }

    private void sendHeaders(URLConnection con)
    {
        if(headers != null)
        {
            String name;
            String value;
            for(Enumeration numerazione = headers.keys(); numerazione.hasMoreElements(); con.setRequestProperty(name, value))
            {
                name = (String)numerazione.nextElement();
                value = (String)headers.get(name);
            }

        }
    }

    public InputStream sendPostMessage()
        throws IOException
    {
        return sendPostMessage(((Properties) (null)));
    }

    public InputStream sendPostMessage(Serializable obj)
        throws IOException
    {
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
        throws IOException
    {
        String argString = "";
        if(args != null)
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

    public void setAuthorization(String name, String password)
    {
        String authorization = Base64Encoder.encode(name + ":" + password);
        setHeader("Authorization", "Basic " + authorization);
    }

    public void setCookie(String name, String value)
    {
        if(headers == null)
            headers = new Hashtable();
        String existingCookies = (String)headers.get("Cookie");
        if(existingCookies == null)
            setHeader("Cookie", name + "=" + value);
        else
            setHeader("Cookie", existingCookies + "; " + name + "=" + value);
    }

    public void setHeader(String name, String value)
    {
        if(headers == null)
            headers = new Hashtable();
        headers.put(name, value);
    }

    private String toEncodedString(Properties args)
    {
        StringBuffer buf = new StringBuffer();
        for(Enumeration names = args.propertyNames(); names.hasMoreElements();)
        {
            String name = (String)names.nextElement();
            String value = args.getProperty(name);
            buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
            if(names.hasMoreElements())
                buf.append("&");
        }

        return buf.toString();
    }

    URL servlet;
    Hashtable headers;
}