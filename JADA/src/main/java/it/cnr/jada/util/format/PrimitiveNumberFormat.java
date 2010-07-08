/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.Locale;
/**
 * Un Format che permette di convertire da/in stringa i tipi numerici interi primitivi di Java. 
 * A differenza di un DecimalFormat l'operazione di parse restituisce sempre una istanza del tipo 
 * primitivo scelto e genera una eccezione se il numero da convertire � fuori dalla precisione consentita dal tipo.
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PrimitiveNumberFormat extends Format implements Serializable{

	private static final long serialVersionUID = 1L;
    private static final Format format = new java.text.DecimalFormat("#", new java.text.DecimalFormatSymbols(Locale.getDefault()));
    @SuppressWarnings("unused")
	private static final Format parser = java.text.NumberFormat.getInstance(Locale.getDefault());
	private final long maxValue;
    private final long minValue;
    private static final PrimitiveNumberFormat LONG = new PrimitiveNumberFormat(0x7fffffffffffffffL, 0x8000000000000000L);
    private static final PrimitiveNumberFormat INTEGER = new PrimitiveNumberFormat(0x7fffffffL, 0xffffffff80000000L);
    private static final PrimitiveNumberFormat SHORT = new PrimitiveNumberFormat(32767L, -32768L);
    private static final PrimitiveNumberFormat BYTE = new PrimitiveNumberFormat(127L, -128L);


	public PrimitiveNumberFormat(){
        this(0x7fffffffffffffffL, 0x8000000000000000L);
    }

    public PrimitiveNumberFormat(long maxValue, long minValue){
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fieldposition){
        return format.format(obj, toAppendTo, fieldposition);
    }
    /**
     * Restituisce una istanza di PrimitiveNumberFormat che formatta/converte numeri interi di tipo byte
     */
    public static Format getByteInstance(){
        return BYTE;
    }
    /**
     * Restituisce una istanza di PrimitiveNumberFormat che formatta/converte numeri interi di tipo int
     */
    public static Format getIntegerInstance(){
        return INTEGER;
    }
    /**
     * Restituisce una istanza di PrimitiveNumberFormat che formatta/converte numeri interi di tipo long
     */
    public static Format getLongInstance(){
        return LONG;
    }
    /**
     * Restituisce una istanza di PrimitiveNumberFormat che formatta/converte numeri interi di tipo short
     */
    public static Format getShortInstance(){
        return SHORT;
    }

    public synchronized Object parseObject(String source, ParsePosition parseposition){
        parseposition.setIndex(source != null ? source.length() + 1 : 1);
        if(source == null)
            return null;
        source = source.trim();
        if(source.length() == 0)
            return null;
        try{
            Number number = (Number)format.parseObject(source);
            if(number instanceof BigDecimal)
                throw new ParseException("Sono ammessi solo numeri interi", source.length());
            if(number instanceof BigInteger)
                throw new ParseException("Numero troppo grande o troppo piccolo", source.length());
            if(number.longValue() > maxValue)
                throw new ParseException("Numero troppo grande (massimo numero assegnabile: " + maxValue, source.length());
            if(number.longValue() < minValue)
                throw new ParseException("Numero troppo piccolo (minimo numero assegnabile: " + minValue, source.length());
            else
                return number;
        }catch(ParseException _ex){
            parseposition.setIndex(0);
        }
        return null;
    }
}