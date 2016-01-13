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
		//email.attach(new FileDataSource(new File("/home/mspasiano/Scaricati/IT01138480031_00001.xml.p7m")),"IT01138480031_00001.xml.p7m","",EmailAttachment.ATTACHMENT);
		//email.attach(new FileDataSource(new File("/home/mspasiano/fatturefrompec/IT05801321216_00001_MT_002.xml")),"IT05801321216_00001_MT_002.xml","",EmailAttachment.ATTACHMENT);

		// send the email
		email.send();		
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
