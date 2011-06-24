package it.cnr.jada.firma.bp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.firma.DatiPEC;
import it.cnr.jada.firma.jaxb.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SendPecMail {
	private static javax.mail.Session mail_session;
	public static void sendMail(String subject, String text, File attach, Address[] addressTO, DatiPEC datiPEC, Address[] addressCC, Address[] addressBCC) throws MessagingException, NamingException, JAXBException, IOException{
		   javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		   if (mail_session == null){
				try{
				   mail_session = (javax.mail.Session) ctx.lookup("java:comp/env/mail/PecMailSession");
				}catch(NamingException e){
				   mail_session = (javax.mail.Session) ctx.lookup("java:mail/PecMailSession");		   
				}           	
		   }
		   MimeMessage msg = new MimeMessage(mail_session);	   
		   msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTO);
		   if(addressCC != null)
			 msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCC);
		   if(addressBCC != null)  
			 msg.setRecipients(javax.mail.Message.RecipientType.BCC, addressBCC);
		   msg.setFrom(new InternetAddress(mail_session.getProperty("mail.from")));
		   msg.setSubject(subject);	   
		   javax.mail.internet.MimeMultipart multipart = new javax.mail.internet.MimeMultipart();
		   javax.mail.internet.MimeBodyPart messageBodyPart = new javax.mail.internet.MimeBodyPart();
		   javax.mail.internet.InternetHeaders internetHeaders = new javax.mail.internet.InternetHeaders();
		   internetHeaders = new javax.mail.internet.InternetHeaders();
		   internetHeaders.addHeader("Content-Description","test.html");
		   internetHeaders.addHeader("Content-Type","text/html");
		   multipart.addBodyPart(new javax.mail.internet.MimeBodyPart(internetHeaders,text.getBytes("ISO-8859-1")));
		   // allego il file firmato
		   MimeBodyPart attachmentPart = new MimeBodyPart();
		   if (attach!=null) {
			   FileDataSource fileDataSource = new FileDataSource(attach) {
				   	@Override
				   	public String getContentType() {
				   		return "application/octet-stream";
				   	}
				   };
				   attachmentPart.setDataHandler(new DataHandler(fileDataSource));
				   attachmentPart.setFileName(attach.getName());
				   multipart.addBodyPart(attachmentPart);
		   }
		   // allego il file segnatura.xml
		   MimeBodyPart attachmentPartSegnatura = new MimeBodyPart();
		   File fileSegnatura = generaSegnaturaXML(datiPEC, attach.getName());
		   FileDataSource fileDataSourceSegnatura = new FileDataSource(fileSegnatura) {
			@Override
			public String getContentType() {
				return "application/xml";
			}
		   };
		   attachmentPartSegnatura.setDataHandler(new DataHandler(fileDataSourceSegnatura));
		   attachmentPartSegnatura.setFileName("Segnatura.xml");
		   multipart.addBodyPart(attachmentPartSegnatura);

		   msg.setContent(multipart);
		   msg.setSentDate(EJBCommonServices.getServerTimestamp());
		   Transport.send(msg);
	}
	public static void sendMail(String subject, String text, File attach, java.util.List<String> addressTO, DatiPEC datiPEC, java.util.List<String> addressCC, java.util.List<String> addressBCC) throws MessagingException, NamingException, JAXBException, IOException{
		sendMail(subject,text, attach, indirizzi(addressTO), datiPEC,indirizzi(addressCC),indirizzi(addressBCC));
	}
	public static void sendMail(String subject, String text, File attach, java.util.List<String> addressTO, DatiPEC datiPEC) throws MessagingException, NamingException, JAXBException, IOException{
		sendMail(subject, text, attach, addressTO, datiPEC, null, null);	
	}
	public static void sendMail(String subject, String text, File attach, Address[] addressTO, DatiPEC datiPEC) throws MessagingException, NamingException, JAXBException, IOException{
		sendMail(subject, text, attach, addressTO, datiPEC, null, null);	
	}
	private static Address[] indirizzi(java.util.List<String> listaIndirizzi){
		if (listaIndirizzi == null)
			return null;
		Address[] address = new Address[listaIndirizzi.size()];
		int indice = 0;
		for (Iterator<String> i = listaIndirizzi.iterator();i.hasNext();){
			try {
				address[indice] = new InternetAddress(i.next());
				indice++;
			} catch (AddressException e) {
			}
		}
		return address;
	}
	public static File generaSegnaturaXML(DatiPEC datiPEC, String nomefile) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance("it.cnr.jada.firma.jaxb");
		Marshaller marshaller=jaxbContext.createMarshaller();
		marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, 
	              Boolean.TRUE );
		it.cnr.jada.firma.jaxb.ObjectFactory factory = new it.cnr.jada.firma.jaxb.ObjectFactory();
		//String fileName = "Segnatura.xml";
		File file = File.createTempFile("segnatura", ".xml", new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/"));
		//File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		
		Segnatura segnatura = factory.createSegnatura();

		// inizio <Intestazione>
		Intestazione intestazione = factory.createIntestazione();
		segnatura.setIntestazione(intestazione);

		
		Identificatore identificatore = factory.createIdentificatore();
		identificatore.setCodiceAmministrazione("000000");
		identificatore.setCodiceAOO("AMMCNT");
		if (datiPEC.getNumeroRegistrazione().equals(""))
			identificatore.setNumeroRegistrazione("0000000");
		else
			identificatore.setNumeroRegistrazione(datiPEC.getNumeroRegistrazione());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        String sdate = dateFormat.format(date);
		identificatore.setDataRegistrazione(sdate);
		intestazione.setIdentificatore(identificatore);

		// inizio <Origine>
		Origine origine = factory.createOrigine();
		IndirizzoTelematico indirizzoTelematico = factory.createIndirizzoTelematico();
		indirizzoTelematico.setTipo("smtp");
		indirizzoTelematico.setvalue("sisca@pec.cnr.it");
		origine.setIndirizzoTelematico(indirizzoTelematico);
		
		Mittente mittente = (Mittente) factory.createMittente();
		Amministrazione amministrazione = factory.createAmministrazione();
		Denominazione denominazione = factory.createDenominazione();
		denominazione.setvalue(datiPEC.getSiglaCds()+" - "+datiPEC.getDsCds());
		amministrazione.setDenominazione(denominazione);
		amministrazione.setCodiceAmministrazione(datiPEC.getCds());
		UnitaOrganizzativa unitaOrganizzativa = factory.createUnitaOrganizzativa();
		//unitaOrganizzativa.setIdentificativo(datiPEC.getUo());
		List<Object> listAmministrazione =  amministrazione.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax();
		listAmministrazione.add(unitaOrganizzativa);
		
		Denominazione denominazioneAmm = factory.createDenominazione();
		denominazioneAmm.setvalue(datiPEC.getDsUo());
		unitaOrganizzativa.setDenominazione(denominazioneAmm);

		Denominazione denominazioneUo = factory.createDenominazione();
		denominazioneUo.setvalue(datiPEC.getDsUo());

		Persona persona = factory.createPersona();
		List<Object> listPersona = persona.getDenominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale();
		Nome nome = factory.createNome();
		Cognome cognome = factory.createCognome();
		Titolo titolo = factory.createTitolo();
		nome.setvalue("");
		cognome.setvalue("");
		titolo.setvalue("");
		listPersona.add(nome);
		listPersona.add(cognome);
		listPersona.add(titolo);

		IndirizzoPostale indirizzoPostale = factory.createIndirizzoPostale();
		List<Object> listIndirizzoPostale = indirizzoPostale.getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione();
		Toponimo toponimo = factory.createToponimo();
		Civico civico = factory.createCivico();
		CAP cap = factory.createCAP();
		Comune comune = factory.createComune();
		Provincia provincia = factory.createProvincia();
		toponimo.setvalue("");
		civico.setvalue("");
		cap.setvalue("");
		comune.setvalue("");
		provincia.setvalue("");
		listIndirizzoPostale.add(toponimo);
		listIndirizzoPostale.add(civico);
		listIndirizzoPostale.add(cap);
		listIndirizzoPostale.add(comune);
		listIndirizzoPostale.add(provincia);
		
		IndirizzoTelematico indirizzoTelematicoPers = factory.createIndirizzoTelematico();
		indirizzoTelematicoPers.setvalue("");
		
		Telefono telefono = factory.createTelefono();
		telefono.setvalue("");
		
		Fax fax = factory.createFax();
		fax.setvalue("");

		unitaOrganizzativa.setDenominazione(denominazioneUo);
		List<Object> listUnitaOrganizzativa = unitaOrganizzativa.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax();
		listUnitaOrganizzativa.add(persona);
		listUnitaOrganizzativa.add(indirizzoPostale);
		listUnitaOrganizzativa.add(indirizzoTelematicoPers);
		listUnitaOrganizzativa.add(telefono);
		listUnitaOrganizzativa.add(fax);
		
		AOO aoo = factory.createAOO();
		Denominazione denominazioneAoo = factory.createDenominazione();
		denominazioneAoo.setvalue("CNR - "+datiPEC.getDsUo());
		aoo.setDenominazione(denominazioneAoo);
		mittente.setAOO(aoo);

		mittente.setAmministrazione(amministrazione);
		origine.setMittente(mittente);
		intestazione.setOrigine(origine);

		// fine <Origine>
		// inizio <Destinazione>
		Destinazione destinazione = factory.createDestinazione();
		IndirizzoTelematico indirizzoTelematicoDest = factory.createIndirizzoTelematico();
		indirizzoTelematicoDest.setTipo("smtp");
		indirizzoTelematicoDest.setvalue(datiPEC.getEmailServizio());
		Destinatario destinatario = factory.createDestinatario();
		destinazione.setIndirizzoTelematico(indirizzoTelematicoDest);
		
		Amministrazione amministrazioneDest = factory.createAmministrazione();
		Denominazione denominazioneDest = factory.createDenominazione();
		denominazioneDest.setvalue("CNR - Consiglio Nazionale delle Ricerche");
		amministrazioneDest.setDenominazione(denominazioneDest);
		amministrazioneDest.setCodiceAmministrazione("000000");
		UnitaOrganizzativa unitaOrganizzativaDest = factory.createUnitaOrganizzativa();
		unitaOrganizzativaDest.setIdentificativo(null);
		
		Denominazione denominazioneUoDest = factory.createDenominazione();
		//denominazioneUoDest.setvalue("Amministrazione Centrale - Ufficio del protocollo");
		denominazioneUoDest.setvalue(datiPEC.getDenominazioneServizio());
		unitaOrganizzativaDest.setDenominazione(denominazioneUoDest);
		List<Object> listAmministrazioneDest = amministrazioneDest.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax();
		listAmministrazioneDest.add(unitaOrganizzativaDest);
		
		Persona personaDest = factory.createPersona();
		List<Object> listPersonaDest = personaDest.getDenominazioneOrNomeOrCognomeOrTitoloOrCodiceFiscale();
		Nome nomeDest = factory.createNome();
		Cognome cognomeDest = factory.createCognome();
		Titolo titoloDest = factory.createTitolo();
		nomeDest.setvalue("");
		cognomeDest.setvalue("");
		titoloDest.setvalue("");
		listPersonaDest.add(nomeDest);
		listPersonaDest.add(cognomeDest);
		listPersonaDest.add(titoloDest);

		IndirizzoPostale indirizzoPostaleDest = factory.createIndirizzoPostale();
		List<Object> listIndirizzoPostaleDest = indirizzoPostaleDest.getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione();
		Toponimo toponimoDest = factory.createToponimo();
		Civico civicoDest = factory.createCivico();
		CAP capDest = factory.createCAP();
		Comune comuneDest = factory.createComune();
		Provincia provinciaDest = factory.createProvincia();
		toponimoDest.setvalue("");
		civicoDest.setvalue("");
		capDest.setvalue("");
		comuneDest.setvalue("");
		provinciaDest.setvalue("");
		listIndirizzoPostaleDest.add(toponimoDest);
		listIndirizzoPostaleDest.add(civicoDest);
		listIndirizzoPostaleDest.add(capDest);
		listIndirizzoPostaleDest.add(comuneDest);
		listIndirizzoPostaleDest.add(provinciaDest);
		
		IndirizzoTelematico indirizzoTelematicoPersDest = factory.createIndirizzoTelematico();
		indirizzoTelematicoPersDest.setvalue("");
		
		Telefono telefonoDest = factory.createTelefono();
		telefonoDest.setvalue("");
		
		Fax faxDest = factory.createFax();
		faxDest.setvalue("");
		
		List<Object> listUnitaOrganizzativaDest =  unitaOrganizzativaDest.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax();
		listUnitaOrganizzativaDest.add(personaDest);
		listUnitaOrganizzativaDest.add(indirizzoPostaleDest);
		listUnitaOrganizzativaDest.add(indirizzoTelematicoPersDest);
		listUnitaOrganizzativaDest.add(telefonoDest);
		listUnitaOrganizzativaDest.add(faxDest);
		
		AOO aooDest = factory.createAOO();
		Denominazione denominazioneAooDest = factory.createDenominazione();
		denominazioneAooDest.setvalue("");
		aooDest.setDenominazione(denominazioneAooDest);
		List<Object> listAmmDestinatari = destinatario.getAmministrazioneOrAOOOrDenominazioneOrPersona();
		destinazione.setConfermaRicezione("si");
		listAmmDestinatari.add(amministrazioneDest);
		listAmmDestinatari.add(aooDest);
		List<Destinazione> listDestinazioni = intestazione.getDestinazione();
		listDestinazioni.add(destinazione);
		
		List<Destinatario> listDestinatari = destinazione.getDestinatario();
		listDestinatari.add(destinatario);
		intestazione.setOggetto(datiPEC.getOggetto());
		// fine <Destinazione>
		// fine <Intestazione>

		// inizio <Descrizione>
		Descrizione descrizione = factory.createDescrizione();
		List<Object> documentoOrTestoDelMessaggio = descrizione.getDocumentoOrTestoDelMessaggio();
		Documento documento = factory.createDocumento();
		documento.setNome(nomefile);
		documento.setTipoRiferimento("MIME");
		documentoOrTestoDelMessaggio.add(documento);
		
		segnatura.setDescrizione(descrizione);
		// fine <Descrizione>
		
		marshaller.marshal(segnatura, fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
		
		return file;
    }
}
