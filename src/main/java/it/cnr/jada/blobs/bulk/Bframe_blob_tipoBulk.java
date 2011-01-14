package it.cnr.jada.blobs.bulk;

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoBase, Bframe_blob_tipoKey

public class Bframe_blob_tipoBulk extends Bframe_blob_tipoBase
{

    public Bframe_blob_tipoBulk()
    {
    }

    public Bframe_blob_tipoBulk(String cd_tipo)
    {
        super(cd_tipo);
    }

    public String getCd_ds_tipo()
    {
        return getCd_tipo() + " - " + getDs_tipo();
    }

    public static final Dictionary getTi_visibilitaKeys()
    {
        return ti_visibilitaKeys;
    }

    public static final String TI_VISIBILITA_PUBLICO = "P";
    public static final String TI_VISIBILITA_UTENTE = "U";
    public static final String TI_VISIBILITA_CDR = "C";
    public static final String TI_VISIBILITA_UNITA_ORGANIZZATIVA = "O";
    public static final String TI_VISIBILITA_CDS = "S";
    public static final String TI_VISIBILITA_CNR = "N";
    private static final Dictionary ti_visibilitaKeys;

    static 
    {
        ti_visibilitaKeys = new OrderedHashtable();
        ti_visibilitaKeys.put("U", "Utente");
        ti_visibilitaKeys.put("P", "Pubblico");
    }
}