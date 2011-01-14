package it.cnr.jada.util.upload;
import java.io.*;
import javax.servlet.ServletInputStream;

public class FilePart extends Part
{

    FilePart(String name, ServletInputStream in, String boundary, String contentType, String fileName, String filePath)
        throws IOException
    {
        super(name);
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        partInput = new PartInputStream(in, boundary);
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public InputStream getInputStream()
    {
        return partInput;
    }

    public boolean isFile()
    {
        return true;
    }

    long write(OutputStream out)
        throws IOException
    {
        if(contentType.equals("application/x-macbinary"))
            out = new MacBinaryDecoderOutputStream(out);
        long size = 0L;
        byte buf[] = new byte[8192];
        int read;
        while((read = partInput.read(buf)) != -1) 
        {
            out.write(buf, 0, read);
            size += read;
        }
        return size;
    }

    public long writeTo(File fileOrDirectory)
        throws IOException
    {
        long written = 0L;
        OutputStream fileOut = null;
        try
        {
            if(fileName != null)
            {
                File file;
                if(fileOrDirectory.isDirectory())
                    file = new File(fileOrDirectory, fileName);
                else
                    file = fileOrDirectory;
                fileOut = new BufferedOutputStream(new FileOutputStream(file));
                written = write(fileOut);
            }
        }
        finally
        {
            if(fileOut != null)
                fileOut.close();
        }
        return written;
    }

    public long writeTo(OutputStream out)
        throws IOException
    {
        long size = 0L;
        if(fileName != null)
            size = write(out);
        return size;
    }

    private String fileName;
    private String filePath;
    private String contentType;
    private PartInputStream partInput;
}