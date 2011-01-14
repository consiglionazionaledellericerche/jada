package it.cnr.jada.util;

//import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

// Referenced classes of package it.cnr.jada.util:
//            PlatformObjectFactory

public abstract class SAXParserFactory
{

    protected SAXParserFactory()
    {
    }

    public abstract SAXParser createParser()
        throws ParserConfigurationException,SAXException;

    public static final SAXParserFactory getDefaultFactory()
    {
        return defaultFactory;
    }

    private static final SAXParserFactory defaultFactory;

    static 
    {
        defaultFactory = (SAXParserFactory)PlatformObjectFactory.createInstance(it.cnr.jada.util.SAXParserFactory.class, "was35");
    }
}