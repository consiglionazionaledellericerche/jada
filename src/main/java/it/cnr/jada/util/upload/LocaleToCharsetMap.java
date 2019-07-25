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

import java.util.Hashtable;
import java.util.Locale;

public class LocaleToCharsetMap {

    private static Hashtable map;

    static {
        map = new Hashtable();
        map.put("ar", "ISO-8859-6");
        map.put("be", "ISO-8859-5");
        map.put("bg", "ISO-8859-5");
        map.put("ca", "UTF-8");
        map.put("cs", "ISO-8859-2");
        map.put("da", "UTF-8");
        map.put("de", "UTF-8");
        map.put("el", "ISO-8859-7");
        map.put("en", "UTF-8");
        map.put("es", "UTF-8");
        map.put("et", "UTF-8");
        map.put("fi", "UTF-8");
        map.put("fr", "UTF-8");
        map.put("hr", "ISO-8859-2");
        map.put("hu", "ISO-8859-2");
        map.put("is", "UTF-8");
        map.put("it", "UTF-8");
        map.put("iw", "ISO-8859-8");
        map.put("ja", "Shift_JIS");
        map.put("ko", "EUC-KR");
        map.put("lt", "ISO-8859-2");
        map.put("lv", "ISO-8859-2");
        map.put("mk", "ISO-8859-5");
        map.put("nl", "UTF-8");
        map.put("no", "UTF-8");
        map.put("pl", "ISO-8859-2");
        map.put("pt", "UTF-8");
        map.put("ro", "ISO-8859-2");
        map.put("ru", "ISO-8859-5");
        map.put("sh", "ISO-8859-5");
        map.put("sk", "ISO-8859-2");
        map.put("sl", "ISO-8859-2");
        map.put("sq", "ISO-8859-2");
        map.put("sr", "ISO-8859-5");
        map.put("sv", "UTF-8");
        map.put("tr", "ISO-8859-9");
        map.put("uk", "ISO-8859-5");
        map.put("zh", "GB2312");
        map.put("zh_TW", "Big5");
    }

    public LocaleToCharsetMap() {
    }

    public static String getCharset(Locale loc) {
        String charset = (String) map.get(loc.toString());
        if (charset != null) {
            return charset;
        } else {
            charset = (String) map.get(loc.getLanguage());
            return charset;
        }
    }
}