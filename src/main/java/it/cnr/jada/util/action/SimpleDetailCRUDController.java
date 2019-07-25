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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.MultipleComparator;
import it.cnr.jada.util.OrderedHashMap;
import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.action:
//            AbstractDetailCRUDController, NestedFormController, Selection, UnsortableException, 
//            FormController

public class SimpleDetailCRUDController extends AbstractDetailCRUDController
        implements Serializable {

    private final String listPropertyName;
    private final Class modelClass;
    private final BulkInfo bulkInfo;
    private Constructor constructor;
    private Map orderByClauses;

    public SimpleDetailCRUDController(String s, Class class1, String s1, FormController formcontroller) {
        this(s, class1, s1, formcontroller, true);
    }

    public SimpleDetailCRUDController(String s, Class class1, String s1, FormController formcontroller, boolean flag) {
        super(s, formcontroller, flag);
        listPropertyName = s1;
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
        try {
            constructor = class1.getConstructor(CRUDController.class);
        } catch (NoSuchMethodException _ex) {
        }
    }

    public int addDetail(OggettoBulk oggettobulk)
            throws BusinessProcessException {
        try {
            oggettobulk.setToBeCreated();
            return Introspector.addToListProperty(getParentModel(), listPropertyName, oggettobulk);
        } catch (InvocationTargetException invocationtargetexception) {
            throw new BusinessProcessException(invocationtargetexception.getTargetException());
        } catch (Throwable throwable) {
            throw new IntrospectionError(throwable.getMessage());
        }
    }

    public void addToSelection(Enumeration enumeration) {
        List list = getDetails();
        for (; enumeration.hasMoreElements(); super.selection.addToSelection(list.indexOf(enumeration.nextElement()))) ;
    }

    public int countDetails() {
        return getDetails().size();
    }

    public OggettoBulk createEmptyModel(ActionContext actioncontext) {
        try {
            OggettoBulk oggettobulk;
            if (constructor != null)
                oggettobulk = (OggettoBulk) constructor.newInstance(new Object[]{
                        this
                });
            else
                oggettobulk = (OggettoBulk) modelClass.newInstance();
            return oggettobulk.initializeForInsert(null, actioncontext);
        } catch (InvocationTargetException invocationtargetexception) {
            throw new IntrospectionError(invocationtargetexception);
        } catch (InstantiationException instantiationexception) {
            throw new IntrospectionError(instantiationexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IntrospectionError(illegalaccessexception);
        }
    }

    public BulkInfo getBulkInfo() {
        if (getModel() == null)
            return bulkInfo;
        else
            return getModel().getBulkInfo();
    }

    protected OggettoBulk getDetail(int i) {
        List list = getDetails();
        if (list == null)
            throw new IndexOutOfBoundsException();
        else
            return (OggettoBulk) list.get(i);
    }

    public List getDetails() {
        try {
            return (List) Introspector.getPropertyValue(getParentModel(), listPropertyName);
        } catch (Exception exception) {
            throw new IntrospectionError(exception.getMessage());
        }
    }

    protected List getDetailsPage() {
        List list = getDetails();
        if (list == null)
            return null;
        else
            return list.subList(getPageSize() * getCurrentPage(), Math.min(getPageSize() * (getCurrentPage() + 1), list.size()));
    }

    public String getListPropertyName() {
        return listPropertyName;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public int getOrderBy(String s) {
        if (orderByClauses == null) {
            return 0;
        } else {
            Integer integer = (Integer) orderByClauses.get(s);
            return integer != null ? integer.intValue() : 0;
        }
    }

    public OggettoBulk removeDetail(int i) {
        try {
            OggettoBulk oggettobulk = (OggettoBulk) Introspector.removeFromListProperty(getParentModel(), listPropertyName, i);
            if (oggettobulk != null)
                oggettobulk.setToBeDeleted();
            return oggettobulk;
        } catch (Exception exception) {
            throw new IntrospectionError(exception.getMessage());
        }
    }

    public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
        return removeDetail(i);
    }

    public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
    }

    public void setOrderBy(ActionContext actioncontext, String s, int i) {
        if (orderByClauses == null)
            orderByClauses = new OrderedHashMap();
        if (i == 0)
            orderByClauses.remove(s);
        else
            orderByClauses.put(s, new Integer(i));
        sort();
    }

    public void setSelection(Enumeration enumeration) {
        super.selection.clear();
        addToSelection(enumeration);
    }

    public void sort() {
        if (orderByClauses != null && !orderByClauses.isEmpty())
            try {
                List list = sortDetailsBy(new MultipleComparator(orderByClauses));
                if (list != null) {
                    super.selection.clear();
                    super.selection.setFocus(list.indexOf(getModel()));
                    basicSetModelIndex(super.selection.getFocus());
                }
            } catch (UnsupportedOperationException unsupportedoperationexception) {
                throw new UnsortableException(unsupportedoperationexception);
            }
    }

    protected List sortDetailsBy(Comparator comparator) {
        List list = getDetails();
        if (list != null)
            Collections.sort(list, comparator);
        return list;
    }

    public void writeHTMLTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2,
                               boolean flag3, TableCustomizer tablecustomizer)
            throws ServletException, IOException {
        List list = getDetails();
        if (super.paged)
            writeHTMLPagedTable(pagecontext, s, flag, flag1, flag2, s1, s2, flag3, tablecustomizer, list.subList(getPageSize() * getCurrentPage(), Math.min(getPageSize() * (getCurrentPage() + 1), list.size())), calcPageCount(list.size()));
        else
            writeHTMLTable(pagecontext, s, flag, flag1, flag2, s1, s2, flag3, tablecustomizer, list);
    }
}