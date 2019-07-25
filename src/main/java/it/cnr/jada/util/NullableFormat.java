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

package it.cnr.jada.util;

import java.io.Serializable;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class NullableFormat extends Format
        implements Serializable {

    private final Format format;

    public NullableFormat(Format format1) {
        format = format1;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition) {
        if (obj == null)
            return null;
        else
            return format.format(obj, stringbuffer, fieldposition);
    }

    public Format getFormat() {
        return format;
    }

    public Object parseObject(String s)
            throws ParseException {
        return format.parseObject(s);
    }

    public Object parseObject(String s, ParsePosition parseposition) {
        if (s == null || s.length() == 0)
            return null;
        else
            return format.parseObject(s, parseposition);
    }
}