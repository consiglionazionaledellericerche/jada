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

package it.cnr.jada.blobs.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.blobs.bp.CRUDBframeBlobBP;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.ejb.BframeBlobComponentSession;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Insert the type's description here.
 * Creation date: (16/05/2003 16.51.29)
 *
 * @author: CNRADM
 */
public class CRUDBframeBlobAction extends it.cnr.jada.util.action.SelezionatoreListaAlberoAction {
    BframeBlobComponentSession bframeBlobComponentSession = (BframeBlobComponentSession) EJBCommonServices.createEJB("BFRAMEBLOBS_EJB_BframeBlobComponentSession");

    /**
     * CRUDBframeBlobBP constructor comment.
     */
    public CRUDBframeBlobAction() {
        super();
    }

    public Forward doCambiaTipo(ActionContext context) {
        try {
            CRUDBframeBlobBP bp = (CRUDBframeBlobBP) context.getBusinessProcess();
            bp.fillModel(context);
            bp.refreshTree(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doCambiaVisibilita(ActionContext context) {
        try {
            CRUDBframeBlobBP bp = (CRUDBframeBlobBP) context.getBusinessProcess();
            bp.fillModel(context);
            bp.refreshTree(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doCrea(ActionContext context) {
        try {
            bframeBlobComponentSession.insertBlob(context.getUserContext());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doElimina(ActionContext context) {
        try {
            CRUDBframeBlobBP bp = (CRUDBframeBlobBP) context.getBusinessProcess();
            bp.setSelection(context);
            Bframe_blob_pathBulk[] selectedElements = null;
            java.util.List l = bp.getSelectedElements(context);
            if (l.size() == 0) {
                Bframe_blob_pathBulk o = (Bframe_blob_pathBulk) bp.getFocusedElement();
                if (o != null)
                    selectedElements = new Bframe_blob_pathBulk[]{o};
            } else {
                selectedElements = (Bframe_blob_pathBulk[]) l.toArray(new Bframe_blob_pathBulk[l.size()]);
            }
            if (selectedElements == null)
                bp.setMessage("Selezionare almeno una riga.");
            else {
                bframeBlobComponentSession.elimina(context.getUserContext(), selectedElements);

                // Aggiorno il remoteIterator
                bp.refresh(context);

                // Se nella directory corrente non  rimasto nessun file o altra directory
                while (bp.getElementsCount() == 0 && bp.getHistory().size() > 0) {
                    // torno indietro di una directory
                    bp.setParentElement((OggettoBulk) bp.getHistory().pop());
                    bp.getSelection().clear();
                    bp.setLeafElement(false);
                    RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, bp.getChildren(context, bp.getParentElement()));
                    bp.setIterator(context, ri);
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}