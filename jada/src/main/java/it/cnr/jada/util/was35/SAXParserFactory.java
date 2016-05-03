package it.cnr.jada.util.was35;

//import com.ibm.xml.parsers.SAXParser;
//import org.xml.sax.Parser;
import javax.xml.parsers.*;

import org.xml.sax.SAXException;
import javax.xml.parsers.*;

public class SAXParserFactory extends it.cnr.jada.util.SAXParserFactory
{
    private static javax.xml.parsers.SAXParserFactory parserFactory = javax.xml.parsers.SAXParserFactory.newInstance(); 
    public SAXParserFactory()
    {
    }

    public SAXParser createParser()
        throws ParserConfigurationException, SAXException
    {
        return parserFactory.newSAXParser();
    }
}