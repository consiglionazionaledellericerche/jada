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

package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteIteratorEnumeration;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Dictionary;
// Referenced classes of package it.cnr.jada.util.action:
//			  AbstractPrintBP

public class BulkListPrintBP extends AbstractPrintBP
        implements Serializable {

    private RemoteIterator iterator;
    private Dictionary columns;
    private String title;

    public BulkListPrintBP() {
    }

    public Dictionary getColumns() {
        return columns;
    }

    public void setColumns(Dictionary dictionary) {
        columns = dictionary;
    }

    public RemoteIterator getIterator() {
        return iterator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void print(PageContext pagecontext)
            throws ServletException, IOException, BusinessProcessException {
        try {
            if (title != null) {
                pagecontext.getOut().println("<H1>");
                pagecontext.getOut().println(title);
                pagecontext.getOut().println("</H1>");
            }
            pagecontext.getOut().println("<table class=\"w-100\">");
            JSPUtils.printableTable(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, Integer.parseInt(Config.getHandler().getProperty(getClass(), "maxRows")));
            pagecontext.getOut().println("</table>");
        } finally {
            try {
                EJBCommonServices.closeRemoteIterator(pagecontext.getSession(), iterator);
            } catch (RemoteException _ex) {
            }
        }
    }

    public int numeriPagina(PageContext pagecontext)
            throws ServletException, IOException, BusinessProcessException {
        int j = iterator.countElements();
        BigDecimal pagina = new BigDecimal(j);
        pagina = pagina.divide(new BigDecimal(20), 0, BigDecimal.ROUND_UP);
        return pagina.intValue();
    }

    public void printExcel(PageContext pagecontext)
            throws ServletException, IOException, BusinessProcessException {
        try {
            if (title != null) {
                pagecontext.getOut().println("<H1>");
                pagecontext.getOut().println(title);
                pagecontext.getOut().println("</H1>");
            }
            pagecontext.getOut().println("<table>");
            JSPUtils.printableTable(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, -1, "H3");
            pagecontext.getOut().println("</table>");
        } finally {
            try {
                EJBCommonServices.closeRemoteIterator(pagecontext.getSession(), iterator);
            } catch (RemoteException _ex) {
            }
        }
    }

    public void printExcel(PageContext pagecontext, int page)
            throws ServletException, IOException, BusinessProcessException {
        try {
            if (title != null) {
                pagecontext.getOut().println("<H1>");
                pagecontext.getOut().println(title);
                pagecontext.getOut().println("</H1>");
            }
            pagecontext.getOut().println("<table>");
            JSPUtils.printableTablePage(pagecontext, null, new RemoteIteratorEnumeration(iterator), columns, page);
            pagecontext.getOut().println("</table>");
        } finally {
            try {
                EJBCommonServices.closeRemoteIterator(pagecontext.getSession(), iterator);
            } catch (RemoteException _ex) {
            }
        }
    }

    public void setIterator(ActionContext actioncontext, RemoteIterator remoteiterator)
            throws RemoteException {
        iterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
    }
}