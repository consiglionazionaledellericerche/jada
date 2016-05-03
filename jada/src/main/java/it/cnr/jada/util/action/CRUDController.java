package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

// Referenced classes of package it.cnr.jada.util.action:
//            FormController, ListController, Selection

public interface CRUDController
    extends FormController, ListController
{

    public abstract void add(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException;

    public abstract int countDetails();

    public abstract String getControllerName();

    public abstract int getModelIndex();

    public abstract void remove(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException;

    public abstract void removeAll(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException;

    public abstract void reset(ActionContext actioncontext);

    public abstract void resync(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract void save(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause)
        throws BusinessProcessException;

    public abstract void setModelIndex(ActionContext actioncontext, int i)
        throws ValidationException;

    public abstract void setPageIndex(ActionContext actioncontext, int i)
        throws ValidationException, BusinessProcessException;

    public abstract Selection setSelection(ActionContext actioncontext)
        throws BusinessProcessException, ValidationException;
}