/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created on Sep 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util;

import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.mail.Address;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author mspasiano
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SendMail {
    public static final String JAVA_COMP_ENV_MAIL_MAIL_SESSION = "java:comp/env/mail/MailSession";
    public static final String JAVA_JBOSS_MAIL_DEFAULT = "java:jboss/mail/Default";
    private static javax.mail.Session mail_session;

    public static void sendErrorMail(String subject, String text, java.util.List<String> addressTO, java.util.List<String> addressCC, java.util.List<String> addressBCC) {
        try {
            String hostname = "";
            try {
                InetAddress addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
            } catch (UnknownHostException e) {
            }
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            if (mail_session == null) {
                try {
                    mail_session = (javax.mail.Session) ctx.lookup(JAVA_COMP_ENV_MAIL_MAIL_SESSION);
                } catch (NamingException e) {
                    mail_session = (javax.mail.Session) ctx.lookup(JAVA_JBOSS_MAIL_DEFAULT);
                }
            }
            MimeMessage msg = new MimeMessage(mail_session);
            msg.setFrom();
            if (addressTO == null) {
                msg.setRecipients(javax.mail.Message.RecipientType.TO, msg.getFrom());
            } else {
                msg.setRecipients(javax.mail.Message.RecipientType.TO, indirizzi(addressTO));
            }
            if (addressCC != null)
                msg.setRecipients(javax.mail.Message.RecipientType.CC, indirizzi(addressCC));
            if (addressBCC != null)
                msg.setRecipients(javax.mail.Message.RecipientType.BCC, indirizzi(addressBCC));

            msg.setSubject(subject + " Hostname:" + hostname);
            javax.mail.internet.MimeMultipart multipart = new javax.mail.internet.MimeMultipart();
            javax.mail.internet.MimeBodyPart messageBodyPart = new javax.mail.internet.MimeBodyPart();
            javax.mail.internet.InternetHeaders internetHeaders = new javax.mail.internet.InternetHeaders();
            internetHeaders = new javax.mail.internet.InternetHeaders();
            internetHeaders.addHeader("Content-Description", "test.html");
            internetHeaders.addHeader("Content-Type", "text/plain");
            multipart.addBodyPart(new javax.mail.internet.MimeBodyPart(internetHeaders, text.getBytes(StandardCharsets.UTF_8)));
            msg.setContent(multipart);
            msg.setSentDate(EJBCommonServices.getServerTimestamp());
            Transport.send(msg);
        } catch (Exception e) {
        }
    }

    public static void sendMail(String subject, String text, Address[] addressTO, Address[] addressCC, Address[] addressBCC) {
        try {
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            if (mail_session == null) {
                try {
                    mail_session = (javax.mail.Session) ctx.lookup(JAVA_COMP_ENV_MAIL_MAIL_SESSION);
                } catch (NamingException e) {
                    mail_session = (javax.mail.Session) ctx.lookup(JAVA_JBOSS_MAIL_DEFAULT);
                }
            }
            MimeMessage msg = new MimeMessage(mail_session);
            msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTO);
            if (addressCC != null)
                msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCC);
            if (addressBCC != null)
                msg.setRecipients(javax.mail.Message.RecipientType.BCC, addressBCC);
            msg.setFrom(new InternetAddress(mail_session.getProperty("mail.from")));
            msg.setSubject(subject);
            javax.mail.internet.MimeMultipart multipart = new javax.mail.internet.MimeMultipart();
            javax.mail.internet.MimeBodyPart messageBodyPart = new javax.mail.internet.MimeBodyPart();
            javax.mail.internet.InternetHeaders internetHeaders = new javax.mail.internet.InternetHeaders();
            internetHeaders = new javax.mail.internet.InternetHeaders();
            internetHeaders.addHeader("Content-Description", "test.html");
            internetHeaders.addHeader("Content-Type", "text/html");
            multipart.addBodyPart(new javax.mail.internet.MimeBodyPart(internetHeaders, text.getBytes(StandardCharsets.UTF_8)));
            msg.setContent(multipart);
            msg.setSentDate(EJBCommonServices.getServerTimestamp());
            Transport.send(msg);
        } catch (Exception e) {
        }
    }

    public static void sendMail(String subject, String text, java.util.List<String> addressTO, java.util.List<String> addressCC, java.util.List<String> addressBCC) {
        sendMail(subject, text, indirizzi(addressTO), indirizzi(addressCC), indirizzi(addressBCC));
    }

    public static void sendMail(String subject, String text, java.util.List<String> addressTO) {
        sendMail(subject, text, addressTO, null, null);
    }

    public static void sendMail(String subject, String text, Address[] addressTO) {
        sendMail(subject, text, addressTO, null, null);
    }

    public static void sendErrorMail(String subject, String text) {
        sendErrorMail(subject, text, null, null, null);
    }

    private static Address[] indirizzi(java.util.List<String> listaIndirizzi) {
        if (listaIndirizzi == null)
            return null;
        Address[] address = new Address[listaIndirizzi.size()];
        int indice = 0;
        for (Iterator<String> i = listaIndirizzi.iterator(); i.hasNext(); ) {
            try {
                address[indice] = new InternetAddress(i.next());
                indice++;
            } catch (AddressException e) {
            }
        }
        return address;
    }
}
