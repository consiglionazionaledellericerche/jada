package it.cnr.jada.util.upload;
import java.io.OutputStream;
import java.io.PrintStream;

class MailPrintStream extends PrintStream
{

    public MailPrintStream(OutputStream out)
    {
        super(out, true);
    }

    void rawPrint(String s)
    {
        int len = s.length();
        for(int i = 0; i < len; i++)
            rawWrite(s.charAt(i));

    }

    void rawWrite(int b)
    {
        super.write(b);
    }

    public void write(int b)
    {
        if(b == 10 && lastChar != 13)
        {
            rawWrite(13);
            rawWrite(b);
        } else
        if(b == 46 && lastChar == 10)
        {
            rawWrite(46);
            rawWrite(b);
        } else
        if(b != 10 && lastChar == 13)
        {
            rawWrite(10);
            rawWrite(b);
            if(b == 46)
                rawWrite(46);
        } else
        {
            rawWrite(b);
        }
        lastChar = b;
    }

    public void write(byte buf[], int off, int len)
    {
        for(int i = 0; i < len; i++)
            write(buf[off + i]);

    }

    int lastChar;
}