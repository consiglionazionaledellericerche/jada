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

import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ParamPart extends Part {

    private byte[] value;

    ParamPart(String name, ServletInputStream in, String boundary)
            throws IOException {
        super(name);
        PartInputStream pis = new PartInputStream(in, boundary);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        byte[] buf = new byte[128];
        int read;
        while ((read = pis.read(buf)) != -1)
            baos.write(buf, 0, read);
        pis.close();
        baos.close();
        value = baos.toByteArray();
    }

    public String getStringValue()
            throws UnsupportedEncodingException {
        return getStringValue("UTF-8");
    }

    public String getStringValue(String encoding)
            throws UnsupportedEncodingException {
        return new String(value, encoding);
    }

    public byte[] getValue() {
        return value;
    }

    public boolean isParam() {
        return true;
    }
}