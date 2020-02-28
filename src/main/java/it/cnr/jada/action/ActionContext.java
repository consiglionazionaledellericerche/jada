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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.UserInfo;

import java.text.ParseException;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.action:
//            BusinessProcessException, BusinessProcess, Action, HookForward, 
//            Forward, ActionMapping

public interface ActionContext {

    BusinessProcess addBusinessProcess(BusinessProcess businessprocess)
            throws BusinessProcessException;

    HookForward addHookForward(BusinessProcess businessprocess, String s, Action action);

    HookForward addHookForward(BusinessProcess businessprocess, String s, Action action, String s1);

    HookForward addHookForward(String s, Action action);

    HookForward addHookForward(String s, Action action, String s1);

    HookForward addHookForward(String s, Forward forward);

    void addRequestTracingUser(String s);

    BusinessProcess closeBusinessProcess()
            throws BusinessProcessException;

    BusinessProcess closeBusinessProcess(BusinessProcess businessprocess)
            throws BusinessProcessException;

    BusinessProcess createBusinessProcess(String s)
            throws BusinessProcessException;

    BusinessProcess createBusinessProcess(String s, Object aobj[])
            throws BusinessProcessException;

    boolean fill(Object obj)
            throws ParseException;

    boolean fill(Object obj, String s)
            throws ParseException;

    boolean fillProperty(Object obj, String s, String s1)
            throws ParseException;

    Forward findActionForward(String s);

    Forward findDefaultForward();

    Forward findForward(String s);

    String getApplicationId();

    BusinessProcess getBusinessProcess();

    void setBusinessProcess(BusinessProcess businessprocess);

    BusinessProcess getBusinessProcess(String s);

    BusinessProcess getBusinessProcessRoot(boolean flag);

    Forward getCaller();

    String getCurrentCommand();

    Enumeration getRequestTracingUsers();

    String getSessionId();

    String getTracingSessionDescription();

    void setTracingSessionDescription(String s);

    UserContext getUserContext(boolean createSession);

    UserContext getUserContext();

    void setUserContext(UserContext usercontext);

    UserInfo getUserInfo();

    void setUserInfo(UserInfo userinfo);

    void invalidateSession();

    boolean isRequestTracingUser();

    boolean isRequestTracingUser(String s);

    void perform(Action action, ActionMapping actionmapping, String s);

    void removeHookForward(String s);

    void removeRequestTracingUser(String s);

    void traceException(Throwable throwable);

    void saveFocusedElement();

    ActionMapping getActionMapping();
}