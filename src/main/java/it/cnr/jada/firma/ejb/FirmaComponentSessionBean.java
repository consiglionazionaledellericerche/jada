package it.cnr.jada.firma.ejb;
import it.cnr.jada.firma.comp.FirmaComponent;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless(name="JADAEJB_EJB_FirmaComponentSession")
public class FirmaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FirmaComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.jada.firma.comp.FirmaComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
}
