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

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class CacheServletOutputStream extends ServletOutputStream {

    ServletOutputStream _flddelegate;
    ByteArrayOutputStream cache;

    CacheServletOutputStream(ServletOutputStream out) {
        _flddelegate = out;
        cache = new ByteArrayOutputStream(4096);
    }

    public ByteArrayOutputStream getBuffer() {
        return cache;
    }

    public void write(int b)
            throws IOException {
        _flddelegate.write(b);
        cache.write(b);
    }

    public void write(byte b[])
            throws IOException {
        _flddelegate.write(b);
        cache.write(b);
    }

    public void write(byte buf[], int offset, int len)
            throws IOException {
        _flddelegate.write(buf, offset, len);
        cache.write(buf, offset, len);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}