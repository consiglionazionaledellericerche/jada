package it.cnr.jada.util;

import java.io.Serializable;
import java.text.*;

public class UppercaseStringFormat extends Format
    implements Serializable
{

    public UppercaseStringFormat()
    {
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition)
    {
        if(obj != null)
            stringbuffer.append(obj.toString().toUpperCase());
        return stringbuffer;
    }

    public Object parseObject(String s, ParsePosition parseposition)
    {
        parseposition.setIndex(s != null ? s.length() : 1);
        if(s == null)
            return null;
        else
            return s.toUpperCase();
    }
}