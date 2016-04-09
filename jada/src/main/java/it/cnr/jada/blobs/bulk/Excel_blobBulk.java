package it.cnr.jada.blobs.bulk;        

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.OrderedHashtable;

import java.util.*;

public class Excel_blobBulk extends Excel_blobBase {
        private String blob;

	public static final String STATO_IN_CODA = "C";
	public static final String STATO_IN_ESECUZIONE = "X";
	public static final String STATO_ERRORE = "E";
	public static final String STATO_ESEGUITO = "S";

	public static final String TIPO_MANUALE = "M";
	public static final String TIPO_AUTOMATICO = "A";
  
  	private static final java.util.Dictionary statoKeys;
  	private static final java.util.Dictionary tipoKeys;

	static {
		statoKeys = new OrderedHashtable();
		statoKeys.put(STATO_IN_CODA,"In coda");
		statoKeys.put(STATO_IN_ESECUZIONE,"In esecuzione");
		statoKeys.put(STATO_ERRORE,"Errore");
		statoKeys.put(STATO_ESEGUITO,"Eseguito");
		tipoKeys = new OrderedHashtable();
		tipoKeys.put(TIPO_MANUALE,"Manuale");
		tipoKeys.put(TIPO_AUTOMATICO,"Automatico");
	}

	public Excel_blobBulk() {
		super();
	}
	public Excel_blobBulk(java.lang.String cd_utente, java.lang.String nome_file) {
	   super(cd_utente, nome_file);
	}


/**
 * @return
 */
public String getBlob() {
	return blob;
}

/**
 * @param string
 */
public void setBlob(String string) {
	blob = string;
}

/**
 * Restituisce il valore della proprietà 'rOExcel_blob'
 *
 * @return Il valore della proprietà 'rOExcel_blob'
 */
public boolean isROExcel_blob() {
	
	return getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
}

public boolean isROExcel_blobManuale() {
	
	return true;
}

public boolean isROAttivaExcelBlob() {
	
	return this == null || this.getNome_file() == null || !isEseguito();
}
/**
 * Metodo che elimina il path dal nome del file
 *
 * @return Il nome del file senza path
 * 
 * @param Il nome completo di path
 */

public static String parseFilename(String file) {

	StringTokenizer fileName = new StringTokenizer(file,"\\",false);
	String newFileName = null;
	
	while (fileName.hasMoreTokens()){
		newFileName = fileName.nextToken();   	
	}

	if (newFileName != null)
		return newFileName;
	
	return file;
}
public boolean isEseguito() {
	return STATO_ESEGUITO.equalsIgnoreCase(getStato());
}
    /**
     * @return
     */
    public java.util.Dictionary getStatoKeys()
    {
        return statoKeys;
    }

    /**
     * @return
     */
    public java.util.Dictionary getTipoKeys()
    {
        return tipoKeys;
    }
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
        setCd_utente(context.getUserContext().getUser());
        setStato(STATO_IN_CODA);
        setTipo(TIPO_MANUALE);
 		return super.initializeForInsert(bp,context);
	}    
}