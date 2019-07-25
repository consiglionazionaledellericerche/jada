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

public class VersionDetector {

    static String servletVersion;
    static String javaVersion;

    public VersionDetector() {
    }

    public static String getJavaVersion() {
        if (javaVersion != null)
            return javaVersion;
        String ver = null;
        try {
            ver = "1.0";
            Class.forName("java.lang.Void");
            ver = "1.1";
            Class.forName("java.lang.ThreadLocal");
            ver = "1.2";
            Class.forName("java.lang.StrictMath");
            ver = "1.3";
            Class.forName("java.net.URI");
            ver = "1.4";
        } catch (Throwable _ex) {
        }
        javaVersion = ver;
        return javaVersion;
    }

    public static String getServletVersion() {
        if (servletVersion != null)
            return servletVersion;
        String ver = null;
        try {
            ver = "1.0";
            Class.forName("javax.servlet.http.HttpSession");
            ver = "2.0";
            Class.forName("javax.servlet.RequestDispatcher");
            ver = "2.1";
            Class.forName("javax.servlet.http.HttpServletResponse").getDeclaredField("SC_EXPECTATION_FAILED");
            ver = "2.2";
            Class.forName("javax.servlet.Filter");
            ver = "2.3";
        } catch (Throwable _ex) {
        }
        servletVersion = ver;
        return servletVersion;
    }
}