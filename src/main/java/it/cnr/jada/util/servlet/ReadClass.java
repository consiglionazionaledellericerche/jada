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
 * Created on Dec 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.servlet;

import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import jakarta.ejb.EJBException;
import java.rmi.RemoteException;

/**
 * @author mspasiano
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReadClass extends DefaultHandler {


    public ReadClass(String nomeFile) {
        SAXParser p = new SAXParser();
        p.setContentHandler(this);
        try {
            p.parse(nomeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        AdminSession adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");
        if (adminSession == null) {
            adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");
        }

        if (localName.equals("class")) {
            String n = atts.getValue("", "name");
            try {
                try {
                    adminSession.loadPersistentInfos(Class.forName(n));
                    if (n.endsWith("Bulk"))
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
}
