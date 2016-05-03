package it.cnr.jada.action;

import java.io.*;

public class ActionPerformingError extends RuntimeException
    implements Serializable
{

    public ActionPerformingError()
    {
    }

    public ActionPerformingError(String s)
    {
        super(s);
    }

    public ActionPerformingError(Throwable throwable)
    {
        detail = throwable;
    }

    public ActionPerformingError(Throwable throwable, String s)
    {
        super(s);
        detail = throwable;
    }

    public Throwable getDetail()
    {
        return detail;
    }

    public void printStackTrace()
    {
        synchronized(System.err)
        {
            super.printStackTrace();
            if(detail != null)
                detail.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream printstream)
    {
        synchronized(printstream)
        {
            super.printStackTrace(printstream);
            if(detail != null)
                detail.printStackTrace(printstream);
        }
    }

    public void printStackTrace(PrintWriter printwriter)
    {
        synchronized(printwriter)
        {
            super.printStackTrace(printwriter);
            if(detail != null)
                detail.printStackTrace(printwriter);
        }
    }

    private Throwable detail;
}