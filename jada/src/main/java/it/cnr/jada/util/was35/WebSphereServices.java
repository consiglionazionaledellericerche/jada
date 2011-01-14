package it.cnr.jada.util.was35;

//import com.ibm.ws.Transaction.TransactionManagerFactory;
//import com.ibm.servlet.engine.ServletEngine;
//import com.ibm.servlet.engine.config.ServletEngineInfo;
//import com.ibm.servlet.engine.config.TransportInfo;
import java.util.Properties;
import javax.servlet.ServletContext;

public class WebSphereServices extends it.cnr.jada.util.WebSphereServices
{

    public WebSphereServices()
    {
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