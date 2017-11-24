package it.cnr.jada.util.action;

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.jsp.Button;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

// Referenced classes of package it.cnr.jada.util.action:
//            FormBP

public abstract class AbstractPrintBP extends FormBP
    implements Serializable
{

    public AbstractPrintBP()
    {
    }

    protected AbstractPrintBP(String s)
    {
        super(s);
    }

    public void writeToolbarBootstrap(JspWriter jspwriter) throws IOException, ServletException {
        Button abutton[] = new Button[2];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.printWindow");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.close");
        writeToolbar(jspwriter, abutton);
    }

    public Button[] createToolbar()
    {
        Button abutton[] = new Button[2];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.close");
        return abutton;
    }

    public abstract void print(PageContext pagecontext)
        throws IOException, ServletException, BusinessProcessException;
}