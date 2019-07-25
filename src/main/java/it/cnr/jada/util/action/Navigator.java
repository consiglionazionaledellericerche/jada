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

import java.io.Serializable;

public abstract class Navigator
        implements Serializable {

    private int currentPage;
    private OggettoBulk[] pageContents;
    private int firstPage;
    private int pageFrameSize;
    private int pageSize;
    private int pageCount;
    private int elementsCount;

    public Navigator() {
        pageFrameSize = 10;
        pageSize = 10;
    }

    protected abstract int countElements()
            throws BusinessProcessException;

    public abstract OggettoBulk[] fetchPageContents(ActionContext actioncontext, int i)
            throws BusinessProcessException;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int i) {
        currentPage = Math.min(pageCount - 1, Math.max(0, i));
        pageContents = null;
        firstPage = pageFrameSize * (currentPage / pageFrameSize);
    }

    public int getElementsCount() {
        return elementsCount;
    }

    public int getFirstPage() {
        return 0;
    }

    public int getLastPage() {
        return Math.min(firstPage + pageFrameSize, pageCount);
    }

    abstract int getOrderBy(String s);

    public OggettoBulk[] getPageContents() {
        return pageContents;
    }

    public void setPageContents(OggettoBulk aoggettobulk[]) {
        pageContents = aoggettobulk;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageFrameSize() {
        return pageFrameSize;
    }

    public void setPageFrameSize(int i) {
        pageFrameSize = i;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int i)
            throws BusinessProcessException {
        pageSize = i;
    }

    public void goToPage(ActionContext actioncontext, int i)
            throws BusinessProcessException {
        setCurrentPage(i);
        setPageContents(fetchPageContents(actioncontext, currentPage));
    }

    public void reset()
            throws BusinessProcessException {
        elementsCount = countElements();
        firstPage = 0;
        pageContents = null;
        pageCount = ((elementsCount + pageSize) - 1) / pageSize;
    }

    public void reset(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            reset();
            setPageContents(fetchPageContents(actioncontext, currentPage));
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    abstract void setOrderBy(String s, int i);
}