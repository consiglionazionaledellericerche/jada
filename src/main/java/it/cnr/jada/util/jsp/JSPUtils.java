package it.cnr.jada.util.jsp;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.RigaAlbero;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
public class JSPUtils
	implements Serializable
{

	public JSPUtils()
	{
	}

	public static String buildAbsoluteUrl(HttpServletRequest httpservletrequest, String s, String s1)
	{
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append(httpservletrequest.getScheme());
		stringbuffer.append("://");
		stringbuffer.append(httpservletrequest.getServerName());
		stringbuffer.append(':');
		stringbuffer.append(httpservletrequest.getServerPort());
		stringbuffer.append(getAppRoot(httpservletrequest));
		if(s != null && s.length() != 0)
		{
			stringbuffer.append(s);
			if(!s.endsWith("/"))
				stringbuffer.append('/');
		}
		stringbuffer.append(s1);
		return stringbuffer.toString();
	}

	public static String buildAbsoluteUrl(PageContext pagecontext, String s, String s1)
	{
		return buildAbsoluteUrl((HttpServletRequest)pagecontext.getRequest(), s, s1);
	}

	public static void button(JspWriter jspwriter, String s, String s1, boolean isBootstrap)
		throws IOException
	{
		Button.write(jspwriter, s, null, s1, isBootstrap);
	}
	public static void buttonWithTitle(JspWriter jspwriter, String s, String s1, String title, boolean isBootstrap)
		throws IOException
	{
		Button.write(jspwriter, null, s, true, null, 1, s1, null, title, null, isBootstrap);
	}
	public static void button(JspWriter jspwriter, String img, String label, String href, boolean isBootstrap)
		throws IOException
	{
		Button.write(jspwriter, img, label, href, null, isBootstrap);
	}

	public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, boolean isBootstrap)
		throws IOException
	{
		Button.write(jspwriter, s, s1, s2, s3, isBootstrap);
	}

	public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean flag, boolean isBootstrap)
		throws IOException
	{
		if(flag)
			Button.write(jspwriter, s, s2, s3, s4, isBootstrap);
		else
			Button.write(jspwriter, s1, s2, null, s4, isBootstrap);
	}
	public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean flag, String accessKey, boolean isBootstrap)
		throws IOException
	{
		if(flag)
			Button.writeWhithAccessKey(jspwriter, s, s2, s3, s4, accessKey, isBootstrap);
		else
			Button.writeWhithAccessKey(jspwriter, s1, s2, null, s4, accessKey, isBootstrap);
	}

	public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, boolean flag, boolean isBootstrap)
		throws IOException
	{
		if(flag)
			Button.write(jspwriter, s, s2, s3, isBootstrap);
		else
			Button.write(jspwriter, s1, s2, null, isBootstrap);
	}

	public static void button(PageContext pagecontext, String s, String s1, boolean isBootstrap)
		throws IOException
	{
		Button.write(pagecontext.getOut(), s, s1, isBootstrap);
	}

	public static String encodeHtmlString(String s)
	{
		StringBuffer stringbuffer = new StringBuffer(s.length() + 10);
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			switch(c)
			{
			case 34: // '"'
				stringbuffer.append("&quot;");
				break;

/*
			case 39: // '\''
				stringbuffer.append("&#39;");
				break;
*/				
            /*
             * Remmato per permettere il tag <BR> all'interno
             * delle label XML
             * Marco Spasiano 11/01/2005
             */
			/*case 60: // '<'
				stringbuffer.append("&lt;");
				break;

			case 62: // '>'
				stringbuffer.append("&gt;");
				break;*/

			default:
				stringbuffer.append(c);
				break;
			}
		}

		return stringbuffer.toString();
	}

	public static String encodeJavascriptString(String s)
	{
		StringBuffer stringbuffer = new StringBuffer(s.length() + 10);
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			switch(c)
			{
			case 34: // '"'
				stringbuffer.append("\\\"");
				break;

			case 10: // '\n'
				stringbuffer.append("\\n");
				break;

			default:
				stringbuffer.append(c);
				break;

			case 13: // '\r'
				break;
			}
		}

		return stringbuffer.toString();
	}

	public static String getApplicationId(HttpServletRequest httpservletrequest)
	{
		try
		{
			return "https://" + InetAddress.getLocalHost().getHostName() + "/" + getContextName(httpservletrequest);
		}
		catch(UnknownHostException _ex)
		{
			return "http://127.0.0.1/" + getContextName(httpservletrequest);
		}
	}

	public static String getAppRoot(HttpServletRequest httpservletrequest)
	{
		String s = httpservletrequest.getRequestURI().substring(0, httpservletrequest.getRequestURI().indexOf(httpservletrequest.getServletPath()));
		if(!s.startsWith("/"))
			s = "/" + s;
		if(!s.endsWith("/"))
			s = s + "/";
		return s;
	}

	public static String getAppServerUrl(HttpServletRequest httpservletrequest)
		throws IOException
	{
		return "//" + httpservletrequest.getServerName() + getAppRoot((HttpServletRequest)httpservletrequest);
	}

	public static String getAppServerUrl(PageContext pagecontext)
		throws IOException
	{
		return "//" + pagecontext.getRequest().getServerName() + getAppRoot((HttpServletRequest)pagecontext.getRequest());
	}

	public static String getBaseUrl(HttpServletRequest httpservletrequest)
		throws IOException
	{
		return httpservletrequest.getScheme() + "://" + httpservletrequest.getServerName() + ':' + httpservletrequest.getServerPort() + getAppRoot(httpservletrequest);
	}

	public static String getBaseUrl(PageContext pagecontext)
		throws IOException
	{
		return pagecontext.getRequest().getScheme() + "://" + pagecontext.getRequest().getServerName() + ':' + pagecontext.getRequest().getServerPort() + getAppRoot((HttpServletRequest)pagecontext.getRequest());
	}

	public static String getClusterAppRoot(HttpServletRequest httpservletrequest)
	{
		String s = Config.getHandler().getProperty("Cluster.url");
		if(s == null)
			s = getAppRoot(httpservletrequest) + "Login.do";
		else
		if(s.startsWith("//"))
			s = httpservletrequest.getScheme() + ":" + s;
		return s;
	}

	public static String getContextName(HttpServletRequest httpservletrequest)
	{
		String s = httpservletrequest.getRequestURI().substring(0, httpservletrequest.getRequestURI().indexOf(httpservletrequest.getServletPath()));
		if(s.endsWith("/"))
			s = s.substring(0, s.length() - 1);
		s = s.substring(s.lastIndexOf('/') + 1, s.length());
		return s;
	}

	public static void include(PageContext pagecontext, String s)
		throws IOException, ServletException
	{
		pagecontext.getOut().flush();
		pagecontext.include(s);
	}

	public static String mergeStyles(String s, String s1)
	{
		if(s == null)
			return s1;
		if(s1 == null)
		{
			return s;
		} else
		{
			StringBuffer stringbuffer = new StringBuffer(s.length() + s1.length() + 1);
			stringbuffer.append(s);
			stringbuffer.append(';');
			stringbuffer.append(s1);
			return stringbuffer.toString();
		}
	}

	public static String mergeStyles(String s, String s1, String s2)
	{
		if(s == null)
			return mergeStyles(s1, s2);
		if(s1 == null)
			return mergeStyles(s, s2);
		if(s2 == null)
		{
			return mergeStyles(s, s1);
		} else
		{
			StringBuffer stringbuffer = new StringBuffer(s.length() + s1.length() + s2.length() + 2);
			stringbuffer.append(s);
			stringbuffer.append(';');
			stringbuffer.append(s1);
			stringbuffer.append(';');
			stringbuffer.append(s2);
			return stringbuffer.toString();
		}
	}

	public static void printableTable(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary)
		throws IOException, ServletException
	{
		printableTable(pagecontext, s, enumeration, dictionary, -1);
	}
	public static void printableTable(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, int i, String columnHeader)
		throws IOException, ServletException
	{
		String openHeader, closeHeader = "";
		if (columnHeader != null)
		{
		   openHeader = "<"+ columnHeader + ">";
		   closeHeader = "</"+ columnHeader + ">";	
		}	
		JspWriter jspwriter = pagecontext.getOut();
		int j = 0;
		jspwriter.println("<tr>");
		for(Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print(closeHeader + "</td>"))
		{
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
			jspwriter.print("<td");
			columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
			jspwriter.print(">"+closeHeader);
			columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
		}

		jspwriter.print("</tr>");
		for(Enumeration enumeration2 = enumeration; (i < 0 || j < i) && enumeration2.hasMoreElements(); j++)
		{
			Object obj = enumeration2.nextElement();
			jspwriter.println("<tr>");
			for(Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>"))
			{
				ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
				jspwriter.print("<td");
				columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
				jspwriter.print(">");
				columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
			}

			jspwriter.println("</tr>");
		}
    
	}	
	public static void printableTable(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, int i)
		throws IOException, ServletException
	{
	   printableTable(pagecontext, s, enumeration, dictionary, i, null);
	}

	public static void printableTablePage(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, int page)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		int j = 0;
		jspwriter.println("<tr>");
		for(Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print("</H4></td>"))
		{
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
			jspwriter.print("<td");
			columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
			jspwriter.print("><H4>");
			columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
		}

		jspwriter.print("</tr>");
		Enumeration enumeration2 = enumeration;
		for (;j < (20*page);j++){
		  enumeration2.nextElement();         
		}  
		for(; j<=((20*(page+1))-1) && enumeration2.hasMoreElements(); j++)
		{
			Object obj = enumeration2.nextElement();
			jspwriter.println("<tr>");
			for(Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>"))
			{
				ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
				jspwriter.print("<td");
				columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
				jspwriter.print(">");
				columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
			}
			jspwriter.println("</tr>");
		}

	}

	public static void printBaseUrl(PageContext pagecontext)
		throws IOException
	{
		pagecontext.getOut().print("<BASE href=\"");
		pagecontext.getOut().print(getAppRoot((HttpServletRequest)pagecontext.getRequest()));
		pagecontext.getOut().println("\">");
		
		pagecontext.getOut().println("<!--[if IE]><!--><script type=\"text/javascript\">");
		pagecontext.getOut().println("// Fix for IE ignoring relative base tags.");
		pagecontext.getOut().println("	var baseTag = document.getElementsByTagName('BASE')[0];");
		pagecontext.getOut().print("baseTag.href = window.location.protocol + \"//\" + window.location.hostname + (window.location.port ? ':' + window.location.port: '') + \"");
		pagecontext.getOut().print(getAppRoot((HttpServletRequest)pagecontext.getRequest()));
		pagecontext.getOut().println("\";");		
		pagecontext.getOut().println("</script><!--<![endif]-->");
		
		printCloneUrl(pagecontext);
		printMetaData(pagecontext);
	}

	public static void printCloneUrl(PageContext pagecontext)
		throws IOException
	{
		pagecontext.getOut().println("<!-- Servlet Engine Clone Info");
		InetAddress inetaddress = InetAddress.getLocalHost();
		inetaddress.hashCode();
		pagecontext.getOut().println("\tHost Address\t:\t" + inetaddress.getHostAddress());
		pagecontext.getOut().println("\tHost Name\t:\t" + inetaddress.getHostName());
		pagecontext.getOut().println("-->");
	}
	public static void printMetaData(PageContext pagecontext) throws IOException{
		pagecontext.getOut().println("<META HTTP-EQUIV=\"expires\" CONTENT=\"0\">");
		pagecontext.getOut().println("<META HTTP-EQUIV=\"pragma\" CONTENT=\"No-cache\">");
		pagecontext.getOut().println("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache\">");
	}
	public static void printJavascriptString(JspWriter jspwriter, String s)
		throws IOException
	{
		if(s == null)
		{
			jspwriter.print("null");
		} else
		{
			jspwriter.print('"');
			jspwriter.print(encodeJavascriptString(s));
			jspwriter.print('"');
		}
	}

	public static void scrollSupport(PageContext pagecontext)
		throws IOException
	{
		HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
		String s = httpservletrequest.getRequestURI();
		JspWriter jspwriter = pagecontext.getOut();
		if(s.equals(httpservletrequest.getParameter("requestor"))) {
			String s1 = httpservletrequest.getParameter("scrollx");
			String s2 = httpservletrequest.getParameter("scrolly");
			String s3 = httpservletrequest.getParameter("focusedElement");
			if(s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0)
				return;
			jspwriter.println("<script language=\"JavaScript\">");
			jspwriter.println("function scroll() {");
			jspwriter.print("\twindow.scrollTo(");
			jspwriter.print(httpservletrequest.getParameter("scrollx"));
			jspwriter.print(",");
			jspwriter.print(httpservletrequest.getParameter("scrolly"));
			jspwriter.println(");");
			if(s3 != null && s3.length() > 0)
			{
				jspwriter.print("setFocusOnInput('");
				jspwriter.print(s3);
				jspwriter.print("')");
			}
			jspwriter.println("}");
			jspwriter.println("addOnloadHandler(scroll,100)");
			jspwriter.println("</script>");
		} else {
            Optional.ofNullable(httpservletrequest)
                    .ifPresent(httpServletRequest ->  Optional.ofNullable(httpServletRequest.getSession(false))
                            .ifPresent(httpSession -> Optional.ofNullable(httpSession.getAttribute(HttpActionContext.CONTEXT_FOCUSED_ELEMENT))
                                    .filter(Map.class::isInstance)
                                    .map(Map.class::cast)
									.ifPresent(focusedElement -> {
                                        if (focusedElement.containsKey(s)) {
                                            try {
                                                jspwriter.println("<script language=\"JavaScript\">");
                                                jspwriter.println("function scroll() {");
                                                jspwriter.print("\twindow.scrollTo(");
                                                if (s.contains("form_lista.jsp")) {
                                                    jspwriter.println("0,0");
                                                } else {
                                                    jspwriter.println(focusedElement.get(s));
                                                }
                                                jspwriter.println(");");
                                                jspwriter.println("}");
                                                jspwriter.println("addOnloadHandler(scroll,100)");
                                                jspwriter.println("</script>");
                                            } catch (IOException e) {
                                                throw new DetailedRuntimeException(e);
                                            }
                                        }
                                    })
                            )
                    );
		}
		jspwriter.println("<input type=hidden name=\"scrollx\">");
		jspwriter.println("<input type=hidden name=\"scrolly\">");
		jspwriter.println("<input type=hidden name=\"focusedElement\">");
		jspwriter.print("<input type=hidden name=\"requestor\" value=\"");
		jspwriter.print(s);
		jspwriter.println("\">");
	}

	public static void tabbed(PageContext pagecontext, String s, String as[][], String s1, String s2)
		throws IOException, ServletException
	{
		tabbed(pagecontext, s, as, s1, s2, null, null);
	}

	public static void tabbed(PageContext pagecontext, String s, String as[][], String s1, String s2, String s3, String s4)
		throws IOException, ServletException
	{
		tabbed(pagecontext, s, as, s1, s2, s3, s4, true);
	}

	public static void tabbed(PageContext pagecontext, String s, String as[][], String s1, String s2, String s3, String s4, boolean flag)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.print("<!-- INIZIO TABBED ");
		jspwriter.print(s);
		jspwriter.println(" -->");
		jspwriter.print("<table cellspacing=\"0\" cellpadding=\"0\" align=\"");
		jspwriter.print(s2);
		jspwriter.print('"');
		if(s3 != null || s4 != null)
		{
			jspwriter.print(" style=\"");
			if(s3 != null)
			{
				jspwriter.print("width:");
				jspwriter.print(s3);
				jspwriter.print(";");
			}
			if(s4 != null)
			{
				jspwriter.print("height:");
				jspwriter.print(s4);
				jspwriter.print(";");
			}
			jspwriter.print('"');
		}
		jspwriter.println(">");
		jspwriter.println("<tr><td>");
		jspwriter.println("<table cellspacing=\"0\" cellpadding=\"0\" align=\"left\" width=\"100%\">");
		jspwriter.println("<tr valign=\"bottom\">");
		jspwriter.println("<td>&nbsp;</td>");
		String s5 = null;
		boolean flag1 = false;
		boolean flag3 = as[0][0].equals(s1);
		for(int i = 0; i < as.length;)
		{
			String as1[] = as[i++];
			boolean flag5 = flag3;
			flag3 = i < as.length && as[i][0].equals(s1);
			jspwriter.print("<td nowrap class=\"TabLabel\"");
			if(flag5)
				jspwriter.print(" style=\"font-weight: bold;border: 1px outset;border-bottom: 0px;padding: 3px\"");
			jspwriter.print(">");
			if(!flag5) {				
				jspwriter.print("<button ");
				String cssClass = "TabLabel btn-primary btn-block hand h6 text-white";
				if(!flag) {
					jspwriter.print("disabled ");
					cssClass = "TabLabel h6";
				}

				jspwriter.print("class=\"" + cssClass + "\" style=\"border: 1px outset;border-bottom: 0px;margin: 0px;");
				if(flag1)
					jspwriter.print("border-left: 0px;");
				if(flag3)
					jspwriter.print("border-right: 0px;");
				jspwriter.print("\"");
				if(flag) {
					jspwriter.print(" onclick=\"doTab('");
					jspwriter.print(s);
					jspwriter.print("','");
					jspwriter.print(as1[0]);
					jspwriter.print("')\"");
				}
				jspwriter.print(">");				
			} else {
				jspwriter.print("<span class=\"TabLabel h6 text-primary\">");
			}
			if(flag5)
				s5 = as1[2];
			jspwriter.print("&nbsp;");
			jspwriter.print(as1[1]);
			jspwriter.print("&nbsp;");
			if(!flag5) {
				jspwriter.print("</button>");
			} else {
				jspwriter.print("</span>");
			}
			jspwriter.println("</td>");
			flag1 = flag5;
		}

		jspwriter.println("<td width=\"100%\">&nbsp;</td>");
		jspwriter.println("</tr><tr>");
		if (HttpActionContext.isFromBootstrap(pagecontext)) {
			jspwriter.println("<td style=\"border-top: 1px outset;border-left: 1px outset\"></td>");
		} else {
			jspwriter.println("<td style=\"border-top: 1px outset;border-left: 1px outset\"><img src=\"img/spacer.gif\"></td>");			
		}
		flag1 = false;
		flag3 = as[0][0].equals(s1);
		for(int j = 0; j < as.length;)
		{
			String[] _tmp = as[j++];
			boolean flag4 = flag3;
			flag3 = j < as.length && as[j][0].equals(s1);
			jspwriter.print("<td nowrap ");
			if(!flag4)
				jspwriter.print(" style=\"border-top: 1px outset\"");
			if (HttpActionContext.isFromBootstrap(pagecontext)) {
				jspwriter.println("></td>");
			} else {
				jspwriter.println("><img src=\"img/spacer.gif\"></td>");				
			}
			boolean flag2 = flag4;
		}
		if (HttpActionContext.isFromBootstrap(pagecontext)) {
			jspwriter.println("<td style=\"border-top: 1px outset;border-right: 1px outset\"></td>");
		} else {
			jspwriter.println("<td style=\"border-top: 1px outset;border-right: 1px outset\"><img src=\"img/spacer.gif\"></td>");			
		}
		jspwriter.println("</tr>");
		jspwriter.println("</table>");
		jspwriter.println("</td></tr>");
		jspwriter.println("<tr height=\"100%\" valign=\"top\"><td style=\"border: 1px outset;border-top:0px;padding: 2px\">");
		jspwriter.print("<!-- INIZIO TAB PAGE ");
		jspwriter.print(s);
		jspwriter.println(" -->");
		if(s5 != null)
		{
			jspwriter.flush();
			pagecontext.include(s5);
		}
		jspwriter.print("<!-- FINE TAB PAGE ");
		jspwriter.print(s);
		jspwriter.println(" -->");
		jspwriter.println("</td></tr>");
		jspwriter.println("</table>");
		jspwriter.print("<!-- FINE TABBED ");
		jspwriter.print(s);
		jspwriter.println(" -->");
	}

	public static void toolbar(JspWriter jspwriter, Button abutton[], boolean aflag[], boolean isBootstrap)
		throws IOException, ServletException
	{
		jspwriter.println("<table class=\"ToolBar\" cellspacing=\"0\" cellpadding=\"2\">");
		jspwriter.println("<tr valign=\"top\" align=\"center\">");
		for(int i = 0; i < abutton.length; i++)
		{
			jspwriter.print("<td>");
			abutton[i].write(jspwriter, aflag[i], isBootstrap);
			jspwriter.println("</td>");
		}

		jspwriter.println("<td width=\"100%\">&nbsp;</td>");
		jspwriter.println("</tr>");
		jspwriter.println("</table>");
	}

	public static void toolbar(JspWriter jspwriter, Button abutton[], Object obj, boolean isBootstrap)
		throws IOException, ServletException
	{
		toolbar(jspwriter, abutton, obj, "&nbsp;", isBootstrap);
	}

	public static void toolbarBootstrap(JspWriter jspwriter, List<Button> abutton, Object obj) throws IOException, ServletException{
		toolbarBootstrap(jspwriter, abutton, obj, "&nbsp;");
	}

	public static void toolbarBootstrap(JspWriter jspwriter, List<Button> abutton, Object obj, String descrizione) throws IOException, ServletException {
        jspwriter.println("<div class=\"btn-group mr-2\" role=\"group\">");		
		for (Button button : abutton) {
			if(!button.isHidden(obj)){
				if (button.hasSeparator()) {
					jspwriter.println("</div>");		
			        jspwriter.println("<div class=\"btn-group mr-2\" role=\"group\">");
				}
				button.write(jspwriter, obj, true);
			}			
		}
		jspwriter.println("</div>");		
	}

	public static void toolbar(JspWriter jspwriter, Button abutton[], Object obj, String descrizione, boolean isBootstrap)
		throws IOException, ServletException
	{
		jspwriter.println("<table class=\"ToolBar\" cellspacing=\"0\" cellpadding=\"2\">");
		jspwriter.println("<tr valign=\"top\" align=\"center\">");
		for(int i = 0; i < abutton.length; i++)
			if(!abutton[i].isHidden(obj))
			{
				jspwriter.print("<td");
				if(abutton[i].hasSeparator())
					jspwriter.print(" class=\"VSeparator\"");
				jspwriter.print(">");
				abutton[i].write(jspwriter, obj, isBootstrap);
				jspwriter.println("</td>");
			}

		jspwriter.println("<td width=\"100%\">"+descrizione+"</td>");
		jspwriter.println("</tr>");
		jspwriter.println("</table>");
	}

	public static void toolbar(PageContext pagecontext, String as[][])
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.println("<table class=\"buttons\">");
		jspwriter.println("\t<tr>");
		for(int i = 0; i < as.length; i++)
		{
			jspwriter.print("\t\t<td width=\"64\"><a class=\"buttons\" href=\"javascript:submitForm('");
			jspwriter.print(as[i][2]);
			jspwriter.print("')\"><img src=\"");
			jspwriter.print(as[i][1]);
			jspwriter.println("\"></a></td>");
		}

		jspwriter.println("\t</tr>");
		jspwriter.println("\t<tr>");
		for(int j = 0; j < as.length; j++)
		{
			jspwriter.print("\t\t<td>");
			jspwriter.print(as[j][0]);
			jspwriter.println("</td>");
		}

		jspwriter.println("\t</tr>");
		jspwriter.println("</table>");
	}

	public static void toolbarButton(PageContext pagecontext, String s, String s1, String s2, boolean flag, boolean isBootstrap)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.print("<td");
		if(flag)
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		Button.write(pagecontext.getOut(), null, s, true, s1, 1, s2, null, null, null, isBootstrap);
		jspwriter.print("</td>");
	}

	public static void toolbarButton(PageContext pagecontext, String s, String s1, boolean flag, boolean isBootstrap)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.print("<td");
		if(flag)
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		/* Modifica effettuata il 01/10/2004 da Marco Spasiano
		 * per ovviare alla mancata esposizione
		 * dei numeri di pagina
		 */
		//Button.write(jspwriter, null, s, true, null, 1, s1, null, null, null);
		Button.write(jspwriter, null, null, true, s, 1, s1, null, null, null, isBootstrap);
		jspwriter.print("</td>");
	}

	public static void toolbarButton(PageContext pagecontext, String s, String s1, boolean flag, String s2, boolean isBootstrap)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.print("<td");
		if(flag)
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		Button.write(jspwriter, null, s, true, null, 1, s1, null, s2, null, isBootstrap);
		jspwriter.print("</td>");
	}

	public static void toolbarButton(PageContext pagecontext, String s, String s1, boolean flag, String s2, String buttonStyle, boolean isBootstrap)
			throws IOException, ServletException
		{
			JspWriter jspwriter = pagecontext.getOut();
			jspwriter.print("<td");
			if(flag)
				jspwriter.print(" class=\"VSeparator\"");
			jspwriter.print(">");
			Button.write(jspwriter, null, s, true, null, 1, s1, buttonStyle, s2, null, null, isBootstrap);
			jspwriter.print("</td>");
		}

	public static void treeBootstrap(JspWriter jspwriter, int i, String selezionaCondizione, String clausolaimg, int rigaSelezionata, Enumeration enumeration) throws IOException{
		int k = 0;
		jspwriter.println("<ul class=\"freesearch\">");
		for(Enumeration enumeration1 = enumeration; enumeration1.hasMoreElements();) {
			RigaAlbero rigaalbero = (RigaAlbero)enumeration1.nextElement();
			jspwriter.print("<li");
			if(rigaSelezionata == k)
				jspwriter.print(" class=\"SelectedRow\"");
			jspwriter.println(">");
			if(rigaalbero.getNodo().getObject() != null)
			{
				jspwriter.print("<a border=\"0\" href=\"javascript:");
				jspwriter.print(selezionaCondizione);
				jspwriter.print("('");
				jspwriter.print(k);
				jspwriter.print("')\">");
			} else
			{
				jspwriter.print("<span>");
			}
			jspwriter.print(Optional.ofNullable(clausolaimg).map(x -> "<i class=\"" + x + "\" aria-hidden=\"true\"></i> ").orElse(""));
			jspwriter.print(rigaalbero.getNodo().getDescrizioneNodo());
			if(rigaalbero.getNodo().getObject() != null)
				jspwriter.print("</a>");
			else
				jspwriter.print("</span>");
			jspwriter.println("</li>");
			k++;
		}
		jspwriter.println("</ul>");
	}
	public static void tree(JspWriter jspwriter, int i, String selezionaCondizione, String treejoinimg, String treejoinimg2, String treejoinimg3, String clausolaimg, String s5, 
			String s6, int j, Enumeration enumeration, boolean isBootstrap)
		throws IOException
	{
		boolean aflag[] = new boolean[i];
		int k = 0;
		for(Enumeration enumeration1 = enumeration; enumeration1.hasMoreElements();)
		{
			RigaAlbero rigaalbero = (RigaAlbero)enumeration1.nextElement();
			jspwriter.print("<tr");
			if(j == k)
				jspwriter.print(" class=\"SelectedRow\"");
			jspwriter.println(">");
			jspwriter.print("<td>");
			if(rigaalbero.getNodo().getObject() != null && s5 != null)
			{
				jspwriter.print("\t<input type=\"radio\" name=\"");
				jspwriter.print(s5);
				jspwriter.print("\" value=\"");
				jspwriter.print("numeroRiga");
				jspwriter.print("\"");
				if(j == k)
					jspwriter.print(" checked");
				if(s6 != null)
				{
					jspwriter.print(" onclick=\"javascript:");
					jspwriter.print(s6);
					jspwriter.print("('");
					jspwriter.print("numeroRiga");
					jspwriter.print("')\"");
				}
				jspwriter.print(">");
			}
			jspwriter.println("</td>");
			for(int l = 0; l < rigaalbero.getLivello() - 1; l++)
			{
				jspwriter.print("<TD>");
				if(!aflag[l]) {
					jspwriter.print(Optional.ofNullable(treejoinimg3).map(x -> "<img src=\"" + x + "\">").orElse(""));
				}
				jspwriter.print("</TD>");
			}

			if(rigaalbero.getLivello() > 0)
			{
				if(!rigaalbero.isUltimo())
				{
					jspwriter.print("<td>");
					jspwriter.print(Optional.ofNullable(treejoinimg2).map(x -> "<img src=\"" + x + "\">").orElse(""));
					jspwriter.println("</td>");
				} else {
					jspwriter.print("<TD>");
					jspwriter.print(Optional.ofNullable(treejoinimg).map(x -> "<img src=\"" + x + "\">").orElse(""));
					jspwriter.println("</td>");
				}
				aflag[rigaalbero.getLivello() - 1] = rigaalbero.isUltimo();
			}
			jspwriter.print("<td  colspan=\"");
			jspwriter.print((i - rigaalbero.getLivello()) + 1);
			jspwriter.print("\">");
			if (isBootstrap) {
				jspwriter.print(Optional.ofNullable(clausolaimg).map(x -> "<i class=\"" + x + "\" aria-hidden=\"true\"></i>").orElse(""));
			} else {
				jspwriter.print(Optional.ofNullable(clausolaimg).map(x -> "<img align=\"middle\" border=\"0\" src=\"" + x + "\">").orElse(""));				
			}
			if(rigaalbero.getNodo().getObject() != null)
			{
				jspwriter.print("<a border=\"0\" href=\"javascript:");
				jspwriter.print(selezionaCondizione);
				jspwriter.print("('");
				jspwriter.print(k);
				jspwriter.print("')\" class=\"TreeItem\">");
			} else
			{
				jspwriter.print("<span class=\"TreeItem\">");
			}
			jspwriter.print(rigaalbero.getNodo().getDescrizioneNodo());
			if(rigaalbero.getNodo().getObject() != null)
				jspwriter.print("</a>");
			else
				jspwriter.print("</span>");
			jspwriter.println("</td>");
			jspwriter.println("</tr>");
			k++;
		}

	}

	public static void waitPanel(JspWriter jspwriter, String s, String s1, String s2)
		throws IOException
	{
		jspwriter.print("<div id=\"");
		jspwriter.print(s);
		jspwriter.print("\" class=\"Window\" style=\"visibility: hidden; position: absolute; width: 100%;\">");
		if(s2 != null)
		{
			jspwriter.print("<img src=\"");
			jspwriter.print(s2);
			jspwriter.print("\">");
		}
		if(s1 != null)
			jspwriter.print(s1);
		jspwriter.println("</div>");
	}
}