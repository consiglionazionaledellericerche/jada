package it.cnr.jada.action;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.UserInfo;

import java.text.ParseException;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.action:
//            BusinessProcessException, BusinessProcess, Action, HookForward, 
//            Forward, ActionMapping

public interface ActionContext
{

    public abstract BusinessProcess addBusinessProcess(BusinessProcess businessprocess)
        throws BusinessProcessException;

    public abstract HookForward addHookForward(BusinessProcess businessprocess, String s, Action action);

    public abstract HookForward addHookForward(BusinessProcess businessprocess, String s, Action action, String s1);

    public abstract HookForward addHookForward(String s, Action action);

    public abstract HookForward addHookForward(String s, Action action, String s1);

    public abstract HookForward addHookForward(String s, Forward forward);

    public abstract void addRequestTracingUser(String s);

    public abstract BusinessProcess closeBusinessProcess()
        throws BusinessProcessException;

    public abstract BusinessProcess closeBusinessProcess(BusinessProcess businessprocess)
        throws BusinessProcessException;

    public abstract BusinessProcess createBusinessProcess(String s)
        throws BusinessProcessException;

    public abstract BusinessProcess createBusinessProcess(String s, Object aobj[])
        throws BusinessProcessException;

    public abstract boolean fill(Object obj)
        throws ParseException;

    public abstract boolean fill(Object obj, String s)
        throws ParseException;

    public abstract boolean fillProperty(Object obj, String s, String s1)
        throws ParseException;

    public abstract Forward findActionForward(String s);

    public abstract Forward findDefaultForward();

    public abstract Forward findForward(String s);

    public abstract String getApplicationId();

    public abstract BusinessProcess getBusinessProcess();

    public abstract BusinessProcess getBusinessProcess(String s);

    public abstract BusinessProcess getBusinessProcessRoot(boolean flag);

    public abstract Forward getCaller();

    public abstract String getCurrentCommand();

    public abstract Enumeration getRequestTracingUsers();

    public abstract String getSessionId();

    public abstract String getTracingSessionDescription();

    public abstract UserContext getUserContext();

    public abstract UserInfo getUserInfo();

    public abstract void invalidateSession();

    public abstract boolean isRequestTracingUser();

    public abstract boolean isRequestTracingUser(String s);

    public abstract void perform(Action action, ActionMapping actionmapping, String s);

    public abstract void removeHookForward(String s);

    public abstract void removeRequestTracingUser(String s);

    public abstract void setBusinessProcess(BusinessProcess businessprocess);

    public abstract void setTracingSessionDescription(String s);

    public abstract void setUserContext(UserContext usercontext);

    public abstract void setUserInfo(UserInfo userinfo);

    public abstract void traceException(Throwable throwable);

    public abstract void saveFocusedElement();
}