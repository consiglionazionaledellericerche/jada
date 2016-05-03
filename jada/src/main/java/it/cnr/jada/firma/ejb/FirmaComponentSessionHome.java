package it.cnr.jada.firma.ejb;
import java.rmi.*;
import javax.ejb.*;

public interface FirmaComponentSessionHome extends javax.ejb.EJBHome {
FirmaComponentSession create() throws RemoteException, CreateException;
}
