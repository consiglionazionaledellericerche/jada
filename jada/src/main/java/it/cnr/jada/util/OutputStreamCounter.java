package it.cnr.jada.util;

import java.io.*;

public class OutputStreamCounter extends OutputStream
    implements Serializable
{

    public OutputStreamCounter()
    {
    }

    public long getCount()
    {
        return count;
    }

    public void write(byte abyte0[])
        throws IOException
    {
        count += abyte0.length;
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        count += j;
    }

    public void write(int i)
        throws IOException
    {
        count++;
    }

    private long count;
}