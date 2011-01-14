package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;

public class BulkListPrintAction extends FormAction {

	public BulkListPrintAction() {
		super();
	}
    public Forward doCloseForm(ActionContext actioncontext)throws BusinessProcessException{
		actioncontext.closeBusinessProcess();
		return actioncontext.findDefaultForward();
	}
}
