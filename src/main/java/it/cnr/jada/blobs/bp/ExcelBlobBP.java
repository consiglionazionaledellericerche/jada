/*
 * Created on Feb 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.blobs.bp;

import it.cnr.jada.action.*;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.blobs.ejb.BframeBlobComponentSession;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.jsp.*;

/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 17:28:04)
 * @author: CNRADM
 */
public class ExcelBlobBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
/**
 * SpoolerStatusBP constructor comment.
 * @param function java.lang.String
 */

public ExcelBlobBP() {
	super();
	table.setMultiSelection(true);
	setBulkInfo(BulkInfo.getBulkInfo(Excel_blobBulk.class));
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[3];
	int i = 0;
	toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.refresh");	
	toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.delete");
	toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.download");
	return toolbar;
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {
	super.init(config,context);
	Excel_blobBulk excel_blob = new Excel_blobBulk();
	excel_blob.setUser(context.getUserContext().getUser());
	setModel(context,excel_blob);
	refresh(context);
}
public BframeBlobComponentSession createComponentSession() throws BusinessProcessException {
	return
		(BframeBlobComponentSession)createComponentSession(
		"BFRAMEBLOBS_EJB_BframeBlobComponentSession",
	     BframeBlobComponentSession.class);
}
public boolean isDeleteButtonEnabled()
{
	return getBlob_path() != null;
}

public boolean isDownloadButtonEnabled()
{
	return getBlob_path() != null && "S".equals(getBlob_path().getStato());
}

public void refresh(ActionContext context) throws BusinessProcessException {
	try {
		setIterator(context,createComponentSession().cerca(
			context.getUserContext(),null,
			((Excel_blobBulk)getModel())));
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
public boolean isPrintButtonEnabled() {
	return 
		getFocusedElement() != null &&
		((Excel_blobBulk)getFocusedElement()).isEseguito();
}

/* Riscritto perchè in questo caso non voglio che quando l'utente
 * seleziona una riga nel selezionatore venga anche impostato
 * il modello del BulkBP
 */
protected void setFocusedElement(it.cnr.jada.action.ActionContext context,Object element) throws it.cnr.jada.action.BusinessProcessException {
	OggettoBulk model = getModel();
	super.setFocusedElement(context,element);
	setModel(context,model);
}
public Excel_blobBulk getBlob_path()
{
	return (Excel_blobBulk)getFocusedElement();
}
public String getDownloadUrl()
{
	Excel_blobBulk excel_blob = getBlob_path();
	if(excel_blob == null)
		return "";
	StringBuffer stringbuffer = new StringBuffer("download_excel/fileExcel");
	stringbuffer.append("/");
	stringbuffer.append(excel_blob.getCd_utente());
	stringbuffer.append("/");
	stringbuffer.append(excel_blob.getNome_file());	
	return stringbuffer.toString();
}
}