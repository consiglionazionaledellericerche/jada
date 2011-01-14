package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.BitSet;

public interface SelectionListener
{

    public abstract void clearSelection(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract void deselectAll(ActionContext actioncontext);

    public abstract BitSet getSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset)
        throws BusinessProcessException;

    public abstract void initializeSelection(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract void selectAll(ActionContext actioncontext)
        throws BusinessProcessException;

    public abstract BitSet setSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset, BitSet bitset1)
        throws BusinessProcessException;
}