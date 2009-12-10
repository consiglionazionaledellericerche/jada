package it.spasia.generator.util;

import java.io.Serializable;
import java.util.Enumeration;


class OrderedHashtableEnumerator
    implements Serializable, Enumeration
{

    public OrderedHashtableEnumerator(OrderedHashtable orderedhashtable)
    {
        oht = orderedhashtable;
        keysEnum = orderedhashtable.keys();
    }

    public boolean hasMoreElements()
    {
        return keysEnum.hasMoreElements();
    }

    public Object nextElement()
    {
        return oht.get(keysEnum.nextElement());
    }

    private Enumeration keysEnum;
    private OrderedHashtable oht;
}