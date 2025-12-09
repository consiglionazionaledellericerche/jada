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

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletRequest;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Config
        implements Serializable {
    private static Config handler;
    private static ServletConfig servletConfig;

    static {
        handler = (Config) PlatformObjectFactory.createInstance(it.cnr.jada.util.Config.class, "was35");
    }

    private File logPath;
    private File path;
    private File propFile;
    private Properties prop;
    private Map classProperties;

    public Config() {
        prop = null;
        classProperties = new HashMap();
    }

    public static Config getHandler() {
        return handler;
    }

    public static boolean hasRequestParameter(ServletRequest servletrequest, String s) {
        Object obj = servletrequest.getAttribute("parameterNames");
        if (obj == null) {
            int i = 0;
            for (Enumeration enumeration = servletrequest.getParameterNames(); enumeration.hasMoreElements(); ) {
                enumeration.nextElement();
                i++;
            }

            obj = new HashSet(i);
            for (Enumeration enumeration1 = servletrequest.getParameterNames(); enumeration1.hasMoreElements(); ((Set) (obj)).add(enumeration1.nextElement()))
                ;
            servletrequest.setAttribute("parameterNames", obj);
        }
        return ((Set) (obj)).contains(s);
    }

    /**
     * @param config
     */
    public static void setServletConfig(ServletConfig config) {
        servletConfig = config;
        String propDir = servletConfig.getServletContext().getRealPath("/");
        handler.path = new File(propDir + "/prop/");
        handler.logPath = new File(propDir + "/log/");
        handler.propFile = new File(handler.path, "application.properties");

    }

    public Locale getLocale() {
        return Locale.ITALY;
    }

    public Properties getProperties() {
        if (prop == null)
            try {
                prop = loadProperties("application");
            } catch (IOException _ex) {
            }
        return prop;
    }

    public Properties getProperties(Class class1) {
        Properties properties = (Properties) classProperties.get(class1);
        if (properties == null)
            try {
                if (class1.getSuperclass() != null)
                    properties = new Properties(getProperties(class1.getSuperclass()));
                else
                    properties = properties = new Properties();
                classProperties.put(class1, properties);
                loadProperties(class1, properties);
            } catch (IOException _ex) {
            }
        return properties;
    }

    public String getProperty(Class class1, String s) {
        return getProperties(class1).getProperty(s);
    }

    public String getProperty(String s) {
        return getProperties().getProperty(s);
    }

    protected Properties loadProperties(Class class1, Properties properties)
            throws IOException {
        InputStream inputstream = null;
        try {
            inputstream = new FileInputStream(new File(path, class1.getName() + ".properties"));
        } catch (IOException e1) {
            URL resurl = this.getClass().getResource("/" + class1.getPackage().getName().replace('.', '/') + "/" + class1.getSimpleName() + ".properties");
            if (resurl != null)
                inputstream = resurl.openStream();
        }
        if (inputstream != null) {
            properties.load(inputstream);
            inputstream.close();
        }
        return properties;
    }

    protected Properties loadProperties(String s)
            throws IOException {
        Properties properties = new Properties();
        InputStream inputstream = null;
        try {
            inputstream = new FileInputStream(new File(path, s + ".properties"));
        } catch (IOException e1) {
            URL resurl = this.getClass().getResource("/" + s.replace('.', '/') + "/" + ".properties");
            if (resurl != null)
                inputstream = resurl.openStream();
        }
        if (inputstream != null) {
            properties.load(inputstream);
            inputstream.close();
        }
        return properties;
    }

    public void reset() {
        classProperties.clear();
        prop = null;
    }

    public final File getLogPath() {
        return logPath;
    }
}