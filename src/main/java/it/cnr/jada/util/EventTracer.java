package it.cnr.jada.util;

import it.cnr.jada.UserContext;

import java.io.*;
import java.util.*;

// Referenced classes of package it.cnr.jada.util:
//            EventTracerWriter

public abstract class EventTracer
    implements Serializable
{

    public EventTracer()
    {
        enabled = true;
        usersToTrace = new HashSet();
    }

    public void addTraceUser(String s)
    {
        usersToTrace.add(s);
    }

    public abstract void getTrace(UserContext usercontext, int i, PrintWriter printwriter)
        throws IOException;

    public String[] getTraceUsers()
    {
        return (String[])usersToTrace.toArray(new String[usersToTrace.size()]);
    }

    public boolean isDumpStackTraceEnabled()
    {
        return dumpStackTraceEnabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isTraceAllUsers()
    {
        return traceAllUsers;
    }

    void printStackTrace(EventTracerWriter eventtracerwriter)
    {
        if(dumpStackTraceEnabled)
            (new Exception()).printStackTrace(eventtracerwriter);
    }

    void printTimestamp(EventTracerWriter eventtracerwriter)
    {
        eventtracerwriter.print('[');
        eventtracerwriter.print(new Date());
        eventtracerwriter.println("]");
    }

    void printUserContext(EventTracerWriter eventtracerwriter, UserContext usercontext)
    {
        if(usercontext == null)
        {
            return;
        } else
        {
            usercontext.writeTo(eventtracerwriter);
            eventtracerwriter.println();
            return;
        }
    }

    public void removeTraceUser(String s)
    {
        usersToTrace.remove(s);
    }

    public void setDumpStackTraceEnabled(boolean flag)
    {
        dumpStackTraceEnabled = flag;
    }

    public void setEnabled(boolean flag)
    {
        enabled = flag;
    }

    public void setTraceAllUsers(boolean flag)
    {
        traceAllUsers = flag;
    }

    public EventTracerWriter startEventTrace(UserContext usercontext)
    {
        EventTracerWriter eventtracerwriter = new EventTracerWriter(this, usercontext, enabled && (traceAllUsers || usercontext == null || usersToTrace.contains(usercontext.getUser())));
        printTimestamp(eventtracerwriter);
        printUserContext(eventtracerwriter, usercontext);
        printStackTrace(eventtracerwriter);
        return eventtracerwriter;
    }

    abstract void trace(UserContext usercontext, String s);

    private boolean dumpStackTraceEnabled;
    private boolean enabled;
    private Set usersToTrace;
    private boolean traceAllUsers;
}