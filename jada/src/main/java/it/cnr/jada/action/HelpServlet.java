package it.cnr.jada.action;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelpServlet extends HttpServlet
    implements Serializable
{

    public HelpServlet()
    {
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        try
        {
            Connection connection = EJBCommonServices.getConnection();
            try
            {
            	LoggableStatement statement = new LoggableStatement(connection,"SELECT HELP_URL FROM " + EJBCommonServices.getDefaultSchema()
            			+ "HELP_LKT WHERE PAGE = ?",true,this.getClass());
            	            	
                statement.setString(1, httpservletrequest.getPathInfo());
                ResultSet resultset = statement.executeQuery();
                
                LoggableStatement LSstr = new LoggableStatement(connection,"SELECT FLAG_MAN FROM " + EJBCommonServices.getDefaultSchema()+ "HELP_LKT WHERE PAGE = ?",true,this.getClass());
            	LSstr.setString(1,httpservletrequest.getPathInfo());
            	ResultSet RS = LSstr.executeQuery();
            	if(RS.next()) {
            		
            		if (resultset.next())
            			if (RS.getString(1).compareTo("Y")==0)
            				httpservletresponse.sendRedirect(resultset.getString(1));
            			else
		                    httpservletresponse.sendRedirect(JSPUtils.buildAbsoluteUrl(httpservletrequest, "", resultset.getString(1)));
            		else
            			httpservletrequest.getRequestDispatcher("/help_not_found.html").forward(httpservletrequest, httpservletresponse);      
            	}
				try{resultset.close();}catch( java.sql.SQLException e ){};
				try{statement.close();}catch( java.sql.SQLException e ){};
            }
            finally
            {
                connection.close();
            }
        }
        catch(Throwable throwable)
        {
            throw new ServletException(throwable);
        }
    }
}