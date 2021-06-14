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

import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.List;

public class CollapsableDetailCRUDController extends SimpleDetailCRUDController {

    public CollapsableDetailCRUDController(String s, Class class1, String s1, FormController formcontroller) {
        super(s, class1, s1, formcontroller);
    }

    public CollapsableDetailCRUDController(String s, Class class1, String s1, FormController formcontroller, boolean flag) {
        super(s, class1, s1, formcontroller, flag);
    }

    public void closeHTMLTable(PageContext pagecontext) throws IOException, ServletException{
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("</div>");
        jspwriter.println("</div>");
    }

    @Override
    protected void writeHTMLTable(PageContext pagecontext, String columnSet,
                                  boolean isAddButtonVisible, boolean isFilterButtonVisible, boolean isRemoveButtonVisible,
                                  String width, String height,
                                  boolean readonly, TableCustomizer tablecustomizer, List rows)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<div class=\"card "+ getBorderClass() + "\">");
        jspwriter.println("<div class=\"card-header d-flex\">");
        jspwriter.println("<a class=\""+ getTextClass() + " d-flex w-100\" onclick=\"submitForm('doToggle(" + getInputPrefix() + ")')\">");
        jspwriter.print("<span class=\"h4 mb-0\">" + getControllerName() + "</span>");
        jspwriter.print("<i aria-hidden=\"true\" class=\"ml-auto fa fa-2x " + getIconClassCollapsed() + "\"></i> ");
        jspwriter.print("</a>");
        jspwriter.println("</div>");
        jspwriter.println("<div class=\"card-block\">");
        if (!isCollapsed())
            super.writeHTMLTable(pagecontext, columnSet, isAddButtonVisible, isFilterButtonVisible, isRemoveButtonVisible,
                    width, height, readonly, tablecustomizer, rows);
    }

    protected String getBorderClass() {
        return "border-primary";
    }

    protected String getTextClass() {
        return "text-primary";
    }

    protected String getIconClassCollapsed() {
        return isCollapsed() ? "fa-angle-down" : "fa-angle-up";
    }
}
