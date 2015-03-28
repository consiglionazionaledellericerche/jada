package it.cnr.jada.util.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;

public class TestPKCS7 {

	public static void main(String[] args) throws EmailException {

		// Create the email message
		SimplePECMail email = new SimplePECMail("patrizia.villani@pec.cnr.it", "patrizia71");
		email.setHostName("smtps.pec.aruba.it");
		email.addTo("sisca@pec.cnr.it", "Sistemi Informativi");
		email.setFrom("patrizia.villani@pec.cnr.it", "Patrizia Villani");
		email.setSubject("Fattura elettronica prova");
		email.setMsg("Ivio Fattura elettronica prova");

		// add the attachment
		email.attach(new FileDataSource(new File("/home/mspasiano/Scaricati/IT01138480031_00001.xml.p7m")),"IT01138480031_00001.xml.p7m","",EmailAttachment.ATTACHMENT);
		email.attach(new FileDataSource(new File("/home/mspasiano/fatturefrompec/IT05801321216_00001_MT_002.xml")),"IT05801321216_00001_MT_002.xml","",EmailAttachment.ATTACHMENT);

		// send the email
		email.send();
		
		//READ EMAIL
		Properties props = System.getProperties();
		props.setProperty("mail.imap.host", "imaps.pec.aruba.it");
		props.setProperty("mail.imap.auth", "true");
		props.setProperty("mail.imap.ssl.enable", "true");
		props.setProperty("mail.imap.port", "995");
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.connectiontimeout", "5000");
		props.setProperty("mail.imap.timeout", "5000");

		try {
		  Session session = Session.getDefaultInstance(props, new SimpleAuthenticator("patrizia.villani@pec.cnr.it",
					"patrizia71"));
		  URLName urlName = new URLName("imaps://imaps.pec.aruba.it");
		  Store store = session.getStore(urlName);
		  if (!store.isConnected()) {
		    store.connect();
		    Folder inbox = store.getFolder("Inbox");	
		    inbox.open(Folder.READ_WRITE);
		    if (inbox.exists()) {
		    	List<SearchTerm> terms = new ArrayList<SearchTerm>();
		    	terms.add(new ReceivedDateTerm(ComparisonTerm.LE, new Date()));
		    	terms.add(new FromStringTerm("pec.fatturapa.it"));
		    	terms.add(new SubjectTerm("Invio File"));
		    	Message messages[] = inbox.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
			    for (int i = 0; i < messages.length; i++) {
			    	try {
		    			estraiParte(messages[i].getContent());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		    }
		    inbox.close(true);
		  }
		  store.close();
		} catch (NoSuchProviderException e) {
		  e.printStackTrace();
		  System.exit(1);
		} catch (MessagingException e) {
		  e.printStackTrace();
		  System.exit(2);
		}		
	}
	
	public static void estraiParte(Object obj) throws MessagingException, IOException {
		if (obj instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) obj;
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);
				estraiParte(bodyPart.getContent());
			}
		} else if (obj instanceof MimeBodyPart) {
			MimeBodyPart multipart = (MimeBodyPart) obj;
			estraiParte(multipart.getContent());
		} else if (obj instanceof MimeMessage) {
			MimeMessage mimemessage = (MimeMessage) obj;
			forwardedEmail(mimemessage.getContent());
		}
	}
	
	public static void forwardedEmail(Object obj) throws MessagingException, IOException {
		if (obj instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) obj;
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);
				String disposition = bodyPart.getDisposition();
				if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
					System.out.println("Content type:" + bodyPart.getContentType());
					System.out.println("File name:" + bodyPart.getFileName());
					File file = new File("/home/mspasiano/fatturefrompec/" + bodyPart.getFileName()); 
					IOUtils.copy(bodyPart.getInputStream(), new FileOutputStream(file));
				}
			}
		}		
	}
}
