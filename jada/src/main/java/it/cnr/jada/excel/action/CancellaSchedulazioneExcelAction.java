package it.cnr.jada.excel.action;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.AdminUserContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.ejb.BframeExcelComponentSession;
import it.cnr.jada.util.action.FormAction;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class CancellaSchedulazioneExcelAction extends FormAction{
	@Override
	@SuppressWarnings("unused")
	public Forward doDefault(ActionContext actioncontext) throws RemoteException {
		if(((HttpActionContext)actioncontext).getParameter("pg")== null)
			return super.doDefault(actioncontext);
		Long pg = new Long(((HttpActionContext)actioncontext).getParameter("pg").substring(2));
		String indirizzoEMail = ((HttpActionContext)actioncontext).getParameter("indirizzoEMail");
		BusinessProcess bp = actioncontext.getBusinessProcess();
		bp.setResource("pg", String.valueOf(pg));
		bp.setResource("indirizzoEMail", indirizzoEMail);
		UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
		try {
			Excel_spoolerBulk excelSpooler = getComponent(actioncontext).findExcelSpooler(userContext, pg);
			if (excelSpooler == null){
				openMessage(actioncontext, "La lista di distribuzione della consultazione, è stata eliminata!");
				return super.doDefault(actioncontext);
			}
			String msg = "Si conferma la cancellazione dell'indirizzo "+indirizzoEMail+"<BR>dalla lista di distribuzione della consultazione \""+excelSpooler.getNome_file()+"\"?";
			OptionBP option = openConfirm( actioncontext, msg, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmCancellaSchedulazione");
			return option;
		} catch (ComponentException e) {
			handleException(actioncontext, e);
		} catch (BusinessProcessException e) {
			handleException(actioncontext, e);
		}
		return super.doDefault(actioncontext);
	}
	public Forward doConfirmCancellaSchedulazione(ActionContext actioncontext,it.cnr.jada.util.action.OptionBP option) {
		UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
		BusinessProcess bp = actioncontext.getBusinessProcess();
		if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			try {
				getComponent(actioncontext).cancellaSchedulazione(userContext, new Long(bp.getResource("pg")), bp.getResource("indirizzoEMail"));
				openMessage(actioncontext, "La cancellazione è stata effettuata.");
			} catch (Exception e) {
				handleException(actioncontext, e);
			}
		}
		return actioncontext.findDefaultForward();
	}
	private BframeExcelComponentSession getComponent(ActionContext actioncontext) throws EJBException, RemoteException{
		return (BframeExcelComponentSession)EJBCommonServices.createEJB("BFRAMEEXCEL_EJB_BframeExcelComponentSession",BframeExcelComponentSession.class);
	}
}
