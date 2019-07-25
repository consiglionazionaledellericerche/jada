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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.ActionPerformingError;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDDetailComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.EmptyRemoteIterator;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.RemotePagedIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.RemoteError;
import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.*;

public class RemoteDetailCRUDController extends AbstractDetailCRUDController implements Serializable {

    private final String attributeName;
    private final Class modelClass;
    private final BulkInfo bulkInfo;
    private final String componentSessionName;
    private Constructor constructor;
    private RemoteIterator remoteIterator;
    private List cachedDetailsPage;
    private CompoundFindClause filter;

    public RemoteDetailCRUDController(String s, Class class1, String s1,
                                      String s2, FormController formcontroller) {
        this(s, class1, s1, s2, formcontroller, true);
    }

    public RemoteDetailCRUDController(String s, Class class1, String s1,
                                      String s2, FormController formcontroller, boolean flag) {
        super(s, formcontroller, flag);
        attributeName = s1;
        componentSessionName = s2;
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
        super.paged = true;
        try {
            constructor = class1
                    .getConstructor(CRUDController.class);
        } catch (NoSuchMethodException _ex) {
        }
    }

    public int addDetail(OggettoBulk oggettobulk) {
        return 0;
    }

    public void basicReset(ActionContext actioncontext) {
        resyncRemoteIterator(actioncontext);
        super.basicReset(actioncontext);
        fetchCurrentPage();
    }

    protected void clearFilter(ActionContext actioncontext) {
        filter = null;
    }

    public int countDetails() {
        try {
            return remoteIterator.countElements();
        } catch (RemoteException remoteexception) {
            throw new RemoteError(remoteexception);
        }
    }

    public CRUDDetailComponentSession createComponentSession()
            throws BusinessProcessException {
        return (CRUDDetailComponentSession) createComponentSession(
                componentSessionName,
                it.cnr.jada.ejb.CRUDDetailComponentSession.class);
    }

