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

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.jsp.Button;
import jakarta.servlet.ServletException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;

import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            FormBP

public abstract class AbstractPrintBP extends FormBP
        implements Serializable {

    public AbstractPrintBP() {
    }

    protected AbstractPrintBP(String s) {
        super(s);
    }

    public void writeToolbarBootstrap(JspWriter jspwriter) throws IOException, ServletException {
        Button[] abutton = new Button[2];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.printWindow");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.close");
        writeToolbar(jspwriter, abutton);
    }

    public Button[] createToolbar() {
        Button[] abutton = new Button[2];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.close");
        return abutton;
    }

    public abstract void print(PageContext pagecontext)
            throws IOException, ServletException, BusinessProcessException;
}