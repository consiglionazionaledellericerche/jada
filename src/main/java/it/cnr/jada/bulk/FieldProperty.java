package it.cnr.jada.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.bulk.annotation.Layout;
import it.cnr.jada.bulk.annotation.TypeProperty;
import it.cnr.jada.persistency.AbstractIntrospector;
import it.cnr.jada.persistency.beans.BeanIntrospector;
import it.cnr.jada.persistency.sql.ColumnMap;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.SQLPersistentInfo;
import it.cnr.jada.util.ArrayEnumeration;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.NullableFormat;
import it.cnr.jada.util.SafeDateFormat;
import it.cnr.jada.util.UppercaseStringFormat;
import it.cnr.jada.util.XmlWriter;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.jsp.Table;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;

import org.codehaus.jackson.annotate.JsonIgnore;
/**
* Classe che descrive le proprietà di un input field da visualizzare in una FORM HTML 
* per visualizzare/modificare il valore di un attributo di un OggettoBulk. 
* Le FieldProperty sono definite nel BulkInfo di un OggettoBulk e possono essere elencate nel file ".info" associato.
* 
* Una field property possiede le seguenti proprietà (tra parentesi i valori di default:
* 
* name (null);
*     Nome della fieldProperty 
* property (null);
*     Nome della property JavaBean per estrarre/valorizzare il valore della FieldProperty 
* formatName (null);
*     Nome del Format da applicare al valore della FieldProperty per la visualizzazione in un 
*     campo di testo (se nullo viene usato toString). Può essere il nome di una classe Format 
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
*     Se valorizzato è il nome di una property dell'OggettoBulk da utilizzare per convertire il valore 
*     della FieldProperty mediante il lookup in un dizionario di chiavi; 
*     la property deve restituire un'istanza di java.util.Dictionary che viene usata 
*     sia in lettura che in scrittura. Se inputType è uguale a SELECT il dizionario 
*     viene anche usato per ottenere l'elenco dei valori con cui riempire l'elenco delle opzioni. 
* optionsProperty (null);
*     Se valorizzato è il nome di una property dell'OggettoBulk da utilizzare per riempire 
*     l'elenco delle opzioni di nel caso in cui inputType è SELECT. La property deve restituire 
*     un oggetto assimilabile a una collezione (un array statico o un'istanza di java.util.Collection, 
*     java.util.Enumeration o di java.util.Iterator 
* printProperty (null);
*     Se valorizzato è il nome di una property da utilizzare per ottenere un valore "stampabile"; 
*     viene utilizzato solo in lettura secondo il seguente schema:
* 
*         * dall'OggettoBulk viene estratto il valore di "property";
*         * se nullo viene restituito null;
*         * se non nullo viene estratto il valore di "printProperty";
*         * se nullo viene restituito null;
*         * se non nullo viene applicato il format; 
* 
*     Le fieldProperty dotate di printProperty sono implicitamente readonly tranne quelle con inputType = SELECT 
*     e optionsProperty non nulla; in questo caso il valore da assegnare in scrittura è uno di quelli 
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
*     Se valorizzato è il nome di una property da utilizzare per ottenere la disabilitazione dinamica 
*     del campo di input. La property deve essere di tipo Boolean e se il suo valore è true il 
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
* completeOnSave (true); Se true il FormController che governa l'editing dell'OggettoBulk può tentare di completare automaticamente il valore della FieldProperty SEARCHTOOL sulla base delle informazioni inserite nei campi di ricerca.
* nullable (true); Se true e inputType = SELECT viene aggiunta una voce vuota allinizio dell'elenco delle opzioni selezionabili. Se l'utente seleziona tale voce la property viene impostata a null.
* enabledOnEdit (true); Se true il campo viene abilitato in modalità FormController.EDIT.
* enabledOnInsert (true); Se true il campo viene abilitato in modalità FormController.INSERT
* enabledOnSearch (false); Se true il campo viene abilitato in modalità FormController.SEARCH
* enabledOnFreeSearch (true); Se true il campo viene abilitato in modalità FormController.FREESEARCH
* enabledOnView (false); Se true il campo viene abilitato in modalità FormController.VIEW
* readonlyPropertyOnEdit (false); Nome della property da usare se il FormController si trova in modalità FormController.EDIT per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnInsert (false); Nome della property da usare se il FormController si trova in modalità FormController.INSERT per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnSearch (false); Nome della property da usare se il FormController si trova in modalità FormController.SEARCH per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnFreeSearch (false); Nome della property da usare se il FormController si trova in modalità FormController.FREESEARCH per disabilitare in modo dinamico il campo di input.
* readonlyPropertyOnView (false); Nome della property da usare se il FormController si trova in modalità FormController.VIEW per disabilitare in modo dinamico il campo di input.
* layout (DEFAULT_LAYOUT);
*     Tipo di layout da applicare; può assumere i valori "HORIZONTAL" e "VERTICAL"; attualmente è valido solo per i inputType = RADIOGROUP e CRUDSEARCH (viene applicato alla form di ricerca, vd. formName). 
* img
*     URL di immagine; valido solo per inputType = "BUTTON" 
* href
*     URL da usare sulle label del campo di input; se inputType = "BUTTON" viene usato sull'evento "onclick"
*/
public class FieldProperty implements Serializable{

