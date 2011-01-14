package it.cnr.jada.util.upload;

public class VersionDetector
{

    public VersionDetector()
    {
    }

    public static String getJavaVersion()
    {
        if(javaVersion != null)
            return javaVersion;
        String ver = null;
        try
        {
            ver = "1.0";
            Class.forName("java.lang.Void");
            ver = "1.1";
            Class.forName("java.lang.ThreadLocal");
            ver = "1.2";
            Class.forName("java.lang.StrictMath");
            ver = "1.3";
            Class.forName("java.net.URI");
            ver = "1.4";
        }
        catch(Throwable _ex) { }
        javaVersion = ver;
        return javaVersion;
    }

    public static String getServletVersion()
    {
        if(servletVersion != null)
            return servletVersion;
        String ver = null;
        try
        {
            ver = "1.0";
            Class.forName("javax.servlet.http.HttpSession");
            ver = "2.0";
            Class.forName("javax.servlet.RequestDispatcher");
            ver = "2.1";
            Class.forName("javax.servlet.http.HttpServletResponse").getDeclaredField("SC_EXPECTATION_FAILED");
            ver = "2.2";
            Class.forName("javax.servlet.Filter");
            ver = "2.3";
        }
        catch(Throwable _ex) { }
        servletVersion = ver;
        return servletVersion;
    }

    static String servletVersion;
    static String javaVersion;
}