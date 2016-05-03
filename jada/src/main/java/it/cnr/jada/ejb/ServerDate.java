package it.cnr.jada.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import javax.ejb.EJBException;
import javax.ejb.Remote;
@Remote
public interface ServerDate{

    public abstract Timestamp getServerDate()
        throws RemoteException, EJBException;

    public abstract Timestamp getServerTimestamp()
        throws RemoteException, EJBException;
}