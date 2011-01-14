/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import java.util.Hashtable;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Excel_spoolerBulk extends Excel_spoolerBase {
	public static final String STATO_IN_CODA = "C";
	public static final String STATO_IN_ESECUZIONE = "X";
	public static final String STATO_ERRORE = "E";
	public static final String STATO_ESEGUITA = "S";
	private static final java.util.Dictionary statoKeys;
	static {
		statoKeys = new OrderedHashtable();
		statoKeys.put(STATO_IN_CODA,"In coda");
		statoKeys.put(STATO_IN_ESECUZIONE,"In esecuzione");
		statoKeys.put(STATO_ERRORE,"Errore");
		statoKeys.put(STATO_ESEGUITA,"Eseguito");
	}
	 public static final String TIPO_INTERVALLO_GIORNI = "G";
	 public static final String TIPO_INTERVALLO_SETTIMANE = "S";
	 public static final String TIPO_INTERVALLO_MESI = "M";
	 public static final Hashtable tipo_intervalloKeys;

	    static 
	    {
	        tipo_intervalloKeys = new Hashtable();
	        tipo_intervalloKeys.put(TIPO_INTERVALLO_GIORNI, "giorni");
	        tipo_intervalloKeys.put(TIPO_INTERVALLO_SETTIMANE, "settimane");
	        tipo_intervalloKeys.put(TIPO_INTERVALLO_MESI, "mesi");
	    }
	public Excel_spoolerBulk() {
		super();
	}
	public Excel_spoolerBulk(java.lang.Long pg_estrazione) {
		super(pg_estrazione);
	}
    /**
     * Insert the method's description here.
     * Creation date: (12/04/2002 11:10:23)
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getStatoKeys() {
    	return statoKeys;
    }
    public boolean isEseguita() {
    	return STATO_ESEGUITA.equalsIgnoreCase(getStato());
    }
    public java.util.Dictionary getTipo_intervalloKeys()
    {
        return tipo_intervalloKeys;
    }
    public void validate()
    throws ValidationException
{
    if(getIntervallo() != null && getTi_intervallo() == null)
        throw new ValidationException("Se si specifica l'intervallo \350 necessario specificarne l'unit\340.");
    if(getIntervallo() == null && getTi_intervallo() != null)
        throw new ValidationException("Se si specifica l'unit\340 di intervallo \350 necessario specificare l'intervallo.");
    if(getIntervallo() != null && getDt_partenza() == null)
        throw new ValidationException("Se si specifica l'intervallo \350 necessario specificare l'ora di partenza.");
    if(getIntervallo() != null && getIntervallo().longValue() == 0L)
        throw new ValidationException("Intervallo non valido");
    if(getIntervallo() == null && getDt_partenza() != null)
    	throw new ValidationException("Se si specifica l'ora di partenza \350 necessario specificare l'intervallo.");
    if(getDt_partenza() != null && getDt_partenza().before(EJBCommonServices.getServerTimestamp()))
    	throw new ValidationException("Se si specifica l'ora di partenza \350 necessario specificare una data e/o un'ora futura.");
    else
        return;
}
}