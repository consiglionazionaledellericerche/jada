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
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.ejb.TransactionalStatefulSessionImpl;
import it.cnr.jada.util.ejb.UserTransactionTimeoutException;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.ejb:
//            TransactionalBulkLoaderIterator

public class TransactionalTransactionalBulkLoaderIterator extends TransactionalStatefulSessionImpl
        implements TransactionalBulkLoaderIterator {

    public TransactionalTransactionalBulkLoaderIterator() {
    }

    public void close()
            throws RemoteException {
        try {
            invoke("close", new Object[0]);
        } catch (UserTransactionTimeoutException _ex) {
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public int countElements()
            throws RemoteException {
        try {
            return ((Integer) invoke("countElements", new Object[0])).intValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public int countPages()
            throws RemoteException {
        try {
            return ((Integer) invoke("countPages", new Object[0])).intValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public int getOrderBy(String s)
            throws RemoteException {
        try {
            return ((Integer) invoke("getOrderBy", new Object[]{
                    s
            })).intValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public boolean hasMoreElements()
            throws RemoteException {
        try {
            return ((Boolean) invoke("hasMoreElements", new Object[0])).booleanValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public boolean hasMorePages()
            throws RemoteException {
        try {
            return ((Boolean) invoke("hasMorePages", new Object[0])).booleanValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public boolean isOrderableBy(String s)
            throws RemoteException {
        try {
            return ((Boolean) invoke("isOrderableBy", new Object[]{
                    s
            })).booleanValue();
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void moveTo(int i)
            throws RemoteException {
        try {
            invoke("moveTo", new Object[]{
                    new Integer(i)
            });
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void moveToPage(int i)
            throws RemoteException {
        try {
            invoke("moveToPage", new Object[]{
                    new Integer(i)
            });
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public Object nextElement()
            throws RemoteException {
        try {
            return invoke("nextElement", new Object[0]);
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public Object[] nextPage()
            throws RemoteException {
        try {
            return (Object[]) invoke("nextPage", new Object[0]);
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void open(UserContext usercontext)
            throws RemoteException, DetailedRuntimeException, ComponentException {
        try {
            invoke("open", new Object[]{
                    usercontext
            });
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (ComponentException componentexception) {
                throw componentexception;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void refresh()
            throws RemoteException {
        try {
            invoke("refresh", new Object[0]);
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void setOrderBy(String s, int i)
            throws RemoteException {
        try {
            invoke("setOrderBy", new Object[]{
                    s, new Integer(i)
            });
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void setPageSize(int i)
            throws RemoteException {
        try {
            invoke("setPageSize", new Object[]{
                    new Integer(i)
            });
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    /* (non-Javadoc)
     * @see it.cnr.jada.ejb.TransactionalBulkLoaderIterator#getQuery()
     */
    public Query getQuery() throws DetailedRuntimeException, RemoteException {
        try {
            return (Query) invoke("getQuery", new Object[0]);
        } catch (RemoteException remoteexception) {
            throw remoteexception;
        } catch (InvocationTargetException invocationtargetexception) {
            try {
                throw invocationtargetexception.getTargetException();
            } catch (RemoteException remoteexception1) {
                throw remoteexception1;
            } catch (Throwable throwable) {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
            throws CreateException, ComponentException {
        // TODO Auto-generated method stub

    }

    public void ejbCreate(UserContext usercontext, Query query1,
                          Class<?> class1, String s) throws CreateException,
            ComponentException {
        // TODO Auto-generated method stub

    }

    public void ejbRemove() throws EJBException {
        // TODO Auto-generated method stub

    }
}
