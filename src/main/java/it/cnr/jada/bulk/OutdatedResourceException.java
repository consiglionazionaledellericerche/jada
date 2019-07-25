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

public class OutdatedResourceException extends Exception
        implements Serializable {

    private OggettoBulk bulk;

    public OutdatedResourceException() {
    }

    public OutdatedResourceException(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }

    public OutdatedResourceException(String s) {
        super(s);
    }

    public OutdatedResourceException(String s, OggettoBulk oggettobulk) {
        super(s);
        bulk = oggettobulk;
    }

    public OggettoBulk getBulk() {
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }
}