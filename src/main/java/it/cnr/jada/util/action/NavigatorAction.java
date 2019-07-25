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
import it.cnr.jada.action.Forward;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            FormAction, Navigator

public abstract class NavigatorAction extends FormAction
        implements Serializable {

    public NavigatorAction() {
    }

    public Forward doGotoPage(ActionContext actioncontext, String s, int i) {
        try {
            getNavigator(actioncontext, s).goToPage(actioncontext, i);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNextFrame(ActionContext actioncontext, String s) {
        try {
            Navigator navigator = getNavigator(actioncontext, s);
            navigator.goToPage(actioncontext, navigator.getCurrentPage() + navigator.getPageFrameSize());
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNextPage(ActionContext actioncontext, String s) {
        try {
            Navigator navigator = getNavigator(actioncontext, s);
            navigator.goToPage(actioncontext, navigator.getCurrentPage() + 1);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPreviousFrame(ActionContext actioncontext, String s) {
        try {
            Navigator navigator = getNavigator(actioncontext, s);
            navigator.goToPage(actioncontext, navigator.getCurrentPage() - navigator.getPageFrameSize());
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPreviousPage(ActionContext actioncontext, String s) {
        try {
            Navigator navigator = getNavigator(actioncontext, s);
            navigator.goToPage(actioncontext, navigator.getCurrentPage() - 1);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSort(ActionContext actioncontext, String s, String s1)
            throws BusinessProcessException {
        Navigator navigator = getNavigator(actioncontext, s);
        int i = navigator.getOrderBy(s1);
        switch (i) {
            case 0: // '\0'
                navigator.setOrderBy(s1, 1);
                break;

            case 1: // '\001'
                navigator.setOrderBy(s1, -1);
                break;

            case -1:
                navigator.setOrderBy(s1, 0);
                break;
        }
        navigator.reset(actioncontext);
        return actioncontext.findDefaultForward();
    }

    protected abstract Navigator getNavigator(ActionContext actioncontext, String s);
}