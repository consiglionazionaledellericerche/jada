package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

public abstract class Navigator
    implements Serializable
{

    public Navigator()
    {
        pageFrameSize = 10;
        pageSize = 10;
    }

    protected abstract int countElements()
        throws BusinessProcessException;

    public abstract OggettoBulk[] fetchPageContents(ActionContext actioncontext, int i)
        throws BusinessProcessException;

    public int getCurrentPage()
    {
        return currentPage;
    }

    public int getElementsCount()
    {
        return elementsCount;
    }

    public int getFirstPage()
    {
        return 0;
    }

    public int getLastPage()
    {
        return Math.min(firstPage + pageFrameSize, pageCount);
    }

    abstract int getOrderBy(String s);

    public OggettoBulk[] getPageContents()
    {
        return pageContents;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public int getPageFrameSize()
    {
        return pageFrameSize;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void goToPage(ActionContext actioncontext, int i)
        throws BusinessProcessException
    {
        setCurrentPage(i);
        setPageContents(fetchPageContents(actioncontext, currentPage));
    }

    public void reset()
        throws BusinessProcessException
    {
        elementsCount = countElements();
        firstPage = 0;
        pageContents = null;
        pageCount = ((elementsCount + pageSize) - 1) / pageSize;
    }

    public void reset(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            reset();
            setPageContents(fetchPageContents(actioncontext, currentPage));
        }
        catch(Throwable throwable)
        {
            throw new BusinessProcessException(throwable);
        }
    }

    public void setCurrentPage(int i)
    {
        currentPage = Math.min(pageCount - 1, Math.max(0, i));
        pageContents = null;
        firstPage = pageFrameSize * (currentPage / pageFrameSize);
    }

    abstract void setOrderBy(String s, int i);

    public void setPageContents(OggettoBulk aoggettobulk[])
    {
        pageContents = aoggettobulk;
    }

    public void setPageFrameSize(int i)
    {
        pageFrameSize = i;
    }

    public void setPageSize(int i)
        throws BusinessProcessException
    {
        pageSize = i;
    }

    private int currentPage;
    private OggettoBulk pageContents[];
    private int firstPage;
    private int pageFrameSize;
    private int pageSize;
    private int pageCount;
    private int elementsCount;
}