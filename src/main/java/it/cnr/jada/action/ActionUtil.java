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

package it.cnr.jada.action;

import it.cnr.jada.util.XMLObjectFiller;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ActionUtil {
    /**
     * Forza la rilettura del file di configurazione della mappatura
     */
    public static ActionMappings reloadActions(File actionDirFile) throws ActionMappingsConfigurationException {
        ActionMappings actionmappings = new ActionMappings();
        File[] afile = actionDirFile.listFiles();
        try {
            XMLObjectFiller xmlobjectfiller = new XMLObjectFiller(actionmappings);
            xmlobjectfiller.mapElementToClass("action-mappings", it.cnr.jada.action.ActionMappings.class);
            xmlobjectfiller.mapElement("action", it.cnr.jada.action.ActionMapping.class, "addActionMapping");
            xmlobjectfiller.mapElement("businessProcess", it.cnr.jada.action.BusinessProcessMapping.class, "addBusinessProcessMapping");
            xmlobjectfiller.mapElement("forward", it.cnr.jada.action.StaticForward.class, "addForward");
            xmlobjectfiller.mapElement("init-param", it.cnr.jada.action.InitParameter.class, "addInitParameter");
            for (int i = 0; i < afile.length; i++)
                if (afile[i].isFile() && afile[i].getName().endsWith(".xml") && afile[i].canRead()) {
                    String encoding = System.getProperty("SIGLA_ENCODING", "UTF-8");
                    xmlobjectfiller.parse(new InputSource(new InputStreamReader(new FileInputStream(afile[i]), encoding)));
                }
        } catch (SAXException saxexception) {
            throw new ActionMappingsConfigurationException(saxexception);
        } catch (ParserConfigurationException parserConfigurationException) {
            throw new ActionMappingsConfigurationException(parserConfigurationException);
        } catch (IOException ioexception) {
            throw new ActionMappingsConfigurationException(ioexception);
        }
        return actionmappings;
    }

}