	private static final Map formats;
	private static final Map editFormats;
	private static Button searchButton;
	private static Button newButton;
	private static Button freeSearchButton;
	private static Button crudButton;
	private static String inputTypeNames[] = {
		"UNDEFINED", "HIDDEN", "PASSWORD", "RADIOGROUP", "ROTEXT", "SEARCHTOOL", "SELECT", "TEXT", "TEXTAREA", "CHECKBOX", 
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
	@JsonIgnore
	private Format format;
	@JsonIgnore
	private String formatName;
	private String label;
	@JsonIgnore
	private String keysProperty;
	@JsonIgnore
	private String optionsProperty;
	@JsonIgnore
	private String printProperty;
	@JsonIgnore
	private String descProperty;
	private String name;
	@JsonIgnore
	private String style;
	@JsonIgnore
	private String labelStyle;
	private int inputType;
	private int maxLength;
	private int inputSize;
	@JsonIgnore
	private String readonlyProperty;
	@JsonIgnore
	private int cols;
	@JsonIgnore
	private int rows;
	@JsonIgnore
	private String formName;
	@JsonIgnore
	private BulkInfo bulkInfo;
	@JsonIgnore
	private boolean enabledOnSearch;
	@JsonIgnore
	private boolean enabledOnInsert;
	@JsonIgnore
	private boolean enabledOnEdit;
	@JsonIgnore
	private boolean caseSensitiveSearch;
	@JsonIgnore
	private String editFormatName;
	@JsonIgnore
	private Format editFormat;
	@JsonIgnore
	private String CRUDBusinessProcessName;
	@JsonIgnore
	private boolean enabledOnFreeSearch;
	@JsonIgnore
	private boolean completeOnSave;
	
	private boolean nullable;
	@JsonIgnore
	private String readonlyPropertyOnEdit;
	@JsonIgnore
	private String readonlyPropertyOnInsert;
	@JsonIgnore
	private String readonlyPropertyOnSearch;
	@JsonIgnore
	private String readonlyPropertyOnFreeSearch;
	@JsonIgnore
	private boolean enabledOnView;
	@JsonIgnore
	private String readonlyPropertyOnView;
	@JsonIgnore
	private int layout;
	@JsonIgnore
	private String img;
	@JsonIgnore
	private String href;
	@JsonIgnore
	private String columnSet;
	@JsonIgnore
	private boolean ghost;
	@JsonIgnore
	private String command;
	private static Button confirmButton;
	private static Button cancelButton;
	private Class propertyType;
	@JsonIgnore
	private String findProperty;
	@JsonIgnore
	private String freeSearchSet;
	@JsonIgnore
	private int ordinalPosition;
	@JsonIgnore
	private String accessKey;
	
	static{
		formats = new HashMap();
		editFormats = new HashMap();
		UNDEFINED_VALUE = Void.TYPE;
		SafeDateFormat safedateformat = new SafeDateFormat("dd/MM/yyyy");
		safedateformat.setLenient(false);
		NullableFormat nullableformat = new NullableFormat(safedateformat);
		formats.put("date_short", nullableformat);
		formats.put("date_medium", new NullableFormat(SafeDateFormat.getDateInstance(2)));
		formats.put("date_long", new NullableFormat(SafeDateFormat.getDateInstance(1)));
		formats.put("it.cnr.jada.util.UppercaseStringFormat", new UppercaseStringFormat());
		editFormats.put("date_short", nullableformat);
		editFormats.put("date_medium", new NullableFormat(SafeDateFormat.getDateInstance(2)));
		editFormats.put("date_long", new NullableFormat(SafeDateFormat.getDateInstance(1)));
		editFormats.put("it.cnr.jada.util.UppercaseStringFormat", new UppercaseStringFormat());
		safedateformat = new SafeDateFormat("dd/MM/yyyy HH:mm");
		safedateformat.setLenient(false);
		nullableformat = new NullableFormat(safedateformat);
		formats.put("timestamp", nullableformat);
		editFormats.put("timestamp", nullableformat);
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

	protected String encodeHtmlText(Object obj)
	{
		if(obj == null)
			return "";
		else
			return JSPUtils.encodeHtmlString(obj.toString());
	}

	protected String encodeHtmlText(String s)
	{
		if(s == null)
			return "";
		else
			return JSPUtils.encodeHtmlString(s);
	}

	public boolean fillBulkFromActionContext(OggettoBulk oggettobulk, ActionContext actioncontext, String s, int i, FieldValidationMap fieldvalidationmap)
		throws FillException
	{
		if(actioncontext instanceof HttpActionContext)
			return fillBulkFromRequest(oggettobulk, ((HttpActionContext)actioncontext).getRequest(), s, i, fieldvalidationmap);
		else
			return false;
	}

	public boolean fillBulkFromRequest(OggettoBulk oggettobulk, ServletRequest servletrequest, String s, int i, FieldValidationMap fieldvalidationmap)
		throws FillException
	{
		if(isReadonly(oggettobulk, i))
			return false;
		try
		{
			if(inputType == 5)
			{
				if(formName != null)
				{
					OggettoBulk oggettobulk1 = (OggettoBulk)getValueFrom(oggettobulk);
					if(oggettobulk1 != null && oggettobulk1.getCrudStatus() != 5)
						return oggettobulk1.fillFromHttpRequest(servletrequest, mergePrefix(s, name), 0, fieldvalidationmap);
				}
				return false;
			}
			if(inputType == 13)
			{
				if(formName != null)
				{
					OggettoBulk oggettobulk2 = (OggettoBulk)getValueFrom(oggettobulk);
					if(oggettobulk2 != null)
						return oggettobulk2.fillFromHttpRequest(servletrequest, mergePrefix(s, name), i, fieldvalidationmap);
				}
				return false;
			}
			Object obj = getValueFromRequest(oggettobulk, servletrequest, s);
			fieldvalidationmap.clear(s, name);
			if(obj == UNDEFINED_VALUE)
				return false;
			Object obj1 = Introspector.getPropertyValue(oggettobulk, property);
			if(obj1 == null && obj == null)
				return false;
			if(obj1 instanceof BigDecimal)
			{
				//Cambiato il compareTo aggiungendo la scale per problema sul cambio
				//21/10/2004 Marco
				if(obj != null && obj1 != null && ((BigDecimal)obj1).compareTo(((BigDecimal)obj).setScale(3,BigDecimal.ROUND_HALF_EVEN)) == 0)
					return false;
			} else
			if(obj1 != null && obj1.equals(obj))
				return false;
			Introspector.setPropertyValue(oggettobulk, property, obj);
			fieldvalidationmap.clear(s, name);
			return !ghost;
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			if(invocationtargetexception.getTargetException() instanceof ValidationException)
			{
				String s1 = invocationtargetexception.getTargetException().getMessage() != null ? invocationtargetexception.getTargetException().getMessage() : "Errore di validazione";
				fieldvalidationmap.put(s, name, new FieldValidationException(s1, invocationtargetexception, s, name, servletrequest.getParameter(mergePrefix(s, name))));
				throw new FillException(s1, invocationtargetexception, s, this, servletrequest.getParameter(mergePrefix(s, name)));
			} else
			{
				throw new IntrospectionError(invocationtargetexception);
			}
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
		catch(FillException fillexception)
		{
			fieldvalidationmap.put(s, name, new FieldValidationException(fillexception.getMessage(), fillexception, fillexception.getPrefix(), fillexception.getField().getName(), fillexception.getText()));
			throw fillexception;
		}
	}

	void fillNullsFrom(FieldProperty fieldproperty)
	{
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
		if(CRUDBusinessProcessName == null)
			CRUDBusinessProcessName = fieldproperty.CRUDBusinessProcessName;
			
	}

	public String format(Object obj)
	{
		if(format == null)
		{
			if(keysProperty == null && printProperty == null)
			{
				Format format1 = (Format)formats.get(getPropertyType());
				if(format1 != null)
					return format1.format(obj);
			}
			return Introspector.standardFormat(obj);
		} else
		{
			return format.format(obj);
		}
	}

	public BulkInfo getBulkInfo()
	{
		return bulkInfo;
	}

	private Button getCancelButton()
	{
		if(cancelButton == null)
			cancelButton = new Button(Config.getHandler().getProperties(getClass()), "cancelButton");
		return cancelButton;
	}

	public int getCols()
	{
		return cols;
	}

	public String getColumnSet()
	{
		return columnSet;
	}

	public String getCommand()
	{
		return command;
	}

	private Button getConfirmButton()
	{
		if(confirmButton == null)
			confirmButton = new Button(Config.getHandler().getProperties(getClass()), "confirmButton");
		return confirmButton;
	}
	@JsonIgnore
	public String getCRUDBusinessProcessName()
	{
		return CRUDBusinessProcessName;
	}

	private Button getCrudButton()
	{
		if(crudButton == null)
			crudButton = new Button(Config.getHandler().getProperties(getClass()), "crudButton");
		return crudButton;
	}

	public Format getEditFormat()
	{
		return editFormat;
	}

	public static Format getEditFormat(Object obj)
	{
		return (Format)editFormats.get(obj);
	}

	public String getEditFormatName()
	{
		return editFormatName;
	}

	public String getFindProperty()
	{
		return findProperty;
	}

	public Format getFormat()
	{
		return format;
	}

	public static Format getFormat(Object obj)
	{
		return (Format)formats.get(obj);
	}

	public String getFormatName()
	{
		return formatName;
	}

	protected String getFormatStyle(Object obj)
	{
		if(format instanceof StyledFormat)
			return ((StyledFormat)format).getStyle(obj);
		else
			return null;
	}

	public String getFormName()
	{
		return formName;
	}

	private Button getFreeSearchButton()
	{
		if(freeSearchButton == null)
			freeSearchButton = new Button(Config.getHandler().getProperties(getClass()), "freeSearchButton");
		return freeSearchButton;
	}

	public String getFreeSearchSet()
	{
		return freeSearchSet;
	}

	public String getHref()
	{
		return href;
	}

	public String getImg()
	{
		return img;
	}

	public int getInputSize()
	{
		return inputSize;
	}

	public String getInputType()
	{
		return inputTypeNames[inputType];
	}
	@JsonIgnore
	public int getInputTypeIndex()
	{
		return inputType;
	}

	public Dictionary getKeysFrom(Object obj)
		throws IntrospectionException, InvocationTargetException
	{
		return (Dictionary)Introspector.getPropertyValue(obj, keysProperty);
	}

	public String getKeysProperty()
	{
		return keysProperty;
	}

	public String getLabel()
	{
		return label;
	}
	public String getLabel(Object bp)
	{
		try {
			String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
			return dLabel;
		}catch (InvocationTargetException e) {
		} catch (IntrospectionException e) {
		}
		return label;
	}
	public String getFindLabel(Object bp)
	{
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

	public String getLabelStyle()
	{
		return labelStyle;
	}

	public String getLayout()
	{
		switch(layout)
		{
		case 1: // '\001'
			return "HORIZONTAL";

		case 0: // '\0'
			return "VERTICAL";
		}
		return null;
	}
	@JsonIgnore
	public int getLayoutType()
	{
		return layout;
	}

	protected String getMandatoryStyle(int i)
		throws IOException
	{
		if(!nullable && i != 0 && i != 5 && i != 4)
			return "background: #F5F5DC";
		else
			return null;
	}

	public int getMaxLength()
	{
		return maxLength;
	}

	public String getName()
	{
		return name;
	}

	private Button getNewButton()
	{
		if(newButton == null)
			newButton = new Button(Config.getHandler().getProperties(getClass()), "newButton");
		return newButton;
	}

	public Object getOptionFrom(Object obj, int i)
	{
		try
		{
			if(obj == null)
				return null;
			Object obj1 = Introspector.getPropertyValue(obj, optionsProperty);
			if(obj1 == null)
				return null;
			if(obj1 instanceof List)
				return ((List)obj1).get(i);
			if(obj1.getClass().isArray())
				return Array.get(obj1, i);
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
				if(j == i)
					return obj2;
			}

			return null;
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			throw new IntrospectionError(invocationtargetexception);
		}
		catch(IndexOutOfBoundsException _ex)
		{
			return null;
		}
	}

	public Enumeration getOptionsFrom(Object obj)
	{
		try
		{
			if(obj == null)
				return null;
			Object obj1 = Introspector.getPropertyValue(obj, optionsProperty);
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
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			throw new IntrospectionError(invocationtargetexception);
		}
	}

	public String getOptionsProperty()
	{
		return optionsProperty;
	}

	public int getOrdinalPosition()
	{
		return ordinalPosition;
	}

	public String getPrintProperty()
	{
		return printProperty;
	}

	public String getProperty()
	{
		return property;
	}

	public Class getPropertyType()
	{
		try
		{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(getBulkInfo().getBulkClass(), property);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
	}

	public Class getPropertyType(OggettoBulk oggettobulk)
	{
		try
		{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(oggettobulk.getClass(), property);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
	}

	public Class getPropertyType(Class class1)
	{
		try
		{
			if(propertyType != null)
				return propertyType;
			else
				return Introspector.getPropertyType(class1, property);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
	}

	public String getReadonlyProperty()
	{
		return readonlyProperty;
	}

	public String getReadonlyPropertyOnEdit()
	{
		return readonlyPropertyOnEdit;
	}

	public String getReadonlyPropertyOnFreeSearch()
	{
		return readonlyPropertyOnFreeSearch;
	}

	public String getReadonlyPropertyOnInsert()
	{
		return readonlyPropertyOnInsert;
	}

	public String getReadonlyPropertyOnSearch()
	{
		return readonlyPropertyOnSearch;
	}

	public String getReadonlyPropertyOnView()
	{
		return readonlyPropertyOnView;
	}

	public int getRows()
	{
		return rows;
	}

	private Button getSearchButton()
	{
		if(searchButton == null)
			searchButton = new Button(Config.getHandler().getProperties(getClass()), "searchButton");
		return searchButton;
	}

	protected String getStringValueFrom(Object obj)
		throws IntrospectionException, InvocationTargetException
	{
		if(obj == null)
			return null;
		else
			return getStringValueFrom(obj, Introspector.getPropertyValue(obj, property));
	}
	public String getStringValueFrom(Object obj, Object obj1)
		throws IntrospectionException, InvocationTargetException
	{
		return getStringValueFrom(null, obj, obj1);
	}
	public String getStringValueFrom(Object bp, Object obj, Object obj1)
		throws IntrospectionException, InvocationTargetException
	{
		if(obj != null && printProperty != null && obj1 != null)
		    if(printProperty.equals("label") && obj instanceof it.cnr.jada.util.action.CondizioneSempliceBulk && 
		       obj1 instanceof FieldProperty && bp != null)
		       obj1 = ((FieldProperty)obj1).getFindLabel(bp); 
		    else
			   obj1 = Introspector.getPropertyValue(obj1, printProperty);
		if(obj != null && keysProperty != null && obj1 != null)
		{
			Dictionary dictionary = (Dictionary)Introspector.getPropertyValue(obj, keysProperty);
			if(dictionary != null)
				obj1 = dictionary.get(obj1);
		}
		if(obj1 == null)
			return null;
		else
			return format(obj1);
	}

	public String getStyle()
	{
		return style;
	}
	@JsonIgnore
	public String getType()
	{
		if(propertyType == null)
			return null;
		else
			return propertyType.getName();
	}

	public Object getValueFrom(Object obj)
		throws IntrospectionException, InvocationTargetException
	{
		return Introspector.getPropertyValue(obj, getProperty());
	}

	public Object getValueFromActionContext(OggettoBulk oggettobulk, ActionContext actioncontext, String s)
		throws FillException
	{
		if(actioncontext instanceof HttpActionContext)
			return getValueFromRequest(oggettobulk, ((HttpActionContext)actioncontext).getRequest(), s);
		else
			return null;
	}

	public Object getValueFromRequest(OggettoBulk oggettobulk, ServletRequest servletrequest, String s)
		throws FillException
	{
		String s1 = mergePrefix(s, name);
		boolean flag = isMulti();
		if(!it.cnr.jada.util.Config.hasRequestParameter(servletrequest, s1))
		{
			if(inputType == 9 && it.cnr.jada.util.Config.hasRequestParameter(servletrequest, "input." + s1))
				return getValueFromText(oggettobulk, "false", s, false);
			if(flag && "true".equals(servletrequest.getParameter("input." + s1)))
				try
				{
					if(getPropertyType().isArray())
						return Array.newInstance(getPropertyType().getComponentType(), 0);
					else
						return Introspector.emptyCollection(getPropertyType());
				}
				catch(IntrospectionException introspectionexception)
				{
					throw new IntrospectionError(introspectionexception);
				}
			else
				return UNDEFINED_VALUE;
		}
		if(flag)
		{
			if(!"true".equals(servletrequest.getParameter("input." + s1)))
				return UNDEFINED_VALUE;
			String as[] = servletrequest.getParameterValues(s1);
			if(getPropertyType().isArray())
			{
				Object obj = Array.newInstance(getPropertyType().getComponentType(), as.length);
				for(int i = 0; i < as.length; i++)
					Array.set(obj, i, getValueFromText(oggettobulk, as[i], s, true));

				return obj;
			}
			try
			{
				Collection collection = Introspector.newCollection(getPropertyType());
				for(int j = 0; j < as.length; j++)
					collection.add(getValueFromText(oggettobulk, as[j], s, true));

				return collection;
			}
			catch(IntrospectionException introspectionexception1)
			{
				throw new IntrospectionError(introspectionexception1);
			}
		} else
		{
			return getValueFromText(oggettobulk, servletrequest.getParameter(s1), s, false);
		}
	}

	public Object getValueFromText(OggettoBulk oggettobulk, String s, String s1, boolean flag)
		throws FillException
	{
		Object obj;
		if(optionsProperty != null)
		{
			if("".equals(s))
				obj = null;
			else
				try
				{
					obj = getOptionFrom(oggettobulk, Integer.parseInt(s));
				}
				catch(NumberFormatException _ex)
				{
					obj = null;
				}
				catch(NoSuchElementException _ex)
				{
					obj = null;
				}
		} else
		{
			try
			{
				obj = parseStringValue(oggettobulk, s);
			}
			catch(ValidationParseException validationparseexception)
			{
				throw new FillException("Errore sul campo " + getLabel() + ": " + validationparseexception.getErrorMessage(), validationparseexception, s1, this, s);
			}
			catch(ParseException parseexception)
			{
				throw new FillException("Errore di formattazione sul campo " + getLabel(), parseexception, s1, this, s);
			}
		}
		if(!flag && obj != null)
		{
			Class class1 = getPropertyType(oggettobulk);
			if(!class1.isAssignableFrom(obj.getClass()))
				obj = Introspector.standardConvert(obj, class1);
		}
		return obj;
	}

	public boolean isCaseSensitiveSearch()
	{
		return caseSensitiveSearch;
	}

	public boolean isCompleteOnSave()
	{
		return completeOnSave;
	}

	public boolean isEnabledOnEdit()
	{
		return enabledOnEdit;
	}

	public boolean isEnabledOnFreeSearch()
	{
		return enabledOnFreeSearch;
	}

	public boolean isEnabledOnInsert()
	{
		return enabledOnInsert;
	}

	public boolean isEnabledOnSearch()
	{
		return enabledOnSearch;
	}

	public boolean isEnabledOnView()
	{
		return enabledOnView;
	}

	public boolean isGhost()
	{
		return ghost;
	}
	@JsonIgnore
	public boolean isMulti()
	{
		return getInputTypeIndex() == 6 && (getPropertyType().isArray() || java.util.Collection.class.isAssignableFrom(getPropertyType()));
	}
	@JsonIgnore
	public boolean isNoWrap()
	{
		return inputType == 5;
	}

	public boolean isNullable()
	{
		return nullable;
	}

	public boolean isReadonly(Object obj, int i)
	{
		try
		{
			if(inputType == 4)
				return true;
			if(obj == null)
				return true;
			if(property == null && inputType == 11 && readonlyProperty == null)
				return false;
			if(property == null && inputType == 11 && readonlyProperty != null && !((Boolean)Introspector.getPropertyValue(obj, readonlyProperty)).booleanValue())
				return false;				
			if(printProperty != null && (inputType == 7 || inputType == 8))
				return true;
			if(i != 4 && !Introspector.isPropertyWriteable(obj, property))
				return true;
			if(readonlyProperty != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyProperty)).booleanValue())
				return true;
			switch(i)
			{
			case 3: // '\003'
			default:
				break;

			case 1: // '\001'
				if(!isEnabledOnInsert())
					return true;
				if(readonlyPropertyOnInsert != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyPropertyOnInsert)).booleanValue())
					return true;
				break;

			case 2: // '\002'
				if(!isEnabledOnEdit())
					return true;
				if(readonlyPropertyOnEdit != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyPropertyOnEdit)).booleanValue())
					return true;
				break;

			case 5: // '\005'
				if(!isEnabledOnView())
					return true;
				if(readonlyPropertyOnView != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyPropertyOnView)).booleanValue())
					return true;
				break;

			case 0: // '\0'
				if(!isEnabledOnSearch())
					return true;
				if(readonlyPropertyOnSearch != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyPropertyOnSearch)).booleanValue())
					return true;
				break;

			case 4: // '\004'
				if(!isEnabledOnFreeSearch())
					return true;
				if(readonlyPropertyOnFreeSearch != null && ((Boolean)Introspector.getPropertyValue(obj, readonlyPropertyOnFreeSearch)).booleanValue())
					return true;
				break;
			}
			return false;
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			throw new IntrospectionError(invocationtargetexception);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
	}

	public static String mergePrefix(String s, String s1)
	{
		if(s == null)
		{
			return s1;
		} else
		{
			StringBuffer stringbuffer = new StringBuffer(s.length() + s1.length() + 1);
			stringbuffer.append(s);
			stringbuffer.append('.');
			stringbuffer.append(s1);
			return stringbuffer.toString();
		}
	}

	protected String mergeStyles(String s, String s1)
	{
		return JSPUtils.mergeStyles(s, s1);
	}

	protected String mergeStyles(String s, String s1, String s2)
	{
		return JSPUtils.mergeStyles(s, s1, s2);
	}

	private Object parse(String s)
		throws ParseException
	{
		if(editFormat == null)
			return parse(s, format);
		else
			return parse(s, editFormat);
	}

	private Object parse(String s, Format format1)
		throws ParseException
	{
		if(format1 == null)
			return s;
		if(s.trim().length() == 0)
			return null;
		else
			return format1.parseObject(s);
	}

	private Object parseStringValue(Object obj, String s)
		throws ParseException
	{
		try
		{
			Object obj1 = null;
			if(format != null)
			{
				obj1 = parse(s);
			} else
			{
				Class class1 = Introspector.getPropertyType(obj.getClass(), property);
				if(class1 == java.lang.String.class)
				{
					obj1 = "".equals(s) ? null : ((Object) (s));
				} else
				{
					Format format1 = (Format)editFormats.get(class1);
					if(format1 == null)
						obj1 = Introspector.standardParse(s, class1);
					else
						obj1 = format1.parseObject(s);
				}
			}
			return obj1;
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
	}

	void setBulkInfo(BulkInfo bulkinfo)
	{
		bulkInfo = bulkinfo;
	}

	public void setCaseSensitiveSearch(boolean flag)
	{
		caseSensitiveSearch = flag;
	}

	public void setCols(int i)
	{
		cols = i;
	}

	public void setColumnSet(String s)
	{
		columnSet = s;
	}

	public void setCommand(String s)
	{
		command = s;
	}

	public void setCompleteOnSave(boolean flag)
	{
		completeOnSave = flag;
	}

	public void setCRUDBusinessProcessName(String s)
	{
		CRUDBusinessProcessName = s;
	}

	public void setEditFormat(Format format1)
	{
		editFormat = format1;
	}

	public void setEditFormatName(String s)
	{
		editFormat = (Format)formats.get(editFormatName = s);
		if(editFormat == null)
			try
			{
				formats.put(editFormatName, editFormat = (Format)Class.forName(editFormatName).newInstance());
			}
			catch(ClassNotFoundException _ex)
			{
				throw new RuntimeException("Il formato " + editFormatName + " non esiste");
			}
			catch(InstantiationException _ex)
			{
				throw new RuntimeException("Non \350 possibile istanziare il formato " + editFormatName);
			}
			catch(IllegalAccessException _ex)
			{
				throw new RuntimeException("Non \350 possibile istanziare il formato " + editFormatName + " (Illegal access exception)");
			}
			catch(ClassCastException _ex)
			{
				throw new RuntimeException("Il formato " + editFormatName + " non \350 una sottoclasse di java.text.Format");
			}
	}

	public void setEnabledOnEdit(boolean flag)
	{
		enabledOnEdit = flag;
	}

	public void setEnabledOnFreeSearch(boolean flag)
	{
		enabledOnFreeSearch = flag;
	}

	public void setEnabledOnInsert(boolean flag)
	{
		enabledOnInsert = flag;
	}

	public void setEnabledOnSearch(boolean flag)
	{
		enabledOnSearch = flag;
	}

	public void setEnabledOnView(boolean flag)
	{
		enabledOnView = flag;
	}

	public void setFindProperty(String s)
	{
		findProperty = s;
	}

	public void setFormat(Format format1)
	{
		format = format1;
	}

	public void setFormatName(String s)
	{
		format = (Format)formats.get(formatName = s);
		if(format == null)
			try
			{
				formats.put(s, format = (Format)Class.forName(s).newInstance());
			}
			catch(ClassNotFoundException _ex)
			{
				throw new RuntimeException("Il formato " + s + " non esiste");
			}
			catch(InstantiationException _ex)
			{
				throw new RuntimeException("Non \350 possibile istanziare il formato " + s);
			}
			catch(IllegalAccessException _ex)
			{
				throw new RuntimeException("Non \350 possibile istanziare il formato " + s + " (Illegal access exception)");
			}
			catch(ClassCastException _ex)
			{
				throw new RuntimeException("Il formato " + s + " non \350 una sottoclasse di java.text.Format");
			}
		if(editFormat == null)
			editFormat = (Format)editFormats.get(s);
	}

	public void setFormName(String s)
	{
		formName = s;
	}

	public void setFreeSearchSet(String s)
	{
		freeSearchSet = s;
	}

	public void setGhost(boolean flag)
	{
		ghost = flag;
	}

	public void setHref(String s)
	{
		href = s;
	}

	public void setImg(String s)
	{
		img = s;
	}

	public void setInputSize(int i)
	{
		inputSize = i;
	}

	public void setInputType(String s)
	{
		for(int i = 0; i < inputTypeNames.length; i++)
			if(inputTypeNames[i].equalsIgnoreCase(s))
			{
				inputType = i;
				return;
			}

	}

	public void setKeysProperty(String s)
	{
		keysProperty = s;
	}

	public void setLabel(String s)
	{
		label = s;
	}

	public void setLabelStyle(String s)
	{
		labelStyle = s;
	}

	public void setLayout(String s)
	{
		if("HORIZONTAL".equalsIgnoreCase(s))
			layout = 1;
		if("VERTICAL".equalsIgnoreCase(s))
			layout = 0;
	}

	public void setMaxLength(int i)
	{
		maxLength = i;
	}

	public void setName(String s)
	{
		name = s;
	}

	public void setNullable(boolean flag)
	{
		nullable = flag;
	}

	public void setOptionsProperty(String s)
	{
		optionsProperty = s;
	}

	public void setOrdinalPosition(int i)
	{
		ordinalPosition = i;
	}

	public void setPrintProperty(String s)
	{
		printProperty = s;
	}

	public void setProperty(String s)
	{
		property = s;
	}

	public void setReadonlyProperty(String s)
	{
		readonlyProperty = s;
	}

	public void setReadonlyPropertyOnEdit(String s)
	{
		readonlyPropertyOnEdit = s;
	}

	public void setReadonlyPropertyOnFreeSearch(String s)
	{
		readonlyPropertyOnFreeSearch = s;
	}

	public void setReadonlyPropertyOnInsert(String s)
	{
		readonlyPropertyOnInsert = s;
	}

	public void setReadonlyPropertyOnSearch(String s)
	{
		readonlyPropertyOnSearch = s;
	}

	public void setReadonlyPropertyOnView(String s)
	{
		readonlyPropertyOnView = s;
	}

	public void setRows(int i)
	{
		rows = i;
	}

	private Object setStringValueOn(Object obj, String s)
		throws ParseException, IntrospectionException, InvocationTargetException
	{
		Object obj1 = parseStringValue(obj, s);
		Introspector.setPropertyValue(obj, name, obj1);
		return obj1;
	}

	public void setStyle(String s)
	{
		style = s;
	}

	public void setType(String s)
		throws ClassNotFoundException
	{
		if(s != null)
			propertyType = Class.forName(s);
	}

	public void setValueIn(Object obj, Object obj1)
		throws InvocationTargetException, IntrospectionException
	{
		Introspector.setPropertyValue(obj, property, obj1);
	}

	public String toString()
	{
		try
		{
			StringWriter stringwriter = new StringWriter();
			XmlWriter xmlwriter = new XmlWriter(stringwriter);
			xmlwriter.openTag("fieldProperty");
			xmlwriter.printAttribute("name", name, null);
			xmlwriter.printAttribute("property", property, null);
			xmlwriter.closeLastTag();
			return stringwriter.getBuffer().toString();
		}
		catch(IOException _ex)
		{
			return super.toString();
		}
	}

	protected void writeButton(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException, ServletException
	{
		JSPUtils.button(jspwriter, img, img, label, href, style, !flag , accessKey);
	}

	protected void writeCheckBox(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		String s3 = mergePrefix(s2, name);
		if(!flag)
		{
			jspwriter.print("<input type=\"hidden\" name=\"input.");
			jspwriter.print(s3);
			jspwriter.print("\" value=\"");
			jspwriter.print(obj1 != null ? obj1.toString() : "null");
			jspwriter.print("\">");
		}
		jspwriter.print("<input type=\"checkbox\" name=\"");
		jspwriter.print(s3);
		jspwriter.print('"');
		if(flag || obj == null)
			jspwriter.print(" disabled");
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		jspwriter.print(" onfocus=\"focused(this)\"");
		jspwriter.print(" value=\"true\"");
		if((obj1 instanceof Boolean) && ((Boolean)obj1).booleanValue())
			jspwriter.print(" checked");
		if(command != null && i != 4)
		{
			jspwriter.print(" onclick=\"submitForm('");
			jspwriter.print(command);
			jspwriter.print("')\"");
		}
		jspwriter.print(">");
	}

	protected void writeCRUDTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.println("<span>");
		if(formName != null)
		{
			jspwriter.println("<table>");
			BulkInfo.getBulkInfo(getPropertyType(getBulkInfo().getBulkClass())).writeForm(jspwriter, obj, formName, null, null, mergePrefix(s2, formName), i, flag, fieldvalidationmap);
			jspwriter.println("</table>");
		}
		getCrudButton().write(jspwriter, !flag, "javascript:submitForm('doCRUD(" + mergePrefix(s2, getName()) + ")')");
		jspwriter.println("</span>");
	}

	private void writeException(JspWriter jspwriter, Throwable throwable)
		throws IOException
	{
		jspwriter.println("<textarea style=\"background-color:red\">");
		throwable.printStackTrace(new PrintWriter(jspwriter));
		jspwriter.println("</textarea>");
	}

	protected String writeFillExceptionStyle(JspWriter jspwriter, FieldValidationException fieldvalidationexception)
		throws IOException
	{
		if(fieldvalidationexception != null)
		{
			jspwriter.print(" onfocus=\"focused(this); setTempMessage(null,'");
			jspwriter.print(fieldvalidationexception.getMessage());
			jspwriter.print("')\"");
			jspwriter.print(" onblur=\"restoreMessage()\"");
			return "background: coral";
		} else
		{
			return null;
		}
	}

	protected void writeForm(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		if(formName != null)
		{
			jspwriter.println("<table class=\"Panel\">");
			BulkInfo.getBulkInfo(getPropertyType(getBulkInfo().getBulkClass())).writeForm(jspwriter, obj1, formName, null, null, mergePrefix(s2, name), i, flag, 0, true, fieldvalidationmap);
			jspwriter.println("</table>");
		}
	}

	protected void writeHidden(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.print("<input type=hidden name=\"");
		jspwriter.print(mergePrefix(s2, name));
		jspwriter.print('"');
		if(flag || obj == null)
			jspwriter.print(" disabled");
		writeInputStyle(jspwriter, s, style, obj, obj1);
		obj1 = getStringValueFrom(obj, obj1);
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		if(obj1 != null)
		{
			jspwriter.print("\" value=\"");
			jspwriter.print(encodeHtmlText(obj1));
			jspwriter.print("\"");
		}
		jspwriter.print(">");
	}
	public void writeInput(JspWriter jspwriter, Object obj, Object obj1, boolean flag, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException
	{
		writeInput((Object)null, jspwriter, obj, obj1, flag, s, s1, s2, 
					i, fieldvalidationmap);
	}
	public void writeInput(Object bp, JspWriter jspwriter, Object obj, Object obj1, boolean flag, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException
	{
		try
		{
			if(!flag||isEnabledOnView())
				flag = isReadonly(obj, i);
			switch(inputType)
			{
			case 7: // '\007'
				writeText(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 2: // '\002'
				writePassword(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 8: // '\b'
				writeTextArea(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 6: // '\006'
				writeSelect(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 3: // '\003'
				writeRadioGroup(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 9: // '\t'
				writeCheckBox(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 4: // '\004'
				writeReadonlyText(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 5: // '\005'
				writeSearchTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 10: // '\n'
				writeCRUDTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 1: // '\001'
				writeHidden(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 11: // '\013'
				writeButton(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 12: // '\f'
				writeTable(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			case 13: // '\r'
				writeForm(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;
                
			case FILE:
				writeFile(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;                

			case DESCTOOL: 
				writeDescTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				break;

			default:
				if(optionsProperty != null)
					writeSelect(bp, jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
				else
					writeReadonlyText(jspwriter, obj, false, obj1, s, s1, s2, i, fieldvalidationmap);
				break;
			}
		}
		catch(IOException ioexception)
		{
			throw ioexception;
		}
		catch(Throwable throwable)
		{
			writeException(jspwriter, throwable);
		}
	}
	public void writeInput(JspWriter jspwriter, Object obj, boolean flag, String s, String s1, String s2, int i, 
			FieldValidationMap fieldvalidationmap)
		throws IOException
	{
		writeInput((Object)null, jspwriter, obj, flag, s, s1, s2, i, 
					fieldvalidationmap);
	}
	public void writeInput(Object bp, JspWriter jspwriter, Object obj, boolean flag, String s, String s1, String s2, int i, 
			FieldValidationMap fieldvalidationmap)
		throws IOException
	{
		try
		{
			writeInput(bp, jspwriter, obj, Introspector.getPropertyValue(obj, property), flag, s, s1, s2, i, fieldvalidationmap);
		}
		catch(IOException ioexception)
		{
			throw ioexception;
		}
		catch(Throwable throwable)
		{
			writeException(jspwriter, throwable);
		}
	}

	protected void writeInputStyle(JspWriter jspwriter, String s, String s1, Object obj, Object obj1)
		throws IOException
	{
		writeStyle(jspwriter, s != null ? s : "FormInput", JSPUtils.mergeStyles(s1, getFormatStyle(obj)), obj);
	}
	public void writeLabel(JspWriter jspwriter, Object obj)
		throws IOException
	{
		writeLabel(null, jspwriter, obj);
	}

	public void writeLabel(Object bp, JspWriter jspwriter, Object obj)
		throws IOException
	{
		writeLabel(bp, jspwriter, obj, null);
	}
	public void writeLabel(JspWriter jspwriter, Object obj, String s)throws IOException{
		writeLabel(null, jspwriter, obj, s);
	}
	public void writeLabel(Object bp, JspWriter jspwriter, Object obj, String s)
		throws IOException
	{
		if(inputType == 11)
		{
			return;
		} else
		{
			jspwriter.print("<span");
			writeLabelStyle(jspwriter, s, labelStyle, obj);
			jspwriter.print(">");
			if (bp != null){
				try {
					String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
					jspwriter.print(encodeHtmlText(dLabel));
				}catch (InvocationTargetException e) {
					jspwriter.print(encodeHtmlText(label));
				} catch (IntrospectionException e) {
					jspwriter.print(encodeHtmlText(label));
				}
			}else
			  jspwriter.print(encodeHtmlText(label));
			jspwriter.print("</span>");
			return;
		}
	}

	protected void writeLabelStyle(JspWriter jspwriter, String s, String s1, Object obj)
		throws IOException
	{
		writeStyle(jspwriter, s != null ? s : "FormLabel", s1, obj);
	}

	protected void writeMandatoryStyle(JspWriter jspwriter, int i)
		throws IOException
	{
		if(!nullable && (i == 2 || i == 1))
			jspwriter.print(" style=\"background: #F5F5DC\"");
	}

	protected void writePassword(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.print("<input type=password name=\"");
		jspwriter.print(mergePrefix(s2, name));
		jspwriter.print('"');
		writeInputStyle(jspwriter, s, style, obj, obj1);
		obj1 = getStringValueFrom(obj, obj1);
		if(flag || obj == null)
			jspwriter.print(" disabled style=\"background-color:silver\"");
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		if(obj1 != null)
		{
			jspwriter.print(" value=\"");
			jspwriter.print(encodeHtmlText(obj1));
			jspwriter.print("\"");
		}
		if(maxLength > 0)
		{
			jspwriter.print(" maxLength=");
			jspwriter.print(maxLength);
		}
		if(inputSize > 0)
		{
			jspwriter.print(" size=");
			jspwriter.print(inputSize);
		}
		jspwriter.print(" onfocus=\"focused(this)\"");
		if(!nullable)
			jspwriter.print(" style=\"background: #AFEEEE\"");
		jspwriter.print(">");
	}

	protected void writeRadioGroup(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.print("<div");
		writeInputStyle(jspwriter, s, style, obj, obj1);
		jspwriter.println('>');
		if(optionsProperty != null)
		{
			int j = 0;
			for(Enumeration enumeration = getOptionsFrom(obj); enumeration.hasMoreElements();)
			{
				Object obj2 = enumeration.nextElement();
				jspwriter.print("<input type=\"radio\" name=\"");
				jspwriter.print(mergePrefix(s2, name));
				jspwriter.print('"');
				if(flag || obj == null)
					jspwriter.print(" disabled");
				if(s1 != null)
				{
					jspwriter.print(' ');
					jspwriter.print(s1);
				}
				jspwriter.print(" value=\"");
				jspwriter.print(j++);
				jspwriter.print('"');
				if(obj2.equals(obj1))
					jspwriter.print(" checked");
				jspwriter.print(" onfocus=\"focused(this)\"");
				jspwriter.print(" onclick=\"cancelBubble(event)\"");
				jspwriter.print(">");
				jspwriter.print(encodeHtmlText(getStringValueFrom(obj, obj2)));
				if(layout != 1)
					jspwriter.print("<br>");
			}

		} else
		if(keysProperty != null)
		{
			Dictionary dictionary = getKeysFrom(obj);
			if(dictionary != null)
			{
				for(Enumeration enumeration1 = dictionary.keys(); enumeration1.hasMoreElements();)
				{
					Object obj3 = enumeration1.nextElement();
					jspwriter.print("<input type=\"radio\" name=\"");
					jspwriter.print(mergePrefix(s2, name));
					jspwriter.print('"');
					if(flag || obj == null)
						jspwriter.print(" disabled");
					if(s1 != null)
					{
						jspwriter.print(' ');
						jspwriter.print(s1);
					}
					jspwriter.print(" value=\"");
					jspwriter.print(format(obj3));
					jspwriter.print('"');
					if(obj3.equals(obj1))
						jspwriter.print(" checked");
					jspwriter.print(" onfocus=\"focused(this)\"");
					jspwriter.print(" onclick=\"cancelBubble(event)\"");
					jspwriter.print(">");
					jspwriter.print(encodeHtmlText(format(dictionary.get(obj3))));
					if(layout != 1)
						jspwriter.print("<br>");
				}

			}
		}
		jspwriter.print("</div>");
	}

	public void writeReadonlyText(JspWriter jspwriter, Object obj, String s, String s1)
		throws IOException
	{
		try
		{
			String s2 = getStringValueFrom(obj);
			jspwriter.print("<span");
			writeInputStyle(jspwriter, s, style, obj, s2);
			if(s1 != null)
			{
				jspwriter.print(' ');
				jspwriter.print(s1);
			}
			jspwriter.print(">");
			jspwriter.print(encodeHtmlText(s2));
			jspwriter.print("</span>");
		}
		catch(Exception _ex)
		{
			jspwriter.print("&nbsp;");
		}
	}

	protected void writeReadonlyText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		writeText(jspwriter, obj, true, obj1, s, s1, s2, i, fieldvalidationmap);
	}

	protected void writeSearchTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.println("<span>");
		if(formName != null)
		{
			jspwriter.println("<table cellspacing=\"0\" cellspacing=\"0\" border=\"0\"><tr><td>");
			jspwriter.println("<table cellspacing=\"0\" cellspacing=\"0\" border=\"0\">");
			BulkInfo.getBulkInfo(getPropertyType(getBulkInfo().getBulkClass())).writeForm(jspwriter, obj1, formName, null, null, mergePrefix(s2, name), 0, flag || (obj1 instanceof OggettoBulk) && ((OggettoBulk)obj1).getCrudStatus() == 5, 1, false, fieldvalidationmap);
			jspwriter.println("</table>");
			jspwriter.println("</td><td>");
		}
		getNewButton().write(jspwriter, !flag, "javascript:submitForm('doBlankSearch(" + mergePrefix(s2, getName()) + ")')");
		getSearchButton().write(jspwriter, !flag, "javascript:submitForm('doSearch(" + mergePrefix(s2, getName()) + ")')");
		getFreeSearchButton().write(jspwriter, !flag, "javascript:submitForm('doFreeSearch(" + mergePrefix(s2, getName()) + ")')");
		if (getCRUDBusinessProcessName() != null)
			getCrudButton().write(jspwriter, !flag, "javascript:submitForm('doCRUD(" + mergePrefix(s2, getName()) + ")')");
		if(formName != null)
		{
			jspwriter.println("</td></tr>");
			jspwriter.println("</table>");
		}
		jspwriter.println("</span>");
	}
	protected void writeDescTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		jspwriter.println("<span>");
		writeText(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
		jspwriter.println("&nbsp;");
		writeImgDesc(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
		jspwriter.println("</span>");
	}
	protected void writeImgDesc(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		if (Introspector.getPropertyValue(obj, getDescProperty()) != null){
		  jspwriter.print("<img src=\"img/zoom16.gif\" ");
		  jspwriter.print("title=\""+ JSPUtils.encodeHtmlString((String)Introspector.getPropertyValue(obj, getDescProperty()))+"\">");
		}		  
	}
	protected void writeSelect(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		writeSelect(null, jspwriter, obj, flag, obj1, s, s1, s2, 
					i, fieldvalidationmap);
	}
	protected void writeSelect(Object bp, JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		boolean flag1 = false;
		String s3 = getStyle();
		StringBuffer stringbuffer = new StringBuffer();
		getPropertyType();
		Object obj2 = null;
		String s4 = mergePrefix(s2, name);
		boolean flag2 = isMulti();
		if(flag2)
			if(obj1 == null)
				obj2 = Collections.EMPTY_LIST;
			else
			if(obj1 instanceof Collection)
				obj2 = (Collection)obj1;
			else
			if(obj1.getClass().isArray())
				obj2 = Arrays.asList((Object[])obj1);
		if(!flag2 && (obj1 == null || isNullable()))
		{
			stringbuffer.append("<option value=\"\"");
			if(obj1 == null)
				stringbuffer.append(" selected");
			stringbuffer.append("></option>");
		}
		if(optionsProperty != null)
		{
			int j = 0;
			Enumeration enumeration = getOptionsFrom(obj);
			flag1 = enumeration != null;
			if(flag1)
				for(; enumeration.hasMoreElements(); stringbuffer.append("</option>"))
				{
					Object obj3 = enumeration.nextElement();
					stringbuffer.append("<option value=\"");
					stringbuffer.append(j++);
					stringbuffer.append('"');
					if(flag2 && ((Collection) (obj2)).contains(obj3) || !flag2 && obj3.equals(obj1))
						stringbuffer.append(" selected");
					stringbuffer.append(">");
					stringbuffer.append(encodeHtmlText(getStringValueFrom(bp, obj, obj3)));
				}

		} else
		if(keysProperty != null)
		{
			Dictionary dictionary = getKeysFrom(obj);
			flag1 = dictionary != null;
			if(flag1)
			{
				for(Enumeration enumeration1 = dictionary.keys(); enumeration1.hasMoreElements(); stringbuffer.append("</option>"))
				{
					Object obj4 = enumeration1.nextElement();
					stringbuffer.append("<option value=\"");
					stringbuffer.append(format(obj4));
					stringbuffer.append('"');
					if(flag2 && ((Collection) (obj2)).contains(obj4) || !flag2 && obj4.equals(obj1))
						stringbuffer.append(" selected");
					stringbuffer.append(">");
					stringbuffer.append(encodeHtmlText(format(dictionary.get(obj4))));
				}

			}
		}
		if(flag2 && !flag)
		{
			jspwriter.print("<input type=\"hidden\" name=\"input.");
			jspwriter.print(s4);
			jspwriter.print("\">");
		}
		jspwriter.print("<select name=\"");
		jspwriter.print(s4);
		jspwriter.print('"');
		if(flag || obj == null || !flag1)
		{
			jspwriter.print(" disabled");
			s3 = mergeStyles(s3, "background-color:ButtonFace");
		} else
		{
			writeMandatoryStyle(jspwriter, i);
		}
		writeInputStyle(jspwriter, s, s3, obj, obj1);
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		jspwriter.print(" onfocus=\"focused(this)\"");
		StringBuffer stringbuffer1 = new StringBuffer();
		jspwriter.print(" onclick=\"cancelBubble(event)\"");
		if(flag2)
		{
			jspwriter.print("multiple=\"true\"");
			stringbuffer1.append("form.elements['input.");
			stringbuffer1.append(s4);
			stringbuffer1.append("'].value=true;");
		}
		if(command != null && !flag && i != 4)
		{
			stringbuffer1.append("submitForm('");
			stringbuffer1.append(command);
			stringbuffer1.append("');");
		}
		if(stringbuffer1.length() > 0)
		{
			jspwriter.print(" onchange=\"");
			jspwriter.print(stringbuffer1);
			jspwriter.print('"');
		}
		jspwriter.println('>');
		if(flag1)
			jspwriter.print(stringbuffer);
		jspwriter.print("</select>");
	}

	protected void writeStyle(JspWriter jspwriter, String s, String s1, Object obj)
		throws IOException
	{
		if(s != null)
		{
			jspwriter.print(" class=\"");
			jspwriter.print(s);
			jspwriter.print('"');
		}
		if(s1 != null)
		{
			jspwriter.print(" style=\"");
			jspwriter.print(s1);
			jspwriter.print('"');
		}
	}

	protected void writeTable(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		Enumeration enumeration = getOptionsFrom(obj);
		Table table = new Table(mergePrefix(s2, name), BulkInfo.getBulkInfo(getPropertyType()).getColumnFieldPropertyDictionary(columnSet));
		table.setMultiSelection(false);
		table.setSingleSelection(true);
		table.setEditableOnFocus(false);
		table.setSelectedElement(obj1);
		table.setRows(enumeration);
		if(s1 != null)
		{
			int j = s1.indexOf("onchange=\"") + 10;
			if(j >= 10)
				table.setOnselect(s1.substring(j, s1.indexOf('"', j)));
		}
		table.writeScrolledTable(jspwriter, "100%", "100px", fieldvalidationmap, 0);
	}

	protected void writeText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		FieldValidationException fieldvalidationexception = fieldvalidationmap.get(s2, name);
		if(fieldvalidationexception != null)
			obj1 = fieldvalidationexception.getText();
		jspwriter.print("<input type=text name=\"");
		String s3 = mergePrefix(s2, name);
		jspwriter.print(s3);
		jspwriter.print('"');
		if(command != null && !flag && i != 4)
			jspwriter.print(" onfocus=\"modalInputFocused(this)\"  onchange=\"modalInputChanged(this)\"");
		String s4 = getStyle();
		if(fieldvalidationexception == null)
			obj1 = getStringValueFrom(obj, obj1);
		if(printProperty != null)
			flag = true;
		s4 = mergeStyles(s4, getMandatoryStyle(i));
		s4 = mergeStyles(s4, writeFillExceptionStyle(jspwriter, fieldvalidationexception));
		if(flag || obj == null)
		{
			jspwriter.print(" readonly");
			s4 = mergeStyles(s4, "background-color:transparent;color:BlackText;");
		}
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		if(href != null)
		{
			jspwriter.print(" onchange=\"");
			jspwriter.print(href);
			jspwriter.print('"');
		}
		if(obj1 != null)
		{
			jspwriter.print(" value=\"");
			jspwriter.print(encodeHtmlText(obj1));
			jspwriter.print("\"");
		}
		//int j = maxLength;
		int j = getMaxLength(obj);
		writeInputStyle(jspwriter, s, s4, obj, obj1);
		if(obj != null && j == 0 && getPropertyType((OggettoBulk)obj) == java.lang.String.class || j == 0 && getPropertyType() == java.lang.String.class)
			try
			{
				SQLPersistentInfo sqlpersistentinfo = (SQLPersistentInfo)BeanIntrospector.getSQLInstance().getPersistentInfo(getBulkInfo().getBulkClass());
				if(sqlpersistentinfo != null)
				{
					ColumnMapping columnmapping = sqlpersistentinfo.getDefaultColumnMap().getMappingForProperty(property);
					if(columnmapping != null)
						j = columnmapping.getColumnSize();
				}
			}
			catch(it.cnr.jada.persistency.IntrospectionException _ex) { }
		/* -- add by Stentella - 16/7/04 */        
			   String sqlt = "";

			   if ((sqlt = getBulkInfo().getFieldProperty(name).getFormatName())!=null){
				   if (sqlt.equalsIgnoreCase("timestamp"))
						   jspwriter.print(" onKeyUp=\"DateTimeFormat(this,this.value,event,false,'3')\" onBlur=\"DateTimeFormat(this,this.value,event,true,'3'); modalInputChanged(this)\"");
				   else if (sqlt.equalsIgnoreCase("date_short"))
						   jspwriter.print(" onKeyUp=\"DateFormat(this,this.value,event,false,'3')\" onBlur=\"DateFormat(this,this.value,event,true,'3'); modalInputChanged(this)\"");
			   }

		if(j > 0)
		{
			jspwriter.print(" maxLength=");
			jspwriter.print(j);
		}
		int k = getInputSize(obj);
		//if(inputSize > 0)
		if(k > 0)
		{
			jspwriter.print(" size=");
			jspwriter.print(k);
		}
		jspwriter.print(" onclick=\"cancelBubble(event)\"");
		jspwriter.print(">");
		if(command != null && !flag && i != 4)
		{
			getConfirmButton().write(jspwriter, !flag, "confirmModalInputChange(this,'" + s3 + "','" + command + "')", "name = \"" + s3 + ".confirm\" onfocus=\"modalInputButtonFocused(this,'" + s3 + "')\"");
			getCancelButton().write(jspwriter, !flag, "cancelModalInputChange(this,'" + s3 + "')", "name = \"" + s3 + ".cancel\" onfocus=\"modalInputButtonFocused(this,'" + s3 + "')\"");
		}
	}
    
	protected void writeFile(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		FieldValidationException fieldvalidationexception = fieldvalidationmap.get(s2, name);
		if(fieldvalidationexception != null)
			obj1 = fieldvalidationexception.getText();
		jspwriter.print("<input type=file name=\"");
		String s3 = mergePrefix(s2, name);
		jspwriter.print(s3);
		jspwriter.print('"');
		if(command != null && !flag && i != 4)
			jspwriter.print(" onfocus=\"modalInputFocused(this)\"  onchange=\"modalInputChanged(this)\"");
		String s4 = getStyle();
		if(fieldvalidationexception == null)
			obj1 = getStringValueFrom(obj, obj1);
		if(printProperty != null)
			flag = true;
		s4 = mergeStyles(s4, getMandatoryStyle(i));
		s4 = mergeStyles(s4, writeFillExceptionStyle(jspwriter, fieldvalidationexception));
		if(flag || obj == null)
		{
			jspwriter.print(" readonly");
			s4 = mergeStyles(s4, "background-color:transparent;color:BlackText;");
		}
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		if(href != null)
		{
			jspwriter.print(" onchange=\"");
			jspwriter.print(href);
			jspwriter.print('"');
		}
		if(obj1 != null)
		{
			jspwriter.print(" value=\"");
			jspwriter.print(encodeHtmlText(obj1));
			jspwriter.print("\"");
		}
		int j = maxLength;
		writeInputStyle(jspwriter, s, s4, obj, obj1);
		if(obj != null && j == 0 && getPropertyType((OggettoBulk)obj) == java.lang.String.class || j == 0 && getPropertyType() == java.lang.String.class)
			try
			{
				SQLPersistentInfo sqlpersistentinfo = (SQLPersistentInfo)BeanIntrospector.getSQLInstance().getPersistentInfo(getBulkInfo().getBulkClass());
				if(sqlpersistentinfo != null)
				{
					ColumnMapping columnmapping = sqlpersistentinfo.getDefaultColumnMap().getMappingForProperty(property);
					if(columnmapping != null)
						j = columnmapping.getColumnSize();
				}
			}
			catch(it.cnr.jada.persistency.IntrospectionException _ex) { }
		if(j > 0)
		{
			jspwriter.print(" maxLength=");
			jspwriter.print(j);
		}
		if(inputSize > 0)
		{
			jspwriter.print(" size=");
			jspwriter.print(inputSize);
		}
		jspwriter.print(" onclick=\"cancelBubble(event)\"");
		jspwriter.print(">");
		if(command != null && !flag && i != 4)
		{
			getConfirmButton().write(jspwriter, !flag, "confirmModalInputChange(this,'" + s3 + "','" + command + "')", "name = \"" + s3 + ".confirm\" onfocus=\"modalInputButtonFocused(this,'" + s3 + "')\"");
			getCancelButton().write(jspwriter, !flag, "cancelModalInputChange(this,'" + s3 + "')", "name = \"" + s3 + ".cancel\" onfocus=\"modalInputButtonFocused(this,'" + s3 + "')\"");
		}
	}    

	protected void writeTextArea(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, 
			int i, FieldValidationMap fieldvalidationmap)
		throws IOException, IntrospectionException, InvocationTargetException
	{
		FieldValidationException fieldvalidationexception = fieldvalidationmap.get(s2, name);
		String s3 = getStyle();
		if(fieldvalidationexception != null)
			obj1 = fieldvalidationexception.getText();
		jspwriter.print("<textarea name=\"");
		jspwriter.print(mergePrefix(s2, name));
		jspwriter.print('"');
		if(printProperty != null)
			flag = true;
		s3 = mergeStyles(s3, getMandatoryStyle(i));
		s3 = mergeStyles(s3, writeFillExceptionStyle(jspwriter, fieldvalidationexception));
		if(flag || obj == null)
		{
			jspwriter.print(" readonly");
			s3 = mergeStyles(s3, "background-color:transparent;color:GrayText;");
		}
		writeInputStyle(jspwriter, s, s3, obj, obj1);
		if(fieldvalidationexception == null)
			obj1 = getStringValueFrom(obj, obj1);
		if(s1 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s1);
		}
		if(cols > 0)
		{
			jspwriter.print(" cols=\"");
			jspwriter.print(cols);
			jspwriter.print("\"");
		}
		if(rows > 0)
		{
			jspwriter.print(" rows=\"");
			jspwriter.print(rows);
			jspwriter.print("\"");
		}
		jspwriter.print(" onfocus=\"focused(this)\"");
		jspwriter.print(" onclick=\"cancelBubble(event)\"");
		jspwriter.print(">");
		if(obj1 != null)
			jspwriter.print(encodeHtmlText(obj1));
		jspwriter.print("</textarea>");
	}

	public void writeTo(XmlWriter xmlwriter)
		throws IOException
	{
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

	private int getMaxLength(Object obj) throws InvocationTargetException {
		if (obj==null) return maxLength;
	    try {
	    	return (Integer)it.cnr.jada.util.Introspector.getPropertyValue(obj, getProperty()+"MaxLength");
	    } catch(Exception exception) {
	        return maxLength;
	    }
    }
	private int getInputSize(Object obj) throws InvocationTargetException {
		if (obj==null) return inputSize;
	    try {
	    	return (Integer)it.cnr.jada.util.Introspector.getPropertyValue(obj, getProperty()+"InputSize");
	    } catch(Exception exception) {
	        return inputSize;
	    }
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