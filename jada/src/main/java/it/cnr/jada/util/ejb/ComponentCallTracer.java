package it.cnr.jada.util.ejb;

import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.OutputStreamCounter;

import java.io.*;
import java.sql.*;

// Referenced classes of package it.cnr.jada.util.ejb:
//            EJBCommonServices

public class ComponentCallTracer
    implements Serializable
{

    public ComponentCallTracer()
    {
        try
        {
            resetStreams();
        }
        catch(IOException _ex) { }
    }

    private void resetStreams()
        throws IOException
    {
        oos = new ObjectOutputStream(os = new OutputStreamCounter());
    }

    public void trace(Class class1, String s, String s1, String s2)
    {
        trace(class1, s, s1, s2, null);
    }

    public void trace(Class class1, String s, String s1, String s2, String s3)
    {
        try
        {
            oos.close();
            Connection conn = EJBCommonServices.getConnection();
            LoggableStatement callablestatement = new LoggableStatement(conn,
            		"{ call " + EJBCommonServices.getDefaultSchema() 
            		+ "IBMUTL400.traceEJBCall(?,?,?,?,?,?,?)}",false,this.getClass());
            try
            {
                if(s1 == null)
                    s1 = "$$$_NO_USER_$$$";
                callablestatement.setString(1, class1.getName());
                callablestatement.setString(2, s);
                callablestatement.setLong(3, in_size);
                callablestatement.setLong(4, out_size);
                callablestatement.setString(5, s1);
                callablestatement.setString(6, s2);
                callablestatement.setString(7, s3);
                callablestatement.execute();
            }finally{
                callablestatement.close();
                conn.close();
            }
        }catch(Throwable _ex) { 
        }
    }

    public void traceParameter(byte byte0)
    {
        try
        {
            oos.writeByte(byte0);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(char c)
    {
        try
        {
            oos.writeChar(c);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(double d)
    {
        try
        {
            oos.writeDouble(d);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(float f)
    {
        try
        {
            oos.writeFloat(f);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(int i)
    {
        try
        {
            oos.writeInt(i);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(long l)
    {
        try
        {
            oos.writeLong(l);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(Object obj)
    {
        try
        {
            oos.writeObject(obj);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(short word0)
    {
        try
        {
            oos.writeShort(word0);
        }
        catch(Throwable _ex) { }
    }

    public void traceParameter(boolean flag)
    {
        try
        {
            oos.writeBoolean(flag);
        }
        catch(Throwable _ex) { }
    }

    private void traceParameters()
    {
        try
        {
            oos.close();
            in_size = os.getCount();
            resetStreams();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(byte byte0)
    {
        try
        {
            traceParameters();
            oos.writeByte(byte0);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(char c)
    {
        try
        {
            traceParameters();
            oos.writeChar(c);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(double d)
    {
        try
        {
            traceParameters();
            oos.writeDouble(d);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(float f)
    {
        try
        {
            traceParameters();
            oos.writeFloat(f);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(int i)
    {
        try
        {
            traceParameters();
            oos.writeInt(i);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(long l)
    {
        try
        {
            traceParameters();
            oos.writeLong(l);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(Object obj)
    {
        try
        {
            traceParameters();
            oos.writeObject(obj);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(short word0)
    {
        try
        {
            traceParameters();
            oos.writeShort(word0);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    public void traceResult(boolean flag)
    {
        try
        {
            traceParameters();
            oos.writeBoolean(flag);
            oos.close();
            out_size = os.getCount();
        }
        catch(Throwable _ex) { }
    }

    private ObjectOutputStream oos;
    private OutputStreamCounter os;
    private long in_size;
    private long out_size;
}