package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            SimpleBulkList, BulkCollection, OggettoBulk

public class BulkList<T extends OggettoBulk> extends SimpleBulkList<T>
    implements Serializable, BulkCollection
{

    public BulkList()
    {
    }

    public BulkList(Collection collection)
    {
        super(collection);
        if(collection instanceof BulkList)
            addAllDeleted((BulkList)collection);
    }

    public void add(int i, T obj)
    {
        super.add(i, obj);
        if(deleteList != null)
            deleteList.remove(obj);
    }

    public boolean addAll(int i, Collection collection)
    {
        if(collection instanceof BulkList)
            deleteList.addAll(((BulkList)collection).deleteList);
        return super.addAll(i, collection);
    }

    public boolean addAll(Collection collection)
    {
        if(collection instanceof BulkList)
            addAllDeleted((BulkList)collection);
        return super.addAll(collection);
    }

    private void addAllDeleted(BulkList bulklist)
    {
        if(bulklist.deleteList != null)
        {
            if(deleteList == null)
                deleteList = new Vector();
            deleteList.addAll(bulklist.deleteList);
        }
    }

    public Iterator deleteIterator()
    {
        if(deleteList == null)
            return super.deleteIterator();
        else
            return deleteList.iterator();
    }

    public T remove(int i)
    {
        T obj = super.remove(i);
        if(obj != null)
        {
            if(deleteList == null)
                deleteList = new Vector();
            deleteList.add(obj);
        }
        return obj;
    }

    public boolean removeByPrimaryKey(OggettoBulk oggettobulk)
    {
        if(super.removeByPrimaryKey(oggettobulk))
        {
            if(deleteList == null)
                deleteList = new Vector();
            deleteList.add(oggettobulk);
            return true;
        } else
        {
            return false;
        }
    }

    Vector deleteList;
}