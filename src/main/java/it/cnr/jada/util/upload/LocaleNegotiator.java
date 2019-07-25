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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class LocaleNegotiator {

    private ResourceBundle chosenBundle;
    private Locale chosenLocale;
    private String chosenCharset;

    public LocaleNegotiator(String bundleName, String languages, String charsets) {
        Locale defaultLocale = new Locale("en", "US");
        String defaultCharset = "UTF-8";
        ResourceBundle defaultBundle = null;
        try {
            defaultBundle = ResourceBundle.getBundle(bundleName, defaultLocale);
        } catch (MissingResourceException _ex) {
        }
        if (languages == null) {
            chosenLocale = defaultLocale;
            chosenCharset = defaultCharset;
            chosenBundle = defaultBundle;
            return;
        }
        for (StringTokenizer tokenizer = new StringTokenizer(languages, ","); tokenizer.hasMoreTokens(); ) {
            String lang = tokenizer.nextToken();
            Locale loc = getLocaleForLanguage(lang);
            ResourceBundle bundle = getBundleNoFallback(bundleName, loc);
            if (bundle != null) {
                String charset = getCharsetForLocale(loc, charsets);
                if (charset != null) {
                    chosenLocale = loc;
                    chosenBundle = bundle;
                    chosenCharset = charset;
                    return;
                }
            }
        }

        chosenLocale = defaultLocale;
        chosenCharset = defaultCharset;
        chosenBundle = defaultBundle;
    }

    public ResourceBundle getBundle() {
        return chosenBundle;
    }

    private ResourceBundle getBundleNoFallback(String bundleName, Locale loc) {
        ResourceBundle fallback = null;
        try {
            fallback = ResourceBundle.getBundle(bundleName, new Locale("bogus", ""));
        } catch (MissingResourceException _ex) {
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, loc);
            if (bundle != fallback)
                return bundle;
            if (bundle == fallback && loc.getLanguage().equals(Locale.getDefault().getLanguage()))
                return bundle;
        } catch (MissingResourceException _ex) {
        }
        return null;
    }

    public String getCharset() {
        return chosenCharset;
    }

    protected String getCharsetForLocale(Locale loc, String charsets) {
        return LocaleToCharsetMap.getCharset(loc);
    }

    public Locale getLocale() {
        return chosenLocale;
    }

    private Locale getLocaleForLanguage(String lang) {
        int semi;
        if ((semi = lang.indexOf(';')) != -1)
            lang = lang.substring(0, semi);
        lang = lang.trim();
        Locale loc;
        int dash;
        if ((dash = lang.indexOf('-')) == -1)
            loc = new Locale(lang, "");
        else
            loc = new Locale(lang.substring(0, dash), lang.substring(dash + 1));
        return loc;
    }
}