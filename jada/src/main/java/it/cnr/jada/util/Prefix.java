package it.cnr.jada.util;

import java.io.Serializable;

public final class Prefix
    implements Serializable
{

    private Prefix()
    {
    }

    public static String prependPrefix(String s, String s1)
    {
        if(s == null)
            return s1;
        else
            return s + "." + s1;
    }
}