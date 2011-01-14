package it.cnr.jada.conf.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import java.io.Serializable;


public class ConfigurazioneJadaComponent extends it.cnr.jada.comp.GenericComponent implements Cloneable,Serializable 
{
    public  ConfigurazioneJadaComponent()
    {
    }
    public it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk getConfigurazione (UserContext userContext,String chiave) throws ComponentException {
		try {
			return (it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk)getHome(userContext,it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk.class).findByPrimaryKey(new it.cnr.jada.conf.bulk.ConfigurazioneJadaKey(chiave));
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
    }
}
