package it.cnr.jada.util;

import java.io.Serializable;
import java.text.*;

public class NullableFormat extends Format
    implements Serializable
{

    public NullableFormat(Format format1)
    {
        format = format1;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition)
    {
        if(obj == null)
            return null;
        else
            return format.format(obj, stringbuffer, fieldposition);
    }

    public Format getFormat()
    {
        return format;
    }

    public Object parseObject(String s)
        throws ParseException
    {
        return format.parseObject(s);
    }

    public Object parseObject(String s, ParsePosition parseposition)
    {
        if(s == null || s.length() == 0)
            return null;
        else
            return format.parseObject(s, parseposition);
    }

    private final Format format;
}