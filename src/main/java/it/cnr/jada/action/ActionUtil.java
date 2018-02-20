package it.cnr.jada.action;

import it.cnr.jada.util.XMLObjectFiller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class ActionUtil {
    /**
     * Forza la rilettura del file di configurazione della mappatura
     */
    public static ActionMappings reloadActions(File actionDirFile) throws ActionMappingsConfigurationException{
        ActionMappings actionmappings = new ActionMappings();
        File afile[] = actionDirFile.listFiles();
        try{
            XMLObjectFiller xmlobjectfiller = new XMLObjectFiller(actionmappings);
            xmlobjectfiller.mapElementToClass("action-mappings", it.cnr.jada.action.ActionMappings.class);
            xmlobjectfiller.mapElement("action", it.cnr.jada.action.ActionMapping.class, "addActionMapping");
            xmlobjectfiller.mapElement("businessProcess", it.cnr.jada.action.BusinessProcessMapping.class, "addBusinessProcessMapping");
            xmlobjectfiller.mapElement("forward", it.cnr.jada.action.StaticForward.class, "addForward");
            xmlobjectfiller.mapElement("init-param", it.cnr.jada.action.InitParameter.class, "addInitParameter");
            for(int i = 0; i < afile.length; i++)
                if(afile[i].isFile() && afile[i].getName().endsWith(".xml") && afile[i].canRead()){                
                    String encoding = System.getProperty("SIGLA_ENCODING","UTF-8");
			        xmlobjectfiller.parse(new InputSource(new InputStreamReader(new FileInputStream(afile[i]),encoding)));
                }
        }catch(SAXException saxexception){
            throw new ActionMappingsConfigurationException(saxexception);
        }catch(ParserConfigurationException parserConfigurationException){
			throw new ActionMappingsConfigurationException(parserConfigurationException);
		}catch(IOException ioexception){
            throw new ActionMappingsConfigurationException(ioexception);
        }
        return actionmappings;
    }

}
