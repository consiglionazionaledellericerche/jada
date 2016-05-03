package it.cnr.jada.action;


// Referenced classes of package it.cnr.jada.action:
//            Config, ActionContext, Forward

public interface Action
{

    public abstract void init(Config config);

    public abstract boolean isThreadsafe(ActionContext actioncontext);

    public abstract Forward perform(ActionContext actioncontext);
}