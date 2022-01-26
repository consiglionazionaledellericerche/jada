/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 *
 */
package it.cnr.jada.bulk.annotation;

import java.lang.annotation.*;

/**
 * @author mspasiano
 *
 */
@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface FieldPropertyAnnotation {

    /** Nome logico del FieldProperty, l'attributo e obbligatorio, ed e univoco all'interno del documento.*/
    String name();

    /** Tipo di FieldProperty*/
    TypeProperty type() default TypeProperty.FieldProperty;

    /**
     * Nome del Property di riferimento
     */
    String property() default "";

    /**
     * Nome del ColumnSet di riferimento,
     * viene usato per i FieldProperty di tipo "SEARCHTOOL",
     * e indica il columnSet da utilizzare all'atto della ricerca.
     */
    String columnSet() default "";

    /**
     * Nome del FreeSearchSet di riferimento,
     * viene usato per i FieldProperty di tipo "SEARCHTOOL",
     * e indica il FreeSearchSet da utilizzare all'atto della ricerca libera.
     */
    String freeSearchSet() default "";

    /**
     * Nome della propieta del JavaBean da visualizzare, viene usato per i FieldProperty di tipo "DESCTOOL".
     */
    String descProperty() default "";

    /**
     * Se valorizzato e il nome di una property da utilizzare per ottenere un valore "stampabile".
     * Le fieldProperty dotate di printProperty sono implicitamente readonly tranne quelle con inputType = "SELECT"
     * e optionsProperty non nulla; in questo caso il valore da assegnare in scrittura e uno di quelli
     * presenti nelle "options"; printProperty viene usata anche per estrarre la descrizione nell'elenco
     * delle opzioni della SELECT.
     */
    String printProperty() default "";

    /**
     * Se valorizzato e il nome di una property del JavaBean da utilizzare per convertire il valore
     * della FieldProperty mediante il lookup in un dizionario di chiavi;
     * la property deve restituire un'istanza di java.util.Dictionary che viene usata sia in lettura che in scrittura.
     * Se inputType e uguale a SELECT il dizionario viene anche usato per ottenere l'elenco dei valori con cui riempire l'elenco delle opzioni.
     */
    String keysProperty() default "";

    /**
     * Se valorizzato e il nome di una property del JavaBean da utilizzare per riempire l'elenco delle
     * opzioni di nel caso in cui inputType e "SELECT".
     * La property deve restituire un oggetto assimilabile a una collezione (un array statico o un'istanza di
     * java.util.Collection, java.util.Enumeration o di java.util.Iterator).
     */
    String optionsProperty() default "";

    /**
     * Se valorizzato e il nome di una property da utilizzare per ottenere la disabilitazione dinamica del campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyProperty() default "";

    /**
     * Nome della property da usare se il FormController si trova in modalita FormController.EDIT
     * per disabilitare in modo dinamico il campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyPropertyOnEdit() default "";

    /**
     * Nome della property da usare se il FormController si trova in modalita FormController.INSERT
     * per disabilitare in modo dinamico il campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyPropertyOnInsert() default "";

    /**
     * Nome della property da usare se il FormController si trova in modalita FormController.SEARCH
     * per disabilitare in modo dinamico il campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyPropertyOnSearch() default "";

    /**
     * Nome della property da usare se il FormController si trova in modalita FormController.FREESEARCH
     * per disabilitare in modo dinamico il campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyPropertyOnFreeSearch() default "";

    /**
     * Nome della property da usare se il FormController si trova in modalita FormController.VIEW
     * per disabilitare in modo dinamico il campo di input.
     * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
     */
    String readonlyPropertyOnView() default "";

    /**
     * Nome della property da usare per impostare la ricerca su una property diversa da quella indicata in property,
     * può essere utilizzata ad esempio pei i "SERACHTOOL" in determinati casi.
     */
    String findProperty() default "";

    /**
     * Nome del tipo di input da usare per la
     * presentazione. Oltre ai classici input html
     * ("TEXT","TEXTAREA","HIDDEN","PASSWORD","RADIOGROUP","SELECT","CHECKBOX","BUTTON","FILE"), sono presenti alcune estensioni:
     * "ROTEXT" --> Corrisponde ad un campo di input html disabilitato.
     * "SEARCHTOOL" --> Corrisponde a diversi (in genere due, tipo "Codice e Descrizione") campi di input html (se valorizzato l'attributo "formName"),
     * 					altrimenti presenta solamente tre pulsanti atti a riplire i campi, effettuare la ricerca, effettuare la ricerca libera.
     * "CRUDTOOL" --> Corrisponde ad un bottone html utilizzato per effettuare operazioni di CRUD su una mappa diversa.
     * "VIEWTOOL" --> Corrisponde ad un bottone html utilizzato per effettuare operazioni di VIEW su una mappa diversa.
     * "DESCTOOL" --> Corrisponde ad campo di input html a cui e associato una "descProperty" da valorizzare per mostrare una property ulteriore a quella di base.
     */
    InputType inputType() default InputType.UNDEFINED;

    /**
     * Tipo di layout da applicare; puo assumere i valori
     * "HORIZONTAL" e "VERTICAL"; attualmente e valido solo
     * per inputType = "RADIOGROUP" e "SEARCHTOOL" (viene applicato alla form di ricerca, vd. formName).
     */
    Layout layout() default Layout.HORIZONTAL;

    /**
     * Nome di una form da utilizzare per visualizzare i campi di ricerca di un SEARCHTOOL;
     * se valorizzato accanto ai bottoni di ricerca del SEARCHTOOL vengono
     * visualizzati tutti i campi presenti nella form specificata nel BulkInfo associato al tipo della
     * "property" di questa FieldProperty;
     */
    String formName() default "";

    /**
     * Lunghezza media in caratteri di un campo di input;
     * valido solo per inputType = "TEXT" o "ROTEXT"
     */
    int inputSize() default 0;

    /**
     * Massima lunghezza editabile di un campo di input quando inputType = "TEXT"
     */
    int maxLength() default 0;

    /**
     * Numero di colonne di un campo di input di tipo TEXTAREA
     */
    int cols() default 0;

    /**
     * Numero di righe di un campo di input di tipo TEXTAREA
     */
    int rows() default 0;

    /**
     * Nome del Format da applicare al valore della FieldProperty per la visualizzazione in un campo di testo (se nullo viene usato toString).
     * Puo essere il nome di una classe Format.
     */
    FormatName formatName() default FormatName.NoFormat;

    /**
     * Se true il campo viene abilitato in modalita FormController.SEARCH
     */
    boolean enabledOnSearch() default false;

    /**
     * Se true il campo viene abilitato in modalita FormController.INSERT
     */
    boolean enabledOnInsert() default true;

    /**
     * Se true il campo viene abilitato in modalita FormController.EDIT
     */
    boolean enabledOnEdit() default true;

    /**
     * Se true il campo viene abilitato in modalita FormController.FREESEARCH
     */
    boolean enabledOnFreeSearch() default true;

    /**
     * Se true il campo viene abilitato in modalita FormController.VIEW
     */
    boolean enabledOnView() default false;

    /**
     * Se vale false le ricerche vengono effettuate "case insensitive"
     * (sia quelle con = che con LIKE), il default e true.
     */
    boolean caseSensitiveSearch() default true;

    /**
     * Se true il FormController che governa l'editing del JavaBean puo tentare di completare automaticamente
     * il valore della FieldProperty SEARCHTOOL sulla base delle informazioni inserite nei campi di ricerca.
     * Il default e true.
     */
    boolean completeOnSave() default true;

    /**
     * Se true e inputType = SELECT viene aggiunta una voce vuota allinizio dell'elenco delle opzioni selezionabili.
     * Se l'utente seleziona tale voce la property viene impostata a null.Il deafult e true,
     * se impostata a false il campo viene presentato di colore diverso.
     */
    boolean nullable() default true;

    /**
     * Nome del CRUDBP da utilizzare sull'attivazione del bottone del CRUDTOOL
     */
    String CRUDBusinessProcessName() default "";

    /**
     * Nome del BP da utilizzare sull'attivazione del bottone del VIEWTOOL
     */
    String VIEWBusinessProcessName() default "";

    /**
     * URL di immagine; valido solo per inputType = "BUTTON"
     */
    String img() default "";

    /**
     * URL da usare sulle label del campo di input;
     * se inputType = "BUTTON" viene usato sull'evento "onclick"
     */
    String href() default "";

    /**
     * Attributo usato per la validazione del campo di input,
     * deve contenre il nome del metodo della classe Action associata da richiamare,
     * all'atto della validazione si puo anche specificare il metodo doDefault comune a tutte le Action.
     */
    String command() default "";

    /**
     * Usato per inputType="BUTTON" fornisce la scorciatoia da tastiera
     */
    String accessKey() default "";

    /**
     * Label da utilizzare per il campo di input.
     */
    String label() default "";

    /**
     * Lo style CSS da applicare al campo di input
     */
    String style() default "";

    /**
     * Lo style CSS da applicare alla label
     */
    String labelStyle() default "";

    /**
     * Lo style CSS da applicare alla HeaderLabel
     */
    String headerStyle() default "";

    /**
     * Lo style CSS da applicare alla Label
     */
    String columnStyle() default "";

    /**
     * Permette di definire una ulteriore Label, che funge da ragruppamento di colonne.
     */
    String headerLabel() default "";

    /**
     * Nome del parametro da passare a JasperReport per la produzione della stampa
     */
    String paramNameJR() default "";

    /**
     * Tipo del parametro da passare a JasperReport per la produzione della stampa, il nome deve essere una classe Java.
     */
    String paramTypeJR() default "";

    /**
     *
     * @return La classe css da applicare all'input
     */
    String inputCssClass() default "";

    /**
     *
     * @return La classe css da applicare all'icona sul button
     */
    String iconClass() default "";

    /**
     *
     * @return La classe css da applicare al button
     */
    String buttonClass() default "";

}
