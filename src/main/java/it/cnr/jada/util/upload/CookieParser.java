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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

public class CookieParser {

    private HttpServletRequest req;
    private Hashtable cookieJar;

    public CookieParser(HttpServletRequest req) {
        cookieJar = new Hashtable();
        this.req = req;
        parseCookies();
    }

    public boolean getBooleanCookie(String name)
            throws CookieNotFoundException {
        return (new Boolean(getStringCookie(name))).booleanValue();
    }

    public boolean getBooleanCookie(String name, boolean def) {
        try {
            return getBooleanCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public byte getByteCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return Byte.parseByte(getStringCookie(name));
    }

    public byte getByteCookie(String name, byte def) {
        try {
            return getByteCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public char getCharCookie(String name)
            throws CookieNotFoundException {
        String param = getStringCookie(name);
        if (param.length() == 0)
            throw new CookieNotFoundException(name + " is empty string");
        else
            return param.charAt(0);
    }

    public char getCharCookie(String name, char def) {
        try {
            return getCharCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public double getDoubleCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return (new Double(getStringCookie(name))).doubleValue();
    }

    public double getDoubleCookie(String name, double def) {
        try {
            return getDoubleCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public float getFloatCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return (new Float(getStringCookie(name))).floatValue();
    }

    public float getFloatCookie(String name, float def) {
        try {
            return getFloatCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public int getIntCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return Integer.parseInt(getStringCookie(name));
    }

    public int getIntCookie(String name, int def) {
        try {
            return getIntCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public long getLongCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return Long.parseLong(getStringCookie(name));
    }

    public long getLongCookie(String name, long def) {
        try {
            return getLongCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public short getShortCookie(String name)
            throws CookieNotFoundException, NumberFormatException {
        return Short.parseShort(getStringCookie(name));
    }

    public short getShortCookie(String name, short def) {
        try {
            return getShortCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    public String getStringCookie(String name)
            throws CookieNotFoundException {
        String value = (String) cookieJar.get(name);
        if (value == null)
            throw new CookieNotFoundException(name + " not found");
        else
            return value;
    }

    public String getStringCookie(String name, String def) {
        try {
            return getStringCookie(name);
        } catch (Exception _ex) {
            return def;
        }
    }

    void parseCookies() {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                String name = cookies[i].getName();
                String value = cookies[i].getValue();
                cookieJar.put(name, value);
            }

        }
    }
}