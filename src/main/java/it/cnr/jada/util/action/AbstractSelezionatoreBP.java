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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Orderable;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package it.cnr.jada.util.action:
//            BulkBP, Selection, SelectionIterator

public abstract class AbstractSelezionatoreBP extends BulkBP
        implements Serializable, Orderable {

    protected Selection selection;
    private String printbp;
    private Object focusedElement;

    public AbstractSelezionatoreBP() {
        this("");
    }

    public AbstractSelezionatoreBP(String s) {
        super(s);
        selection = new Selection();
    }

    public void clearSelection(ActionContext actioncontext)
            throws BusinessProcessException {
        selection.clear();
        setFocusedElement(actioncontext, null);
    }

    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws BusinessProcessException {
        return null;
    }

    public abstract Object getElementAt(ActionContext actioncontext, int i)
            throws BusinessProcessException;

    public Object getFocusedElement() {
        return focusedElement;
    }

    public Object getFocusedElement(ActionContext actioncontext)
            throws BusinessProcessException {
        if (selection.getFocus() < 0)
            return null;
        else
            return getElementAt(actioncontext, selection.getFocus());
    }

    public int getOrderBy(String s) {
        return 0;
    }

    public String getPrintbp() {
        return printbp != null ? printbp : "BulkListPrintBP";
    }

    public void setPrintbp(String s) {
        printbp = s;
    }

    public List getSelectedElements(ActionContext actioncontext)
            throws BusinessProcessException {
        ArrayList arraylist = new ArrayList(selection.size());
        for (SelectionIterator selectioniterator = selection.iterator(); selectioniterator.hasNext(); arraylist.add(getElementAt(actioncontext, selectioniterator.nextIndex())))
            ;
        if (arraylist.isEmpty() && selection.getFocus() >= 0)
            arraylist.add(getElementAt(actioncontext, selection.getFocus()));
        return arraylist;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection1) {
        selection = selection1;
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isOrderableBy(String s) {
        return false;
    }

    public void reset(ActionContext actioncontext)
            throws BusinessProcessException {
        clearSelection(actioncontext);
    }

    public int setFocus(ActionContext actioncontext)
            throws BusinessProcessException {
        selection.setFocus(actioncontext, "mainTable");
        if (selection.getFocus() >= 0)
            setFocusedElement(actioncontext, getElementAt(actioncontext, selection.getFocus()));
        else
            setFocusedElement(actioncontext, null);
        return setSelection(actioncontext).getFocus();
    }

    protected void setFocusedElement(ActionContext actioncontext, Object obj)
            throws BusinessProcessException {
        focusedElement = obj;
        if (obj instanceof OggettoBulk)
            setModel(actioncontext, (OggettoBulk) obj);
    }

    public void setOrderBy(ActionContext actioncontext, String s, int i) {
    }

    public Selection setSelection(ActionContext actioncontext) {
        selection.setSelection(actioncontext, "mainTable");
        return selection;
    }
}