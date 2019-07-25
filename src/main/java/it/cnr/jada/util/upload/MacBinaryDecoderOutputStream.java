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

package it.cnr.jada.util.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MacBinaryDecoderOutputStream extends FilterOutputStream {

    private int bytesFiltered;
    private int dataForkLength;

    public MacBinaryDecoderOutputStream(OutputStream out) {
        super(out);
        bytesFiltered = 0;
        dataForkLength = 0;
    }

    public void write(int b)
            throws IOException {
        if (bytesFiltered <= 86 && bytesFiltered >= 83) {
            int leftShift = (86 - bytesFiltered) * 8;
            dataForkLength = dataForkLength | (b & 0xff) << leftShift;
        } else if (bytesFiltered < 128 + dataForkLength && bytesFiltered >= 128)
            super.out.write(b);
        bytesFiltered++;
    }

    public void write(byte b[])
            throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len)
            throws IOException {
        if (bytesFiltered >= 128 + dataForkLength)
            bytesFiltered += len;
        else if (bytesFiltered >= 128 && bytesFiltered + len <= 128 + dataForkLength) {
            super.out.write(b, off, len);
            bytesFiltered += len;
        } else {
            for (int i = 0; i < len; i++)
                write(b[off + i]);

        }
    }
}