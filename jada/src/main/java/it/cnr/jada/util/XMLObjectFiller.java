package it.cnr.jada.util;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

// Referenced classes of package it.cnr.jada.util:
//            SAXParserFactory, Introspector

public class XMLObjectFiller extends DefaultHandler
    implements Serializable
{

    public XMLObjectFiller()
        throws ParserConfigurationException, SAXException
    {
        objects = new Stack();
        elementClasses = new Hashtable();
        elementSetters = new Hashtable();
        parser = createParser();
        //parser.setDocumentHandler(this);
    }

    public XMLObjectFiller(Object obj)
        throws ParserConfigurationException, SAXException
    {
        this();
        document = obj;
    }

    public static SAXParser createParser()
        throws ParserConfigurationException, SAXException
    {
        return parserFactory.createParser();
    }

	public void endElement(String uri, String localName, String qName)
		throws SAXException
	{
        endElement(qName);
	}
    public void endElement(String s)
        throws SAXException
    {
        objects.pop();
        if(objects.isEmpty())
            currentObject = root = null;
        else
            currentObject = objects.peek();
    }

    public void mapElement(String s, Class class1, String s1)
    {
        mapElementToClass(s, class1);
        mapElementToSetter(s, s1);
    }

    public void mapElementToClass(String s, Class class1)
    {
        elementClasses.put(s, class1);
    }

    public void mapElementToSetter(String s, String s1)
    {
        elementSetters.put(s, s1);
    }

    public Object parse(InputSource inputsource)
        throws IOException, SAXException
    {
        parser.parse(inputsource,this);
        return document;
    }

    public void push(Object obj)
    {
        objects.push(currentObject = obj);
    }

	public void startElement(String uri, String localName, String qName, Attributes attributelist)
		throws SAXException
	{
		startElement(qName,attributelist);
	}
    public void startElement(String s, Attributes attributelist)
        throws SAXException
    {
        Object obj = null;
        Class class1 = (Class)elementClasses.get(s);
        if(class1 != null)
            if(objects.isEmpty() && document != null && document.getClass() == class1)
                obj = document;
            else
                try
                {
                    obj = class1.newInstance();
                }
                catch(Throwable throwable)
                {
                    throwable.printStackTrace();
                }
        if(obj != null){
            for(int i = 0; i < attributelist.getLength(); i++){
                String s2 = attributelist.getQName(i);
                if (!(s.equalsIgnoreCase("bulkInfo") && 
                		(s2.equalsIgnoreCase("xmlns:xsi") ||
                		 s2.equalsIgnoreCase("xmlns")||
                		 s2.equalsIgnoreCase("xsi:schemaLocation")))){
                    try{
                        Introspector.setPropertyValue(obj, s2, attributelist.getValue(i));
                    }catch(Throwable throwable3){
                        throwable3.printStackTrace();
                    }
                }
            }

            if(currentObject != null)
            {
                String s1 = (String)elementSetters.get(s);
                if(s1 != null)
                    try
                    {
                        Introspector.invoke(currentObject, s1, obj);
                    }
                    catch(InvocationTargetException invocationtargetexception)
                    {
                        invocationtargetexception.printStackTrace();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable1.printStackTrace();
                    }
                else
                    try
                    {
                        Introspector.setPropertyValue(currentObject, s, obj);
                    }
                    catch(InvocationTargetException invocationtargetexception1)
                    {
                        if(invocationtargetexception1.getTargetException() instanceof RuntimeException)
                            throw (RuntimeException)invocationtargetexception1.getTargetException();
                    }
                    catch(Throwable throwable2)
                    {
                        throwable2.printStackTrace();
                    }
            }
        }
        push(obj);
    }

    private Object document;
    private Object root;
    private Object currentObject;
    private Stack objects;
    private Hashtable elementClasses;
    private Hashtable elementSetters;
    private SAXParser parser;
    private static final SAXParserFactory parserFactory = SAXParserFactory.getDefaultFactory();

}