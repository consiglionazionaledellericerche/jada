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

import jakarta.servlet.ServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class ParameterParser {

    private ServletRequest req;
    private String encoding;

    public ParameterParser(ServletRequest req) {
        this.req = req;
    }

    public boolean getBooleanParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        String value = getStringParameter(name).toLowerCase();
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes"))
            return true;
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("no"))
            return false;
        else
            throw new NumberFormatException("Parameter " + name + " value " + value + " is not a boolean");
    }

    public boolean getBooleanParameter(String name, boolean def) {
        try {
            return getBooleanParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public byte getByteParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Byte.parseByte(getStringParameter(name));
    }

    public byte getByteParameter(String name, byte def) {
        try {
            return getByteParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public char getCharParameter(String name)
            throws ParameterNotFoundException {
        String param = getStringParameter(name);
        if (param.length() == 0)
            throw new ParameterNotFoundException(name + " is empty string");
        else
            return param.charAt(0);
    }

    public char getCharParameter(String name, char def) {
        try {
            return getCharParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public double getDoubleParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Double.parseDouble(getStringParameter(name));
    }

    public double getDoubleParameter(String name, double def) {
        try {
            return getDoubleParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public float getFloatParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Float.parseFloat(getStringParameter(name));
    }

    public float getFloatParameter(String name, float def) {
        try {
            return getFloatParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public int getIntParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Integer.parseInt(getStringParameter(name));
    }

    public int getIntParameter(String name, int def) {
        try {
            return getIntParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public long getLongParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Long.parseLong(getStringParameter(name));
    }

    public long getLongParameter(String name, long def) {
        try {
            return getLongParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public String[] getMissingParameters(String required[]) {
        Vector missing = new Vector();
        for (int i = 0; i < required.length; i++) {
            String val = getStringParameter(required[i], null);
            if (val == null)
                missing.addElement(required[i]);
        }

        if (missing.size() == 0) {
            return null;
        } else {
            String[] ret = new String[missing.size()];
            missing.copyInto(ret);
            return ret;
        }
    }

    public short getShortParameter(String name)
            throws ParameterNotFoundException, NumberFormatException {
        return Short.parseShort(getStringParameter(name));
    }

    public short getShortParameter(String name, short def) {
        try {
            return getShortParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public String getStringParameter(String name)
            throws ParameterNotFoundException {
        String[] values = req.getParameterValues(name);
        if (values == null)
            throw new ParameterNotFoundException(name + " not found");
        if (values[0].length() == 0)
            throw new ParameterNotFoundException(name + " was empty");
        if (encoding == null)
            return values[0];
        try {
            return new String(values[0].getBytes("8859_1"), encoding);
        } catch (UnsupportedEncodingException _ex) {
            return values[0];
        }
    }

    public String getStringParameter(String name, String def) {
        try {
            return getStringParameter(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public void setCharacterEncoding(String encoding)
            throws UnsupportedEncodingException {
        new String("".getBytes("8859_1"), encoding);
        this.encoding = encoding;
    }
}