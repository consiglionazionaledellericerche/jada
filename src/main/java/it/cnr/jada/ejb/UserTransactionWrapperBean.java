/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.EJBTracer;
import it.cnr.jada.util.ejb.UserTransactionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.*;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Stateful(name = "JADAEJB_UserTransactionWrapper")
@TransactionManagement(TransactionManagementType.BEAN)
@CacheConfigLongTime
public class UserTransactionWrapperBean implements UserTransactionWrapper {

    static final long serialVersionUID = 0x2c7e5503d9bf9553L;
    private static final Logger logger = LoggerFactory.getLogger(UserTransactionWrapperBean.class);
    @Resource
    private SessionContext mySessionCtx;
    @Resource
    private TransactionSynchronizationRegistry registry;
    private Map<String, Object> ejbObjectsToBeRemoved;

    public UserTransactionWrapperBean() {
        ejbObjectsToBeRemoved = null;
    }

    public void addToEjbObjectsToBeRemoved(Object ejbobject) {
        if (ejbObjectsToBeRemoved == null)
            ejbObjectsToBeRemoved = new HashMap<String, Object>();
        ejbObjectsToBeRemoved.put(ejbobject.toString(), ejbobject);
    }

    private Object basicInvoke(Object ejbobject, String s, Object aobj[]) throws InvocationTargetException {
        try {
            if (aobj == null || aobj.length == 0) {
                Object obj = Introspector.invoke(ejbobject, s, aobj);
                if (s.equals("ejbRemove") && ejbObjectsToBeRemoved != null)
                    ejbObjectsToBeRemoved.remove(ejbobject.toString());
                return obj;
            } else {
                if (aobj.length > 0 && (aobj[0] instanceof UserContext)) {
                    addToEjbObjectsToBeRemoved(ejbobject);
                    ((UserContext) aobj[0]).setTransactional(true);
                }
                Object obj = Introspector.invoke(ejbobject, s, aobj);
                return obj;
            }
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new InvocationTargetException(nosuchmethodexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new InvocationTargetException(illegalaccessexception);
        } catch (InvocationTargetException invocationtargetexception) {
            if (invocationtargetexception.getTargetException() instanceof DetailedRuntimeException)
                throw new InvocationTargetException(((DetailedRuntimeException) invocationtargetexception.getTargetException()).getDetail(), "Uncaught exception on transactional invoke");
            else
                throw invocationtargetexception;
        } catch (Throwable throwable) {
            throw new InvocationTargetException(throwable, "Uncaught exception on transactional invoke");
        }
    }

    public void begin() throws RemoteException {
        try {
            UserTransaction usertransaction = mySessionCtx.getUserTransaction();
            if (usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
                usertransaction.begin();
            EJBCommonServices.lockTransaction();
        } catch (NotSupportedException notsupportedexception) {
            throw new RemoteException("Can't begin transaction", notsupportedexception);
        } catch (SystemException systemexception) {
            throw new RemoteException("Can't begin transaction", systemexception);
        } catch (EJBException ejbexception) {
            throw new RemoteException("Can't begin transaction", ejbexception);
        }
    }

    public void begin(int i) throws RemoteException {
        try {
            UserTransaction usertransaction = mySessionCtx.getUserTransaction();
            usertransaction.setTransactionTimeout(i);
            if (usertransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
                usertransaction.begin();
            EJBCommonServices.lockTransaction();
        } catch (NotSupportedException notsupportedexception) {
            throw new RemoteException("Can't begin transaction", notsupportedexception);
        } catch (SystemException systemexception) {
            throw new RemoteException("Can't begin transaction", systemexception);
        } catch (EJBException ejbexception) {
            throw new RemoteException("Can't begin transaction", ejbexception);
        }
    }

    public void commit() throws RemoteException, RollbackException {
        try {
            EJBCommonServices.unlockTransaction();
            mySessionCtx.getUserTransaction().commit();
        } catch (SystemException systemexception) {
            throw new RemoteException("System exception", systemexception);
        } catch (HeuristicMixedException heuristicmixedexception) {
            throw new RollbackException(heuristicmixedexception.getMessage());
        } catch (HeuristicRollbackException heuristicrollbackexception) {
            throw new RollbackException(heuristicrollbackexception.getMessage());
        } catch (EJBException ejbexception) {
            throw new RemoteException("Can't commit transaction", ejbexception);
        }
    }

    @PostConstruct
    public void ejbCreate() {
        EJBTracer.getInstance().incrementActiveUserTransactionCounter();
    }

    @Remove
    public void ejbRemove() throws RemoteException {
        if (ejbObjectsToBeRemoved != null) {
            for (Iterator<Object> iterator = ejbObjectsToBeRemoved.values().iterator(); iterator.hasNext(); )
                try {
                    Object obj = iterator.next();
                    if (obj instanceof TransactionalBulkLoaderIterator) {
                        ((TransactionalBulkLoaderIterator) obj).ejbRemove();
                    } else if (obj instanceof GenericComponentSession) {
                        ((GenericComponentSession) obj).ejbRemove();
                    }
                } catch (Throwable _ex) {
                    logger.error("Exception while remove EJB:", _ex);
                }
            ejbObjectsToBeRemoved = null;
        }
        EJBTracer.getInstance().decrementActiveUserTransactionCounter();
    }

    public Object invoke(String s, String s1, Object aobj[]) throws InvocationTargetException, RemoteException {
        try {
            testUserTransaction();
            return basicInvoke(EJBCommonServices.createEJB(s), s1, aobj);
        } catch (UserTransactionTimeoutException usertransactiontimeoutexception) {
            throw new InvocationTargetException(usertransactiontimeoutexception);
        } catch (EJBException ejbexception) {
            throw new RemoteException("Can't create EJB", ejbexception);
        }
    }

    public Object invoke(Object ejbobject, String s, Object aobj[]) throws InvocationTargetException, RemoteException {
        try {
            testUserTransaction();
            return basicInvoke(ejbobject, s, aobj);
        } catch (UserTransactionTimeoutException usertransactiontimeoutexception) {
            throw new InvocationTargetException(usertransactiontimeoutexception);
        }
    }

    public void ping() {
        logger.debug("Ping userTransaction:" + registry.getTransactionKey());
    }

    public void rollback() throws RemoteException {
        try {
            if (mySessionCtx.getUserTransaction().getStatus() == Status.STATUS_ACTIVE)
                mySessionCtx.getUserTransaction().rollback();
        } catch (SystemException systemexception) {
            throw new RemoteException("System exception", systemexception);
        }
    }

    private void testUserTransaction() throws RemoteException {
        try {
            if (mySessionCtx.getUserTransaction().getStatus() != Status.STATUS_ACTIVE)
                throw new UserTransactionTimeoutException("UserTransaction timed out");
        } catch (SystemException systemexception) {
            throw new RemoteException(systemexception.getMessage());
        }
    }
}
