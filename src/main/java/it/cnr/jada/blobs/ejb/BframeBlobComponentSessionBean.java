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

package it.cnr.jada.blobs.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.blobs.comp.BframeBlobComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless(name = "BFRAMEBLOBS_EJB_BframeBlobComponentSession")
public class BframeBlobComponentSessionBean extends it.cnr.jada.ejb.RicercaComponentSessionBean implements BframeBlobComponentSession {
    public static it.cnr.jada.ejb.RicercaComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new BframeBlobComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new BframeBlobComponent();
    }

    @Remove
    public void ejbRemove() throws javax.ejb.EJBException {
        componentObj.release();
    }

    public void elimina(UserContext param0, Bframe_blob_pathBulk param1[])
            throws ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((BframeBlobComponent) componentObj).elimina(param0, param1);
            component_invocation_succes(param0, componentObj);
        } catch (NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public RemoteIterator getBlobChildren(UserContext param0, Bframe_blob_pathBulk param1, String param2)
            throws ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            RemoteIterator result = ((BframeBlobComponent) componentObj).getBlobChildren(param0, param1, param2);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext param0)
            throws ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Selezione_blob_tipoVBulk result = ((BframeBlobComponent) componentObj).getSelezione_blob_tipo(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public void insertBlob(UserContext param0) throws Exception {
        pre_component_invocation(param0, componentObj);
        try {
            ((BframeBlobComponent) componentObj).insertBlob(param0);
            component_invocation_succes(param0, componentObj);

        } catch (NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }
}