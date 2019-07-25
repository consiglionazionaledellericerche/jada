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

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class PrimaryKeyChangedException extends Exception
        implements Serializable {

    private OggettoBulk bulk;

    public PrimaryKeyChangedException() {
    }

    public PrimaryKeyChangedException(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }

    public PrimaryKeyChangedException(String s) {
        super(s);
    }

    public OggettoBulk getBulk() {
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }
}