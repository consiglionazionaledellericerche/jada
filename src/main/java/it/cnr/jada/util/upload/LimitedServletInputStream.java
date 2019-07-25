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

import javax.servlet.ServletInputStream;
import java.io.IOException;

public class LimitedServletInputStream extends ServletInputStream {

    private ServletInputStream in;
    private int totalExpected;
    private int totalRead;

    public LimitedServletInputStream(ServletInputStream in, int totalExpected) {
        totalRead = 0;
        this.in = in;
        this.totalExpected = totalExpected;
    }

    public int read()
            throws IOException {
        if (totalRead >= totalExpected)
            return -1;
        else
            return in.read();
    }

    public int read(byte b[], int off, int len)
            throws IOException {
        int left = totalExpected - totalRead;
        if (left <= 0)
            return -1;
        int result = in.read(b, off, Math.min(left, len));
        if (result > 0)
            totalRead += result;
        return result;
    }

    public int readLine(byte b[], int off, int len)
            throws IOException {
        int left = totalExpected - totalRead;
        if (left <= 0)
            return -1;
        int result = in.readLine(b, off, Math.min(left, len));
        if (result > 0)
            totalRead += result;
        return result;
    }
}