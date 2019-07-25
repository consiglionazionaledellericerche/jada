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

package it.cnr.jada.action;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

public class ActionPerformingError extends RuntimeException
        implements Serializable {

    private Throwable detail;

    public ActionPerformingError() {
    }

    public ActionPerformingError(String s) {
        super(s);
    }

    public ActionPerformingError(Throwable throwable) {
        detail = throwable;
    }

    public ActionPerformingError(Throwable throwable, String s) {
        super(s);
        detail = throwable;
    }

    public Throwable getDetail() {
        return detail;
    }

    public void printStackTrace() {
        synchronized (System.err) {
            super.printStackTrace();
            if (detail != null)
                detail.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream printstream) {
        synchronized (printstream) {
            super.printStackTrace(printstream);
            if (detail != null)
                detail.printStackTrace(printstream);
        }
    }

    public void printStackTrace(PrintWriter printwriter) {
        synchronized (printwriter) {
            super.printStackTrace(printwriter);
            if (detail != null)
                detail.printStackTrace(printwriter);
        }
    }
}