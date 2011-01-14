package it.cnr.jada.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package it.cnr.jada.action:
//            Forward, ActionContext

public class HookForward
    implements Serializable, Forward
{

    HookForward(String s, Forward forward1)
    {
        parameters = new HashMap();
        name = s;
        forward = forward1;
    }

    public void addParameter(String s, Object obj)
    {
        parameters.put(s, obj);
    }

    public Object getParameter(String s)
    {
        return parameters.get(s);
    }

    public void perform(ActionContext actioncontext)
    {
        actioncontext.removeHookForward(name);
        forward.perform(actioncontext);
    }

    private Map parameters;
    private Forward forward;
    private String name;
}