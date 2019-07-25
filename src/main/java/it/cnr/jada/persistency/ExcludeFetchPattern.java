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
//            FetchPattern

public class ExcludeFetchPattern extends FetchPattern
        implements Serializable {

    public ExcludeFetchPattern() {
    }

    public ExcludeFetchPattern(String s) {
        super(s);
    }

    public boolean excludePrefix(String s, boolean flag) {
        if (!flag)
            return super.starred && s.startsWith(super.pattern);
        else
            return true;
    }

    public boolean includePrefix(String s, boolean flag) {
        if (flag) {
            if (super.starred)
                return !super.pattern.startsWith(s) && !s.startsWith(super.pattern);
            else
                return !super.pattern.startsWith(s);
        } else {
            return false;
        }
    }

    public boolean match(String s, boolean flag) {
        if (flag)
            return !match(s);
        else
            return false;
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        xmlwriter.openInlineTag("exclude");
        xmlwriter.printAttribute("pattern", super.starred ? super.pattern + "*" : super.pattern, "");
        xmlwriter.closeLastTag();
    }
}