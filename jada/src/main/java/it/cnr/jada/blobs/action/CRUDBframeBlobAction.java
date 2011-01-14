package it.cnr.jada.blobs.action;

import javax.ejb.EJB;

import it.cnr.jada.action.*;
import it.cnr.jada.blobs.bp.*;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.blobs.comp.*;
import it.cnr.jada.blobs.ejb.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Insert the type's description here.
 * Creation date: (16/05/2003 16.51.29)
 * @author: CNRADM
 */
public class CRUDBframeBlobAction extends it.cnr.jada.util.action.SelezionatoreListaAlberoAction {
	BframeBlobComponentSession bframeBlobComponentSession = (BframeBlobComponentSession) EJBCommonServices.createEJB("BFRAMEBLOBS_EJB_BframeBlobComponentSession");

/**
 * CRUDBframeBlobBP constructor comment.
 */
public CRUDBframeBlobAction() {
	super();
}
public Forward doCambiaTipo(ActionContext context) {
	try {
		CRUDBframeBlobBP bp = (CRUDBframeBlobBP)context.getBusinessProcess();
		bp.fillModel(context);
		bp.refreshTree(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doCambiaVisibilita(ActionContext context) {
	try {
		CRUDBframeBlobBP bp = (CRUDBframeBlobBP)context.getBusinessProcess();
		bp.fillModel(context);
		bp.refreshTree(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doCrea(ActionContext context) {
	try {
	 bframeBlobComponentSession.InsertBlob(context.getUserContext());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doElimina(ActionContext context) {
	try {
		CRUDBframeBlobBP bp = (CRUDBframeBlobBP)context.getBusinessProcess();
		bp.setSelection(context);
		Bframe_blob_pathBulk[] selectedElements = null;
		java.util.List l = bp.getSelectedElements(context);
		if (l.size() == 0) {
			Bframe_blob_pathBulk o = (Bframe_blob_pathBulk)bp.getFocusedElement();
			if (o != null)
				selectedElements = new Bframe_blob_pathBulk[] { o };
		} else {
			selectedElements = (Bframe_blob_pathBulk[])l.toArray(new Bframe_blob_pathBulk[l.size()]);
		}
		if (selectedElements == null)
			bp.setMessage("Selezionare almeno una riga.");
		else {
			bframeBlobComponentSession.elimina(context.getUserContext(),selectedElements);

			// Aggiorno il remoteIterator
			bp.refresh(context);

			// Se nella directory corrente non � rimasto nessun file o altra directory
			while(bp.getElementsCount() == 0 && bp.getHistory().size() > 0) {
				// torno indietro di una directory
				bp.setParentElement((OggettoBulk)bp.getHistory().pop());
				bp.getSelection().clear();
				bp.setLeafElement(false);
				bp.setIterator(context,it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,bp.getChildren(context,bp.getParentElement())));
			}
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}