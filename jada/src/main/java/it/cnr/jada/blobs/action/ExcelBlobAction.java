/*
 * Created on Feb 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.blobs.action;

import it.cnr.jada.action.*;
import it.cnr.jada.blobs.bp.ExcelBlobBP;
import it.cnr.jada.blobs.bulk.Excel_blobBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.PersistencyException;

/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 17:28:27)
 * @author: CNRADM
 */
public class ExcelBlobAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
/**
 * SpoolerStatusAction constructor comment.
 */
public ExcelBlobAction() {
	super();
}
public Forward doDelete(ActionContext context) {
	try {
		ExcelBlobBP bp = (ExcelBlobBP)context.getBusinessProcess();
		bp.setSelection(context);
		Excel_blobBulk[] array = null;
		if (!bp.getSelection().isEmpty()) {
			array = new Excel_blobBulk[bp.getSelection().size()];
			int j = 0;
			for (it.cnr.jada.util.action.SelectionIterator i = bp.getSelection().iterator();i.hasNext();)
				array[j++] = (Excel_blobBulk)bp.getElementAt(context,i.nextIndex());
		} else if (bp.getFocusedElement() != null) {
			array = new Excel_blobBulk[1];
			array[0] = (Excel_blobBulk)bp.getFocusedElement();
		}
		if (array != null){
				bp.createComponentSession().DeleteAllExcelBlob(context.getUserContext(), array);
					}
		else{
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare almeno una riga.");
		}
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doRefresh(ActionContext context) {
	try {
		ExcelBlobBP bp = (ExcelBlobBP)context.getBusinessProcess();
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doSelection(ActionContext context,String name) {
	try {
		ExcelBlobBP bp = (ExcelBlobBP)context.getBusinessProcess();
		bp.setFocus(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
