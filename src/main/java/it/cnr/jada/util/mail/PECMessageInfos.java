package it.cnr.jada.util.mail;

import java.security.cert.Certificate;
import java.util.Set;

import org.w3c.dom.Document;

public class PECMessageInfos {
        public Set<Certificate> signatures;
        public Document certificate;
        public PECBodyParts bodyParts;
}
