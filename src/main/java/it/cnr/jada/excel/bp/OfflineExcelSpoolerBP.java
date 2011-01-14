package it.cnr.jada.excel.bp;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.util.jsp.Button;


/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OfflineExcelSpoolerBP extends ExcelSpoolerBP {
	
	
	public OfflineExcelSpoolerBP() {
		super();
	}
	
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.excel");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.close");
		return toolbar;
	}
	
		public void controllaCampiEMail() throws ValidationException{
		if (isEMailEnabled()){
			if (((Excel_spoolerBulk)this.getModel()).getEmail_a()==null)
				throw new it.cnr.jada.bulk.ValidationException("Specificare il destinatario della E-Mail.");
			if (((Excel_spoolerBulk)this.getModel()).getEmail_subject()==null)
				throw new it.cnr.jada.bulk.ValidationException("Specificare l'oggetto della E-Mail.");
			try {
				StringTokenizer st = new StringTokenizer(((Excel_spoolerBulk)this.getModel()).getEmail_a(),",");
			     while (st.hasMoreTokens()) {
					new InternetAddress(st.nextToken()).validate();
			     }
			} catch (AddressException e) {
				throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario non valido!");
			}
			if (((Excel_spoolerBulk)this.getModel()).getEmail_cc()!=null){
				try {
					StringTokenizer st = new StringTokenizer(((Excel_spoolerBulk)this.getModel()).getEmail_cc(),",");
				     while (st.hasMoreTokens()) {
						new InternetAddress(st.nextToken()).validate();
				     }
				} catch (AddressException e) {
					throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario per conoscenza non valido!");
				}
			}
			if (((Excel_spoolerBulk)this.getModel()).getEmail_ccn()!=null){
				try {
					StringTokenizer st = new StringTokenizer(((Excel_spoolerBulk)this.getModel()).getEmail_ccn(),",");
				     while (st.hasMoreTokens()) {
						new InternetAddress(st.nextToken()).validate();
				     }
				} catch (AddressException e) {
					throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario per conoscenza nacosta non valido!");
				}
			}
		}
	}
	
	
	@Override
	public void writeToolbar(PageContext pagecontext) throws IOException,
			ServletException {
		Button[] toolbar = getToolbar();
		writeToolbar(pagecontext.getOut(),toolbar);
	}
	public boolean isExcelButtonEnabled() {
		return !super.isScaricaButtonEnabled();
	}
}
