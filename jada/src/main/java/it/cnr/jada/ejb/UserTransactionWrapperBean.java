package it.cnr.jada.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.ejb.*;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.PostRemove;
import javax.transaction.*;

@Stateful(name="JADAEJB_UserTransactionWrapper")
@TransactionManagement(TransactionManagementType.BEAN)
@CacheConfigLongTime
public class UserTransactionWrapperBean implements UserTransactionWrapper{

    @Resource private SessionContext mySessionCtx;
    private static final long serialVersionUID = 0x2c7e5503d9bf9553L;
    private List ejbObjectsToBeRemoved;

    public UserTransactionWrapperBean(){
        ejbObjectsToBeRemoved = null;
    }

    public void addToEjbObjectsToBeRemoved(Object ejbobject){
        if(ejbObjectsToBeRemoved == null)
            ejbObjectsToBeRemoved = new ArrayList();
        ejbObjectsToBeRemoved.add(ejbobject);
    }

    private Object basicInvoke(Object ejbobject, String s, Object aobj[]) throws InvocationTargetException{
        try{
            if(aobj.length > 0 && (aobj[0] instanceof UserContext))
                ((UserContext)aobj[0]).setTransactional(true);
            Object obj = Introspector.invoke(ejbobject, s, aobj);
            addToEjbObjectsToBeRemoved(ejbobject);
            return obj;
        }catch(NoSuchMethodException nosuchmethodexception){
            throw new InvocationTargetException(nosuchmethodexception);
        }catch(IllegalAccessException illegalaccessexception){
            throw new InvocationTargetException(illegalaccessexception);
        }catch(InvocationTargetException invocationtargetexception){
            if(invocationtargetexception.getTargetException() instanceof DetailedRuntimeException)
                throw new InvocationTargetException(((DetailedRuntimeException)invocationtargetexception.getTargetException()).getDetail(), "Uncaught exception on transactional invoke");
            else
                throw invocationtargetexception;
        }catch(Throwable throwable){
            throw new InvocationTargetException(throwable, "Uncaught exception on transactional invoke");
        }
    }

    public void begin() throws RemoteException{
        try{
            UserTransaction usertransaction = mySessionCtx.getUserTransaction();
            if(usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
                usertransaction.begin();
            EJBCommonServices.lockTransaction();
        }catch(NotSupportedException notsupportedexception){
            throw new RemoteException("Can't begin transaction", notsupportedexception);
        }catch(SystemException systemexception){
            throw new RemoteException("Can't begin transaction", systemexception);
        }catch(EJBException ejbexception){
            throw new RemoteException("Can't begin transaction", ejbexception);
        }
    }

    public void begin(int i) throws RemoteException{
        try{
            UserTransaction usertransaction = mySessionCtx.getUserTransaction();
            usertransaction.setTransactionTimeout(i);
            if(usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
                usertransaction.begin();
            EJBCommonServices.lockTransaction();
        }catch(NotSupportedException notsupportedexception){
            throw new RemoteException("Can't begin transaction", notsupportedexception);
        }catch(SystemException systemexception){
            throw new RemoteException("Can't begin transaction", systemexception);
        }catch(EJBException ejbexception){
            throw new RemoteException("Can't begin transaction", ejbexception);
        }
    }

    public void commit() throws RemoteException, RollbackException{
        try{
            EJBCommonServices.unlockTransaction();
            mySessionCtx.getUserTransaction().commit();
        }catch(SystemException systemexception){
            throw new RemoteException("System exception", systemexception);
        }catch(HeuristicMixedException heuristicmixedexception){
            throw new RollbackException(heuristicmixedexception.getMessage());
        }catch(HeuristicRollbackException heuristicrollbackexception){
            throw new RollbackException(heuristicrollbackexception.getMessage());
        }catch(EJBException ejbexception){
            throw new RemoteException("Can't commit transaction", ejbexception);
        }
    }
    @PostConstruct
    public void ejbCreate() throws CreateException{
        EJBTracer.getInstance().incrementActiveUserTransactionCounter();
    }
    
    @Remove
    public void ejbRemove() throws RemoteException{
    	if(ejbObjectsToBeRemoved != null){
            for(Iterator iterator = ejbObjectsToBeRemoved.iterator(); iterator.hasNext();)
                try{
                	Object obj = iterator.next();
                	if (obj instanceof TransactionalBulkLoaderIterator){
                		((TransactionalBulkLoaderIterator)obj).ejbRemove();
                	}else if (obj instanceof GenericComponentSession) {
                		((GenericComponentSession)obj).ejbRemove();
					}
                }catch(Throwable _ex) { }
            ejbObjectsToBeRemoved = null;
        }
        EJBTracer.getInstance().decrementActiveUserTransactionCounter();
    }

    public Object invoke(String s, String s1, Object aobj[]) throws InvocationTargetException, RemoteException{
        try{
            testUserTransaction();
            return basicInvoke(EJBCommonServices.createEJB(s), s1, aobj);
        }catch(UserTransactionTimeoutException usertransactiontimeoutexception){
            throw new InvocationTargetException(usertransactiontimeoutexception);
        }catch(EJBException ejbexception){
            throw new RemoteException("Can't create EJB", ejbexception);
        }
    }

    public Object invoke(Object ejbobject, String s, Object aobj[]) throws InvocationTargetException, RemoteException{
        try{
            testUserTransaction();
            return basicInvoke(ejbobject, s, aobj);
        }catch(UserTransactionTimeoutException usertransactiontimeoutexception){
            throw new InvocationTargetException(usertransactiontimeoutexception);
        }
    }

    public void ping(){
    }

    public void rollback() throws RemoteException{
        try{
            if(mySessionCtx.getUserTransaction().getStatus() == Status.STATUS_ACTIVE)
            	mySessionCtx.getUserTransaction().rollback();
        }catch(SystemException systemexception){
            throw new RemoteException("System exception", systemexception);
        }
    }

    private void testUserTransaction() throws RemoteException, UserTransactionTimeoutException{
        try{
            if(mySessionCtx.getUserTransaction().getStatus() != Status.STATUS_ACTIVE)
                throw new UserTransactionTimeoutException("UserTransaction timed out");
        }catch(SystemException systemexception){
            throw new RemoteException(systemexception.getMessage());
        }
    }
}
