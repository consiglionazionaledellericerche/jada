package it.cnr.jada.blobs.bulk;

import it.cnr.jada.persistency.Keyed;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blobKey

public class Bframe_blobBase extends Bframe_blobKey
    implements Keyed
{

    public Bframe_blobBase()
    {
    }

    public Bframe_blobBase(String cd_tipo, String filename, String path)
    {
        super(cd_tipo, filename, path);
    }

    public String getDs_file()
    {
        return ds_file;
    }

    public String getDs_utente()
    {
        return ds_utente;
    }

    public String getStato()
    {
        return stato;
    }

    public String getTi_visibilita()
    {
        return ti_visibilita;
    }

    public void setDs_file(String ds_file)
    {
        this.ds_file = ds_file;
    }

    public void setDs_utente(String ds_utente)
    {
        this.ds_utente = ds_utente;
    }

    public void setStato(String stato)
    {
        this.stato = stato;
    }

    public void setTi_visibilita(String ti_visibilita)
    {
        this.ti_visibilita = ti_visibilita;
    }

    private String ds_file;
    private String ds_utente;
    private String stato;
    private String ti_visibilita;
}