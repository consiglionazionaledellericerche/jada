package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public final class BulkCollections
    implements Serializable
{

    private BulkCollections()
    {
    }

    public static boolean containsByPrimaryKey(Collection collection, OggettoBulk oggettobulk)
    {
        Iterator iterator = collection.iterator();
        if(oggettobulk == null){
            while(iterator.hasNext()) 
                if(iterator.next() == null)
                    return true;
        }            
        else{
            while(iterator.hasNext()) 
                if(oggettobulk.equalsByPrimaryKey(iterator.next()))
                    return true;
        }            
        return false;
    }

    public static int indexOfByPrimaryKey(List list, OggettoBulk oggettobulk)
    {
        ListIterator listiterator = list.listIterator();
        if(oggettobulk == null){
            while(listiterator.hasNext()) 
                if(listiterator.next() == null)
                    return listiterator.previousIndex();
        }            
        else{
            while(listiterator.hasNext()) 
                if(oggettobulk.equalsByPrimaryKey(listiterator.next()))
                    return listiterator.previousIndex();
        }            
        return -1;
    }

    public static int lastIndexOfByPrimaryKey(List list, OggettoBulk oggettobulk)
    {
        ListIterator listiterator = list.listIterator(list.size());
        if(oggettobulk == null){
            while(listiterator.hasPrevious()) 
                if(listiterator.previous() == null)
                    return listiterator.nextIndex();
        }            
        else{
            while(listiterator.hasPrevious()) 
                if(oggettobulk.equalsByPrimaryKey(listiterator.previous()))
                    return listiterator.nextIndex();
        }            
        return -1;
    }

    public static boolean removeByPrimaryKey(Collection collection, OggettoBulk oggettobulk)
    {
        for(Iterator iterator = collection.iterator(); iterator.hasNext();)
            if(oggettobulk.equalsByPrimaryKey(iterator.next()))
            {
                iterator.remove();
                return true;
            }

        return false;
    }
}