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

package it.cnr.jada.util.upload;

import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.security.Provider;
import java.security.Security;

public class HttpsMessage extends HttpMessage {

    static boolean m_bStreamHandlerSet = false;

    public HttpsMessage(String szURL)
            throws Exception {
        super(null);
        if (!m_bStreamHandlerSet) {
            String szVendor = System.getProperty("java.vendor");
            String szVersion = System.getProperty("java.version");
            Double dVersion = new Double(szVersion.substring(0, 3));
            if (szVendor.indexOf("Microsoft") > -1)
                try {
                    Class clsFactory = Class.forName("com.ms.net.wininet.WininetStreamHandlerFactory");
                    if (clsFactory != null)
                        URL.setURLStreamHandlerFactory((URLStreamHandlerFactory) clsFactory.newInstance());
                } catch (ClassNotFoundException cfe) {
                    throw new Exception("Unable to load the Microsoft SSL stream handler.  Check classpath." + cfe.toString());
                } catch (Error _ex) {
                    m_bStreamHandlerSet = true;
                }
            else if (dVersion.doubleValue() >= 1.2D) {
                System.getProperties().put("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
                try {
                    Class clsFactory = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
                    if (clsFactory != null && Security.getProvider("SunJSSE") == null)
                        Security.addProvider((Provider) clsFactory.newInstance());
                } catch (ClassNotFoundException cfe) {
                    throw new Exception("Unable to load the JSSE SSL stream handler.  Check classpath." + cfe.toString());
                }
            }
            m_bStreamHandlerSet = true;
        }
        super.servlet = new URL(szURL);
    }

}