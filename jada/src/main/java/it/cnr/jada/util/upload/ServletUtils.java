package it.cnr.jada.util.upload;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.*;

public class ServletUtils
{

    public ServletUtils()
    {
    }

    public static URL getResource(ServletContext context, String resource)
        throws IOException
    {
        if(resource == null)
            throw new FileNotFoundException("Requested resource was null (passed in null)");
        if(resource.endsWith("/") || resource.endsWith("\\") || resource.endsWith("."))
            throw new MalformedURLException("Path may not end with a slash or dot");
        if(resource.indexOf("..") != -1)
            throw new MalformedURLException("Path may not contain double dots");
        String upperResource = resource.toUpperCase();
        if(upperResource.startsWith("/WEB-INF") || upperResource.startsWith("/META-INF"))
            throw new MalformedURLException("Path may not begin with /WEB-INF or /META-INF");
        if(upperResource.endsWith(".JSP"))
            throw new MalformedURLException("Path may not end with .jsp");
        URL url = context.getResource(resource);
        if(url == null)
            throw new FileNotFoundException("Requested resource was null (" + resource + ")");
        else
            return url;
    }

    public static String getStackTraceAsString(Throwable t)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bytes, true);
        t.printStackTrace(writer);
        return bytes.toString();
    }

    public static void returnFile(String filename, OutputStream out)
        throws FileNotFoundException, IOException
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(filename);
            byte buf[] = new byte[4096];
            int bytesRead;
            while((bytesRead = fis.read(buf)) != -1) 
                out.write(buf, 0, bytesRead);
        }
        finally
        {
            if(fis != null)
                fis.close();
        }
    }

    public static void returnURL(URL url, OutputStream out)
        throws IOException
    {
        InputStream in = url.openStream();
        byte buf[] = new byte[4096];
        int bytesRead;
        while((bytesRead = in.read(buf)) != -1) 
            out.write(buf, 0, bytesRead);
    }

    public static void returnURL(URL url, PrintWriter out)
        throws IOException
    {
        URLConnection con = url.openConnection();
        con.connect();
        String encoding = con.getContentEncoding();
        BufferedReader in = null;
        if(encoding == null)
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        else
            in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
        char buf[] = new char[4096];
        int charsRead;
        while((charsRead = in.read(buf)) != -1) 
            out.write(buf, 0, charsRead);
    }

    public static String[] split(String str, String delim)
    {
        Vector v = new Vector();
        for(StringTokenizer tokenizer = new StringTokenizer(str, delim); tokenizer.hasMoreTokens(); v.addElement(tokenizer.nextToken()));
        String ret[] = new String[v.size()];
        for(int i = 0; i < ret.length; i++)
            ret[i] = (String)v.elementAt(i);

        return ret;
    }
}