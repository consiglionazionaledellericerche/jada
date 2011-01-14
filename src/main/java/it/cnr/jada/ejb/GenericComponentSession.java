package it.cnr.jada.ejb;

import javax.ejb.Remote;

@Remote
public interface GenericComponentSession{
	public abstract String getTransactionalInterface();
	public abstract void ejbRemove() throws javax.ejb.EJBException;
}