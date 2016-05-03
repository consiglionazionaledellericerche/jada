package it.cnr.jada.action;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

// Referenced classes of package it.cnr.jada.action:
//            Forward, ActionContext, HttpActionContext, ActionPerformingError

public class StaticForward
    implements Serializable, Forward
{

    public StaticForward()
    {
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }

    public boolean isRedirect()
    {
        return redirect;
    }

    public void perform(ActionContext actioncontext)
    {
        if(actioncontext instanceof ActionContext)
            perform((HttpActionContext)actioncontext);
    }

    public void perform(HttpActionContext httpactioncontext)
    {
        try
        {
            if(redirect)
                httpactioncontext.getResponse().sendRedirect(path);
            httpactioncontext.getServlet().getServletContext().getRequestDispatcher(path).forward(httpactioncontext.getRequest(), httpactioncontext.getResponse());
        }
        catch(IOException ioexception)
        {
            throw new ActionPerformingError(ioexception);
        }
        catch(ServletException servletexception)
        {
            throw new ActionPerformingError(servletexception);
        }
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setPath(String s)
    {
        path = s;
    }

    public void setRedirect(boolean flag)
    {
        redirect = flag;
    }

    private String name;
    private String path;
    private boolean redirect;
}