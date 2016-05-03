package it.cnr.jada.blobs.bulk;        

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Excel_blobKey extends OggettoBulk implements KeyedPersistent {
	// CD_UTENTE  VARCHAR2(20) NOT NULL
	private java.lang.String cd_utente;
	
	// NOME_FILE  VARCHAR2(400) NOT NULL
	private java.lang.String nome_file;

public Excel_blobKey() {
	super();
}
public Excel_blobKey(java.lang.String cd_utente, java.lang.String nome_file) {
	super();
	this.cd_utente = cd_utente;
	this.nome_file = nome_file;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Excel_blobKey)) return false;
	Excel_blobKey k = (Excel_blobKey)o;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	if(!compareKey(getNome_file(),k.getNome_file())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_utente()) + calculateKeyHashCode(getNome_file());
}
    /**
     * @return
     */
    public java.lang.String getCd_utente()
    {
        return cd_utente;
    }

    /**
     * @return
     */
    public java.lang.String getNome_file()
    {
        return nome_file;
    }

    /**
     * @param string
     */
    public void setCd_utente(java.lang.String string)
    {
        cd_utente = string;
    }

    /**
     * @param string
     */
    public void setNome_file(java.lang.String string)
    {
        nome_file = string;
    }

}