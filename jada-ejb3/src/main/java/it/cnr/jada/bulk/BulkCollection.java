package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.Iterator;

public interface BulkCollection
    extends Serializable
{

    public abstract Iterator deleteIterator();

    public abstract Iterator iterator();
}