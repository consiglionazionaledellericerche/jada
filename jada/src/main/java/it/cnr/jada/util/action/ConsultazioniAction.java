/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.rest.RestForward;
import it.cnr.jada.rest.RestInfo;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author Marco Spasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniAction extends SelezionatoreListaAction {
	private static final long serialVersionUID = 1L;

	public Forward doBringBack(ActionContext actioncontext) {
		return null;
	}
	
	public Forward doFiltraFiles(ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(bp.getFreeSearchSet());
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			context.addHookForward("searchResult",this,"doRigheSelezionate");
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doCloseForm(ActionContext context)
		throws BusinessProcessException
	{
		try
		{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.setFindclause(null);
			return super.doCloseForm(context);
		}
		catch(Throwable throwable)
		{
			return handleException(context, throwable);
		}
	}

	public Forward doCancellaFiltro(ActionContext context) {
	  try 
	  {
		ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
		RemoteIterator ri = bp.search(context,(CompoundFindClause) null,(OggettoBulk) bp.getBulkInfo().getBulkClass().newInstance()); 
		bp.setIterator(context,ri);
		return context.findDefaultForward();
	  } catch(Throwable e) {
		return handleException(context,e);
	  }
    }

	public Forward doChiudiRicerca(ActionContext context)
	{
		try 
		{
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doRigheSelezionate(ActionContext context)
	{
	
		try 
		{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			HookForward hook = (HookForward)context.getCaller();
			it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator");
			bp.setIterator(context,ri);
			bp.setDirty(true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doSelectAll(it.cnr.jada.action.ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			bp.selectAll(context);
            
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doDeselectAll(it.cnr.jada.action.ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			bp.deselectAll(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Forward doRestInfo(ActionContext context) throws BusinessProcessException{
		ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
		return new RestInfo(bp.getColumns());		
	}
	
	public Forward doRestResponse(ActionContext context) throws BusinessProcessException {
		return new RestForward();		
	}
}
