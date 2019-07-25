/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.bulk;

import it.cnr.jada.util.Config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class PrimitiveNumberFormat extends Format
        implements Serializable {

    private static final Format format = new java.text.DecimalFormat("#", new java.text.DecimalFormatSymbols(Config.getHandler().getLocale()));
    private static final Format parser = java.text.NumberFormat.getInstance(Config.getHandler().getLocale());
    private static final PrimitiveNumberFormat LONG = new PrimitiveNumberFormat(0x7fffffffffffffffL, 0x8000000000000000L);
    private static final PrimitiveNumberFormat INTEGER = new PrimitiveNumberFormat(0x7fffffffL, 0xffffffff80000000L);
    private static final PrimitiveNumberFormat SHORT = new PrimitiveNumberFormat(32767L, -32768L);
    private static final PrimitiveNumberFormat BYTE = new PrimitiveNumberFormat(127L, -128L);
    private final long maxValue;
    private final long minValue;

    public PrimitiveNumberFormat() {
        this(0x7fffffffffffffffL, 0x8000000000000000L);
    }
    public PrimitiveNumberFormat(long l, long l1) {
        maxValue = l;
        minValue = l1;
    }

    public static Format getByteInstance() {
        return BYTE;
    }

    public static Format getIntegerInstance() {
        return INTEGER;
    }

    public static Format getLongInstance() {
        return LONG;
    }

    public static Format getShortInstance() {
        return SHORT;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition) {
        return format.format(obj, stringbuffer, fieldposition);
    }

    public synchronized Object parseObject(String s, ParsePosition parseposition) {
        parseposition.setIndex(s != null ? s.length() + 1 : 1);
        if (s == null)
            return null;
        s = s.trim();
        if (s.length() == 0)
            return null;
        try {
            Number number = (Number) format.parseObject(s);
            if (number instanceof BigDecimal)
                throw new ParseException("Sono ammessi solo numeri interi", s.length());
            if (number instanceof BigInteger)
                throw new ParseException("Numero troppo grande o troppo piccolo", s.length());
            if (number.longValue() > maxValue)
                throw new ParseException("Numero troppo grande (massimo numero assegnabile: " + maxValue, s.length());
            if (number.longValue() < minValue)
                throw new ParseException("Numero troppo piccolo (minimo numero assegnabile: " + minValue, s.length());
            else
                return number;
        } catch (ParseException _ex) {
            parseposition.setIndex(0);
        }
        return null;
    }

}