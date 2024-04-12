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

package it.cnr.jada.excel.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.comp.ExcelComponent;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import java.rmi.RemoteException;

/**
 * Bean implementation class for Enterprise Bean: BFRAMEEXCEL_EJB_BframeExcelComponentSession
 */
@Stateless(name = "BFRAMEEXCEL_EJB_BframeExcelComponentSession")
public class BframeExcelComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements BframeExcelComponentSession {
    private ExcelComponent componentObj;

    public static BframeExcelComponentSessionBean newInstance() throws EJBException {
        return new BframeExcelComponentSessionBean();
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    @Remove
    public void ejbRemove() throws EJBException {
        componentObj.release();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new ExcelComponent();
    }

    public Excel_spoolerBulk addQueue(it.cnr.jada.UserContext param0,
                                      it.cnr.jada.util.OrderedHashtable param1,
                                      it.cnr.jada.util.OrderedHashtable param2,
                                      String param3,
                                      java.util.Dictionary param4,
                                      String param5,
                                      String param6,
                                      it.cnr.jada.persistency.sql.ColumnMap param7,
                                      it.cnr.jada.bulk.OggettoBulk param8) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Excel_spoolerBulk result = componentObj.addQueue(param0, param1, param2, param3, param4, param5, param6, param7, param8);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public void deleteJobs(it.cnr.jada.UserContext param0, it.cnr.jada.excel.bulk.Excel_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            componentObj.deleteJobs(param0, param1);
            component_invocation_succes(param0, componentObj);
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = componentObj.queryJobs(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public void cancellaSchedulazione(UserContext param0, Long long1,
                                      String resource) throws ComponentException {
        pre_component_invocation(param0, componentObj);
        try {
            componentObj.cancellaSchedulazione(param0, long1, resource);
            component_invocation_succes(param0, componentObj);
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public Excel_spoolerBulk findExcelSpooler(UserContext param0, Long pg)
            throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            Excel_spoolerBulk result = componentObj.findExcelSpooler(param0, pg);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public void modifyQueue(UserContext param0, Excel_spoolerBulk param1)
            throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            componentObj.modifyQueue(param0, param1);
            component_invocation_succes(param0, componentObj);
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }
}
