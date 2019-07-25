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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class OutputStreamCounter extends OutputStream
        implements Serializable {

    private long count;

    public OutputStreamCounter() {
    }

    public long getCount() {
        return count;
    }

    public void write(byte abyte0[])
            throws IOException {
        count += abyte0.length;
    }

    public void write(byte abyte0[], int i, int j)
            throws IOException {
        count += j;
    }

    public void write(int i)
            throws IOException {
        count++;
    }
}