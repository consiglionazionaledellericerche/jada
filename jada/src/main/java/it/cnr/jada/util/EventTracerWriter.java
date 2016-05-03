package it.cnr.jada.util;

import it.cnr.jada.UserContext;

import java.io.*;

// Referenced classes of package it.cnr.jada.util:
//            NullWriter, EventTracer

public class EventTracerWriter extends PrintWriter
    implements Serializable
{

    EventTracerWriter(EventTracer eventtracer, UserContext usercontext, boolean flag)
    {
        super(((java.io.Writer) (flag ? ((java.io.Writer) (new StringWriter())) : ((java.io.Writer) (new NullWriter())))));
        tracer = eventtracer;
        userContext = usercontext;
    }

    public void close()
    {
        if(super.out instanceof StringWriter)
        {
            flush();
            tracer.trace(userContext, ((StringWriter)super.out).getBuffer().toString());
        }
        super.close();
    }

    private final EventTracer tracer;
    private final UserContext userContext;
}