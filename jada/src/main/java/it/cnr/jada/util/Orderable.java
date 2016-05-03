package it.cnr.jada.util;

import it.cnr.jada.action.ActionContext;

// Referenced classes of package it.cnr.jada.util:
//            OrderConstants

public interface Orderable
    extends OrderConstants
{

    public abstract int getOrderBy(String s);

    public abstract boolean isOrderableBy(String s);

    public abstract void setOrderBy(ActionContext actioncontext, String s, int i);
}