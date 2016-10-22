/*
 * Created on Dec 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.servlet;
import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReadClass extends DefaultHandler{


	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
		AdminSession adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");
		if (adminSession == null){
			adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");
		}
			
		if (localName.equals("class")) {
			String n = atts.getValue("","name");
			try {
				  try {
					  adminSession.loadPersistentInfos(Class.forName(n));
					  if(n.endsWith("Bulk"))
						adminSession.loadBulkInfos(Class.forName(n));								
				} catch (ClassNotFoundException e1) {
					throw new RuntimeException(e1);
				}
			} catch (EJBException e) {
				throw new RuntimeException(e);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public ReadClass(String nomeFile) {
		SAXParser p = new SAXParser();
		p.setContentHandler(this);
		try { p.parse(nomeFile); } 
		catch (Exception e) {e.printStackTrace();}
	}
}
