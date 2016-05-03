package it.cnr.jada.bulk;

import it.cnr.jada.util.Config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;

public class PrimitiveNumberFormat extends Format
    implements Serializable
{

    public PrimitiveNumberFormat()
    {
        this(0x7fffffffffffffffL, 0x8000000000000000L);
    }

    public PrimitiveNumberFormat(long l, long l1)
    {
        maxValue = l;
        minValue = l1;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition)
    {
        return format.format(obj, stringbuffer, fieldposition);
    }

    public static Format getByteInstance()
    {
        return BYTE;
    }

    public static Format getIntegerInstance()
    {
        return INTEGER;
    }

    public static Format getLongInstance()
    {
        return LONG;
    }

    public static Format getShortInstance()
    {
        return SHORT;
    }

    public synchronized Object parseObject(String s, ParsePosition parseposition)
    {
        parseposition.setIndex(s != null ? s.length() + 1 : 1);
        if(s == null)
            return null;
        s = s.trim();
        if(s.length() == 0)
            return null;
        try
        {
            Number number = (Number)format.parseObject(s);
            if(number instanceof BigDecimal)
                throw new ParseException("Sono ammessi solo numeri interi", s.length());
            if(number instanceof BigInteger)
                throw new ParseException("Numero troppo grande o troppo piccolo", s.length());
            if(number.longValue() > maxValue)
                throw new ParseException("Numero troppo grande (massimo numero assegnabile: " + maxValue, s.length());
            if(number.longValue() < minValue)
                throw new ParseException("Numero troppo piccolo (minimo numero assegnabile: " + minValue, s.length());
            else
                return number;
        }
        catch(ParseException _ex)
        {
            parseposition.setIndex(0);
        }
        return null;
    }

    private static final Format format = new java.text.DecimalFormat("#", new java.text.DecimalFormatSymbols(Config.getHandler().getLocale()));
    private static final Format parser = java.text.NumberFormat.getInstance(Config.getHandler().getLocale());
    private final long maxValue;
    private final long minValue;
    private static final PrimitiveNumberFormat LONG = new PrimitiveNumberFormat(0x7fffffffffffffffL, 0x8000000000000000L);
    private static final PrimitiveNumberFormat INTEGER = new PrimitiveNumberFormat(0x7fffffffL, 0xffffffff80000000L);
    private static final PrimitiveNumberFormat SHORT = new PrimitiveNumberFormat(32767L, -32768L);
    private static final PrimitiveNumberFormat BYTE = new PrimitiveNumberFormat(127L, -128L);

}