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

package it.cnr.jada.util;

import it.cnr.jada.UserContext;

import java.io.*;

// Referenced classes of package it.cnr.jada.util:
//            EventTracer

public class SessionEventTracer extends EventTracer
        implements Serializable {

    private final File path;
    private final String name;

    public SessionEventTracer(File file, String s) {
        path = file;
        name = s;
    }

    public final File getPath() {
        return path;
    }

    public synchronized void getTrace(UserContext usercontext, int i, PrintWriter printwriter)
            throws IOException {
        RandomAccessFile randomaccessfile = new RandomAccessFile(getTraceFile(usercontext), "r");
        try {
            String[] as = new String[i];
            int j = 0;
            int k = 0;
            int l;
            for (l = 0; (as[j++] = randomaccessfile.readLine()) != null; l++) {
                if (j >= i)
                    j = 0;
                if (k < i)
                    k++;
            }

            if (k < i)
                j = 0;
            l -= k;
            for (; k > 0; k--) {
                printwriter.print(l++);
                printwriter.print(':');
                printwriter.println(as[j++]);
                if (j >= i)
                    j = 0;
            }

        } finally {
            randomaccessfile.close();
        }
    }

    private File getTraceFile(UserContext usercontext)
            throws IOException {
        return new File(path, name + "_" + usercontext.getUser() + "_" + usercontext.getSessionId() + ".log");
    }

    synchronized void trace(UserContext usercontext, String s) {
        try {
            RandomAccessFile randomaccessfile = new RandomAccessFile(getTraceFile(usercontext), "rw");
            try {
                randomaccessfile.seek(randomaccessfile.length());
                randomaccessfile.writeByte(10);
                randomaccessfile.writeBytes(s);
            } finally {
                randomaccessfile.close();
            }
        } catch (IOException _ex) {
        }
    }
}