    public Object createComponentSession(String s, Class class1)
            throws BusinessProcessException {
        try {
            it.cnr.jada.UserTransaction usertransaction = getUserTransaction();
            if (usertransaction == null)
                return EJBCommonServices.createEJB(s);
            else
                return EJBCommonServices.createEJB(usertransaction, s);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public OggettoBulk createEmptyModel(ActionContext actioncontext) {
        return null;
    }

    protected RemoteIterator createRemoteIterator(ActionContext actioncontext) {
        try {
            return EJBCommonServices.openRemoteIterator(
                    actioncontext,
                    createComponentSession().cerca(
                            actioncontext.getUserContext(), filter, modelClass,
                            getParentModel(), attributeName));
        } catch (ComponentException componentexception) {
            throw new ActionPerformingError(componentexception);
        } catch (BusinessProcessException businessprocessexception) {
            throw new ActionPerformingError(businessprocessexception);
        } catch (RemoteException remoteexception) {
            throw new RemoteError(remoteexception);
        }
    }

    private void fetchCurrentPage() {
        cachedDetailsPage = fetchPage(getCurrentPage());
    }

    private List fetchPage(int i) {
        try {
            if (remoteIterator instanceof RemotePagedIterator) {
                RemotePagedIterator remotepagediterator = (RemotePagedIterator) remoteIterator;
                remotepagediterator.moveToPage(i);
                Object[] aobj = remotepagediterator.nextPage();
                for (int l = 0; l < aobj.length; l++)
                    aobj[l] = initializeForEdit((OggettoBulk) aobj[l]);

                return Arrays.asList(aobj);
            }
            int j = i * getPageSize();
            int k = Math.min(j + getPageSize(), remoteIterator.countElements());
            ArrayList arraylist = new ArrayList(k - j);
            remoteIterator.moveTo(j);
            while (j++ < k)
                arraylist.add(initializeForEdit((OggettoBulk) remoteIterator
                        .nextElement()));
            return arraylist;
        } catch (RemoteException remoteexception) {
            throw new RemoteError(remoteexception);
        }
    }

    public BulkInfo getBulkInfo() {
        if (getModel() == null)
            return bulkInfo;
        else
            return getModel().getBulkInfo();
    }

    protected OggettoBulk getDetail(int i) {
        try {
            if (cachedDetailsPage == null)
                return null;
            if (i < 0)
                return null;
            int j = i / getPageSize();
            if (j == getCurrentPage()) {
                return (OggettoBulk) cachedDetailsPage.get(i % getPageSize());
            } else {
                remoteIterator.moveTo(i);
                return initializeForEdit((OggettoBulk) remoteIterator
                        .nextElement());
            }
        } catch (RemoteException remoteexception) {
            throw new RemoteError(remoteexception);
        }
    }

    public List getDetails() {
        throw new UnsupportedOperationException();
    }

    protected List getDetailsPage() {
        return cachedDetailsPage;
    }

    public CompoundFindClause getFilter() {
        return filter;
    }

    public int getOrderBy(String s) {
        try {
            if (remoteIterator instanceof RemoteOrderable)
                return ((RemoteOrderable) remoteIterator).getOrderBy(s);
            else
                return 0;
        } catch (RemoteException remoteexception) {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    protected int indexOfDetail(OggettoBulk oggettobulk) {
        return 0;
    }

    public OggettoBulk initializeForEdit(OggettoBulk oggettobulk) {
        return oggettobulk;
    }

    public boolean isFiltered() {
        return filter != null;
    }

    public void remove(ActionContext actioncontext) throws ValidationException,
            BusinessProcessException {
        basicSetSelection(actioncontext);
        List list = getDetailsPage();
        BitSet bitset = super.selection.getSelection(getCurrentPage()
                * getPageSize(), getPageSize());
        if (bitset.length() > 0) {
            int i = 0;
            for (int j = 0; j < getPageSize(); j++)
                if (bitset.get(j)) {
                    validateForDelete(actioncontext, (OggettoBulk) list.get(j));
                    i++;
                }

            OggettoBulk[] aoggettobulk = new OggettoBulk[i];
            int k = getPageSize() - 1;
            int l = 0;
            for (; k >= 0; k--)
                if (bitset.get(k))
                    aoggettobulk[l++] = (OggettoBulk) list.get(k);

            removeDetails(actioncontext, aoggettobulk);
        } else if (super.selection.getFocus() >= 0) {
            OggettoBulk oggettobulk = getDetail(super.selection.getFocus());
            validateForDelete(actioncontext, oggettobulk);
            removeDetails(actioncontext, new OggettoBulk[]{oggettobulk});
        }
        getParentController().setDirty(true);
        reset(actioncontext);
    }

    public void removeAll(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException {
        try {
            createComponentSession().eliminaConBulk(
                    actioncontext.getUserContext(), getParentModel(),
                    attributeName);
            reset(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    protected OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
        return null;
    }

    protected void removeDetails(ActionContext actioncontext,
                                 OggettoBulk aoggettobulk[]) throws BusinessProcessException {
        try {
            createComponentSession().eliminaConBulk(
                    actioncontext.getUserContext(), aoggettobulk,
                    getParentModel(), attributeName);
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        } catch (ComponentException componentexception) {
            throw new BusinessProcessException(componentexception);
        }
    }

    public void resync(ActionContext actioncontext)
            throws BusinessProcessException {
        resyncRemoteIterator(actioncontext);
        super.resync(actioncontext);
        fetchCurrentPage();
    }

    private void resyncRemoteIterator(ActionContext actioncontext) {
        try {
            EJBCommonServices.closeRemoteIterator(actioncontext, remoteIterator);
            if (getParentModel() == null)
                remoteIterator = new EmptyRemoteIterator();
            else
                remoteIterator = createRemoteIterator(actioncontext);
            remoteIterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteIterator);
            if (remoteIterator instanceof RemotePagedIterator) {
                ((RemotePagedIterator) remoteIterator).setPageSize(getPageSize());
            }
        } catch (RemoteException remoteexception) {
            throw new RemoteError(remoteexception);
        }
    }

    public void save(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            createComponentSession().modificaConBulk(
                    actioncontext.getUserContext(), getParentModel(),
                    oggettobulk, attributeName);
            resync(actioncontext);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public void setFilter(ActionContext actioncontext,
                          CompoundFindClause compoundfindclause) {
        filter = compoundfindclause;
        basicReset(actioncontext);
    }

    protected void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) {
        try {
            if (oggettobulk != null)
                oggettobulk = createComponentSession()
                        .inizializzaBulkPerModifica(
                                actioncontext.getUserContext(), oggettobulk,
                                getParentModel(), attributeName);
        } catch (Throwable throwable) {
            throw new DetailedRuntimeException(throwable);
        }
        super.setModel(actioncontext, oggettobulk);
    }

    public void setModelIndex(ActionContext actioncontext, int i) {
        int j = getCurrentPage();
        super.setModelIndex(actioncontext, i);
        if (j != getCurrentPage())
            fetchCurrentPage();
    }

    public void setOrderBy(ActionContext actioncontext, String s, int i) {
        try {
            ((RemoteOrderable) remoteIterator).setOrderBy(s, i);
            clearFilter(actioncontext);
            basicSetModelIndex(-1);
            setModel(actioncontext, null);
            super.selection.clear();
            resetChildren(actioncontext);
            fetchCurrentPage();
        } catch (RemoteException remoteexception) {
            throw new DetailedRuntimeException(remoteexception);
        }
    }

    public void setPageIndex(ActionContext actioncontext, int i)
            throws ValidationException, BusinessProcessException {
        int j = getCurrentPage();
        super.setPageIndex(actioncontext, i);
        if (j != getCurrentPage())
            fetchCurrentPage();
    }

    protected List sortDetailsBy(Comparator comparator) {
        return null;
    }

    public void updateDetail(ActionContext actioncontext,
                             OggettoBulk oggettobulk) throws BusinessProcessException {
        try {
            createComponentSession().modificaConBulk(
                    actioncontext.getUserContext(), getParentModel(),
                    getModel(), attributeName);
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public void writeHTMLTable(PageContext pagecontext, String s, boolean flag,
                               boolean flag1, boolean flag2, String s1, String s2, boolean flag3,
                               TableCustomizer tablecustomizer) throws ServletException,
            IOException {
        writeHTMLPagedTable(pagecontext, s, flag, flag1, flag2, s1, s2, flag3,
                tablecustomizer, cachedDetailsPage,
                calcPageCount(remoteIterator.countElements()));
    }

    public void closed(ActionContext context) throws RemoteException {
        if (remoteIterator != null)
            EJBCommonServices.closeRemoteIterator(context, remoteIterator);
    }
}
