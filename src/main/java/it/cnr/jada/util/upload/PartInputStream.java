package it.cnr.jada.util.upload;
import java.io.*;
import javax.servlet.ServletInputStream;

public class PartInputStream extends FilterInputStream
{

    PartInputStream(ServletInputStream in, String boundary)
        throws IOException
    {
        super(in);
        buf = new byte[0x10000];
        this.boundary = boundary;
    }

    public int available()
        throws IOException
    {
        int avail = (count - pos - 2) + super.in.available();
        return avail >= 0 ? avail : 0;
    }

    public void close()
        throws IOException
    {
        if(!eof)
            while(read(buf, 0, buf.length) != -1) ;
    }

    private void fill()
        throws IOException
    {
        if(eof)
            return;
        if(count > 0)
            if(count - pos == 2)
            {
                System.arraycopy(buf, pos, buf, 0, count - pos);
                count -= pos;
                pos = 0;
            } else
            {
                throw new IllegalStateException("fill() detected illegal buffer state");
            }
        int read = 0;
        for(int maxRead = buf.length - boundary.length(); count < maxRead; count += read)
        {
            read = ((ServletInputStream)super.in).readLine(buf, count, buf.length - count);
            if(read == -1)
                throw new IOException("unexpected end of part");
            if(read < boundary.length())
                continue;
            eof = true;
            for(int i = 0; i < boundary.length(); i++)
            {
                if(boundary.charAt(i) == buf[count + i])
                    continue;
                eof = false;
                break;
            }

            if(eof)
                break;
        }

    }

    public int read()
        throws IOException
    {
        if(count - pos <= 2)
        {
            fill();
            if(count - pos <= 2)
                return -1;
        }
        return buf[pos++] & 0xff;
    }

    public int read(byte b[])
        throws IOException
    {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len)
        throws IOException
    {
        int total = 0;
        if(len == 0)
            return 0;
        int avail = count - pos - 2;
        if(avail <= 0)
        {
            fill();
            avail = count - pos - 2;
            if(avail <= 0)
                return -1;
        }
        int copy = Math.min(len, avail);
        System.arraycopy(buf, pos, b, off, copy);
        pos += copy;
        for(total += copy; total < len; total += copy)
        {
            fill();
            avail = count - pos - 2;
            if(avail <= 0)
                return total;
            copy = Math.min(len - total, avail);
            System.arraycopy(buf, pos, b, off + total, copy);
            pos += copy;
        }

        return total;
    }

    private String boundary;
    private byte buf[];
    private int count;
    private int pos;
    private boolean eof;
}