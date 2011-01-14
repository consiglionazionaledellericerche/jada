package it.cnr.jada.util.ejb.was35;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.ejb.UserTransactionWrapper;
import it.cnr.jada.util.WebSphereServices;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.TransactionalStatefulSessionImpl;
import it.cnr.jada.util.ejb.UserTransactionTimeoutException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.*;

class UserTransaction implements Serializable, it.cnr.jada.UserTransaction{
    private TransactionManager tm = null;
    private UserTransactionWrapper userTransactionWrapper;
    private final Object owner;
    private static int aliveUserTransactionWrapperCount = 0;

    public UserTransaction(Object obj){
        owner = obj;
    }

    public void commit() throws RemoteException{
        if(userTransactionWrapper != null)
            try{
                userTransactionWrapper.commit();
            }catch(RollbackException rollbackexception){
                throw new RemoteException("Transaction can't be committed", rollbackexception);
            }catch(RemoteException remoteexception){
                try{
                    throw extractDetail(remoteexception);
                }catch(TransactionRolledbackException _ex){
                    throw new UserTransactionTimeoutException("Transaction timed out", this);
                }catch(RemoteException remoteexception1){
                    throw remoteexception1;
                }catch(Throwable _ex){
                    throw remoteexception;
                }
            }finally{
                removeUserTransactionWrapper();
            }
    }

    private UserTransactionWrapper createUserTransactionWrapper() throws EJBException, RemoteException{
        try{
            if(userTransactionWrapper == null){
                userTransactionWrapper = EJBCommonServices.createUserTransactionWrapper();
                aliveUserTransactionWrapperCount++;
            }
            return userTransactionWrapper;
        }catch(EJBException ejbexception){
            throw ejbexception;
        }catch(RemoteException remoteexception){
            throw remoteexception;
        }catch(RuntimeException runtimeexception){
            throw runtimeexception;
        }catch(Error error){
            throw error;
        }
    }

    private Throwable extractDetail(RemoteException remoteexception) throws RemoteException{
        if(remoteexception.detail instanceof RemoteException)
            extractDetail((RemoteException)remoteexception.detail);
        return remoteexception.detail;
    }

    public final Object getOwner(){
        return owner;
    }

    private RemoteException handleRemoteException(UserTransactionWrapper usertransactionwrapper, RemoteException remoteexception){
        try{
            throw extractDetail(remoteexception);
        }catch(TransactionRolledbackException _ex){
            removeUserTransactionWrapper(usertransactionwrapper);
        }catch(NoSuchObjectException _ex){
            resetUserTransactionWrapper(usertransactionwrapper);
        }catch(UserTransactionTimeoutException usertransactiontimeoutexception){
            removeUserTransactionWrapper(usertransactionwrapper);
            return usertransactiontimeoutexception;
        }catch(Throwable _ex){
            rollbackAndRemoveWrapper(usertransactionwrapper);
        }
        return new UserTransactionTimeoutException("Timeout exception", this);
    }

    public Object invoke(TransactionalStatefulSessionImpl transactionalstatefulsessionimpl, String s, Object aobj[]) throws InvocationTargetException, RemoteException{
        try{
            UserTransactionWrapper usertransactionwrapper = createUserTransactionWrapper();
            if(!usertransactionwrapper.equals(transactionalstatefulsessionimpl.getUserTransactionWrapper()))
                throw new UserTransactionTimeoutException("Timeout exception: tentativo di accedere ad un TransactionalStatefulSessionImpl con uno UserTransactionWrapper diverso da quello che l'ha creato", this);
            Object obj;
            try{
                obj = postInvoke(usertransactionwrapper, usertransactionwrapper.invoke(transactionalstatefulsessionimpl.getEjbObject(), s, aobj));
            }catch(RemoteException remoteexception){
                throw handleRemoteException(usertransactionwrapper, remoteexception);
            }catch(InvocationTargetException invocationtargetexception){
                throw invocationtargetexception;
            }catch(Throwable throwable){
                rollbackAndRemoveWrapper(usertransactionwrapper);
                throw new InvocationTargetException(throwable, "Uncaught exception");
            }
            pingUserTransactionWrapper(usertransactionwrapper);
            return obj;
        }catch(EJBException ejbexception){
            throw new DetailedRuntimeException(ejbexception);
        }
    }

