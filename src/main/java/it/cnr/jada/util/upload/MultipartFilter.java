package it.cnr.jada.util.upload;
import java.io.File;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import it.cnr.jada.util.servlet.*;

public class MultipartFilter
    implements Filter
{

    public MultipartFilter()
    {
        config = null;
        dir = null;
    }

    public void destroy()
    {
        config = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;
        String type = req.getHeader("Content-Type");
        if(type == null || !type.startsWith("multipart/form-data"))
        {
            chain.doFilter(request, response);
        } else
        {
            MultipartWrapper multi = new MultipartWrapper(req, dir);
            chain.doFilter(multi, response);
        }
    }

    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        dir = config.getInitParameter("uploadDir");
        if(dir == null)
        {
            File tempdir = (File)config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            if(tempdir != null)
                dir = tempdir.toString();
            else
                throw new ServletException("MultipartFilter: No upload directory found: set an uploadDir init parameter or ensure the javax.servlet.context.tempdir directory is valid");
        }
    }

    private FilterConfig config;
    private String dir;
}