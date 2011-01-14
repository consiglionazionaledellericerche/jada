package it.cnr.jada.bulk;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import it.cnr.jada.util.Config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;

public class BigIntegerFormat extends Format
    implements Serializable
{

    public BigIntegerFormat()
    {
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition)
    {
        return format.format(obj, stringbuffer, fieldposition);
    }

    public static Format getInstance()
    {
        return instance;
    }

    public Object parseObject(String s, ParsePosition parseposition)
    {
        parseposition.setIndex(s != null ? s.length() + 1 : 1);
        if(s == null)
            return null;
        s = s.trim();
        if(s.length() == 0)
            return null;
        try
        {
            Number number = (Number)parser.parseObject(s);
            if(number instanceof BigDecimal)
                throw new ParseException("Sono ammessi solo numeri interi", s.length());
            if(number instanceof BigInteger)
                return number;
            else
                return BigInteger.valueOf(number.longValue());
        }
        catch(ParseException _ex)
        {
            parseposition.setIndex(0);
        }
        return null;
    }

    private static final BigIntegerFormat instance = new BigIntegerFormat();
    private static final Format format = new DecimalFormat("#", new DecimalFormatSymbols(Config.getHandler().getLocale()));
    private static final Format parser = new DecimalFormat("#,###", new DecimalFormatSymbols(Config.getHandler().getLocale()));

}