package it.cnr.jada.util.upload;
import java.io.*;
import javax.servlet.ServletOutputStream;

class CacheServletOutputStream extends ServletOutputStream
{

    CacheServletOutputStream(ServletOutputStream out)
    {
        _flddelegate = out;
        cache = new ByteArrayOutputStream(4096);
    }

    public ByteArrayOutputStream getBuffer()
    {
        return cache;
    }

    public void write(int b)
        throws IOException
    {
        _flddelegate.write(b);
        cache.write(b);
    }

    public void write(byte b[])
        throws IOException
    {
        _flddelegate.write(b);
        cache.write(b);
    }

    public void write(byte buf[], int offset, int len)
        throws IOException
    {
        _flddelegate.write(buf, offset, len);
        cache.write(buf, offset, len);
    }

    ServletOutputStream _flddelegate;
    ByteArrayOutputStream cache;
}