package it.cnr.jada.util.mail;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SimpleAuthenticator extends Authenticator {
        private String user;
        private String password;

        public SimpleAuthenticator(String user, String password) {
                super();
                this.user = user;
                this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
        }

}