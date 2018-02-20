package it.cnr.jada.util.upload;
import java.io.*;
import javax.servlet.ServletInputStream;

public class ParamPart extends Part
{

    ParamPart(String name, ServletInputStream in, String boundary)
        throws IOException
    {
        super(name);
        PartInputStream pis = new PartInputStream(in, boundary);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        byte buf[] = new byte[128];
        int read;
        while((read = pis.read(buf)) != -1) 
            baos.write(buf, 0, read);
        pis.close();
        baos.close();
        value = baos.toByteArray();
    }

    public String getStringValue()
        throws UnsupportedEncodingException
    {
        return getStringValue("UTF-8");
    }

    public String getStringValue(String encoding)
        throws UnsupportedEncodingException
    {
        return new String(value, encoding);
    }

    public byte[] getValue()
    {
        return value;
    }

    public boolean isParam()
    {
        return true;
    }

    private byte value[];
}