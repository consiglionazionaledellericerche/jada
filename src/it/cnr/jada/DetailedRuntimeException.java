/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
import java.io.*;
import java.lang.reflect.Field;
/**
 * Eccezione dettagliata: contiene un'eccezione che rappresenta la causa dell'eccezione stessa.
 */
public class DetailedRuntimeException extends RuntimeException implements Serializable, DetailedThrowable{

	private static final long serialVersionUID = 1L;
    private transient Throwable detail;
    private transient String stackTrace;
    private static Field ThrowableDetailMessage;
    static{
        try{
            ThrowableDetailMessage = java.lang.Throwable.class.getDeclaredField("detailMessage");
            ThrowableDetailMessage.setAccessible(true);
        }catch(Throwable _ex){
        }
    }
    
	public DetailedRuntimeException(){
    }

    public DetailedRuntimeException(String s){
        super(s);
    }
    /**
     * Costruisce una DetailedRuntimeException
     * s - Un messaggio che descrive il motivo dell'eccezionedetail - Una eccezione che rappresenta l'eccezione originale che ha generato questa eccezione.
     */
    public DetailedRuntimeException(String s, Throwable detail){
        super(s);
        this.detail = detail;
    }

    public DetailedRuntimeException(Throwable throwable){
        super(throwable.getMessage());
        detail = throwable;
    }

    public Throwable getDetail(){
        return detail;
    }

    public void printStackTrace(){
        synchronized(System.err){
            if(stackTrace != null){
                System.err.print(stackTrace);
            }else{
                super.printStackTrace();
                if(detail != null)
                    detail.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream printstream){
        synchronized(printstream){
            if(stackTrace != null){
                printstream.print(stackTrace);
            }else{
                super.printStackTrace(printstream);
                if(detail != null)
                    detail.printStackTrace(printstream);
            }
        }
    }

    public void printStackTrace(PrintWriter printwriter){
        synchronized(printwriter){
            if(stackTrace != null){
                printwriter.print(stackTrace);
            }else{
                super.printStackTrace(printwriter);
                if(detail != null)
                    detail.printStackTrace(printwriter);
            }
        }
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException{
        stackTrace = objectinputstream.readUTF();
        detail = (Throwable)objectinputstream.readObject();
        if(detail != null && !(detail instanceof DetailedThrowable))
            try{
                ThrowableDetailMessage.set(detail, objectinputstream.readUTF());
            }catch(IOException ioexception){
                throw ioexception;
            }catch(Throwable _ex){
            }
    }

    public void setDetail(Throwable throwable){
        detail = throwable;
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException{
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        printStackTrace(printwriter);
        printwriter.close();
        objectoutputstream.writeUTF(stringwriter.getBuffer().toString());
        objectoutputstream.writeObject(detail);
        if(detail != null && !(detail instanceof DetailedException)){
            StringWriter stringwriter1 = new StringWriter();
            PrintWriter printwriter1 = new PrintWriter(stringwriter1);
            detail.printStackTrace(printwriter1);
            printwriter1.close();
            objectoutputstream.writeUTF(stringwriter1.getBuffer().toString());
        }
    }
}