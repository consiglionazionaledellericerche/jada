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

package it.cnr.jada.util.was35;

//import com.ibm.ws.Transaction.TransactionManagerFactory;
//import com.ibm.servlet.engine.ServletEngine;
//import com.ibm.servlet.engine.config.ServletEngineInfo;
//import com.ibm.servlet.engine.config.TransportInfo;


import jakarta.servlet.ServletContext;

public class WebSphereServices extends it.cnr.jada.util.WebSphereServices {

    public WebSphereServices() {
    }

/*
    public String getCloneIndex()
    {
        ServletEngineInfo servletengineinfo = ServletEngine.getEngine().getInfo();
        TransportInfo transportinfo = servletengineinfo.getTransportInfo(servletengineinfo.getActiveTransportName());
        Properties properties = transportinfo.getArgs();
        return properties.getProperty("cloneIndex");
    }

    public String getQueueName()
    {
        ServletEngineInfo servletengineinfo = ServletEngine.getEngine().getInfo();
        TransportInfo transportinfo = servletengineinfo.getTransportInfo(servletengineinfo.getActiveTransportName());
        Properties properties = transportinfo.getArgs();
        return properties.getProperty("queueName");
    }

    public String getResourcePath(ServletContext servletcontext, String s)
    {
        return servletcontext.getRealPath("../" + s);
    }
*/
    /*public void suspendTransaction()
    {
        try
        {
			TransactionManagerFactory.getTransactionManager().suspend();
        }
        catch(Throwable _ex) { }
    }*/

    /* (non-Javadoc)
     * @see it.cnr.jada.util.WebSphereServices#getCloneIndex()
     */
    public String getCloneIndex() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see it.cnr.jada.util.WebSphereServices#getQueueName()
     */
    public String getQueueName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see it.cnr.jada.util.WebSphereServices#getResourcePath(javax.servlet.ServletContext, java.lang.String)
     */
    public String getResourcePath(ServletContext servletcontext, String s) {
        // TODO Auto-generated method stub
        return null;
    }
}