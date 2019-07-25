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

package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

public abstract class FetchPattern
        implements XmlWriteable, Serializable {

    protected String pattern;
    protected boolean starred;

    public FetchPattern() {
    }

    public FetchPattern(String s) {
        this();
        setPattern(s);
    }

    public abstract boolean excludePrefix(String s, boolean flag);

    public String getPattern() {
        if (starred)
            return pattern + "*";
        else
            return pattern;
    }

    public void setPattern(String s) {
        pattern = s;
        starred = s.charAt(s.length() - 1) == '*';
        if (starred)
            pattern = s.substring(0, s.length() - 1);
    }

    public abstract boolean includePrefix(String s, boolean flag);

    public boolean match(String s) {
        if (starred)
            return s.startsWith(pattern);
        else
            return s.equals(pattern);
    }

    public abstract boolean match(String s, boolean flag);

    public boolean matchPrefix(String s) {
        if (starred)
            return s.startsWith(pattern);
        else
            return pattern.startsWith(s);
    }

    void newMethod() {
    }

    public String toString() {
        return XmlWriter.toString(this);
    }

    public abstract void writeTo(XmlWriter xmlwriter)
            throws IOException;
}