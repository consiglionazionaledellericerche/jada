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

public class BigDecimalFormat extends Format
        implements Serializable {

    private static final BigDecimalFormat instance = new BigDecimalFormat();
    private static final Format format = new java.text.DecimalFormat("#.####################", new java.text.DecimalFormatSymbols(Config.getHandler().getLocale()));
    private static final Format parser = java.text.NumberFormat.getInstance(Config.getHandler().getLocale());

    public BigDecimalFormat() {
    }

    public static Format getInstance() {
        return instance;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition) {
        return format.format(obj, stringbuffer, fieldposition);
    }

    public Object parseObject(String s, ParsePosition parseposition) {
        parseposition.setIndex(s != null ? s.length() + 1 : 1);
        if (s == null)
            return null;
        s = s.trim();
        if (s.length() == 0)
            return null;
        try {
            Number number = (Number) parser.parseObject(s);
            if (number instanceof BigDecimal)
                return number;
            if (number instanceof BigInteger)
                return new BigDecimal((BigInteger) number, 0);
            else
                return new BigDecimal(number.toString());
        } catch (ParseException _ex) {
            parseposition.setIndex(0);
        }
        return null;
    }

}