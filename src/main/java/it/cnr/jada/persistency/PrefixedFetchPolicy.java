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

import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy, FetchAllPolicy, ComplexFetchPolicy, Prefix, 
//            FetchNonePolicy

class PrefixedFetchPolicy
        implements Serializable, FetchPolicy {

    private final String prefix;
    private final FetchPolicy fetchPolicy;

    PrefixedFetchPolicy(FetchPolicy fetchpolicy, String s) {
        fetchPolicy = fetchpolicy;
        prefix = s;
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy) {
        if (fetchpolicy == null)
            return this;
        if (fetchpolicy.equals(FetchAllPolicy.FETCHALL))
            return fetchpolicy;
        if (fetchpolicy instanceof ComplexFetchPolicy)
            return fetchpolicy.addFetchPolicy(this);
        if (fetchpolicy.equals(this))
            return this;
        else
            return new ComplexFetchPolicy(this, fetchpolicy);
    }

    public FetchPolicy addPrefix(String s) {
        if (s == null)
            return this;
        s = Prefix.prependPrefix(prefix, s);
        if (includePrefix(s))
            return FetchAllPolicy.FETCHALL;
        if (excludePrefix(s))
            return FetchNonePolicy.FETCHNONE;
        else
            return new PrefixedFetchPolicy(fetchPolicy, s);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PrefixedFetchPolicy))
            return false;
        PrefixedFetchPolicy prefixedfetchpolicy = (PrefixedFetchPolicy) obj;
        return prefixedfetchpolicy.prefix.equals(prefix) && prefixedfetchpolicy.fetchPolicy.equals(fetchPolicy);
    }

    public boolean excludePrefix(String s) {
        return !s.startsWith(prefix) || fetchPolicy.excludePrefix(s);
    }

    public int hashCode() {
        return prefix.hashCode() + fetchPolicy.hashCode();
    }

    public boolean include(FetchPolicy fetchpolicy) {
        return equals(fetchpolicy);
    }

    public boolean include(String s) {
        return fetchPolicy.include(Prefix.prependPrefix(prefix, s));
    }

    public boolean includePrefix(String s) {
        return s.startsWith(prefix) && fetchPolicy.includePrefix(s);
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy) {
        if (fetchpolicy != null && fetchpolicy.include(this))
            return null;
        else
            return this;
    }

    public String toString() {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        xmlwriter.openTag("prefixedFetchPolicy");
        xmlwriter.printAttribute("prefix", prefix, null);
        fetchPolicy.writeTo(xmlwriter);
        xmlwriter.closeLastTag();
    }
}