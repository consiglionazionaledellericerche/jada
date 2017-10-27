package it.cnr.jada;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Optional;


public class DetailedRuntimeException extends RuntimeException
        implements Serializable, DetailedThrowable {

    private static Field ThrowableDetailMessage;

    static {
        try {
            ThrowableDetailMessage = java.lang.Throwable.class.getDeclaredField("detailMessage");
            ThrowableDetailMessage.setAccessible(true);
        } catch (Throwable _ex) {
        }
    }

    private transient Throwable detail;
    private transient String stackTrace;

    public DetailedRuntimeException() {
    }

    public DetailedRuntimeException(String s) {
        super(s);
    }

    public DetailedRuntimeException(String s, Throwable throwable) {
        super(s);
        detail = throwable;
    }

    public DetailedRuntimeException(Throwable throwable) {
        super(throwable.getMessage());
        detail = throwable;
    }

    public Throwable getDetail() {
        return detail;
    }

    public void setDetail(Throwable throwable) {
        detail = throwable;
    }

    public void printStackTrace() {
        synchronized (System.err) {
            if (stackTrace != null) {
                System.err.print(stackTrace);
            } else {
                super.printStackTrace();
                if (detail != null)
                    detail.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream printstream) {
        synchronized (printstream) {
            if (stackTrace != null) {
                printstream.print(stackTrace);
            } else {
                super.printStackTrace(printstream);
                if (detail != null)
                    detail.printStackTrace(printstream);
            }
        }
    }

    public void printStackTrace(PrintWriter printwriter) {
        synchronized (printwriter) {
            if (stackTrace != null) {
                printwriter.print(stackTrace);
            } else {
                super.printStackTrace(printwriter);
                if (detail != null)
                    detail.printStackTrace(printwriter);
            }
        }
    }

    private void readObject(ObjectInputStream objectinputstream)
            throws IOException, ClassNotFoundException {
        stackTrace = IOUtils.toString(objectinputstream);
        detail = (Throwable) objectinputstream.readObject();
        if (detail != null && !(detail instanceof DetailedThrowable))
            try {
                ThrowableDetailMessage.set(detail, IOUtils.toString(objectinputstream));
            } catch (IOException ioexception) {
                throw ioexception;
            } catch (Throwable _ex) {
            }
    }

    private void writeObject(ObjectOutputStream objectoutputstream)
            throws IOException {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        printStackTrace(printwriter);
        printwriter.close();
        objectoutputstream.write(
                stringwriter.getBuffer().toString().getBytes()
        );
        objectoutputstream.writeObject(detail);
        if (detail != null && !(detail instanceof DetailedException)) {
            StringWriter stringwriter1 = new StringWriter();
            PrintWriter printwriter1 = new PrintWriter(stringwriter1);
            detail.printStackTrace(printwriter1);
            printwriter1.close();
            objectoutputstream.write(
                    stringwriter1.getBuffer().toString().getBytes()
            );
        }
    }
}