package it.cnr.jada.bulk;

import it.cnr.jada.util.Prefix;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            FieldValidationException

public class FieldValidationMap
    implements Serializable
{

    public FieldValidationMap()
    {
        fieldValidationExceptions = new HashMap();
    }

    public void clear(String s, String s1)
    {
        fieldValidationExceptions.remove(Prefix.prependPrefix(s, s1));
    }

    public void clearAll(String s)
    {
        for(Iterator iterator = fieldValidationExceptions.keySet().iterator(); iterator.hasNext();)
            if(((String)iterator.next()).startsWith(s))
                iterator.remove();

    }

    public FieldValidationException get(String s, String s1)
    {
        return (FieldValidationException)fieldValidationExceptions.get(Prefix.prependPrefix(s, s1));
    }

    public FieldValidationException put(String s, String s1, FieldValidationException fieldvalidationexception)
    {
        fieldValidationExceptions.put(Prefix.prependPrefix(s, s1), fieldvalidationexception);
        return fieldvalidationexception;
    }

    private Map fieldValidationExceptions;
}