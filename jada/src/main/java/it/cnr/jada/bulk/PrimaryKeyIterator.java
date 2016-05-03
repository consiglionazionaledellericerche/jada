package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.Iterator;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey

class PrimaryKeyIterator
    implements Serializable, Iterator
{

    public PrimaryKeyIterator(Iterator iterator1)
    {
        iterator = iterator1;
    }

    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    public Object next()
    {
        return BulkPrimaryKey.getBulk(iterator.next());
    }

    public void remove()
    {
        iterator.remove();
    }

    private Iterator iterator;
}