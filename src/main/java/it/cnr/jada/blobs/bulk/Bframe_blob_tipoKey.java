package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Bframe_blob_tipoKey extends OggettoBulk
    implements KeyedPersistent
{

    public Bframe_blob_tipoKey()
    {
    }

    public Bframe_blob_tipoKey(String cd_tipo)
    {
        this.cd_tipo = cd_tipo;
    }

    public boolean equalsByPrimaryKey(Object o)
    {
        if(this == o)
            return true;
        if(!(o instanceof Bframe_blob_tipoKey))
            return false;
        Bframe_blob_tipoKey k = (Bframe_blob_tipoKey)o;
        return compareKey(getCd_tipo(), k.getCd_tipo());
    }

    public String getCd_tipo()
    {
        return cd_tipo;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getCd_tipo());
    }

    public void setCd_tipo(String cd_tipo)
    {
        this.cd_tipo = cd_tipo;
    }

    private String cd_tipo;
}