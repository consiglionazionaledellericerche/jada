package it.cnr.jada.conf.ejb;
import javax.ejb.Remote;
@Remote
public interface ConfigurazioneJadaComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
	 it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk getConfigurazione(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
