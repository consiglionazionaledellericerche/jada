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

//import org.xml.sax.Parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

// Referenced classes of package it.cnr.jada.util:
//            PlatformObjectFactory

public abstract class SAXParserFactory {

    private static final SAXParserFactory defaultFactory;

    static {
        defaultFactory = (SAXParserFactory) PlatformObjectFactory.createInstance(it.cnr.jada.util.SAXParserFactory.class, "was35");
    }

    protected SAXParserFactory() {
    }

    public static final SAXParserFactory getDefaultFactory() {
        return defaultFactory;
    }

    public abstract SAXParser createParser()
            throws ParserConfigurationException, SAXException;
}