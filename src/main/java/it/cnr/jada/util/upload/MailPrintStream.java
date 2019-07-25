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

import java.io.OutputStream;
import java.io.PrintStream;

class MailPrintStream extends PrintStream {

    int lastChar;

    public MailPrintStream(OutputStream out) {
        super(out, true);
    }

    void rawPrint(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++)
            rawWrite(s.charAt(i));

    }

    void rawWrite(int b) {
        super.write(b);
    }

    public void write(int b) {
        if (b == 10 && lastChar != 13) {
            rawWrite(13);
            rawWrite(b);
        } else if (b == 46 && lastChar == 10) {
            rawWrite(46);
            rawWrite(b);
        } else if (b != 10 && lastChar == 13) {
            rawWrite(10);
            rawWrite(b);
            if (b == 46)
                rawWrite(46);
        } else {
            rawWrite(b);
        }
        lastChar = b;
    }

    public void write(byte buf[], int off, int len) {
        for (int i = 0; i < len; i++)
            write(buf[off + i]);

    }
}