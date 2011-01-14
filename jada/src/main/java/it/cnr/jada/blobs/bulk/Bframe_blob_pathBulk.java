package it.cnr.jada.blobs.bulk;


// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blobBulk, Bframe_blobKey

public class Bframe_blob_pathBulk extends Bframe_blobBulk
{

    public Bframe_blob_pathBulk()
    {
    }

    public Bframe_blob_pathBulk(String cd_tipo, String filename, String path)
    {
        super(cd_tipo, filename, path);
    }

    public Boolean getFl_dir()
    {
        return fl_dir;
    }

    public String getItemname()
    {
        if(getFl_dir() == null || !getFl_dir().booleanValue())
            return getFilename();
        else
            return "[" + getFilename() + "]";
    }

    public String getRelativepath()
    {
        return relativepath;
    }

    public boolean isDirectory()
    {
        return getFl_dir() != null && getFl_dir().booleanValue();
    }

    public void setFl_dir(Boolean newFl_dir)
    {
        fl_dir = newFl_dir;
    }

    public void setRelativepath(String newRelativepath)
    {
        relativepath = newRelativepath;
    }

    private String relativepath;
    private Boolean fl_dir;
}