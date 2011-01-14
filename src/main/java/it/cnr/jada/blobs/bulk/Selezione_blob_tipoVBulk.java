package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.OggettoBulk;

import java.util.Dictionary;
import java.util.List;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoBulk

public class Selezione_blob_tipoVBulk extends OggettoBulk
{

    public Selezione_blob_tipoVBulk()
    {
    }

    public Bframe_blob_tipoBulk getBlob_tipo()
    {
        return blob_tipo;
    }

    public List getBlob_tipoList()
    {
        return blob_tipoList;
    }

    public String getTi_visibilita()
    {
        return ti_visibilita;
    }

    public final Dictionary getTi_visibilitaKeys()
    {
        return Bframe_blob_tipoBulk.getTi_visibilitaKeys();
    }

    public void setBlob_tipo(Bframe_blob_tipoBulk newBlob_tipo)
    {
        blob_tipo = newBlob_tipo;
    }

    public void setBlob_tipoList(List newBlob_tipoList)
    {
        blob_tipoList = newBlob_tipoList;
    }

    public void setTi_visibilita(String newTi_visibilita)
    {
        ti_visibilita = newTi_visibilita;
    }

    private Bframe_blob_tipoBulk blob_tipo;
    private List blob_tipoList;
    private String ti_visibilita;
}