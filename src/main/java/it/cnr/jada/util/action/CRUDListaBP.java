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

package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.ejb.MultipleCRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import javax.ejb.EJBException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreListaBP, AbstractSelezionatoreBP, Selection, SelectionIterator, 
//            FormBP, SearchProvider

public class CRUDListaBP extends SelezionatoreListaBP
        implements Serializable {

    private final SearchProvider searchProvider;
    private String componentSessioneName;
    private Class bulkClass;
    private boolean dirty;
    private Set dirtyModels;

    public CRUDListaBP() {
        searchProvider = new SearchProvider() {

            public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
                    throws BusinessProcessException {
                return find(actioncontext, compoundfindclause, oggettobulk);
            }

        };
        super.table.setMultiSelection(true);
        super.table.setOnselect("javascript:select");
        super.table.setReadonly(false);
        super.table.setStatus(2);
    }

    public CRUDListaBP(String s) {
        this();
    }

    public CRUDComponentSession createComponentSession() throws EJBException, RemoteException {
        return (CRUDComponentSession) EJBCommonServices.createEJB(componentSessioneName);
    }

    public OggettoBulk createEmptyModelForFreeSearch(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            return createComponentSession().inizializzaBulkPerRicercaLibera(actioncontext.getUserContext(), createNewBulk(actioncontext));
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public OggettoBulk createEmptyModelForSearch(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            return createComponentSession().inizializzaBulkPerRicercaLibera(actioncontext.getUserContext(), createNewBulk(actioncontext));
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public OggettoBulk createNewBulk(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            OggettoBulk oggettobulk = (OggettoBulk) bulkClass.newInstance();
            oggettobulk.setUser(actioncontext.getUserInfo().getUserid());
            return oggettobulk;
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public Button[] createToolbar() {
        Button[] abutton = new Button[4];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.save");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.delete");
        return abutton;
    }

    public void delete(ActionContext actioncontext)
            throws BusinessProcessException, ValidationException {
        try {
            CRUDComponentSession crudcomponentsession = createComponentSession();
            if (super.selection.size() > 0 && (crudcomponentsession instanceof MultipleCRUDComponentSession)) {
                OggettoBulk[] aoggettobulk = new OggettoBulk[super.selection.size()];
                int i = 0;
                for (SelectionIterator selectioniterator1 = super.selection.iterator(); selectioniterator1.hasNext(); ) {
                    int j = selectioniterator1.nextIndex();
                    aoggettobulk[i++] = (OggettoBulk) getElementAt(actioncontext, j);
                }

                delete(actioncontext, (MultipleCRUDComponentSession) crudcomponentsession, aoggettobulk);
            } else if (super.selection.size() > 0) {
                for (SelectionIterator selectioniterator = super.selection.iterator(); selectioniterator.hasNext(); delete(actioncontext, crudcomponentsession, (OggettoBulk) getElementAt(actioncontext, selectioniterator.nextIndex())))
                    ;
            } else if (super.selection.getFocus() > 0)
                delete(actioncontext, crudcomponentsession, (OggettoBulk) getElementAt(actioncontext, super.selection.getFocus()));
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        } catch (EJBException ejbexception) {
            throw handleException(ejbexception);
        }
    }

    private void delete(ActionContext actioncontext, CRUDComponentSession crudcomponentsession, OggettoBulk oggettobulk)
            throws BusinessProcessException, ValidationException {
        int i = oggettobulk.getCrudStatus();
        oggettobulk.setToBeDeleted();
        try {
            crudcomponentsession.eliminaConBulk(actioncontext.getUserContext(), oggettobulk);
        } catch (RemoteException remoteexception) {
            oggettobulk.setCrudStatus(i);
            throw handleException(remoteexception);
        } catch (ComponentException componentexception) {
            oggettobulk.setCrudStatus(i);
            throw handleException(componentexception);
        }
    }

    private void delete(ActionContext actioncontext, MultipleCRUDComponentSession multiplecrudcomponentsession, OggettoBulk aoggettobulk[])
            throws BusinessProcessException, ValidationException {
        int[] ai = new int[aoggettobulk.length];
        for (int i = 0; i < aoggettobulk.length; i++) {
            OggettoBulk oggettobulk = aoggettobulk[i];
            ai[i] = oggettobulk.getCrudStatus();
            oggettobulk.setToBeDeleted();
        }

        try {
            multiplecrudcomponentsession.eliminaConBulk(actioncontext.getUserContext(), aoggettobulk);
        } catch (RemoteException remoteexception) {
            for (int j = 0; j < aoggettobulk.length; j++)
                aoggettobulk[j].setCrudStatus(ai[j]);

            throw handleException(remoteexception);
        } catch (ComponentException componentexception) {
            for (int k = 0; k < aoggettobulk.length; k++)
                aoggettobulk[k].setCrudStatus(ai[k]);

            throw handleException(componentexception);
        }
    }

    public OggettoBulk[] fillModels(ActionContext actioncontext)
            throws FillException {
        OggettoBulk[] aoggettobulk = getPageContents();
        for (int i = 0; i < aoggettobulk.length; i++) {
            OggettoBulk oggettobulk = aoggettobulk[i];
            if (oggettobulk.fillFromActionContext(actioncontext, "mainTable.[" + (i + getFirstElementIndexOnCurrentPage()), 2, getFieldValidationMap())) {
                dirty = true;
                dirtyModels.add(oggettobulk);
            }
        }

        return (OggettoBulk[]) dirtyModels.toArray(new OggettoBulk[dirtyModels.size()]);
    }

    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            return EJBCommonServices.openRemoteIterator(actioncontext, createComponentSession().cerca(actioncontext.getUserContext(), compoundfindclause, oggettobulk));
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws BusinessProcessException {
        try {
            return EJBCommonServices.openRemoteIterator(actioncontext, createComponentSession().cerca(actioncontext.getUserContext(), compoundfindclause, oggettobulk, oggettobulk1, s));
        } catch (Exception exception) {
            throw new BusinessProcessException(exception);
        }
    }

    public String getComponentSessioneName() {
        return componentSessioneName;
    }

    public void setComponentSessioneName(String s) {
        componentSessioneName = s;
    }

    public OggettoBulk getPrototype(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            return (OggettoBulk) bulkClass.newInstance();
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public final SearchProvider getSearchProvider() {
        return searchProvider;
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            setBulkClassName(config.getInitParameter("bulkClassName"));
            setComponentSessioneName(config.getInitParameter("componentSessionName"));
            setIterator(actioncontext, find(actioncontext, null, createEmptyModelForSearch(actioncontext)));
        } catch (ClassNotFoundException _ex) {
            throw new RuntimeException("Non trovata la classe bulk");
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk[] initializeBulks(ActionContext actioncontext, OggettoBulk aoggettobulk[])
            throws BusinessProcessException {
        try {
            CRUDComponentSession crudcomponentsession = createComponentSession();
            if (crudcomponentsession instanceof MultipleCRUDComponentSession)
                return crudcomponentsession.inizializzaBulkPerModifica(actioncontext.getUserContext(), aoggettobulk);
            for (int i = 0; i < aoggettobulk.length; i++)
                aoggettobulk[i] = crudcomponentsession.inizializzaBulkPerModifica(actioncontext.getUserContext(), aoggettobulk[i]);

            return super.initializeBulks(actioncontext, aoggettobulk);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean flag) {
        dirty = flag;
    }

    protected void save(ActionContext actioncontext)
            throws BusinessProcessException, ValidationException {
        try {
            CRUDComponentSession crudcomponentsession = createComponentSession();
            if (crudcomponentsession instanceof MultipleCRUDComponentSession) {
                OggettoBulk[] aoggettobulk = new OggettoBulk[dirtyModels.size()];
                dirtyModels.toArray(aoggettobulk);
                for (Iterator iterator1 = dirtyModels.iterator(); iterator1.hasNext(); ((OggettoBulk) iterator1.next()).setToBeUpdated())
                    ;
                crudcomponentsession.modificaConBulk(actioncontext.getUserContext(), aoggettobulk);
            } else {
                OggettoBulk oggettobulk;
                for (Iterator iterator = dirtyModels.iterator(); iterator.hasNext(); crudcomponentsession.modificaConBulk(actioncontext.getUserContext(), oggettobulk)) {
                    oggettobulk = (OggettoBulk) iterator.next();
                    oggettobulk.setToBeUpdated();
                }

            }
            setMessage(INFO_MESSAGE, "Salvataggio eseguito in modo corretto.");
        } catch (RemoteException remoteexception) {
            throw handleException(remoteexception);
        } catch (ComponentException componentexception) {
            throw handleException(componentexception);
        } catch (EJBException ejbexception) {
            throw handleException(ejbexception);
        } finally {
            dirtyModels.clear();
        }
    }

    public void setBulkClassName(String s)
            throws ClassNotFoundException {
        bulkClass = getClass().getClassLoader().loadClass(s);
        setBulkInfo(BulkInfo.getBulkInfo(bulkClass));
    }

    public void setPageSize(int i)
            throws RemoteException {
        super.setPageSize(i);
        dirtyModels = new HashSet(i);
    }
}