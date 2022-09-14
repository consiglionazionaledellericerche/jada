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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.rmi.RemoteException;
import java.util.List;

public class TransactionalCRUDComponentSession extends TransactionalSessionImpl implements CRUDComponentSession {
    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, it.cnr.jada.bulk.OggettoBulk param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("creaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerInserimento", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerModifica", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("inizializzaBulkPerModifica", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicerca", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicercaLibera", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public boolean isLockedBulk(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isLockedBulk", new Object[]{
                    userContext,
                    oggettoBulk});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Persistent findByPrimaryKey(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, RemoteException {
        try {
            return (Persistent) invoke("findByPrimaryKey", new Object[]{
                    userContext,
                    oggettoBulk});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public <T extends OggettoBulk, U extends OggettoBulk> List<U> find(UserContext userContext, Class<T> oggettoBulk, String methodName, Object... parameters) throws ComponentException, RemoteException {
        try {
            return (List<U>) invoke("find", new Object[]{
                    userContext,
                    oggettoBulk,
                    methodName,
                    parameters
            });
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, String param3, Object... param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4
            });
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
}
