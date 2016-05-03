package it.cnr.jada.util;

import java.io.*;

public class NullWriter extends Writer
    implements Serializable
{

    public NullWriter()
    {
    }

    public void close()
        throws IOException
    {
    }

    public void flush()
        throws IOException
    {
    }

    public void write(char ac[], int i, int j)
        throws IOException
    {
    }
}