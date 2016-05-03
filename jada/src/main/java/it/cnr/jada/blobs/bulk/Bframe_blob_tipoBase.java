package it.cnr.jada.blobs.bulk;

import it.cnr.jada.persistency.Keyed;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoKey

public class Bframe_blob_tipoBase extends Bframe_blob_tipoKey
    implements Keyed
{

    public Bframe_blob_tipoBase()
    {
    }

    public Bframe_blob_tipoBase(String cd_tipo)
    {
        super(cd_tipo);
    }

    public String getDs_tipo()
    {
        return ds_tipo;
    }

    public Boolean getFl_binario()
    {
        return fl_binario;
    }

    public String getRoot()
    {
        return root;
    }

    public void setDs_tipo(String ds_tipo)
    {
        this.ds_tipo = ds_tipo;
    }

    public void setFl_binario(Boolean fl_binario)
    {
        this.fl_binario = fl_binario;
    }

    public void setRoot(String root)
    {
        this.root = root;
    }

    private String ds_tipo;
    private Boolean fl_binario;
    private String root;
}