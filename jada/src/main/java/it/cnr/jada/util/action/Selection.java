package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.action:
//            SelectionIterator

public class Selection
    implements Cloneable, Serializable
{
    protected class ListItr
        implements Serializable, Iterator
    {

        public boolean hasNext()
        {
            return next >= 0;
        }

        private int moveToNext()
        {
            curr = next++;
            for(; next <= max; next++){
                if(selection.get(next))
                    return curr;
            } 
            next = -1;
            return curr;
        }

        public Object next()
        {
            if(next < 0)
                throw new IndexOutOfBoundsException();
            else
                return list.get(moveToNext());
        }

        public void remove()
        {
            removeFromSelection(curr);
        }

        int next;
        int curr;
        int max;
        List list;

        public ListItr(List list1, int i, int j)
        {
            next = -1;
            curr = -1;
            next = i - 1;
            max = Math.max(selection.length() - 1, next + j);
            list = list1;
            moveToNext();
        }

        public ListItr(List list1)
        {
            this(list1, 0, list1.size());
        }
    }

    protected class RemoveItr
        implements Serializable, Iterator
    {

        public boolean hasNext()
        {
            return next >= 0;
        }

        private int moveToNext()
        {
            curr = next++;
            for(; next <= max; next++){
                if(selection.get(next))
                    return curr;
            }
            next = -1;
            return curr;
        }

        public Object next()
        {
            if(next < 0)
                throw new IndexOutOfBoundsException();
            else
                return list.get(moveToNext() - removed);
        }

        public void remove()
        {
            list.remove(curr - removed++);
        }

        int next;
        int curr;
        int max;
        int removed;
        final List list;
        final BitSet selection;

        public RemoveItr(List list1)
        {
            next = -1;
            curr = -1;
            removed = 0;
            selection = Selection.this.selection;
            max = selection.length() - 1;
            list = list1;
            moveToNext();
            clear();
        }
    }

    class Itr
        implements Serializable, SelectionIterator
    {

        public boolean hasNext()
        {
            return next >= 0;
        }

        private int moveToNext()
        {
            curr = next++;
            for(; next <= max; next++){
                if(selection.get(next))
                    return curr;
            }
            next = -1;
            return curr;
        }

        public int nextIndex()
        {
            if(next < 0)
                throw new IndexOutOfBoundsException();
            else
                return moveToNext();
        }

        public Object next()
        {
            if(next < 0)
                throw new IndexOutOfBoundsException();
            else
                return new Integer(moveToNext());
        }

        public void remove()
        {
            removeFromSelection(curr);
        }

        int curr;
        int next;
        int max;

        public Itr(int i, int j)
        {
            curr = -1;
            next = -1;
            next = i - 1;
            max = next + j;
            moveToNext();
        }

        public Itr()
        {
            this(0, selection.length());
        }
    }

    class RItr
        implements Serializable, SelectionIterator
    {

        public boolean hasNext()
        {
            return i >= 0;
        }

        private int moveToNext()
        {
            int j = i--;
            for(; i >= 0; i--)
                if(selection.get(i))
                    return j;

            return j;
        }

        public int nextIndex()
        {
            if(i >= selection.length())
                throw new IndexOutOfBoundsException();
            else
                return moveToNext();
        }

        public Object next()
        {
            if(i >= selection.length())
                throw new IndexOutOfBoundsException();
            else
                return new Integer(moveToNext());
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        int i;

        public RItr()
        {
            i = selection.length();
            moveToNext();
        }
    }


    public Selection()
    {
        selection = new BitSet();
        focus = -1;
    }

    public Selection(ActionContext actioncontext, String s)
    {
        selection = new BitSet();
        focus = -1;
        setFocus(actioncontext, s);
        setSelection(actioncontext, s);
    }

    public Selection(Selection selection1)
    {
        selection = new BitSet();
        focus = -1;
        selection = (BitSet)selection1.selection.clone();
        focus = selection1.focus;
        size = selection1.size;
    }

    public void addToSelection(int i)
    {
        if(!selection.get(i))
        {
            selection.set(i);
            size++;
        }
    }

    public void clear()
    {
        selection = new BitSet();
        size = 0;
        focus = -1;
    }

    public void clearFocus()
    {
        focus = -1;
    }

    public void clearSelection()
    {
        selection = new BitSet();
        size = 0;
    }

    public void clearSelection(int i, int j)
    {
        for(; j > 0; j--)
            removeFromSelection(i++);

    }

    public int getFocus()
    {
        return focus;
    }

    public BitSet getSelection(int i, int j)
    {
        BitSet bitset = new BitSet(j);
        int k = i;
        for(int l = 0; l < j; l++)
        {
            if(selection.get(k))
                bitset.set(l);
            k++;
        }

        return bitset;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public boolean isSelected(int i)
    {
        return selection.get(i);
    }

    public SelectionIterator iterator()
    {
        return new Itr();
    }

    public SelectionIterator iterator(int i, int j)
    {
        return new Itr(i, j);
    }

    public Iterator iterator(List list)
    {
        return new ListItr(list);
    }

    public Iterator iterator(List list, int i, int j)
    {
        return new ListItr(list, i, j);
    }

    public void removeFromSelection(int i)
    {
        if(selection.get(i))
        {
            size--;
            selection.clear(i);
        }
    }

    public synchronized Iterator removeIterator(List list)
    {
        return new RemoveItr(list);
    }

    public SelectionIterator reverseIterator()
    {
        return new RItr();
    }

    public Object[] select(Object aobj[])
    {
        Object aobj1[] = (Object[])Array.newInstance(((Object) (aobj)).getClass().getComponentType(), size());
        int i = 0;
        for(SelectionIterator selectioniterator = iterator(); selectioniterator.hasNext();)
            aobj1[i++] = aobj[selectioniterator.nextIndex()];

        return aobj1;
    }

    public List select(List list)
    {
        ArrayList arraylist = new ArrayList(size());
        int i = 0;
        for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
            if(selection.get(i++))
                arraylist.add(iterator1.next());
            else
                iterator1.next();

        return arraylist;
    }

    public void setFocus(int i)
    {
        focus = i;
    }

    public boolean setFocus(ActionContext actioncontext, String s)
    {
        try
        {
            return actioncontext.fillProperty(this, "focus", s);
        }
        catch(ParseException _ex)
        {
            return false;
        }
    }

    public void setSelected(int i)
    {
        addToSelection(i);
    }

    public void setSelection(String as[])
    {
        if(as == null)
            return;
        for(int i = 0; i < as.length; i++)
            try
            {
                addToSelection(Integer.parseInt(as[i]));
            }
            catch(NumberFormatException _ex) { }

    }

    public void setSelection(int i, int j)
    {
        int k = i;
        for(; j > 0; j--)
        {
            addToSelection(k);
            k++;
        }

    }

    public void setSelection(int i, int j, BitSet bitset)
    {
        int k = i;
        for(int l = 0; l < j; l++)
        {
            setSelection(k, bitset.get(l));
            k++;
        }

    }

    public void setSelection(int i, boolean flag)
    {
        if(selection.get(i) != flag)
            if(flag)
            {
                selection.set(i);
                size++;
            } else
            {
                selection.clear(i);
                size--;
            }
    }

    public void setSelection(ActionContext actioncontext, String s)
    {
        try
        {
            int i = focus;
            if(actioncontext.fillProperty(this, "focus", s))
            {
                clearSelection();
                actioncontext.fillProperty(this, "selection", s);
            }
            focus = i;
        }
        catch(ParseException _ex) { }
    }

    public void setSelection(ActionContext actioncontext, String s, int i, int j)
    {
        try
        {
            int k = focus;
            if(actioncontext.fillProperty(this, "focus", s))
            {
                clearSelection(i, j);
                actioncontext.fillProperty(this, "selection", s);
            }
            focus = k;
        }
        catch(ParseException _ex) { }
    }

    public int size()
    {
        return size;
    }
    
	public String[] getSelection()
	{
		return null;
	}

    private BitSet selection;
    private int focus;
    private int size;

}