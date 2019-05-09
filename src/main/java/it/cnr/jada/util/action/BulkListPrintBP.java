package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Dictionary;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.math.*;
// Referenced classes of package it.cnr.jada.util.action:
//			  AbstractPrintBP

public class BulkListPrintBP extends AbstractPrintBP
	implements Serializable
{

	public BulkListPrintBP()
	{
	}

	public Dictionary getColumns()
	{
		return columns;
	}

	public RemoteIterator getIterator()
	{
		return iterator;
	}

	public String getTitle()
	{
		return title;
	}

	public void print(PageContext pagecontext)
		throws ServletException, IOException, BusinessProcessException
	{
		try
		{
			if(title != null)
			{
				pagecontext.getOut().println("<H1>");
				pagecontext.getOut().println(title);
				pagecontext.getOut().println("</H1>");
			}
			pagecontext.getOut().println("<table class=\"w-100\">");
			JSPUtils.printableTable(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, Integer.parseInt(Config.getHandler().getProperty(getClass(), "maxRows")));
			pagecontext.getOut().println("</table>");
		}
		finally
		{
			try
			{
				EJBCommonServices.closeRemoteIterator(pagecontext.getSession(), iterator);
			}
			catch(RemoteException _ex) { }
		}
	}
	public int numeriPagina(PageContext pagecontext)
		throws ServletException, IOException, BusinessProcessException
	{
	  int j = iterator.countElements();            
	  BigDecimal pagina = new BigDecimal(j);
	  pagina = pagina.divide(new BigDecimal(20),0,BigDecimal.ROUND_UP);
	  return pagina.intValue();      	
	}	
	public void printExcel(PageContext pagecontext)
		throws ServletException, IOException, BusinessProcessException
	{
		try
		{
			if(title != null)
			{
				pagecontext.getOut().println("<H1>");
				pagecontext.getOut().println(title);
				pagecontext.getOut().println("</H1>");
			}
			pagecontext.getOut().println("<table>");
			JSPUtils.printableTable(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, -1, "H3");
			pagecontext.getOut().println("</table>");
		}
		finally
		{
			try
			{
				EJBCommonServices.closeRemoteIterator(pagecontext.getSession(), iterator);
			}
			catch(RemoteException _ex) { }
		}
	}

	public void printExcel(PageContext pagecontext,int page)
		throws ServletException, IOException, BusinessProcessException
	{
		try
		{
			if(title != null)
			{
				pagecontext.getOut().println("<H1>");
				pagecontext.getOut().println(title);
				pagecontext.getOut().println("</H1>");
			}
			pagecontext.getOut().println("<table>");
			JSPUtils.printableTablePage(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, page);
			pagecontext.getOut().println("</table>");
		}
		finally
		{
			try
			{
				EJBCommonServices.closeRemoteIterator(pagecontext.getSession(),iterator);
			}
			catch(RemoteException _ex) { }
		}
	}

	public void setColumns(Dictionary dictionary)
	{
		columns = dictionary;
	}

	public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator)
		throws RemoteException
	{
		iterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
	}

	public void setTitle(String s)
	{
		title = s;
	}

	private RemoteIterator iterator;
	private Dictionary columns;
	private String title;
}