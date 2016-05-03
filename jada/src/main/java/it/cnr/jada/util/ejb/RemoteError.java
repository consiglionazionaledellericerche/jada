package it.cnr.jada.util.ejb;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;
import java.rmi.RemoteException;

public class RemoteError extends DetailedRuntimeException
    implements Serializable
{

    public RemoteError(RemoteException remoteexception)
    {
        super(remoteexception.getMessage(), remoteexception.detail);
    }
}