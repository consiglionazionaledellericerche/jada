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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Referenced classes of package it.cnr.jada.util:
//            EventTracerWriter

public abstract class EventTracer
        implements Serializable {

    private boolean dumpStackTraceEnabled;
    private boolean enabled;
    private Set usersToTrace;
    private boolean traceAllUsers;

    public EventTracer() {
        enabled = true;
        usersToTrace = new HashSet();
    }

    public void addTraceUser(String s) {
        usersToTrace.add(s);
    }

    public abstract void getTrace(UserContext usercontext, int i, PrintWriter printwriter)
            throws IOException;

    public String[] getTraceUsers() {
        return (String[]) usersToTrace.toArray(new String[usersToTrace.size()]);
    }

    public boolean isDumpStackTraceEnabled() {
        return dumpStackTraceEnabled;
    }

    public void setDumpStackTraceEnabled(boolean flag) {
        dumpStackTraceEnabled = flag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean flag) {
        enabled = flag;
    }

    public boolean isTraceAllUsers() {
        return traceAllUsers;
    }

    public void setTraceAllUsers(boolean flag) {
        traceAllUsers = flag;
    }

    void printStackTrace(EventTracerWriter eventtracerwriter) {
        if (dumpStackTraceEnabled)
            (new Exception()).printStackTrace(eventtracerwriter);
    }

    void printTimestamp(EventTracerWriter eventtracerwriter) {
        eventtracerwriter.print('[');
        eventtracerwriter.print(new Date());
        eventtracerwriter.println("]");
    }

    void printUserContext(EventTracerWriter eventtracerwriter, UserContext usercontext) {
        if (usercontext == null) {
            return;
        } else {
            usercontext.writeTo(eventtracerwriter);
            eventtracerwriter.println();
            return;
        }
    }

    public void removeTraceUser(String s) {
        usersToTrace.remove(s);
    }

    public EventTracerWriter startEventTrace(UserContext usercontext) {
        EventTracerWriter eventtracerwriter = new EventTracerWriter(this, usercontext, enabled && (traceAllUsers || usercontext == null || usersToTrace.contains(usercontext.getUser())));
        printTimestamp(eventtracerwriter);
        printUserContext(eventtracerwriter, usercontext);
        printStackTrace(eventtracerwriter);
        return eventtracerwriter;
    }

    abstract void trace(UserContext usercontext, String s);
}