package it.cnr.jada.util.action;

import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.List;

public class CollapsableDetailCRUDController extends SimpleDetailCRUDController{

    public CollapsableDetailCRUDController(String s, Class class1, String s1, FormController formcontroller) {
        super(s, class1, s1, formcontroller);
    }

    public CollapsableDetailCRUDController(String s, Class class1, String s1, FormController formcontroller, boolean flag) {
        super(s, class1, s1, formcontroller, flag);
    }

    @Override
    protected void writeHTMLTable(PageContext pagecontext, String columnSet,
                                  boolean isAddButtonVisible, boolean isFilterButtonVisible, boolean isRemoveButtonVisible,
                                  String width, String height,
                                  boolean readonly, TableCustomizer tablecustomizer, List rows)
            throws IOException, ServletException {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<div class=\"card border-primary\">");
        jspwriter.println("<div class=\"card-header\">");
        jspwriter.println("<h5 class=\"mb-0\">");
        jspwriter.println("<a class=\"text-primary\" onclick=\"submitForm('doToggle(" + getInputPrefix() + ")')\">");
        jspwriter.print("<i aria-hidden=\"true\" class=\"fa " + getIconClassCollapsed() + "\"></i> ");
        jspwriter.print(getControllerName());
        jspwriter.print("</a>");
        jspwriter.println("</h5>");
        jspwriter.println("</div>");
        jspwriter.println("<div class=\"card-block\">");
        if (!isCollapsed())
            super.writeHTMLTable(pagecontext, columnSet, isAddButtonVisible, isFilterButtonVisible, isRemoveButtonVisible,
                    width, height, readonly, tablecustomizer, rows);
        jspwriter.println("</div>");
        jspwriter.println("</div>");
    }

    protected String getIconClassCollapsed() {
        return isCollapsed() ? "fa-chevron-circle-down" : "fa-chevron-circle-up";
    }
}
