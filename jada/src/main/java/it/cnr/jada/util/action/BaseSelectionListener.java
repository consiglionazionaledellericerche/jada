package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.util.BitSet;

// Referenced classes of package it.cnr.jada.util.action:
//            SelectionListener

public class BaseSelectionListener
    implements Serializable, SelectionListener
{

    public BaseSelectionListener()
    {
    }

    public void clearSelection(ActionContext actioncontext)
    {
    }

    public void deselectAll(ActionContext actioncontext)
    {
    }

    public BitSet getSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset)
    {
        return null;
    }

    public void initializeSelection(ActionContext actioncontext)
    {
    }

    public void selectAll(ActionContext actioncontext)
    {
    }

    public BitSet setSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset, BitSet bitset1)
    {
        return null;
    }
}