/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bp.FormController;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.bulk.annotation.Layout;
import it.cnr.jada.bulk.annotation.TypeProperty;
import it.cnr.jada.util.ArrayEnumeration;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.XmlWriter;
import it.cnr.jada.util.format.BigDecimalFormat;
import it.cnr.jada.util.format.BigIntegerFormat;
import it.cnr.jada.util.format.PrimitiveNumberFormat;
import it.cnr.jada.util.format.SafeDateFormat;
import it.cnr.jada.util.format.UppercaseStringFormat;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
/**
* Classe che descrive le propriet� di un input field da visualizzare in una FORM HTML 
* per visualizzare/modificare il valore di un attributo di un OggettoBulk. 
* Le FieldProperty sono definite nel BulkInfo di un OggettoBulk e possono essere elencate nel file ".info" associato.
* 
* Una field property possiede le seguenti propriet� (tra parentesi i valori di default:
* 
* name (null);
*     Nome della fieldProperty 
* property (null);
*     Nome della property JavaBean per estrarre/valorizzare il valore della FieldProperty 
* formatName (null);
*     Nome del Format da applicare al valore della FieldProperty per la visualizzazione in un 
*     campo di testo (se nullo viene usato toString). Pu� essere il nome di una classe Format 
*     o uno dei seguenti formati predefiniti:
* 
*     date_short
*         contiene java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT); 
*     date_medium
*         contiene java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM); 
*     date_long
*         contiene java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG); 
* 
* label (null);
*     Label da utilizzare per il campo di input. 
* keysProperty (null);
*     Se valorizzato � il nome di una property dell'OggettoBulk da utilizzare per convertire il valore 
*     della FieldProperty mediante il lookup in un dizionario di chiavi; 
*     la property deve restituire un'istanza di java.util.Dictionary che viene usata 
*     sia in lettura che in scrittura. Se inputType � uguale a SELECT il dizionario 
*     viene anche usato per ottenere l'elenco dei valori con cui riempire l'elenco delle opzioni. 
* optionsProperty (null);
*     Se valorizzato � il nome di una property dell'OggettoBulk da utilizzare per riempire 
*     l'elenco delle opzioni di nel caso in cui inputType � SELECT. La property deve restituire 
*     un oggetto assimilabile a una collezione (un array statico o un'istanza di java.util.Collection, 
*     java.util.Enumeration o di java.util.Iterator 
* printProperty (null);
*     Se valorizzato � il nome di una property da utilizzare per ottenere un valore "stampabile"; 
*     viene utilizzato solo in lettura secondo il seguente schema:
* 
*         * dall'OggettoBulk viene estratto il valore di "property";
*         * se nullo viene restituito null;
*         * se non nullo viene estratto il valore di "printProperty";
*         * se nullo viene restituito null;
*         * se non nullo viene applicato il format; 
* 
*     Le fieldProperty dotate di printProperty sono implicitamente readonly tranne quelle con inputType = SELECT 
*     e optionsProperty non nulla; in questo caso il valore da assegnare in scrittura � uno di quelli 
*     presenti nelle "options"; printProperty viene usata anche per estrarre la descrizione nell'elenco 
*     delle opzioni della SELECT. 
* style (null);
*     Lo style CSS da applicare al campo di input 
* labelStyle (null)
*     Lo style CSS da applicare alla label 
* inputType (UNDEFINED);
*     Uno dei seguenti:
* 
*         * "HIDDEN",
*         * "PASSWORD",
*         * "RADIOGROUP",
*         * "ROTEXT",
*         * "SEARCHTOOL",
*         * "SELECT",
*         * "TEXT",
*         * "TEXTAREA",
*         * "CHECKBOX",
*         * "CRUDTOOL"
*         * "BUTTON" 
*         * "DESCTOOL"
* 
* maxLength (0);
*     Massima lunghezza editabile di un campo di input quando inputType = TEXT 
* inputSize (0);
*     Lunghezza media in caratteri di un campo di input; valido solo per inputType = TEXT o ROTEXT 
* readonlyProperty (null);
*     Se valorizzato � il nome di una property da utilizzare per ottenere la disabilitazione dinamica 
*     del campo di input. La property deve essere di tipo Boolean e se il suo valore � true il 
*     campo viene disabilitato. 
* cols (0);
*     Numero di colonne di un campo di input di tipo TEXTAREA 
* rows (0);
*     Numero di righe di un campo di input di tipo TEXTAREA 
* formName (null);
*     Nome di una form da utilizzare per visualizzare i campi di ricerca di un SEARCHTOOL; 
*     se valorizzato accanto ai bottoni di ricerca del SEARCHTOOL vengono visualizzati tutti i 
*     campi presenti nella form specificata nel BulkInfo associato al tipo della "property" di questa FieldProperty; 
*     tali campi vengono abilitati in modo automatico solo se lo stato dell'OggettoBulk 
* caseSensitiveSearch (true);
*     Se vale false le ricerche vengono effettuate "case insensitive" (sia quelle con = che con LIKE) 
* editFormatName (null);
*     Nome del Format da utilizzare per convertire il testo digitato in un campo di input di tipo TEXT o TEXTAREA 
*     in valore da assegnare alla property. Se nullo viene utilizzato "format". 
* CRUDBusinessProcessName (null); Nome del CRUDBP da utilizzare sull'attivazione del bottone del CRUDTOOL
* completeOnSave (true); Se true il FormController che governa l'editing dell'OggettoBulk pu� tentare di completare automaticamente il valore della FieldProperty SEARCHTOOL sulla base delle informazioni inserite nei campi di ricerca.
* nullable (true); Se true e inputType = SELECT viene aggiunta una voce vuota allinizio dell'elenco delle opzioni selezionabili. Se l'utente seleziona tale voce la property viene impostata a null.
* enabledOnEdit (true); Se true il campo viene abilitato in modalit� FormController.EDIT.
* enabledOnInsert (true); Se true il campo viene abilitato in modalit� FormController.INSERT
* enabledOnSearch (false); Se true il campo viene abilitato in modalit� FormController.SEARCH
* enabledOnFreeSearch (true); Se true il campo viene abilitato in modalit� FormController.FREESEARCH
* enabledOnView (false); Se true il campo viene abilitato in modalit� FormController.VIEW
* readonlyPropertyOnEdit (false); Nome della property da usare se il FormController si trova in modalit� FormController.EDIT per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnInsert (false); Nome della property da usare se il FormController si trova in modalit� FormController.INSERT per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnSearch (false); Nome della property da usare se il FormController si trova in modalit� FormController.SEARCH per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnFreeSearch (false); Nome della property da usare se il FormController si trova in modalit� FormController.FREESEARCH per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnView (false); Nome della property da usare se il FormController si trova in modalit� FormController.VIEW per disabilitare in modo dinamico il campo di input.
* layout (DEFAULT_LAYOUT);
*     Tipo di layout da applicare; pu� assumere i valori "HORIZONTAL" e "VERTICAL"; attualmente � valido solo per i inputType = RADIOGROUP e CRUDSEARCH (viene applicato alla form di ricerca, vd. formName). 
* img
*     URL di immagine; valido solo per inputType = "BUTTON" 
* href
*     URL da usare sulle label del campo di input; se inputType = "BUTTON" viene usato sull'evento "onclick"
*/
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@SuppressWarnings("unchecked")
public class FieldProperty implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Map formats;
	private static final Map editFormats;
	private static String inputTypeNames[] = {
		"UNDEFINED", "HIDDEN", "PASSWORD", "RADIOGROUP", "ROTEXT", 
		"SEARCHTOOL", "SELECT", "TEXT", "TEXTAREA", "CHECKBOX", 
		"CRUDTOOL", "BUTTON", "TABLE", "FORM", "FILE", "DESCTOOL"
	};
	public static final int UNDEFINED = 0;
	public static final int HIDDEN = 1;
	public static final int PASSWORD = 2;
	public static final int RADIOGROUP = 3;
	public static final int ROTEXT = 4;
	public static final int SEARCHTOOL = 5;
	public static final int SELECT = 6;
	public static final int TEXT = 7;
	public static final int TEXTAREA = 8;
	public static final int CHECKBOX = 9;
	public static final int CRUDTOOL = 10;
	public static final int BUTTON = 11;
	public static final int TABLE = 12;
	public static final int FORM = 13;
	public static final int FILE = 14;
	public static final int DESCTOOL = 15;
	
	public static final int DEFAULT_LAYOUT = -1;
	public static final int VERTICAL_LAYOUT = 0;
	public static final int HORIZONTAL_LAYOUT = 1;
	public static final Object UNDEFINED_VALUE;
	private String property;
	private Format format;
	private String formatName;
	private String label;
	private String keysProperty;
	private String optionsProperty;
	private String printProperty;
	private String descProperty;
	private String name;
	private String style;
	private String labelStyle;
	private int inputType;
	private int maxLength;
	private int inputSize;
	private String readonlyProperty;
	private int cols;
	private int rows;
	private String formName;
	private BulkInfo bulkInfo;
	private boolean enabledOnSearch;
	private boolean enabledOnInsert;
	private boolean enabledOnEdit;
	private boolean caseSensitiveSearch;
	private String editFormatName;
	private Format editFormat;
	private String CRUDBusinessProcessName;
	private boolean enabledOnFreeSearch;
	private boolean completeOnSave;
	private boolean nullable;
	private String readonlyPropertyOnEdit;
	private String readonlyPropertyOnInsert;
	private String readonlyPropertyOnSearch;
	private String readonlyPropertyOnFreeSearch;
	private boolean enabledOnView;
	private String readonlyPropertyOnView;
	private int layout;
	private String img;
	private String href;
	private String columnSet;
	private boolean ghost;
	private String command;
	private Class propertyType;
	private String findProperty;
	private String freeSearchSet;
	private int ordinalPosition;
	private String accessKey;

	static{
		formats = new HashMap();
		editFormats = new HashMap();
		UNDEFINED_VALUE = Void.TYPE;
		SafeDateFormat safedateformat = new SafeDateFormat("dd/MM/yyyy");
		safedateformat.setLenient(false);
		formats.put("date_short", SafeDateFormat.getDateInstance());
		formats.put("date_medium", SafeDateFormat.getDateInstance(2));
		formats.put("date_long", SafeDateFormat.getDateInstance(1));
		formats.put("it.jada.util.format.UppercaseStringFormat", new UppercaseStringFormat());
		editFormats.put("date_short", SafeDateFormat.getDateInstance());
		editFormats.put("date_medium", SafeDateFormat.getDateInstance(2));
		editFormats.put("date_long", SafeDateFormat.getDateInstance(1));
		editFormats.put("it.jada.util.format.UppercaseStringFormat", new UppercaseStringFormat());
		safedateformat = new SafeDateFormat("dd/MM/yyyy HH:mm");
		safedateformat.setLenient(false);
		formats.put("timestamp", safedateformat);
		editFormats.put("timestamp", safedateformat);
		formats.put(java.lang.Integer.class, PrimitiveNumberFormat.getIntegerInstance());
		formats.put(java.lang.Long.class, PrimitiveNumberFormat.getLongInstance());
		formats.put(java.lang.Short.class, PrimitiveNumberFormat.getShortInstance());
		formats.put(java.lang.Byte.class, PrimitiveNumberFormat.getByteInstance());
		formats.put(java.math.BigInteger.class, BigIntegerFormat.getInstance());
		formats.put(java.math.BigDecimal.class, BigDecimalFormat.getInstance());
		editFormats.put(java.lang.Integer.class, PrimitiveNumberFormat.getIntegerInstance());
		editFormats.put(java.lang.Long.class, PrimitiveNumberFormat.getLongInstance());
		editFormats.put(java.lang.Short.class, PrimitiveNumberFormat.getShortInstance());
		editFormats.put(java.lang.Byte.class, PrimitiveNumberFormat.getByteInstance());
		editFormats.put(java.math.BigInteger.class, BigIntegerFormat.getInstance());
		editFormats.put(java.math.BigDecimal.class, BigDecimalFormat.getInstance());
	}
	public FieldProperty(){
		enabledOnSearch = false;
		enabledOnInsert = true;
		enabledOnEdit = true;
		caseSensitiveSearch = true;
		enabledOnFreeSearch = true;
		completeOnSave = true;
		nullable = true;
		enabledOnView = false;
		layout = -1;
		ordinalPosition = 0;
	}

	void fillNullsFrom(FieldProperty fieldproperty){
		if(fieldproperty == null)
			return;
		if(property == null)
			property = fieldproperty.property;
		if(descProperty == null)
		    descProperty = fieldproperty.descProperty;
		if(readonlyProperty == null)		
			readonlyProperty = fieldproperty.readonlyProperty;
		if(readonlyPropertyOnInsert == null)
			readonlyPropertyOnInsert = fieldproperty.readonlyPropertyOnInsert;
		if(readonlyPropertyOnSearch == null)
			readonlyPropertyOnSearch = fieldproperty.readonlyPropertyOnSearch;
		if(readonlyPropertyOnFreeSearch == null)
			readonlyPropertyOnFreeSearch = fieldproperty.readonlyPropertyOnFreeSearch;
		if(readonlyPropertyOnEdit == null)
			readonlyPropertyOnEdit = fieldproperty.readonlyPropertyOnEdit;
		if(readonlyPropertyOnView == null)
			readonlyPropertyOnView = fieldproperty.readonlyPropertyOnView;
		if(format == null)
			format = fieldproperty.format;
		if(editFormat == null)
			editFormat = fieldproperty.editFormat;
		if(label == null)
			label = fieldproperty.label;
		if(keysProperty == null)
			keysProperty = fieldproperty.keysProperty;
		if(optionsProperty == null)
			optionsProperty = fieldproperty.optionsProperty;
		if(printProperty == null)
			printProperty = fieldproperty.printProperty;
		if(style == null)
			style = fieldproperty.style;
		if(labelStyle == null)
			labelStyle = fieldproperty.labelStyle;
		if(inputType == 0)
			inputType = fieldproperty.inputType;
		if(maxLength == 0)
			maxLength = fieldproperty.maxLength;
		if(inputSize == 0)
			inputSize = fieldproperty.inputSize;
		if(cols == 0)
			cols = fieldproperty.cols;
		if(rows == 0)
			rows = fieldproperty.rows;
		if(formName == null)
			formName = fieldproperty.formName;
		if(!enabledOnSearch)
			enabledOnSearch = fieldproperty.enabledOnSearch;
		if(enabledOnInsert)
			enabledOnInsert = fieldproperty.enabledOnInsert;
		if(enabledOnFreeSearch)
			enabledOnFreeSearch = fieldproperty.enabledOnFreeSearch;
		if(enabledOnEdit)
			enabledOnEdit = fieldproperty.enabledOnEdit;
		if(!enabledOnView)
			enabledOnView = fieldproperty.enabledOnView;
		if(caseSensitiveSearch)
			caseSensitiveSearch = fieldproperty.caseSensitiveSearch;
		if(!completeOnSave)
			completeOnSave = fieldproperty.completeOnSave;
		if(nullable)
			nullable = fieldproperty.nullable;
		if(layout == -1)
			layout = fieldproperty.layout;
		if(href == null)
			href = fieldproperty.href;
		if(img == null)
			img = fieldproperty.img;
		if(!ghost)
			ghost = fieldproperty.ghost;
		if(command == null)
			command = fieldproperty.command;
		if(propertyType == null)
			propertyType = fieldproperty.propertyType;
		if(findProperty == null)
			findProperty = fieldproperty.findProperty;
		if(columnSet == null)
			columnSet = fieldproperty.columnSet;
		if(freeSearchSet == null)
			freeSearchSet = fieldproperty.freeSearchSet;
		if(accessKey == null)
			accessKey = fieldproperty.accessKey;
			
	}
    /**
     * Applica il formato specificato da questa FieldProperty
     */
	public String format(Object value){
		if(format == null){
			if(keysProperty == null && printProperty == null){
				Format format1 = (Format)formats.get(getPropertyType());
				if(format1 != null)
					return format1.format(value);
			}
			return Introspector.standardFormat(value);
		}else{
			return format.format(value);
		}
	}
    /**
     * Restituisce il BulkInfo a cui appartiene il ricevente.
     */
	public BulkInfo getBulkInfo(){
		return bulkInfo;
	}

	public int getCols(){
		return cols;
	}

	public String getColumnSet(){
		return columnSet;
	}

	public String getCommand(){
		return command;
	}


	public String getCRUDBusinessProcessName(){
		return CRUDBusinessProcessName;
	}

	public Format getEditFormat(){
		return editFormat;
	}

	public static Format getEditFormat(Object obj){
		return (Format)editFormats.get(obj);
	}

	public String getEditFormatName(){
		return editFormatName;
	}

	public String getFindProperty(){
		return findProperty;
	}

	public Format getFormat(){
		return format;
	}

	public static Format getFormat(Object obj){
		return (Format)formats.get(obj);
	}

	public String getFormatName(){
		return formatName;
	}

	public String getFormName(){
		return formName;
	}

	public String getFreeSearchSet(){
		return freeSearchSet;
	}

	public String getHref(){
		return href;
	}

	public String getImg(){
		return img;
	}

	public int getInputSize(){
		return inputSize;
	}

	public String getInputType(){
		return inputTypeNames[inputType];
	}

	public int getInputTypeIndex(){
		return inputType;
	}

	public Dictionary getKeysFrom(Object obj) throws IntrospectionException, InvocationTargetException{
		return (Dictionary)Introspector.getPropertyValue(obj, keysProperty);
	}

	public String getKeysProperty(){
		return keysProperty;
	}

	public String getLabel(){
		return label;
	}
	public String getLabel(Object bp){
		try {
			String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
			return dLabel;
		}catch (InvocationTargetException e) {
		} catch (IntrospectionException e) {
		}
		return label;
	}
	public String getFindLabel(Object bp){
		try {
			String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("findLabel", getName()));
			return dLabel;
		}catch (InvocationTargetException e) {
		} catch (IntrospectionException e) {
			try {
				String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
				return dLabel;
			} catch (IntrospectionException e1) {
			} catch (InvocationTargetException e1) {
			}
		}
		return label;
	}

	public String getLabelStyle(){
		return labelStyle;
	}

	public String getLayout(){
		switch(layout){
			case HORIZONTAL_LAYOUT:
				return "HORIZONTAL";
	
			case VERTICAL_LAYOUT:
				return "VERTICAL";
		}
		return null;
	}

	public int getLayoutType(){
		return layout;
	}

	protected String getMandatoryStyle(int status) throws IOException{
		if(!nullable && status != FormController.SEARCH && status != FormController.VIEW && status != FormController.FREESEARCH)
			return "background: #F5F5DC";
		else
			return null;
	}

	public int getMaxLength(){
		return maxLength;
	}

	public String getName(){
		return name;
	}

	public Object getOptionFrom(Object bean, int index){
		try{
			if(bean == null)
				return null;
			Object obj1 = Introspector.getPropertyValue(bean, optionsProperty);
			if(obj1 == null)
				return null;
			if(obj1 instanceof List)
				return ((List)obj1).get(index);
			if(obj1.getClass().isArray())
				return Array.get(obj1, index);
			Enumeration enumeration;
			if(obj1 instanceof Enumeration)
				enumeration = (Enumeration)obj1;
			else
			if(obj1 instanceof Dictionary)
				enumeration = ((Dictionary)obj1).elements();
			else
			if(obj1 instanceof Collection)
				enumeration = Collections.enumeration((Collection)obj1);
			else
				return null;
			for(int j = 0; enumeration.hasMoreElements(); j++)
			{
				Object obj2 = enumeration.nextElement();
				if(j == index)
					return obj2;
			}
			return null;
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}catch(InvocationTargetException invocationtargetexception){
			throw new IntrospectionError(invocationtargetexception);
		}catch(IndexOutOfBoundsException _ex){
			return null;
		}
	}
    /**
     * Restituisce una Enumeration dei valori ottenuti dalla "optionsProperty"
     */
	public Enumeration getOptionsFrom(Object bean){
		try{
			if(bean == null)
				return null;
			Object obj1 = Introspector.getPropertyValue(bean, optionsProperty);
			if(obj1 == null)
				return null;
			if(obj1 instanceof Enumeration)
				return (Enumeration)obj1;
			if(obj1 instanceof Vector)
				return ((Vector)obj1).elements();
			if(obj1.getClass().isArray())
				return new ArrayEnumeration((Object[])obj1);
			if(obj1 instanceof Dictionary)
				return ((Dictionary)obj1).elements();
			if(obj1 instanceof Collection)
				return Collections.enumeration((Collection)obj1);
			else
				return null;
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}catch(InvocationTargetException invocationtargetexception){
			throw new IntrospectionError(invocationtargetexception);
		}
	}

	public String getOptionsProperty(){
		return optionsProperty;
	}

	public int getOrdinalPosition(){
		return ordinalPosition;
	}

	public String getPrintProperty(){
		return printProperty;
	}

	public String getProperty(){
		return property;
	}

	public Class getPropertyType(){
		try{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(getBulkInfo().getBulkClass(), property);
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}
	}

	public Class getPropertyType(OggettoBulk oggettobulk){
		try{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(oggettobulk.getClass(), property);
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}
	}

	public Class getPropertyType(Class class1){
		try{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(class1, property);
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}
	}

	public String getReadonlyProperty(){
		return readonlyProperty;
	}

	public String getReadonlyPropertyOnEdit(){
		return readonlyPropertyOnEdit;
	}

	public String getReadonlyPropertyOnFreeSearch(){
		return readonlyPropertyOnFreeSearch;
	}

	public String getReadonlyPropertyOnInsert(){
		return readonlyPropertyOnInsert;
	}

	public String getReadonlyPropertyOnSearch(){
		return readonlyPropertyOnSearch;
	}

	public String getReadonlyPropertyOnView(){
		return readonlyPropertyOnView;
	}

	public int getRows(){
		return rows;
	}

	protected String getStringValueFromPrintProperty(Object bean) throws IntrospectionException, InvocationTargetException{
		if(bean == null)
			return null;
		else
			return (String)Introspector.getPropertyValue(bean, printProperty);
	}

	protected String getStringValueFrom(Object bean) throws IntrospectionException, InvocationTargetException{
		if(bean == null)
			return null;
		else
			return getStringValueFrom(bean, Introspector.getPropertyValue(bean, property));
	}
	public String getStringValueFrom(Object bean, Object value) throws IntrospectionException, InvocationTargetException{
		return getStringValueFrom(null, bean, value);
	}
	public String getStringValueFrom(Object bp, Object bean, Object value) throws IntrospectionException, InvocationTargetException{
		if(bean != null && printProperty != null && value != null)
		    if(printProperty.equals("label") && 
		       value instanceof FieldProperty && bp != null)
		       value = ((FieldProperty)value).getFindLabel(bp); 
		    else
			   value = Introspector.getPropertyValue(value, printProperty);
		if(bean != null && keysProperty != null && value != null){
			Dictionary dictionary = (Dictionary)Introspector.getPropertyValue(bean, keysProperty);
			if(dictionary != null)
				value = dictionary.get(value);
		}
		if(value == null)
			return null;
		else
			return format(value);
	}

	public String getStyle(){
		return style;
	}

	public String getType(){
		if(propertyType == null)
			return null;
		else
			return propertyType.getName();
	}

	public Object getValueFrom(Object bean) throws IntrospectionException, InvocationTargetException{
		return Introspector.getPropertyValue(bean, getProperty());
	}

	public boolean isCaseSensitiveSearch(){
		return caseSensitiveSearch;
	}

	public boolean isCompleteOnSave(){
		return completeOnSave;
	}

	public boolean isEnabledOnEdit(){
		return enabledOnEdit;
	}

	public boolean isEnabledOnFreeSearch(){
		return enabledOnFreeSearch;
	}

	public boolean isEnabledOnInsert(){
		return enabledOnInsert;
	}

	public boolean isEnabledOnSearch(){
		return enabledOnSearch;
	}

	public boolean isEnabledOnView(){
		return enabledOnView;
	}

	public boolean isGhost(){
		return ghost;
	}

	public boolean isMulti(){
		return getInputTypeIndex() == 6 && (getPropertyType().isArray() || java.util.Collection.class.isAssignableFrom(getPropertyType()));
	}

	public boolean isNoWrap(){
		return inputType == 5;
	}

	public boolean isNullable(){
		return nullable;
	}

	public boolean isReadonly(Object bean, int status)
	{
		try{
			if(inputType == ROTEXT)
				return true;
			if(bean == null)
				return true;
			if(property == null && inputType == BUTTON && readonlyProperty == null)
				return false;
			if(property == null && inputType == BUTTON && readonlyProperty != null && !((Boolean)Introspector.getPropertyValue(bean, readonlyProperty)).booleanValue())
				return false;				
			if(printProperty != null && (inputType == TEXT || inputType == TEXTAREA))
				return true;
			if(status != FormController.FREESEARCH && !Introspector.isPropertyWriteable(bean, property))
				return true;
			if(readonlyProperty != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyProperty)).booleanValue())
				return true;
			switch(status){
				case 3:
				default:
					break;
	
				case FormController.INSERT:
					if(!isEnabledOnInsert())
						return true;
					if(readonlyPropertyOnInsert != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyPropertyOnInsert)).booleanValue())
						return true;
					break;
	
				case FormController.EDIT:
					if(!isEnabledOnEdit())
						return true;
					if(readonlyPropertyOnEdit != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyPropertyOnEdit)).booleanValue())
						return true;
					break;
	
				case FormController.VIEW:
					if(!isEnabledOnView())
						return true;
					if(readonlyPropertyOnView != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyPropertyOnView)).booleanValue())
						return true;
					break;
	
				case FormController.SEARCH:
					if(!isEnabledOnSearch())
						return true;
					if(readonlyPropertyOnSearch != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyPropertyOnSearch)).booleanValue())
						return true;
					break;
	
				case FormController.FREESEARCH:
					if(!isEnabledOnFreeSearch())
						return true;
					if(readonlyPropertyOnFreeSearch != null && ((Boolean)Introspector.getPropertyValue(bean, readonlyPropertyOnFreeSearch)).booleanValue())
						return true;
					break;
			}
			return false;
		}catch(InvocationTargetException invocationtargetexception){
			throw new IntrospectionError(invocationtargetexception);
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}
	}
    /**
     * Resituisce la stringa formata dal prefisso specificato pi� il nome di una propriet�.
     */
	public static String mergePrefix(String prefix, String name){
		if(prefix == null){
			return name;
		}else{
			StringBuffer stringbuffer = new StringBuffer(prefix.length() + name.length() + 1);
			stringbuffer.append(prefix);
			stringbuffer.append('.');
			stringbuffer.append(name);
			return stringbuffer.toString();
		}
	}

	private Object parse(String s) throws ParseException{
		if(editFormat == null)
			return parse(s, format);
		else
			return parse(s, editFormat);
	}

	private Object parse(String s, Format format1) throws ParseException{
		if(format1 == null)
			return s;
		if(s.trim().length() == 0)
			return null;
		else
			return format1.parseObject(s);
	}

	private Object parseStringValue(Object obj, String s) throws ParseException{
		try{
			Object obj1 = null;
			if(format != null){
				obj1 = parse(s);
			}else{
				Class class1 = Introspector.getPropertyType(obj.getClass(), property);
				if(class1 == java.lang.String.class){
					obj1 = "".equals(s) ? null : ((Object) (s));
				}else{
					Format format1 = (Format)editFormats.get(class1);
					if(format1 == null)
						obj1 = Introspector.standardParse(s, class1);
					else
						obj1 = format1.parseObject(s);
				}
			}
			return obj1;
		}catch(IntrospectionException introspectionexception){
			throw new IntrospectionError(introspectionexception);
		}
	}

	void setBulkInfo(BulkInfo bulkinfo){
		bulkInfo = bulkinfo;
	}

	public void setCaseSensitiveSearch(boolean flag){
		caseSensitiveSearch = flag;
	}

	public void setCols(int newCols){
		cols = newCols;
	}

	public void setColumnSet(String newColumnSet){
		columnSet = newColumnSet;
	}

	public void setCommand(String newCommand){
		command = newCommand;
	}

	public void setCompleteOnSave(boolean newCompleteOnSave){
		completeOnSave = newCompleteOnSave;
	}

	public void setCRUDBusinessProcessName(String newCRUDBusinessProcessName){
		CRUDBusinessProcessName = newCRUDBusinessProcessName;
	}

	public void setEditFormat(Format format1){
		editFormat = format1;
	}

	public void setEditFormatName(String newEditFormatName){
		editFormat = (Format)formats.get(editFormatName = newEditFormatName);
		if(editFormat == null)
			try{
				formats.put(editFormatName, editFormat = (Format)Class.forName(editFormatName).newInstance());
			}catch(ClassNotFoundException _ex){
				throw new RuntimeException("Il formato " + editFormatName + " non esiste");
			}catch(InstantiationException _ex){
				throw new RuntimeException("Non \350 possibile istanziare il formato " + editFormatName);
			}catch(IllegalAccessException _ex){
				throw new RuntimeException("Non \350 possibile istanziare il formato " + editFormatName + " (Illegal access exception)");
			}catch(ClassCastException _ex){
				throw new RuntimeException("Il formato " + editFormatName + " non \350 una sottoclasse di java.text.Format");
			}
	}

	public void setEnabledOnEdit(boolean newEnabledOnEdit){
		enabledOnEdit = newEnabledOnEdit;
	}

	public void setEnabledOnFreeSearch(boolean newEnabledOnFreeSearch){
		enabledOnFreeSearch = newEnabledOnFreeSearch;
	}

	public void setEnabledOnInsert(boolean newEnabledOnInsert){
		enabledOnInsert = newEnabledOnInsert;
	}

	public void setEnabledOnSearch(boolean newEnabledOnSearch){
		enabledOnSearch = newEnabledOnSearch;
	}

	public void setEnabledOnView(boolean newEnabledOnView){
		enabledOnView = newEnabledOnView;
	}

	public void setFindProperty(String newFindProperty){
		findProperty = newFindProperty;
	}

	public void setFormat(Format format1){
		format = format1;
	}

	public void setFormatName(String newFormatName)
	{
		format = (Format)formats.get(formatName = newFormatName);
		if(format == null)
			try{
				formats.put(newFormatName, format = (Format)Class.forName(newFormatName).newInstance());
			}catch(ClassNotFoundException _ex){
				throw new RuntimeException("Il formato " + newFormatName + " non esiste");
			}catch(InstantiationException _ex){
				throw new RuntimeException("Non \350 possibile istanziare il formato " + newFormatName);
			}catch(IllegalAccessException _ex){
				throw new RuntimeException("Non \350 possibile istanziare il formato " + newFormatName + " (Illegal access exception)");
			}catch(ClassCastException _ex){
				throw new RuntimeException("Il formato " + newFormatName + " non \350 una sottoclasse di java.text.Format");
			}
		if(editFormat == null)
			editFormat = (Format)editFormats.get(newFormatName);
	}

	public void setFormName(String newFormatName){
		formName = newFormatName;
	}

	public void setFreeSearchSet(String newFreeSearchSet){
		freeSearchSet = newFreeSearchSet;
	}

	public void setGhost(boolean newGhost){
		ghost = newGhost;
	}

	public void setHref(String newHref){
		href = newHref;
	}

	public void setImg(String newImg){
		img = newImg;
	}

	public void setInputSize(int newInputSize){
		inputSize = newInputSize;
	}

	public void setInputType(String newInputType){
		for(int i = 0; i < inputTypeNames.length; i++)
			if(inputTypeNames[i].equalsIgnoreCase(newInputType)){
				inputType = i;
				return;
			}
	}

	public void setKeysProperty(String newKeysProperty){
		keysProperty = newKeysProperty;
	}

	public void setLabel(String newLabel){
		label = newLabel;
	}

	public void setLabelStyle(String newLabelStyle){
		labelStyle = newLabelStyle;
	}

	public void setLayout(String newLayout){
		if("HORIZONTAL".equalsIgnoreCase(newLayout))
			layout = HORIZONTAL_LAYOUT;
		if("VERTICAL".equalsIgnoreCase(newLayout))
			layout = VERTICAL_LAYOUT;
	}

	public void setMaxLength(int newMaxLength){
		maxLength = newMaxLength;
	}

	public void setName(String newName){
		name = newName;
	}

	public void setNullable(boolean newNullable){
		nullable = newNullable;
	}

	public void setOptionsProperty(String newOptionsProperty){
		optionsProperty = newOptionsProperty;
	}

	public void setOrdinalPosition(int newOrdinalPosition){
		ordinalPosition = newOrdinalPosition;
	}

	public void setPrintProperty(String newPrintProperty){
		printProperty = newPrintProperty;
	}

	public void setProperty(String newProperty){
		property = newProperty;
	}

	public void setReadonlyProperty(String newReadonlyProperty){
		readonlyProperty = newReadonlyProperty;
	}

	public void setReadonlyPropertyOnEdit(String newReadonlyPropertyOnEdit){
		readonlyPropertyOnEdit = newReadonlyPropertyOnEdit;
	}

	public void setReadonlyPropertyOnFreeSearch(String newReadonlyPropertyOnFreeSearch){
		readonlyPropertyOnFreeSearch = newReadonlyPropertyOnFreeSearch;
	}

	public void setReadonlyPropertyOnInsert(String newReadonlyPropertyOnInsert){
		readonlyPropertyOnInsert = newReadonlyPropertyOnInsert;
	}

	public void setReadonlyPropertyOnSearch(String newReadonlyPropertyOnSearch){
		readonlyPropertyOnSearch = newReadonlyPropertyOnSearch;
	}

	public void setReadonlyPropertyOnView(String newReadonlyPropertyOnView){
		readonlyPropertyOnView = newReadonlyPropertyOnView;
	}

	public void setRows(int newRows){
		rows = newRows;
	}

	@SuppressWarnings("unused")
	private Object setStringValueOn(Object bulk, String value) throws ParseException, IntrospectionException, InvocationTargetException{
		Object obj1 = parseStringValue(bulk, value);
		Introspector.setPropertyValue(bulk, name, obj1);
		return obj1;
	}

	public void setStyle(String newStyle){
		style = newStyle;
	}

	public void setType(String newType) throws ClassNotFoundException{
		if(newType != null)
			propertyType = Class.forName(newType);
	}

	public void setValueIn(Object bulk, Object value) throws InvocationTargetException, IntrospectionException{
		Introspector.setPropertyValue(bulk, property, value);
	}

	public String toString(){
		try{
			StringWriter stringwriter = new StringWriter();
			XmlWriter xmlwriter = new XmlWriter(stringwriter);
			xmlwriter.openTag("fieldProperty");
			xmlwriter.printAttribute("name", name, null);
			xmlwriter.printAttribute("property", property, null);
			xmlwriter.closeLastTag();
			return stringwriter.getBuffer().toString();
		}catch(IOException _ex){
			return super.toString();
		}
	}

	public void writeTo(XmlWriter xmlwriter) throws IOException{
		xmlwriter.openTag("fieldProperty");
		xmlwriter.printAttribute("name", name, null);
		xmlwriter.printAttribute("property", property, null);
		xmlwriter.printAttribute("descPproperty", descProperty, null);		
		xmlwriter.printAttribute("printProperty", printProperty, null);
		xmlwriter.printAttribute("formatName", formatName, null);
		xmlwriter.printAttribute("maxLength", maxLength, 0);
		xmlwriter.printAttribute("inputSize", inputSize, 0);
		xmlwriter.printAttribute("inputType", getInputType(), null);
		xmlwriter.printAttribute("label", label, null);
		xmlwriter.printAttribute("caseSensitiveSearch", caseSensitiveSearch, true);
		xmlwriter.closeLastTag();
	}

	/**
	 * @return
	 */
	public String getAccessKey()
	{
		return accessKey;
	}

	/**
	 * @param string
	 */
	public void setAccessKey(String string)
	{
		accessKey = string;
	}

	/**
	 * @return
	 */
	public String getDescProperty() {
		return descProperty;
	}

	/**
	 * @param string
	 */
	public void setDescProperty(String string) {
		descProperty = string;
	}
	protected String nvl(String target){
		if (target.length() == 0)
			return null;
		return target;
	}
	
	public void createWithAnnotation( FieldPropertyAnnotation fieldPropertyAnnotation) {
		createWithAnnotation(fieldPropertyAnnotation, null);
	}
	
	public void createWithAnnotation( FieldPropertyAnnotation fieldPropertyAnnotation, String property) {
		setName(fieldPropertyAnnotation.name());
		if (property != null)
			setProperty(property);
		setColumnSet(nvl(fieldPropertyAnnotation.columnSet()));
		setFreeSearchSet(nvl(fieldPropertyAnnotation.freeSearchSet()));
		setDescProperty(nvl(fieldPropertyAnnotation.descProperty()));
		setPrintProperty(nvl(fieldPropertyAnnotation.printProperty()));
		setKeysProperty(nvl(fieldPropertyAnnotation.keysProperty()));
		setOptionsProperty(nvl(fieldPropertyAnnotation.optionsProperty()));
		setReadonlyProperty(nvl(fieldPropertyAnnotation.readonlyProperty()));
		setReadonlyPropertyOnEdit(nvl(fieldPropertyAnnotation.readonlyPropertyOnEdit()));
		setReadonlyPropertyOnInsert(nvl(fieldPropertyAnnotation.readonlyPropertyOnInsert()));
		setReadonlyPropertyOnSearch(nvl(fieldPropertyAnnotation.readonlyPropertyOnSearch()));
		setReadonlyPropertyOnFreeSearch(nvl(fieldPropertyAnnotation.readonlyPropertyOnFreeSearch()));
		setReadonlyPropertyOnView(nvl(fieldPropertyAnnotation.readonlyPropertyOnView()));
		setFindProperty(nvl(fieldPropertyAnnotation.findProperty()));
		if (!fieldPropertyAnnotation.inputType().equals(InputType.UNDEFINED))
			setInputType(fieldPropertyAnnotation.inputType().name());
		
		if (!fieldPropertyAnnotation.type().equals(TypeProperty.FieldProperty) && 
			!fieldPropertyAnnotation.layout().equals(Layout.HORIZONTAL))
			setLayout(fieldPropertyAnnotation.layout().name());

		setFormName(nvl(fieldPropertyAnnotation.formName()));
		setInputSize(fieldPropertyAnnotation.inputSize());
		setMaxLength(fieldPropertyAnnotation.maxLength());
		setCols(fieldPropertyAnnotation.cols());
		setRows(fieldPropertyAnnotation.rows());
		if (!fieldPropertyAnnotation.formatName().equals(FormatName.NoFormat))
			setFormatName(FormatName.formatNameForAnnotation.get(fieldPropertyAnnotation.formatName().name()));		

		setEnabledOnSearch(fieldPropertyAnnotation.enabledOnSearch());
		setEnabledOnInsert(fieldPropertyAnnotation.enabledOnInsert());
		setEnabledOnEdit(fieldPropertyAnnotation.enabledOnEdit());
		setEnabledOnFreeSearch(fieldPropertyAnnotation.enabledOnFreeSearch());
		setEnabledOnView(fieldPropertyAnnotation.enabledOnView());
		setCaseSensitiveSearch(fieldPropertyAnnotation.caseSensitiveSearch());
		setCompleteOnSave(fieldPropertyAnnotation.completeOnSave());
		
		setNullable(fieldPropertyAnnotation.nullable());
		setCRUDBusinessProcessName(nvl(fieldPropertyAnnotation.CRUDBusinessProcessName()));
		setImg(nvl(fieldPropertyAnnotation.img()));
		setHref(nvl(fieldPropertyAnnotation.href()));
		setCommand(nvl(fieldPropertyAnnotation.command()));
		setAccessKey(nvl(fieldPropertyAnnotation.accessKey()));
		setLabel(nvl(fieldPropertyAnnotation.label()));
		setStyle(nvl(fieldPropertyAnnotation.style()));
		setLabelStyle(nvl(fieldPropertyAnnotation.labelStyle()));
	}	
}