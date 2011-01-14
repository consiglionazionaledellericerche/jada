package it.cnr.jada.blobs.bulk;

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blobBase

public class Bframe_blobBulk extends Bframe_blobBase
{

    public Bframe_blobBulk()
    {
    }

    public Bframe_blobBulk(String cd_tipo, String filename, String path)
    {
        super(cd_tipo, filename, path);
    }

    public static final Dictionary getStatoKeys()
    {
        return statoKeys;
    }

    public static final String STATO_IN_CODA = "C";
    public static final String STATO_IN_ESECUZIONE = "X";
    public static final String STATO_ERRORE = "E";
    public static final String STATO_ESEGUITA = "S";
    private static final Dictionary statoKeys;

    static 
    {
        statoKeys = new OrderedHashtable();
        statoKeys.put("C", "In coda");
        statoKeys.put("X", "In esecuzione");
        statoKeys.put("E", "Errore");
        statoKeys.put("S", "Eseguita");
    }
}