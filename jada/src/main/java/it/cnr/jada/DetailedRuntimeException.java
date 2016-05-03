package it.cnr.jada;

import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

// Referenced classes of package it.cnr.jada:
//            DetailedThrowable, DetailedException

public class DetailedRuntimeException extends RuntimeException
    implements Serializable, DetailedThrowable
{

    public DetailedRuntimeException()
    {
    }

    public DetailedRuntimeException(String s)
    {
        super(s);
    }

    public DetailedRuntimeException(String s, Throwable throwable)
    {
        super(s);
        detail = throwable;
    }

    public DetailedRuntimeException(Throwable throwable)
    {
        super(throwable.getMessage());
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
            if(stackTrace != null)
            {
                System.err.print(stackTrace);
            } else
            {
                super.printStackTrace();
                if(detail != null)
                    detail.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream printstream)
    {
        synchronized(printstream)
        {
            if(stackTrace != null)
            {
                printstream.print(stackTrace);
            } else
            {
                super.printStackTrace(printstream);
                if(detail != null)
                    detail.printStackTrace(printstream);
            }
        }
    }

    public void printStackTrace(PrintWriter printwriter)
    {
        synchronized(printwriter)
        {
            if(stackTrace != null)
            {
                printwriter.print(stackTrace);
            } else
            {
                super.printStackTrace(printwriter);
                if(detail != null)
                    detail.printStackTrace(printwriter);
            }
        }
    }

    private void readObject(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException
    {
        stackTrace = objectinputstream.readUTF();
        detail = (Throwable)objectinputstream.readObject();
        if(detail != null && !(detail instanceof DetailedThrowable))
            try
            {
                ThrowableDetailMessage.set(detail, objectinputstream.readUTF());
            }
            catch(IOException ioexception)
            {
                throw ioexception;
            }
            catch(Throwable _ex) { }
    }

    public void setDetail(Throwable throwable)
    {
        detail = throwable;
    }

    private void writeObject(ObjectOutputStream objectoutputstream)
        throws IOException
    {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        printStackTrace(printwriter);
        printwriter.close();
        objectoutputstream.writeUTF(stringwriter.getBuffer().toString());
        objectoutputstream.writeObject(detail);
        if(detail != null && !(detail instanceof DetailedException))
        {
            StringWriter stringwriter1 = new StringWriter();
            PrintWriter printwriter1 = new PrintWriter(stringwriter1);
            detail.printStackTrace(printwriter1);
            printwriter1.close();
            objectoutputstream.writeUTF(stringwriter1.getBuffer().toString());
        }
    }

    private transient Throwable detail;
    private transient String stackTrace;
    private static Field ThrowableDetailMessage;

    static 
    {
        try
        {
            ThrowableDetailMessage = java.lang.Throwable.class.getDeclaredField("detailMessage");
            ThrowableDetailMessage.setAccessible(true);
        }
        catch(Throwable _ex) { }
    }
}