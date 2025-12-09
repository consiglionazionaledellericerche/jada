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

package it.cnr.jada.blobs.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.blobs.ejb.BframeBlobComponentSession;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteBulkTree;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaAlberoBP;
import it.cnr.jada.util.action.SimpleNestedFormController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.PageContext;

import java.rmi.RemoteException;

public class CRUDBframeBlobBP extends SelezionatoreListaAlberoBP {
    private final SimpleNestedFormController selezione_blob_tipoController;
    private final RemoteBframe_blob_pathTree tree;
    private Selezione_blob_tipoVBulk selezione_blob_tipo;
    private boolean readonly;

    public CRUDBframeBlobBP() {
        this("");
    }

    public CRUDBframeBlobBP(String s) {
        super(s);
        selezione_blob_tipoController = new SimpleNestedFormController("selezione_blob_tipo", it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk.class, this);
        tree = new RemoteBframe_blob_pathTree();
        setBulkInfo(BulkInfo.getBulkInfo(it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class));
        readonly = s.indexOf('M') < 0;
        super.table.setMultiSelection(true);
    }

    public BframeBlobComponentSession createComponentSession()
            throws BusinessProcessException {
        return (BframeBlobComponentSession) createComponentSession("BFRAMEBLOBS_EJB_BframeBlobComponentSession", it.cnr.jada.blobs.ejb.BframeBlobComponentSession.class);
    }

    public Button[] createToolbar() {
        Button[] abutton = new Button[4];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.expand");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.back");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.delete");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.download");
        return abutton;
    }

    public Bframe_blob_pathBulk getBlob_path() {
        return (Bframe_blob_pathBulk) getFocusedElement();
    }

    public String getDownloadUrl(PageContext pageContext) {
        Bframe_blob_pathBulk bframe_blob_pathbulk = getBlob_path();
        if (bframe_blob_pathbulk == null)
            return null;
        StringBuffer stringbuffer = new StringBuffer(JSPUtils.getAppRoot((HttpServletRequest) pageContext.getRequest()) + "download_blob/");
        stringbuffer.append(bframe_blob_pathbulk.getCd_tipo());
        stringbuffer.append('/');
        if (bframe_blob_pathbulk.getPath() != null)
            stringbuffer.append(bframe_blob_pathbulk.getRelativepath());
        if (bframe_blob_pathbulk.getFilename() != null)
            stringbuffer.append(bframe_blob_pathbulk.getFilename());
        return stringbuffer.toString();
    }

    public String getFormTitle() {
        return "<script>document.write(document.title)</script>";
    }

    public final SimpleNestedFormController getSelezione_blob_tipoController() {
        return selezione_blob_tipoController;
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            getSelezione_blob_tipoController().setModel(actioncontext, selezione_blob_tipo = createComponentSession().getSelezione_blob_tipo(actioncontext.getUserContext()));
            refreshTree(actioncontext);
        } catch (ComponentException componentexception) {
            throw handleException(componentexception);
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        }
    }

    public boolean isDeleteButtonEnabled() {
        return !isReadonly();
    }

    public boolean isDownloadButtonEnabled() {
        return getBlob_path() != null && !getBlob_path().isDirectory() && "S".equals(getBlob_path().getStato());
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean flag) {
        readonly = flag;
    }

    public void refreshTree(ActionContext actioncontext)
            throws BusinessProcessException {
        setTipo(actioncontext, selezione_blob_tipo.getBlob_tipo());
    }

    public void setTipo(ActionContext actioncontext, Bframe_blob_tipoBulk bframe_blob_tipobulk)
            throws BusinessProcessException {
        if (bframe_blob_tipobulk == null) {
            setRemoteBulkTree(actioncontext, null);
        } else {
            Bframe_blob_pathBulk bframe_blob_pathbulk = new Bframe_blob_pathBulk();
            bframe_blob_pathbulk.setCd_tipo(bframe_blob_tipobulk.getCd_tipo());
            bframe_blob_pathbulk.setPath(bframe_blob_tipobulk.getRoot());
            bframe_blob_pathbulk.setFl_dir(Boolean.TRUE);
            setRemoteBulkTree(actioncontext, tree, bframe_blob_pathbulk);
        }
    }

    class RemoteBframe_blob_pathTree
            implements RemoteBulkTree {

        RemoteBframe_blob_pathTree() {
        }

        public RemoteIterator getChildren(ActionContext actioncontext, OggettoBulk oggettobulk)
                throws RemoteException {
            try {
                return createComponentSession().getBlobChildren(actioncontext.getUserContext(), (Bframe_blob_pathBulk) oggettobulk, selezione_blob_tipo.getTi_visibilita());
            } catch (ComponentException componentexception) {
                throw new RemoteException(componentexception.getMessage(), componentexception.getDetail());
            } catch (BusinessProcessException businessprocessexception) {
                throw new RemoteException(businessprocessexception.getMessage(), businessprocessexception.getDetail());
            }
        }

        public OggettoBulk getParent(ActionContext actioncontext, OggettoBulk oggettobulk)
                throws RemoteException {
            return null;
        }

        public boolean isLeaf(ActionContext actioncontext, OggettoBulk oggettobulk)
                throws RemoteException {
            return !((Bframe_blob_pathBulk) oggettobulk).isDirectory();
        }
    }

}