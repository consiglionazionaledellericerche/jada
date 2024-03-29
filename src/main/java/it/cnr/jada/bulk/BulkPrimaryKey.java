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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class BulkPrimaryKey
        implements Serializable {

    private OggettoBulk bulk;

    private BulkPrimaryKey(Object obj) {
        bulk = (OggettoBulk) obj;
    }

    public static Object getBulk(Object obj) {
        if (obj instanceof BulkPrimaryKey)
            return ((BulkPrimaryKey) obj).getBulk();
        else
            return obj;
    }

    public static Object getPrimaryKey(Object obj) {
        if (obj instanceof OggettoBulk)
            return new BulkPrimaryKey(obj);
        else
            return obj;
    }

    public boolean equals(Object obj) {
        return bulk.equalsByPrimaryKey(getBulk(obj));
    }

    public final OggettoBulk getBulk() {
        return bulk;
    }

    public int hashCode() {
        return bulk.primaryKeyHashCode();
    }

    private synchronized void readObject(ObjectInputStream objectinputstream)
            throws IOException, ClassNotFoundException {
        bulk = (OggettoBulk) objectinputstream.readObject();
    }

    private synchronized void writeObject(ObjectOutputStream objectoutputstream)
            throws IOException {
        objectoutputstream.writeObject(bulk);
    }
}