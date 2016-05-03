package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Excel_blobBase extends Excel_blobKey implements Keyed {

	// DS_FILE VARCHAR(2000)
	private java.lang.String ds_file;

	// STATO CHAR(1)
	private java.lang.String stato;
	
	// TIPO CHAR(1)
	private java.lang.String tipo;

public Excel_blobBase() {
	super();
}
public Excel_blobBase(java.lang.String cd_utente, java.lang.String nome_file) {
	super(cd_utente, nome_file);
}

    /**
     * @return
     */
    public java.lang.String getDs_file()
    {
        return ds_file;
    }

    /**
     * @return
     */
    public java.lang.String getStato()
    {
        return stato;
    }

    /**
     * @return
     */
    public java.lang.String getTipo()
    {
        return tipo;
    }

    /**
     * @param string
     */
    public void setDs_file(java.lang.String string)
    {
        ds_file = string;
    }

    /**
     * @param string
     */
    public void setStato(java.lang.String string)
    {
        stato = string;
    }

    /**
     * @param string
     */
    public void setTipo(java.lang.String string)
    {
        tipo = string;
    }

}