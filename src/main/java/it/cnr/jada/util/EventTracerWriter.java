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

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

// Referenced classes of package it.cnr.jada.util:
//            NullWriter, EventTracer

public class EventTracerWriter extends PrintWriter
        implements Serializable {

    private final EventTracer tracer;
    private final UserContext userContext;

    EventTracerWriter(EventTracer eventtracer, UserContext usercontext, boolean flag) {
        super(flag ? new StringWriter() : new NullWriter());
        tracer = eventtracer;
        userContext = usercontext;
    }

    public void close() {
        if (super.out instanceof StringWriter) {
            flush();
            tracer.trace(userContext, ((StringWriter) super.out).getBuffer().toString());
        }
        super.close();
    }
}