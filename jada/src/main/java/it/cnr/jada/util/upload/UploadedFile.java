package it.cnr.jada.util.upload;
import java.io.File;

public class UploadedFile
{

    UploadedFile(String dir, String filename, String type)
    {
        this.dir = dir;
        this.filename = filename;
        this.type = type;
    }

    public String getContentType()
    {
        return type;
    }

    public File getFile()
    {
        if(dir == null || filename == null)
            return null;
        else
            return new File(dir + File.separator + filename);
    }

    public String getFilesystemName()
    {
        return filename;
    }

    public String getName(){
    	if (getFile() == null)
    		return "";
    	return getFile().getName();
    }
    
    public long length(){
    	if (getFile() == null)
    		return 0;
    	return getFile().length();
    }
    
    private String dir;
    private String filename;
    private String type;
}