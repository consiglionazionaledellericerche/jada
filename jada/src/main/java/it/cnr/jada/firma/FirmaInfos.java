package it.cnr.jada.firma;

import java.io.File;

public interface FirmaInfos {
	String TIPO_PERSISTENZA_INTERNA="INT";
	String TIPO_PERSISTENZA_ESTERNA="EST";
	String TIPO_PERSISTENZA_ENTRAMBO="ENT";
	
	public String descrizione();
	public DatiPEC datiPEC();
	public String tipoPersistenza();
	public void rendiPersistente(String signFile);
	
}
