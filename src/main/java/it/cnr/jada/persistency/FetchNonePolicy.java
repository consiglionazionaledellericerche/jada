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

public class FetchNonePolicy
        implements Serializable, XmlWriteable, FetchPolicy {

    public static final FetchNonePolicy FETCHNONE = new FetchNonePolicy();

    private FetchNonePolicy() {
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy) {
        return fetchpolicy;
    }

    public FetchPolicy addPrefix(String s) {
        return this;
    }

    public boolean excludePrefix(String s) {
        return true;
    }

    public boolean include(FetchPolicy fetchpolicy) {
        return false;
    }

    public boolean include(String s) {
        return false;
    }

    public boolean includePrefix(String s) {
        return false;
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy) {
        if (fetchpolicy == null)
            return this;
        else
            return null;
    }

    public String toString() {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        xmlwriter.openTag("fetchPolicy");
        xmlwriter.printAttribute("name", "FETCHNONE", null);
        xmlwriter.openInlineTag("exclude");
        xmlwriter.printAttribute("pattern", "*", null);
        xmlwriter.closeLastTag();
        xmlwriter.closeLastTag();
    }

}