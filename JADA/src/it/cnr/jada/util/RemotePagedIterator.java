/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import it.cnr.jada.bulk.OggettoBulk;

import java.util.List;

/**
 * Particolare RemoteIterator che possiede la capacit� di "paginare" l'iterazione; 
 * questa capacit� coincide solitamente con una migliore sfruttamento di risorse dell'oggetto remoto, 
 * quindi in presenza di un RemoteIterator � meglio chiedersi se si tratta anche di un RemotePagedIterator 
 * per poter sfruttare tali capacit�
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface RemotePagedIterator extends RemoteIterator{
    /**
     * Restituisce il numero di pagine che l'iterator � in grado di scorrere.
     */
    public abstract int countPages();

    /**
     * Interroga il ricevente se possiede ancora pagine da scorrere partendo dalla posizione corrente del cursore
     */
    public abstract boolean hasMorePages();

    /**
     * Muove il cursore del ricevente all'inizio di una pagina
     */
    public abstract void moveToPage(int index);

    /**
     * Legge l'intero contenuto della pagina corrente; all'uscita il cursore del ricevente � posizionato all'inizio della pagina successiva
     */
    public abstract List<OggettoBulk> nextPage();

    /**
     * Imposta la dimensione della paginatura.
     */
    public abstract void setPageSize(int pageSize);
}