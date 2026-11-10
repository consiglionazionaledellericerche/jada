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

package it.cnr.jada.util;

import it.cnr.jada.util.ejb.EJBCommonServices;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.InternetHeaders;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Invio di email tramite la sessione di posta configurata nell'application server.
 * <p>
 * Il nome JNDI della sessione viene risolto in quest'ordine:
 * <ol>
 *     <li>system property {@value #PROP_MAIL_JNDI} (es. {@code -Dsigla.mail.jndi=java:jboss/mail/sigla})</li>
 *     <li>variabile d'ambiente {@value #ENV_MAIL_JNDI}</li>
 *     <li>{@value #JAVA_COMP_ENV_MAIL_MAIL_SESSION}</li>
 *     <li>{@value #JAVA_JBOSS_MAIL_DEFAULT}</li>
 * </ol>
 *
 * @author mspasiano
 */
public class SendMail {
    public static final String JAVA_COMP_ENV_MAIL_MAIL_SESSION = "java:comp/env/mail/MailSession";
    public static final String JAVA_JBOSS_MAIL_DEFAULT = "java:jboss/mail/Default";
    public static final String JAVA_JBOSS_MAIL_SIGLA ="java:jboss/mail/sigla";
    public static final String PROP_MAIL_JNDI = "sigla.mail.jndi";
    public static final String ENV_MAIL_JNDI = "SIGLA_MAIL_JNDI";

    private static final Logger LOGGER = Logger.getLogger(SendMail.class.getName());
    private static final String CONTENT_DESCRIPTION = "test.html";

    private static volatile Session mail_session;

    private SendMail() {
    }

    /**
     * Restituisce la sessione di posta, cercandola una sola volta e mantenendola in cache.
     */
    private static Session getMailSession() throws NamingException {
        Session session = mail_session;
        if (session != null) {
            return session;
        }
        synchronized (SendMail.class) {
            if (mail_session != null) {
                return mail_session;
            }
            InitialContext ctx = new InitialContext();
            List<String> candidati = new ArrayList<>();

            String configurato = System.getProperty(PROP_MAIL_JNDI);
            if (configurato == null || configurato.isBlank()) {
                configurato = System.getenv(ENV_MAIL_JNDI);
            }
            if (configurato != null && !configurato.isBlank()) {
                candidati.add(configurato.trim());
            }
            candidati.add(JAVA_COMP_ENV_MAIL_MAIL_SESSION);
            candidati.add(JAVA_JBOSS_MAIL_DEFAULT);
            candidati.add(JAVA_JBOSS_MAIL_SIGLA);

            NamingException ultimo = null;
            for (String nome : candidati) {
                try {
                    mail_session = (Session) ctx.lookup(nome);
                    LOGGER.log(Level.INFO, "Sessione di posta trovata: {0}", nome);
                    return mail_session;
                } catch (NamingException e) {
                    LOGGER.log(Level.FINE, "Sessione di posta non trovata: {0}", nome);
                    ultimo = e;
                }
            }
            throw ultimo;
        }
    }

    private static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    private static void send(Session session, String subject, String text, String contentType,
                             Address[] addressTO, Address[] addressCC, Address[] addressBCC,
                             boolean useSessionFrom) throws Exception {
        MimeMessage msg = new MimeMessage(session);

        String from = session.getProperty("mail.from");
        if (useSessionFrom || from == null || from.isBlank()) {
            msg.setFrom();
        } else {
            msg.setFrom(new InternetAddress(from));
        }

        if (addressTO == null) {
            msg.setRecipients(Message.RecipientType.TO, msg.getFrom());
        } else {
            msg.setRecipients(Message.RecipientType.TO, addressTO);
        }
        if (addressCC != null) {
            msg.setRecipients(Message.RecipientType.CC, addressCC);
        }
        if (addressBCC != null) {
            msg.setRecipients(Message.RecipientType.BCC, addressBCC);
        }

        msg.setSubject(subject);

        InternetHeaders internetHeaders = new InternetHeaders();
        internetHeaders.addHeader("Content-Description", CONTENT_DESCRIPTION);
        internetHeaders.addHeader("Content-Type", contentType);

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(new MimeBodyPart(internetHeaders, text.getBytes(StandardCharsets.UTF_8)));
        msg.setContent(multipart);
        msg.setSentDate(EJBCommonServices.getServerTimestamp());
        Transport.send(msg);
    }

    public static void sendErrorMail(String subject, String text, List<String> addressTO, List<String> addressCC, List<String> addressBCC) {
        try {
            Session session = getMailSession();
            send(session, subject + " Hostname:" + getHostname(), text, "text/plain",
                    indirizzi(addressTO), indirizzi(addressCC), indirizzi(addressBCC), true);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio della mail di errore: " + subject, e);
        }
    }

    public static void sendErrorMail(String subject, String text) {
        sendErrorMail(subject, text, null, null, null);
    }

    public static void sendMail(String subject, String text, Address[] addressTO, Address[] addressCC, Address[] addressBCC) {
        try {
            Session session = getMailSession();
            send(session, subject, text, "text/html", addressTO, addressCC, addressBCC, false);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore nell'invio della mail: " + subject, e);
        }
    }

    public static void sendMail(String subject, String text, List<String> addressTO, List<String> addressCC, List<String> addressBCC) {
        sendMail(subject, text, indirizzi(addressTO), indirizzi(addressCC), indirizzi(addressBCC));
    }

    public static void sendMail(String subject, String text, List<String> addressTO) {
        sendMail(subject, text, addressTO, null, null);
    }

    public static void sendMail(String subject, String text, Address[] addressTO) {
        sendMail(subject, text, addressTO, null, null);
    }

    private static Address[] indirizzi(List<String> listaIndirizzi) {
        if (listaIndirizzi == null) {
            return null;
        }
        List<Address> address = new ArrayList<>(listaIndirizzi.size());
        for (String indirizzo : listaIndirizzi) {
            try {
                address.add(new InternetAddress(indirizzo));
            } catch (AddressException e) {
                LOGGER.log(Level.WARNING, "Indirizzo email non valido ignorato: {0}", indirizzo);
            }
        }
        return address.toArray(new Address[0]);
    }
}
