package it.cnr.jada.firma.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.firma.DatiPEC;
import it.cnr.jada.firma.FirmaInfos;
import it.cnr.jada.firma.Verifica;
import it.cnr.jada.firma.bulk.Doc_firma_digitaleBulk;
import it.cnr.jada.util.Log;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.File;

public class CRUDFirmaBP extends SimpleCRUDBP {
	private static final long LUNGHEZZA_MAX=0x1000000;
	private String signFileRicevuto;
	private BusinessProcess caller;
	private static final Log log = Log.getInstance(CRUDFirmaBP.class);

	public CRUDFirmaBP() {
		super();
	}

	public CRUDFirmaBP(String function)  throws BusinessProcessException{
		super(function);
	}

	public CRUDFirmaBP(String function, BusinessProcess caller, String signFileRicevuto)  throws BusinessProcessException{
		super(function);
		this.caller=caller;
		this.signFileRicevuto=signFileRicevuto;
	}

	protected void initialize(ActionContext context) throws BusinessProcessException {
		super.initialize(context);
		if (caller instanceof FirmaInfos) {
			FirmaInfos firmaInfos = (FirmaInfos) caller;
			if (firmaInfos.tipoPersistenza().equals(FirmaInfos.TIPO_PERSISTENZA_ESTERNA)) {
				firmaInfos.rendiPersistente(signFileRicevuto);
			}
		}
	}
	
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data"); 
	}

	public void validate(ActionContext actioncontext) throws ValidationException {
		Doc_firma_digitaleBulk doc = (Doc_firma_digitaleBulk)getModel();
		UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.blob");

		if ( doc.getNome_file() == null ) {
			if (file == null || file.getName().equals(""))
				throw new ValidationException("Attenzione: selezionare un File da caricare.");
		}
		if (!(file == null || file.getName().equals(""))) { 
			//if (file.length() > LUNGHEZZA_MAX)
			//	throw new ValidationException("Attenzione: la dimensione del file  superiore alla massima consentita (10 Mb).");

			doc.setFile(file.getFile());
			doc.setNome_file(doc.parseFilename(file.getName()));
			doc.setToBeUpdated();
			setDirty(true);
		}
		
		try {
			verifyAndSendMail();
		} catch (Exception e) {
			log.error(e);
			throw new ValidationException("Attenzione: impossibile inviare il messaggio di Posta Elettronica Certificata.");
		}
		super.validate(actioncontext);
	}

	public void verifyAndSendMail() throws Exception {
		Doc_firma_digitaleBulk doc = (Doc_firma_digitaleBulk)getModel();
		File signedFile = doc.getFile();
		Verifica.verificaBustaFirmata(doc.getFile());
		// dopo il salvataggio o prima????
		/*
		if (!(signedFile == null || signedFile.getName().equals(""))) {
			if (caller instanceof FirmaInfos) {
				FirmaInfos firmaInfos = (FirmaInfos) caller;
				DatiPEC datiPEC = firmaInfos.datiPEC();
				List<String> lista = datiPEC.emailListTotale();
				//SendPecMail.sendMail(firmaInfos.descrizione(), firmaInfos.descrizione(), signedFile, lista, datiPEC);
				SendPecMail.sendMail(datiPEC.getOggetto(), datiPEC.getOggetto(), signedFile, lista, datiPEC);
			}
		}
		*/
		/*
		else {
			// per test!!!
			if (caller instanceof FirmaInfos) {
				FirmaInfos firmaInfos = (FirmaInfos) caller;
				DatiPEC datiPEC = firmaInfos.datiPEC();
				List<String> lista = datiPEC.emailListTotale();
				SendPecMail.sendMail(firmaInfos.descrizione(), firmaInfos.descrizione(), null, lista, null, null);
			}
			else {
				java.util.List<String> indirizzi = new ArrayList<String>();
				indirizzi.add("mario.incarnato@cnr.it");
				SendPecMail.sendMail("TEST PEC", "TEST PEC", null, indirizzi, null, null);
			}
		}
		*/
	}
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk=super.initializeModelForInsert(actioncontext, oggettobulk);
		Doc_firma_digitaleBulk doc = (Doc_firma_digitaleBulk)oggettobulk;
		
		if (this.signFileRicevuto!=null) {
			//doc.setBlob(signFileRicevuto);
			File file = new File(signFileRicevuto);
			doc.setNome_file(file.getName());
			doc.setFile(file);
			
			/*
			if(caller instanceof PrintSpoolerBP) {
				Print_spoolerBulk pbulk = (Print_spoolerBulk) ((PrintSpoolerBP) caller).getFocusedElement();
				if (pbulk!=null)
					doc.setDs_file(pbulk.getDs_stampa());
			}*/
			if(caller instanceof FirmaInfos) {
				DatiPEC datiPEC = ((FirmaInfos) caller).datiPEC();
				if (!datiPEC.getOggetto().equals(""))
					doc.setDs_file(datiPEC.getOggetto());
				else
					doc.setDs_file(((FirmaInfos) caller).descrizione());
			}
		}
		return doc;
	}

	public BusinessProcess getCaller() {
		return caller;
	}
	
	public boolean isNewButtonEnabled() {
		return
			super.isNewButtonEnabled() &&
			caller==null;
	}
	
	public boolean isSearchButtonHidden() {
		return
			super.isSearchButtonHidden() ||
			caller!=null;
	}
	
	public boolean isFreeSearchButtonHidden() {
		return
			super.isFreeSearchButtonHidden() ||
			caller!=null;
	}
}