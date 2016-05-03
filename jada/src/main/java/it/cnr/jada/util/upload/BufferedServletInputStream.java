package it.cnr.jada.util.upload;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;

public class BufferedServletInputStream extends ServletInputStream
{

    public BufferedServletInputStream(ServletInputStream in)
    {
        buf = new byte[0x10000];
        this.in = in;
    }

    private void fill()
        throws IOException
    {
        int i = in.read(buf, 0, buf.length);
        if(i > 0)
        {
            pos = 0;
            count = i;
        }
    }

    private static int findeol(byte b[], int pos, int len)
    {
        int end = pos + len;
        for(int i = pos; i < end;)
            if(b[i++] == 10)
                return i - pos;

        return -1;
    }

    public int read()
        throws IOException
    {
        if(count <= pos)
        {
            fill();
            if(count <= pos)
                return -1;
        }
        return buf[pos++] & 0xff;
    }

    public int read(byte b[], int off, int len)
        throws IOException
    {
        int total;
        int copy;
        for(total = 0; total < len; total += copy)
        {
            int avail = count - pos;
            if(avail <= 0)
            {
                fill();
                avail = count - pos;
                if(avail <= 0)
                    if(total > 0)
                        return total;
                    else
                        return -1;
            }
            copy = Math.min(len - total, avail);
            System.arraycopy(buf, pos, b, off + total, copy);
            pos += copy;
        }

        return total;
    }

    public int readLine(byte b[], int off, int len)
        throws IOException
    {
        int total = 0;
        if(len == 0)
            return 0;
        int avail = count - pos;
        if(avail <= 0)
        {
            fill();
            avail = count - pos;
            if(avail <= 0)
                return -1;
        }
        int copy = Math.min(len, avail);
        int eol = findeol(buf, pos, copy);
        if(eol != -1)
            copy = eol;
        System.arraycopy(buf, pos, b, off, copy);
        pos += copy;
        for(total += copy; total < len && eol == -1; total += copy)
        {
            fill();
            avail = count - pos;
            if(avail <= 0)
                return total;
            copy = Math.min(len - total, avail);
            eol = findeol(buf, pos, copy);
            if(eol != -1)
                copy = eol;
            System.arraycopy(buf, pos, b, off + total, copy);
            pos += copy;
        }

        return total;
    }

    private ServletInputStream in;
    private byte buf[];
    private int count;
    private int pos;
}