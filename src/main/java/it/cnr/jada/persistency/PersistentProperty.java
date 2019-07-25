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

public class PersistentProperty
        implements Serializable {

    private String name;
    private boolean partOfOid;

    public PersistentProperty() {
    }

    public PersistentProperty(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public boolean isPartOfOid() {
        return partOfOid;
    }

    public void setPartOfOid(boolean flag) {
        partOfOid = flag;
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        xmlwriter.openTag("persistentProperty");
        xmlwriter.printAttribute("name", getName(), null);
        xmlwriter.printAttribute("partOfOid", isPartOfOid(), false);
        xmlwriter.closeLastTag();
    }
}