    public Object invoke(String s, String s1, Object aobj[]) throws InvocationTargetException, RemoteException{
        try{
            UserTransactionWrapper usertransactionwrapper = createUserTransactionWrapper();
            Object obj;
            try{
                obj = postInvoke(usertransactionwrapper, usertransactionwrapper.invoke(s, s1, aobj));
            }catch(RemoteException remoteexception){
                throw handleRemoteException(usertransactionwrapper, remoteexception);
            }catch(InvocationTargetException invocationtargetexception){
                throw invocationtargetexception;
            }catch(Throwable throwable){
                rollbackAndRemoveWrapper(usertransactionwrapper);
                throw new InvocationTargetException(throwable, "Uncaught exception");
            }
            pingUserTransactionWrapper(usertransactionwrapper);
            return obj;
        }catch(EJBException ejbexception){
            throw new DetailedRuntimeException(ejbexception);
        }
    }

    public Object invoke(Object ejbobject, String s, Object aobj[])
        throws InvocationTargetException, RemoteException
    {
        try
        {
            UserTransactionWrapper usertransactionwrapper = createUserTransactionWrapper();
            if(ejbobject instanceof TransactionalStatefulSessionImpl)
            {
                TransactionalStatefulSessionImpl transactionalstatefulsessionimpl = (TransactionalStatefulSessionImpl)ejbobject;
                if(!usertransactionwrapper.equals(transactionalstatefulsessionimpl.getUserTransactionWrapper()))
                    throw new UserTransactionTimeoutException("Timeout exception: tentativo di accedere ad un TransactionalStatefulSessionImpl con uno UserTransactionWrapper diverso da quello che l'ha creato", this);
                ejbobject = transactionalstatefulsessionimpl.getEjbObject();
            }
            Object obj;
            try
            {
                obj = postInvoke(usertransactionwrapper, usertransactionwrapper.invoke(ejbobject, s, aobj));
            }
            catch(RemoteException remoteexception)
            {
                throw handleRemoteException(usertransactionwrapper, remoteexception);
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw invocationtargetexception;
            }
            catch(Throwable throwable)
            {
                rollbackAndRemoveWrapper(usertransactionwrapper);
                throw new InvocationTargetException(throwable, "Uncaught exception");
            }
            pingUserTransactionWrapper(usertransactionwrapper);
            return obj;
        }
        catch(EJBException ejbexception)
        {
            throw new DetailedRuntimeException(ejbexception);
        }
    }

    private void pingUserTransactionWrapper(UserTransactionWrapper usertransactionwrapper)
        throws RemoteException
    {
        try
        {
            usertransactionwrapper.ping();
        }
        catch(RemoteException remoteexception)
        {
            throw handleRemoteException(usertransactionwrapper, remoteexception);
        }
    }

    private Object postInvoke(UserTransactionWrapper usertransactionwrapper, Object obj) throws EJBException, RemoteException{
        return obj;
    }

    public void remove(){
        rollbackAndRemoveWrapper();
    }

    private void removeUserTransactionWrapper(){
        removeUserTransactionWrapper(userTransactionWrapper);
    }

    private void removeUserTransactionWrapper(UserTransactionWrapper usertransactionwrapper){
        if(usertransactionwrapper != null)
            try{
                usertransactionwrapper.ejbRemove();
                aliveUserTransactionWrapperCount--;
            }catch(RemoteException remoteexception){
                try{
                    throw extractDetail(remoteexception);
                }catch(NoSuchObjectException _ex) {
                }catch(Throwable _ex) {
                }
            }catch(Throwable _ex) {
            }finally{
                resetUserTransactionWrapper(usertransactionwrapper);
            }
    }

    private void resetUserTransactionWrapper(UserTransactionWrapper usertransactionwrapper){
        if(userTransactionWrapper == usertransactionwrapper)
            userTransactionWrapper = null;
    }

    public void rollback() throws EJBException, RemoteException{
        rollbackAndRemoveWrapper();
    }

    private void rollbackAndRemoveWrapper(){
        rollbackAndRemoveWrapper(userTransactionWrapper);
    }

    private void rollbackAndRemoveWrapper(UserTransactionWrapper usertransactionwrapper){
        if(usertransactionwrapper != null)
            try{
                usertransactionwrapper.rollback();
                removeUserTransactionWrapper(usertransactionwrapper);
            }catch(RemoteException remoteexception){
                try{
                    throw extractDetail(remoteexception);
                }catch(TransactionRolledbackException _ex) { 
                }catch(NoSuchObjectException _ex){
                    resetUserTransactionWrapper(usertransactionwrapper);
                    usertransactionwrapper = null;
                }catch(UserTransactionTimeoutException _ex) { 
                }catch(Throwable _ex) {
                }
            }catch(Throwable _ex) { 
            }
    }

    public String toString(){
        if(userTransactionWrapper != null)
            return getClass().getName() + "@" + userTransactionWrapper;
        else
            return super.toString();
    }
}
