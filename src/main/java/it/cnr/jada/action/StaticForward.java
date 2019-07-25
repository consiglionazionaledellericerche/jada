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

package it.cnr.jada.action;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.action:
//            Forward, ActionContext, HttpActionContext, ActionPerformingError

public class StaticForward
        implements Serializable, Forward {

    private String name;
    private String path;
    private boolean redirect;

    public StaticForward() {
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String s) {
        path = s;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean flag) {
        redirect = flag;
    }

    public void perform(ActionContext actioncontext) {
        if (actioncontext instanceof ActionContext)
            perform((HttpActionContext) actioncontext);
    }

    public void perform(HttpActionContext httpactioncontext) {
        try {
            if (redirect)
                httpactioncontext.getResponse().sendRedirect(path);
            httpactioncontext.getServlet().getServletContext().getRequestDispatcher(path).forward(httpactioncontext.getRequest(), httpactioncontext.getResponse());
        } catch (IOException ioexception) {
            throw new ActionPerformingError(ioexception);
        } catch (ServletException servletexception) {
            throw new ActionPerformingError(servletexception);
        }
    }
}