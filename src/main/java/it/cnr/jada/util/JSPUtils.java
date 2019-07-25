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

package it.cnr.jada.util;

import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;

public class JSPUtils
        implements Serializable {

    public JSPUtils() {
    }

    public static void button(JspWriter jspwriter, String s, String s1)
            throws IOException, ServletException {
        button(jspwriter, s, null, s1);
    }

    public static void button(JspWriter jspwriter, String s, String s1, String s2)
            throws IOException, ServletException {
        button(jspwriter, s, s1, s2, null);
    }

    public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3)
            throws IOException, ServletException {
        jspwriter.print("<a class=\"");
        if (s2 != null)
            jspwriter.print("Button");
        else
            jspwriter.print("DisabledButton");
        jspwriter.print("\"");
        if (s3 != null) {
            jspwriter.print(" style=\"");
            jspwriter.print(s3);
            jspwriter.print('"');
        }
        if (s2 != null) {
            jspwriter.print(" href=\"");
            jspwriter.print(s2);
            jspwriter.print("\"");
            jspwriter.print(" onclick=\"return disableDblClick()\"");
        }
        jspwriter.print("><img align=\"middle\" class=\"");
        if (s2 != null)
            jspwriter.print("Button");
        else
            jspwriter.print("DisabledButton");
        jspwriter.print("\" src=\"");
        jspwriter.print(s);
        jspwriter.print("\">");
        if (s1 != null) {
            jspwriter.print("<br>");
            jspwriter.print(s1);
        }
        jspwriter.print("</a>");
    }

    public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean flag)
            throws IOException, ServletException {
        if (flag)
            button(jspwriter, s, s2, s3, s4);
        else
            button(jspwriter, s1, s2, null, s4);
    }

    public static void button(JspWriter jspwriter, String s, String s1, String s2, String s3, boolean flag)
            throws IOException, ServletException {
        if (flag)
            button(jspwriter, s, s2, s3);
        else
            button(jspwriter, s1, s2, null);
    }

    public static void button(PageContext pagecontext, String s, String s1)
            throws IOException, ServletException {
        button(pagecontext.getOut(), s, s1);
    }

    public static String getAppRoot(HttpServletRequest httpservletrequest) {
        String s = httpservletrequest.getRequestURI().substring(0, httpservletrequest.getRequestURI().indexOf(httpservletrequest.getServletPath()));
        if (!s.startsWith("/"))
            s = "/" + s;
        if (!s.endsWith("/"))
            s = s + "/";
        return s;
    }

    public static void include(PageContext pagecontext, String s)
            throws IOException, ServletException {
        pagecontext.getOut().flush();
        pagecontext.include(s);
    }

    public static void printBaseUrl(PageContext pagecontext)
            throws IOException {
        pagecontext.getOut().print("<BASE href=\"");
        pagecontext.getOut().print(pagecontext.getRequest().getScheme());
        pagecontext.getOut().print("://");
        pagecontext.getOut().print(pagecontext.getRequest().getServerName());
        pagecontext.getOut().print(':');
        pagecontext.getOut().print(pagecontext.getRequest().getServerPort());
        pagecontext.getOut().print(getAppRoot((HttpServletRequest) pagecontext.getRequest()));
        pagecontext.getOut().println("\">");
    }

    public static void scrollSupport(PageContext pagecontext)
            throws IOException {
        HttpServletRequest httpservletrequest = (HttpServletRequest) pagecontext.getRequest();
        String s = httpservletrequest.getRequestURI();
        JspWriter jspwriter = pagecontext.getOut();
        if (s.equals(httpservletrequest.getParameter("scrolledpage"))) {
            jspwriter.println("<script language=\"JavaScript\">");
            jspwriter.println("function scroll() {");
            jspwriter.println("\twindow.scrollTo(");
            jspwriter.print(httpservletrequest.getParameter("scrollx"));
            jspwriter.println(",");
            jspwriter.print(httpservletrequest.getParameter("scrolly"));
            jspwriter.println(");");
            jspwriter.println("}");
            jspwriter.println("window.onload=scroll;");
            jspwriter.println("</script>");
        }
        jspwriter.println("<input type=hidden name=\"scrollx\">");
        jspwriter.println("<input type=hidden name=\"scrolly\">");
        jspwriter.print("<input type=hidden name=\"scrolledpage\" value=\"");
        jspwriter.print(s);
        jspwriter.println("\">");
    }

    public static void tabbed(PageContext pagecontext, String s, String as[][], String s1, String s2)
            throws IOException, ServletException {
        tabbed(pagecontext, s, as, s1, s2, null, null);
    }

    public static void tabbed(PageContext pagecontext, String s, String as[][], String s1, String s2, String s3, String s4)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.print("<input type=\"hidden\" name=\"");
        jspwriter.print(s);
        jspwriter.println("\">");
        jspwriter.print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"");
        jspwriter.print(s2);
        jspwriter.print('"');
        if (s3 != null || s4 != null) {
            jspwriter.print(" style=\"");
            if (s3 != null) {
                jspwriter.print("width:");
                jspwriter.print(s3);
                jspwriter.print(";");
            }
            if (s4 != null) {
                jspwriter.print("height:");
                jspwriter.print(s4);
                jspwriter.print(";");
            }
            jspwriter.print('"');
        }
        jspwriter.println(">");
        jspwriter.println("\t<tr><td>");
        jspwriter.println("\t\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">");
        jspwriter.println("\t\t\t<tr>");
        String s5 = null;
        for (int i = 0; i < as.length; i++) {
            String[] as1 = as[i];
            boolean flag = as1[0].equals(s1);
            jspwriter.print("\t\t\t\t<td nowrap class=\"");
            jspwriter.print(flag ? "TabOn" : "TabOff");
            jspwriter.print("\"><div class=\"");
            jspwriter.print(flag ? "TabOn" : "TabOff");
            jspwriter.print("\"><a class=\"TabLabel\"");
            if (flag) {
                s5 = as1[2];
            } else {
                jspwriter.print("href=\"javascript:doTab('");
                jspwriter.print(s);
                jspwriter.print("','");
                jspwriter.print(as1[0]);
                jspwriter.print("')\"");
            }
            jspwriter.print(">&nbsp;");
            jspwriter.print(as1[1]);
            jspwriter.println("&nbsp;</a></div></td>");
        }

        jspwriter.println("\t\t\t\t<td width=\"100%\" class=\"TabOff\">&nbsp;</td>");
        jspwriter.println("\t\t\t</tr>");
        jspwriter.println("\t\t</table>");
        jspwriter.println("\t</td></tr>");
        jspwriter.println("\t<tr><td class=\"TabPage\" valign=\"top\">");
        if (s5 != null) {
            jspwriter.flush();
            pagecontext.include(s5);
        }
        jspwriter.println("\t</td></tr>");
        jspwriter.println("</table");
    }

    public static void table(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, String s1, String as[][])
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        int i = 0;
        jspwriter.print("<input type=\"hidden\" name=\"rowIndex\">");
        jspwriter.println("<tr>");
        for (Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print("</td>")) {
            ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration1.nextElement();
            jspwriter.print("<td");
            columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
            jspwriter.print(">");
            columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
        }

        for (int j = 0; j < as.length; j++)
            jspwriter.println("<td class=\"TableHeader\">&nbsp;</td>");

        jspwriter.print("</tr>");
        for (Enumeration enumeration2 = enumeration; enumeration2.hasMoreElements(); ) {
            Object obj = enumeration2.nextElement();
            boolean flag = s1 != null && (obj instanceof OggettoBulk) && ((OggettoBulk) obj).isOperabile();
            jspwriter.println("<tr>");
            for (Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>")) {
                ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty) enumeration3.nextElement();
                jspwriter.print("<td");
                columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
                jspwriter.print(">");
                if (flag) {
                    jspwriter.print("<a border=0 href=\"javascript:");
                    jspwriter.print(s1);
                    jspwriter.print("('");
                    jspwriter.print(i);
                    jspwriter.print("','");
                    jspwriter.print(s);
                    jspwriter.print("')\" class=\"ListItem\">");
                }
                columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
                if (flag)
                    jspwriter.print("</a>");
            }

            for (int k = 0; k < as.length; k++) {
                String[] as1 = as[k];
                jspwriter.print("<td><a class=\"buttons\" href=\"javascript:");
                jspwriter.print(as1[1]);
                jspwriter.print("('");
                jspwriter.print(i);
                jspwriter.print("')\"><img src=\"");
                jspwriter.print(as1[0]);
                jspwriter.println("\"></a></td>");
            }

            jspwriter.println("</tr>");
            i++;
        }

    }

    public static void table(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, String s1, String as[][], int i)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        int j = 0;
        jspwriter.print("<input type=\"hidden\" name=\"rowIndex\">");
        jspwriter.println("<tr>");
        for (Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print("</td>")) {
            ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration1.nextElement();
            jspwriter.print("<td");
            columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
            jspwriter.print(">");
            columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
        }

        for (int k = 0; k < as.length; k++)
            jspwriter.println("<td class=\"TableHeader\">&nbsp;</td>");

        jspwriter.print("</tr>");
        for (Enumeration enumeration2 = enumeration; enumeration2.hasMoreElements(); ) {
            Object obj = enumeration2.nextElement();
            boolean flag = s1 != null && (obj instanceof OggettoBulk) && ((OggettoBulk) obj).isOperabile();
            jspwriter.print("<tr");
            if (i == j)
                jspwriter.print(" bgcolor=\"LightBlue\"");
            jspwriter.println(">");
            for (Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>")) {
                ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty) enumeration3.nextElement();
                jspwriter.print("<td");
                columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
                jspwriter.print(">");
                if (flag) {
                    jspwriter.print("<a border=0 href=\"javascript:");
                    jspwriter.print(s1);
                    jspwriter.print("('");
                    jspwriter.print(j);
                    jspwriter.print("','");
                    jspwriter.print(s);
                    jspwriter.print("')\" class=\"ListItem\">");
                }
                columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
                if (flag)
                    jspwriter.print("</a>");
            }

            for (int l = 0; l < as.length; l++) {
                String[] as1 = as[l];
                jspwriter.print("<td><a class=\"buttons\" href=\"javascript:");
                jspwriter.print(as1[1]);
                jspwriter.print("('");
                jspwriter.print(j);
                jspwriter.print("')\"><img src=\"");
                jspwriter.print(as1[0]);
                jspwriter.println("\"></a></td>");
            }

            jspwriter.println("</tr>");
            j++;
        }

    }

    public static void table(PageContext pagecontext, String s, Enumeration enumeration, Dictionary dictionary, String s1, String as[][], int i, boolean flag,
                             String s2)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        int j = 0;
        jspwriter.println("<tr>");
        if (flag) {
            jspwriter.print("<td class=\"TableHeader\" align=\"center\">");
            if (s2 != null) {
                jspwriter.print("<img src=\"");
                jspwriter.print(s2);
                jspwriter.print("\">");
            }
            jspwriter.print("</td>");
        }
        for (Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print("</td>")) {
            ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration1.nextElement();
            jspwriter.print("<td");
            columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
            jspwriter.print(">");
            columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
        }

        if (as != null) {
            for (int k = 0; k < as.length; k++)
                jspwriter.println("<td class=\"TableHeader\">&nbsp;</td>");

        }
        jspwriter.print("</tr>");
        for (Enumeration enumeration2 = enumeration; enumeration2.hasMoreElements(); ) {
            Object obj = enumeration2.nextElement();
            boolean flag1 = s1 != null && (obj instanceof OggettoBulk) && ((OggettoBulk) obj).isOperabile();
            jspwriter.print("<tr");
            if (i == j)
                jspwriter.print(" bgcolor=\"LightBlue\"");
            jspwriter.println(">");
            if (flag1 && flag) {
                jspwriter.print("<td><input type=\"checkbox\" name=\"");
                jspwriter.print(s);
                jspwriter.print(".selection.");
                jspwriter.print(j);
                jspwriter.println("\"></td>");
            }
            for (Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>")) {
                ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty) enumeration3.nextElement();
                jspwriter.print("<td");
                columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
                jspwriter.print(">");
                if (flag1) {
                    jspwriter.print("<a border=0 href=\"javascript:");
                    jspwriter.print(s1);
                    jspwriter.print("('");
                    jspwriter.print(j);
                    jspwriter.print("','");
                    jspwriter.print(s);
                    jspwriter.print("')\" class=\"ListItem\">");
                }
                columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
                if (flag1)
                    jspwriter.print("</a>");
            }

            if (as != null) {
                for (int l = 0; l < as.length; l++) {
                    String[] as1 = as[l];
                    jspwriter.print("<td><a class=\"buttons\" href=\"javascript:");
                    jspwriter.print(as1[1]);
                    jspwriter.print("('");
                    jspwriter.print(j);
                    jspwriter.print("')\"><img src=\"");
                    jspwriter.print(as1[0]);
                    jspwriter.println("\"></a></td>");
                }

            }
            jspwriter.println("</tr>");
            j++;
        }

    }

    public static void table(PageContext pagecontext, Enumeration enumeration, Dictionary dictionary, String s, String as[][])
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        int i = 0;
        jspwriter.println("<tr>");
        for (Enumeration enumeration1 = dictionary.elements(); enumeration1.hasMoreElements(); jspwriter.print("</td>")) {
            ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration1.nextElement();
            jspwriter.print("<td");
            columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
            jspwriter.print(">");
            columnfieldproperty.writeLabel(jspwriter, null, HttpActionContext.isFromBootstrap(pagecontext));
        }

        for (int j = 0; j < as.length; j++)
            jspwriter.println("<td class=\"TableHeader\">&nbsp;</td>");

        jspwriter.print("</tr>");
        for (Enumeration enumeration2 = enumeration; enumeration2.hasMoreElements(); ) {
            Object obj = enumeration2.nextElement();
            boolean flag = s != null && (obj instanceof OggettoBulk) && ((OggettoBulk) obj).isOperabile();
            jspwriter.println("<tr>");
            for (Enumeration enumeration3 = dictionary.elements(); enumeration3.hasMoreElements(); jspwriter.println("</td>")) {
                ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty) enumeration3.nextElement();
                jspwriter.print("<td");
                columnfieldproperty1.writeColumnStyle(jspwriter, null, "TableColumn");
                jspwriter.print(">");
                if (flag) {
                    jspwriter.print("<a border=0 href=\"javascript:");
                    jspwriter.print(s);
                    jspwriter.print("('");
                    jspwriter.print(i);
                    jspwriter.print("')\" class=\"ListItem\">");
                }
                columnfieldproperty1.writeReadonlyText(jspwriter, obj, "ListItem", null);
                if (flag)
                    jspwriter.print("</a>");
            }

            for (int k = 0; k < as.length; k++) {
                String[] as1 = as[k];
                jspwriter.print("<td><a class=\"buttons\" href=\"javascript:");
                jspwriter.print(as1[1]);
                jspwriter.print("('");
                jspwriter.print(i);
                jspwriter.print("')\"><img src=\"");
                jspwriter.print(as1[0]);
                jspwriter.println("\"></a></td>");
            }

            jspwriter.println("</tr>");
            i++;
        }

    }

    public static void toolbar(PageContext pagecontext, String as[][])
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<table class=\"buttons\">");
        jspwriter.println("\t<tr>");
        for (int i = 0; i < as.length; i++) {
            jspwriter.print("\t\t<td width=\"64\"><a class=\"buttons\" href=\"javascript:submitForm('");
            jspwriter.print(as[i][2]);
            jspwriter.print("')\"><img src=\"");
            jspwriter.print(as[i][1]);
            jspwriter.println("\"></a></td>");
        }

        jspwriter.println("\t</tr>");
        jspwriter.println("\t<tr>");
        for (int j = 0; j < as.length; j++) {
            jspwriter.print("\t\t<td>");
            jspwriter.print(as[j][0]);
            jspwriter.println("</td>");
        }

        jspwriter.println("\t</tr>");
        jspwriter.println("</table>");
    }

    public static void toolbarButton(PageContext pagecontext, String s, String s1, boolean flag)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.print("<td");
        if (flag)
            jspwriter.print(" class=\"VSeparator\"");
        jspwriter.print(">");
        button(pagecontext, s, s1);
        jspwriter.print("</td>");
    }
}