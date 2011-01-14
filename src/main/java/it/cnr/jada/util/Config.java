package it.cnr.jada.util;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.servlet.*;

public class Config
	implements Serializable
{
	private File logPath;
	private File path;
	private File propFile;
	private static Config handler;
	private Properties prop;
	private Map classProperties;
	private static ServletConfig servletConfig;
    
	static 
	{
		handler = (Config)PlatformObjectFactory.createInstance(it.cnr.jada.util.Config.class, "was35");
	}
	
	public Config()
	{
		prop = null;
		classProperties = new HashMap();	
	}

	public static Config getHandler()
	{
		return handler;
	}

	public Locale getLocale()
	{
		return Locale.ITALY;
	}

	public Properties getProperties()
	{
		if(prop == null)
			try
			{
				prop = loadProperties("application");
			}
			catch(IOException _ex) { }
		return prop;
	}

	public Properties getProperties(Class class1)
	{
		Properties properties = (Properties)classProperties.get(class1);
		if(properties == null)
			try
			{
				if(class1.getSuperclass() != null)
					properties = new Properties(getProperties(class1.getSuperclass()));
				else
					properties = properties = new Properties();
				classProperties.put(class1, properties);
				loadProperties(class1, properties);
			}
			catch(IOException _ex) { }
		return properties;
	}

	public String getProperty(Class class1, String s)
	{
		return getProperties(class1).getProperty(s);
	}

	public String getProperty(String s)
	{
		return getProperties().getProperty(s);
	}

	protected Properties loadProperties(Class class1, Properties properties)
		throws IOException
	{
		InputStream inputstream = null; 
		try {
			inputstream = new FileInputStream(new File(path, class1.getName() + ".properties"));
		} catch (IOException e1) {
    		URL resurl = this.getClass().getResource("/" + class1.getPackage().getName().replace('.', '/') + "/" + class1.getSimpleName() + ".properties");
    		if (resurl!=null)
    			inputstream = resurl.openStream();
		}
		if(inputstream != null)
		{
			properties.load(inputstream);
			inputstream.close();
		}
		return properties;
	}

	protected Properties loadProperties(String s)
		throws IOException
	{
		Properties properties = new Properties();
		InputStream inputstream = null; 
		try {
			inputstream = new FileInputStream(new File(path, s + ".properties"));
		} catch (IOException e1) {
    		URL resurl = this.getClass().getResource("/" + s.replace('.', '/') + "/" + ".properties");
    		if (resurl!=null)
    			inputstream = resurl.openStream();
		}
		if (inputstream!=null) {
			properties.load(inputstream);
			inputstream.close();
		}
		return properties;
	}

	public void reset()
	{
		classProperties.clear();
		prop = null;
	}
	public final File getLogPath()
	{
		return logPath;
	}
	public static boolean hasRequestParameter(ServletRequest servletrequest, String s)
		{
			Object obj = (Set)servletrequest.getAttribute("parameterNames");
			if(obj == null)
			{
				int i = 0;
				for(Enumeration enumeration = servletrequest.getParameterNames(); enumeration.hasMoreElements();)
				{
					enumeration.nextElement();
					i++;
				}

				obj = new HashSet(i);
				for(Enumeration enumeration1 = servletrequest.getParameterNames(); enumeration1.hasMoreElements(); ((Set) (obj)).add(enumeration1.nextElement()));
				servletrequest.setAttribute("parameterNames", obj);
			}
			return ((Set) (obj)).contains(s);
		}
    
	/**
	 * @param config
	 */
	public static void setServletConfig(ServletConfig config)
	{
		servletConfig = config;
		String propDir = servletConfig.getServletContext().getRealPath("/");	
		handler.path = new File(propDir + "/prop/");
		handler.logPath = new File(propDir + "/log/");    	
		handler.propFile = new File(handler.path, "application.properties");
        
	}
}