package it.cnr.jada.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public final class EmptyEnumeration
    implements Enumeration
{

    private EmptyEnumeration()
    {
    }

    public static final EmptyEnumeration getInstance()
    {
        return instance;
    }

    public final boolean hasMoreElements()
    {
        return false;
    }

    public final Object nextElement()
    {
        throw new NoSuchElementException();
    }

    private static final EmptyEnumeration instance = new EmptyEnumeration();

}