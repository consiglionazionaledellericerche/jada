package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Bframe_blobKey extends OggettoBulk
    implements KeyedPersistent
{

    public Bframe_blobKey()
    {
    }

    public Bframe_blobKey(String cd_tipo, String filename, String path)
    {
        this.cd_tipo = cd_tipo;
        this.filename = filename;
        this.path = path;
    }

    public boolean equalsByPrimaryKey(Object o)
    {
        if(this == o)
            return true;
        if(!(o instanceof Bframe_blobKey))
            return false;
        Bframe_blobKey k = (Bframe_blobKey)o;
        if(!compareKey(getCd_tipo(), k.getCd_tipo()))
            return false;
        if(!compareKey(getFilename(), k.getFilename()))
            return false;
        return compareKey(getPath(), k.getPath());
    }

    public String getCd_tipo()
    {
        return cd_tipo;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getPath()
    {
        return path;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getCd_tipo()) + calculateKeyHashCode(getFilename()) + calculateKeyHashCode(getPath());
    }

    public void setCd_tipo(String cd_tipo)
    {
        this.cd_tipo = cd_tipo;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    private String path;
    private String filename;
    private String cd_tipo;
}