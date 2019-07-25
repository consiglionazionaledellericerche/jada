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

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy

public class FetchAllPolicy
        implements Serializable, XmlWriteable, FetchPolicy {

    public static final FetchAllPolicy FETCHALL = new FetchAllPolicy();

    private FetchAllPolicy() {
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy) {
        return this;
    }

    public FetchPolicy addPrefix(String s) {
        return this;
    }

    public boolean excludePrefix(String s) {
        return false;
    }

    public boolean include(FetchPolicy fetchpolicy) {
        return true;
    }

    public boolean include(String s) {
        return true;
    }

    public boolean includePrefix(String s) {
        return true;
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy) {
        if (fetchpolicy == this)
            return null;
        else
            return this;
    }

    public String toString() {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        xmlwriter.openTag("fetchPolicy");
        xmlwriter.printAttribute("name", "FETCHALL", null);
        xmlwriter.openInlineTag("include");
        xmlwriter.printAttribute("pattern", "*", null);
        xmlwriter.closeLastTag();
        xmlwriter.closeLastTag();
    }

}