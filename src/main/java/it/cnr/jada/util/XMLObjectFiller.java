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

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Stack;

// Referenced classes of package it.cnr.jada.util:
//            SAXParserFactory, Introspector

public class XMLObjectFiller extends DefaultHandler
        implements Serializable {

    private static final SAXParserFactory parserFactory = SAXParserFactory.getDefaultFactory();
    private Object document;
    private Object root;
    private Object currentObject;
    private Stack objects;
    private Hashtable elementClasses;
    private Hashtable elementSetters;
    private SAXParser parser;

    public XMLObjectFiller()
            throws ParserConfigurationException, SAXException {
        objects = new Stack();
        elementClasses = new Hashtable();
        elementSetters = new Hashtable();
        parser = createParser();
        //parser.setDocumentHandler(this);
    }

    public XMLObjectFiller(Object obj)
            throws ParserConfigurationException, SAXException {
        this();
        document = obj;
    }

    public static SAXParser createParser()
            throws ParserConfigurationException, SAXException {
        return parserFactory.createParser();
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        endElement(qName);
    }

    public void endElement(String s)
            throws SAXException {
        objects.pop();
        if (objects.isEmpty())
            currentObject = root = null;
        else
            currentObject = objects.peek();
    }

    public void mapElement(String s, Class class1, String s1) {
        mapElementToClass(s, class1);
        mapElementToSetter(s, s1);
    }

    public void mapElementToClass(String s, Class class1) {
        elementClasses.put(s, class1);
    }

    public void mapElementToSetter(String s, String s1) {
        elementSetters.put(s, s1);
    }

    public Object parse(InputSource inputsource)
            throws IOException, SAXException {
        parser.parse(inputsource, this);
        return document;
    }

    public void push(Object obj) {
        objects.push(currentObject = obj);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributelist)
            throws SAXException {
        startElement(qName, attributelist);
    }

    public void startElement(String s, Attributes attributelist)
            throws SAXException {
        Object obj = null;
        Class class1 = (Class) elementClasses.get(s);
        if (class1 != null)
            if (objects.isEmpty() && document != null && document.getClass() == class1)
                obj = document;
            else
                try {
                    obj = class1.newInstance();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
        if (obj != null) {
            for (int i = 0; i < attributelist.getLength(); i++) {
                String s2 = attributelist.getQName(i);
                if (!(s.equalsIgnoreCase("bulkInfo") &&
                        (s2.equalsIgnoreCase("xmlns:xsi") ||
                                s2.equalsIgnoreCase("xmlns") ||
                                s2.equalsIgnoreCase("xsi:schemaLocation")))) {
                    try {
                        Introspector.setPropertyValue(obj, s2, attributelist.getValue(i));
                    } catch (Throwable throwable3) {
                        throwable3.printStackTrace();
                    }
                }
            }

            if (currentObject != null) {
                String s1 = (String) elementSetters.get(s);
                if (s1 != null)
                    try {
                        Introspector.invoke(currentObject, s1, obj);
                    } catch (InvocationTargetException invocationtargetexception) {
                        invocationtargetexception.printStackTrace();
                    } catch (Throwable throwable1) {
                        throwable1.printStackTrace();
                    }
                else
                    try {
                        Introspector.setPropertyValue(currentObject, s, obj);
                    } catch (InvocationTargetException invocationtargetexception1) {
                        if (invocationtargetexception1.getTargetException() instanceof RuntimeException)
                            throw (RuntimeException) invocationtargetexception1.getTargetException();
                    } catch (Throwable throwable2) {
                        throwable2.printStackTrace();
                    }
            }
        }
        push(obj);
    }

}