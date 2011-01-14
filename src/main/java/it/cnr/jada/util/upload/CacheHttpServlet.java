package it.cnr.jada.util.upload;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public abstract class CacheHttpServlet extends HttpServlet
{

    public CacheHttpServlet()
    {
        cacheLastMod = -1L;
        cacheQueryString = null;
        cachePathInfo = null;
        cacheServletPath = null;
        lock = new Object();
    }

    private boolean equal(String s1, String s2)
    {
        if(s1 == null && s2 == null)
            return true;
        if(s1 == null || s2 == null)
            return false;
        else
            return s1.equals(s2);
    }

    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        String method = req.getMethod();
        if(!method.equals("GET"))
        {
            super.service(req, res);
            return;
        }
        long servletLastMod = getLastModified(req);
        if(servletLastMod == -1L)
        {
            super.service(req, res);
            return;
        }
        if((servletLastMod / 1000L) * 1000L <= req.getDateHeader("If-Modified-Since"))
        {
            res.setStatus(304);
            return;
        }
        CacheHttpServletResponse localResponseCopy = null;
        synchronized(lock)
        {
            if(servletLastMod <= cacheLastMod && cacheResponse.isValid() && equal(cacheQueryString, req.getQueryString()) && equal(cachePathInfo, req.getPathInfo()) && equal(cacheServletPath, req.getServletPath()))
                localResponseCopy = cacheResponse;
        }
        if(localResponseCopy != null)
        {
            localResponseCopy.writeTo(res);
            return;
        }
        localResponseCopy = new CacheHttpServletResponse(res);
        super.service(req, localResponseCopy);
        synchronized(lock)
        {
            cacheResponse = localResponseCopy;
            cacheLastMod = servletLastMod;
            cacheQueryString = req.getQueryString();
            cachePathInfo = req.getPathInfo();
            cacheServletPath = req.getServletPath();
        }
    }

    CacheHttpServletResponse cacheResponse;
    long cacheLastMod;
    String cacheQueryString;
    String cachePathInfo;
    String cacheServletPath;
    Object lock;
}