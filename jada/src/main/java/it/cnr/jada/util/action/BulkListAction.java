package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            FormAction, BulkListBP

public class BulkListAction extends FormAction
    implements Serializable
{

    public BulkListAction()
    {
    }

    public Forward doSelection(ActionContext actioncontext, String s)
    {
        BulkListBP bulklistbp = (BulkListBP)actioncontext.getBusinessProcess();
        bulklistbp.setSelection(actioncontext);
        return actioncontext.findDefaultForward();
    }
}