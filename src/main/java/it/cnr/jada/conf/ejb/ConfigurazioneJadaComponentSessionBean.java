package it.cnr.jada.conf.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="JADAEJB_ConfigurazioneJadaComponentSession")
public class ConfigurazioneJadaComponentSessionBean  extends it.cnr.jada.ejb.GenericComponentSessionBean implements ConfigurazioneJadaComponentSession{
	private it.cnr.jada.conf.comp.ConfigurazioneJadaComponent componentObj;
	
	@PostConstruct
		public void ejbCreate() {
		componentObj = new it.cnr.jada.conf.comp.ConfigurazioneJadaComponent();
	}
	public static 
	ConfigurazioneJadaComponentSessionBean newInstance() throws EJBException {
		return new ConfigurazioneJadaComponentSessionBean();
	}
	public it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk getConfigurazione(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.conf.bulk.ConfigurazioneJadaBulk result = componentObj.getConfigurazione(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void ejbActivate() throws EJBException {
	}
	public void ejbPassivate() throws EJBException {
	}
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
}
