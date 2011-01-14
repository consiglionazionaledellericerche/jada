package it.cnr.jada.util.upload;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;

public class LimitedServletInputStream extends ServletInputStream
{

    public LimitedServletInputStream(ServletInputStream in, int totalExpected)
    {
        totalRead = 0;
        this.in = in;
        this.totalExpected = totalExpected;
    }

    public int read()
        throws IOException
    {
        if(totalRead >= totalExpected)
            return -1;
        else
            return in.read();
    }

    public int read(byte b[], int off, int len)
        throws IOException
    {
        int left = totalExpected - totalRead;
        if(left <= 0)
            return -1;
        int result = in.read(b, off, Math.min(left, len));
        if(result > 0)
            totalRead += result;
        return result;
    }

    public int readLine(byte b[], int off, int len)
        throws IOException
    {
        int left = totalExpected - totalRead;
        if(left <= 0)
            return -1;
        int result = in.readLine(b, off, Math.min(left, len));
        if(result > 0)
            totalRead += result;
        return result;
    }

    private ServletInputStream in;
    private int totalExpected;
    private int totalRead;
}