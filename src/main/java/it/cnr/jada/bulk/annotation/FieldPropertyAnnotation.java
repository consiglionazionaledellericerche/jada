/**
 * 
 */
package it.cnr.jada.bulk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author mspasiano
 * 
 */
@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface FieldPropertyAnnotation{
	
	/** Nome logico del FieldProperty, l'attributo e obbligatorio, ed e univoco all'interno del documento.*/
	public String name();
	
	/** Tipo di FieldProperty*/
	public TypeProperty type() default TypeProperty.FieldProperty;
	
	/**
	 * Nome del ColumnSet di riferimento, 
	 * viene usato per i FieldProperty di tipo "SEARCHTOOL",
	 * e indica il columnSet da utilizzare all'atto della ricerca.
	 */
	public String columnSet() default "";
	/**
	 * Nome del FreeSearchSet di riferimento, 
	 * viene usato per i FieldProperty di tipo "SEARCHTOOL",
	 * e indica il FreeSearchSet da utilizzare all'atto della ricerca libera.
	 */
	public String freeSearchSet() default "";
	/**
	 * Nome della propieta del JavaBean da visualizzare, viene usato per i FieldProperty di tipo "DESCTOOL".
	 */
	public String descProperty() default "";
	/**
	 * Se valorizzato e il nome di una property da utilizzare per ottenere un valore "stampabile".
	 * Le fieldProperty dotate di printProperty sono implicitamente readonly tranne quelle con inputType = "SELECT"
	 * e optionsProperty non nulla; in questo caso il valore da assegnare in scrittura e uno di quelli
	 * presenti nelle "options"; printProperty viene usata anche per estrarre la descrizione nell'elenco
	 * delle opzioni della SELECT. 
	 */
	public String printProperty() default "";
	/**
	 * Se valorizzato e il nome di una property del JavaBean da utilizzare per convertire il valore
	 * della FieldProperty mediante il lookup in un dizionario di chiavi;
	 * la property deve restituire un'istanza di java.util.Dictionary che viene usata sia in lettura che in scrittura. 
	 * Se inputType e uguale a SELECT il dizionario viene anche usato per ottenere l'elenco dei valori con cui riempire l'elenco delle opzioni.
	 */
	public String keysProperty() default "";
	/**
	 * Se valorizzato e il nome di una property del JavaBean da utilizzare per riempire l'elenco delle
	 * opzioni di nel caso in cui inputType e "SELECT".
	 * La property deve restituire un oggetto assimilabile a una collezione (un array statico o un'istanza di
	 * java.util.Collection, java.util.Enumeration o di java.util.Iterator).
	 */
	public String optionsProperty() default "";
	/**
	 * Se valorizzato e il nome di una property da utilizzare per ottenere la disabilitazione dinamica del campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyProperty() default "";
	/**
	 * Nome della property da usare se il FormController si trova in modalita FormController.EDIT 
	 * per disabilitare in modo dinamico il campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyPropertyOnEdit() default "";
	/**
	 * Nome della property da usare se il FormController si trova in modalita FormController.INSERT 
	 * per disabilitare in modo dinamico il campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyPropertyOnInsert() default "";
	/**
	 * Nome della property da usare se il FormController si trova in modalita FormController.SEARCH 
	 * per disabilitare in modo dinamico il campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyPropertyOnSearch() default "";
	/**
	 * Nome della property da usare se il FormController si trova in modalita FormController.FREESEARCH 
	 * per disabilitare in modo dinamico il campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyPropertyOnFreeSearch() default "";
	/**
	 * Nome della property da usare se il FormController si trova in modalita FormController.VIEW 
	 * per disabilitare in modo dinamico il campo di input.
	 * La property deve essere di tipo Boolean e se il suo valore e true il campo viene disabilitato.
	 */
	public String readonlyPropertyOnView() default "";
	/**
	 * Nome della property da usare per impostare la ricerca su una property diversa da quella indicata in property,
	 * può essere utilizzata ad esempio pei i "SERACHTOOL" in determinati casi.
	 */
	public String findProperty() default "";
	/**
	 * Nome del tipo di input da usare per la
	 * presentazione. Oltre ai classici input html
	 * ("TEXT","TEXTAREA","HIDDEN","PASSWORD","RADIOGROUP","SELECT","CHECKBOX","BUTTON","FILE"), sono presenti alcune estensioni:
	 * "ROTEXT" --> Corrisponde ad un campo di input html disabilitato.
	 * "SEARCHTOOL" --> Corrisponde a diversi (in genere due, tipo "Codice e Descrizione") campi di input html (se valorizzato l'attributo "formName"),
	 * 					altrimenti presenta solamente tre pulsanti atti a riplire i campi, effettuare la ricerca, effettuare la ricerca libera.
	 * "CRUDTOOL" --> Corrisponde ad un bottone html utilizzato per effettuare operazioni di CRUD su una mappa diversa.
	 * "DESCTOOL" --> Corrisponde ad campo di input html a cui e associato una "descProperty" da valorizzare per mostrare una property ulteriore a quella di base.
	 */
	public InputType inputType() default InputType.UNDEFINED;
	/**
	 * Tipo di layout da applicare; puo assumere i valori
	 * "HORIZONTAL" e "VERTICAL"; attualmente e valido solo
	 * per inputType = "RADIOGROUP" e "SEARCHTOOL" (viene applicato alla form di ricerca, vd. formName).
	 */
	public Layout layout() default Layout.HORIZONTAL;
	/**
	 * Nome di una form da utilizzare per visualizzare i campi di ricerca di un SEARCHTOOL; 
	 * se valorizzato accanto ai bottoni di ricerca del SEARCHTOOL vengono
	 * visualizzati tutti i campi presenti nella form specificata nel BulkInfo associato al tipo della
	 * "property" di questa FieldProperty;
	 */
	public String formName() default "";
	/**
	 * Lunghezza media in caratteri di un campo di input;
	 * valido solo per inputType = "TEXT" o "ROTEXT"
	 */
	public int inputSize() default 0;
	/**
	 * Massima lunghezza editabile di un campo di input quando inputType = "TEXT"
	 */
	public int maxLength() default 0;
	/**
	 * Numero di colonne di un campo di input di tipo TEXTAREA 
	 */
	public int cols() default 0;
	/**
	 * Numero di righe di un campo di input di tipo TEXTAREA
	 */
	public int rows() default 0;
	/**
	 * Nome del Format da applicare al valore della FieldProperty per la visualizzazione in un campo di testo (se nullo viene usato toString). 
	 * Puo essere il nome di una classe Format. 
	 */
	public FormatName formatName() default FormatName.NoFormat;
	/**
	 * Se true il campo viene abilitato in modalita FormController.SEARCH
	 */
	public boolean enabledOnSearch() default false;
	/**
	 * Se true il campo viene abilitato in modalita FormController.INSERT
	 */
	public boolean enabledOnInsert() default true;
	/**
	 * Se true il campo viene abilitato in modalita FormController.EDIT
	 */
	public boolean enabledOnEdit() default true;
	/**
	 * Se true il campo viene abilitato in modalita FormController.FREESEARCH
	 */
	public boolean enabledOnFreeSearch() default true;
	/**
	 * Se true il campo viene abilitato in modalita FormController.VIEW
	 */
	public boolean enabledOnView() default false;
	/**
	 * Se vale false le ricerche vengono effettuate "case insensitive" 
	 * (sia quelle con = che con LIKE), il default e true.
	 */
	public boolean caseSensitiveSearch() default true;
	/**
	 * Se true il FormController che governa l'editing del JavaBean puo tentare di completare automaticamente
	 * il valore della FieldProperty SEARCHTOOL sulla base delle informazioni inserite nei campi di ricerca. 
	 * Il default e true.
	 */
	public boolean completeOnSave() default true;
	/**
	 * Se true e inputType = SELECT viene aggiunta una voce vuota allinizio dell'elenco delle opzioni selezionabili. 
	 * Se l'utente seleziona tale voce la property viene impostata a null.Il deafult e true,
	 * se impostata a false il campo viene presentato di colore diverso.
	 */
	public boolean nullable() default true;
	/**
	 * Nome del CRUDBP da utilizzare sull'attivazione del bottone del CRUDTOOL
	 */
	public String CRUDBusinessProcessName() default "";
	/**
	 * URL di immagine; valido solo per inputType = "BUTTON"
	 */
	public String img() default "";
	/**
	 * URL da usare sulle label del campo di input; 
	 * se inputType = "BUTTON" viene usato sull'evento "onclick"
	 */
	public String href() default "";
	/**
	 * Attributo usato per la validazione del campo di input,
	 * deve contenre il nome del metodo della classe Action associata da richiamare,
	 * all'atto della validazione si puo anche specificare il metodo doDefault comune a tutte le Action. 
	 */
	public String command() default "";
	/**
	 * Usato per inputType="BUTTON" fornisce la scorciatoia da tastiera
	 */
	public String accessKey() default "";
	/**
	 * Label da utilizzare per il campo di input.
	 */
	public String label() default "";
	/**
	 * Lo style CSS da applicare al campo di input
	 */
	public String style() default "";
	/**
	 * Lo style CSS da applicare alla label
	 */
	public String labelStyle() default "";
	/**
	 * Lo style CSS da applicare alla HeaderLabel
	 */
	public String headerStyle() default "";
	/**
	 * Lo style CSS da applicare alla Label 
	 */
	public String columnStyle() default "";
	/**
	 * Permette di definire una ulteriore Label, che funge da ragruppamento di colonne.
	 */
	public String headerLabel() default "";
	/**
	 * Nome del parametro da passare a JasperReport per la produzione della stampa
	 */
	public String paramNameJR() default "";
	/**
	 * Tipo del parametro da passare a JasperReport per la produzione della stampa, il nome deve essere una classe Java.
	 */
	public String paramTypeJR() default "";

	/**
	 *
	 * @return La classe css da applicare all'input
	 */
	public String inputCssClass() default "";

	/**
	 *
	 * @return La classe css da applicare all'icona sul button
	 */
	public String iconClass() default "";

	/**
	 *
	 * @return La classe css da applicare al button
	 */
	public String buttonClass() default "";

}
