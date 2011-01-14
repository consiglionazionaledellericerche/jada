package it.cnr.jada.action;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.action:
//            Forward, ActionContext, Action, ActionMapping

public class ActionForward
    implements Serializable, Forward
{

    public ActionForward(Action action1)
    {
        action = action1;
    }

    public ActionForward(Action action1, ActionMapping actionmapping)
    {
        action = action1;
        actionMapping = actionmapping;
    }

    public ActionForward(Action action1, ActionMapping actionmapping, String s)
    {
        action = action1;
        actionMapping = actionmapping;
        command = s;
    }

    public void perform(ActionContext actioncontext)
    {
        actioncontext.perform(action, actionMapping, command);
    }

    private Action action;
    private String command;
    private ActionMapping actionMapping;
}