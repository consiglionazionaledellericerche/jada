/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.BulkInfoAnnotation;
import it.cnr.jada.bulk.annotation.ColumnSetAnnotation;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormAnnotation;
import it.cnr.jada.bulk.annotation.FreeSearchSetAnnotation;
import it.cnr.jada.bulk.annotation.PrintFormAnnotation;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.OrderedHashtable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bzdyl.ejb3.criteria.Criteria;
import net.bzdyl.ejb3.criteria.CriteriaFactory;
import net.bzdyl.ejb3.criteria.restrictions.Restrictions;
import net.bzdyl.ejb3.criteria.restrictions.SimpleExpression;

/**
 * Ad ogni classe di oggetti bulk è associata una istanza di BulkInfo. 
 * In essa sono descritte le seguenti proprietà: fieldProperties: elenco di FieldProperty 
 * che descrivono gli input field per questa classe di oggetti bulk; 
 * formFieldProperties: elenco di FieldProperty da usare nelle form HTML; 
 * findFieldProperties: elenco di FieldProperty da usare nella ricerca generica; 
 * columnFiledProperties: elenco di FieldProperty da usare per compilare elenchi tabulari. 
 * I BulkInfo vengono costruiti leggendo un file XML. 
 * Tali file devono essere nella classpath ed avere il nome costruito sulla base 
 * della classe che descrivono mediante le seguenti regole: estensione ".bixml"; 
 * nome derivato dal nome completo della classe, sostituendo '.' con '_' e aggiungendo il suffisso "Info" ; 
 * ad esempio alla classe it.jada.bulk.UserInfo deve corrispondere il file "it_jada_bulk_UserInfoInfo.xml".
 * La strututtura di tali file deve rispettare la seguente sintassi:
 * See Also:Serialized Form
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class BulkInfo implements Serializable{

	private static final long serialVersionUID = 1L;
    private OrderedHashtable fieldProperties;
    private Map<String, Object> forms;
    private Map<String, Object> columnSets;
    private Map<String, Object> freeSearchSets;
    private Map<String, Object> printForms;
    private FieldPropertySet findFieldProperties;
    private Class<?> bulkClass;
    private static final Hashtable<Class<?>, BulkInfo> bulkInfos = new Hashtable<Class<?>, BulkInfo>();
    private String shortDescription;
    private String longDescription;
    private HashMap<String, FieldProperty> fieldPropertiesByProperty;
    /**
     * Costruttore pubblico
     */
	public BulkInfo(){
        forms = new HashMap<String, Object>();
        columnSets = new HashMap<String, Object>();
        freeSearchSets = new HashMap<String, Object>();
        printForms = new HashMap<String, Object>();
        fieldPropertiesByProperty = new HashMap<String, FieldProperty>();
    }
    /**
     * Costruttore privato
     */
    @SuppressWarnings("unchecked")
	protected BulkInfo(Class class1){
        forms = new HashMap();
        columnSets = new HashMap();
        freeSearchSets = new HashMap();
        printForms = new HashMap();
        fieldPropertiesByProperty = new HashMap();
        if(!OggettoBulk.class.isAssignableFrom(class1))
            throw new RuntimeException("Tentativo di costruire un BulkInfo per una classe che non \350 un OggettoBulk");
        bulkClass = class1;
        if(class1 != OggettoBulk.class){
            BulkInfo bulkinfo = internalGetBulkInfo(class1.getSuperclass());
            fieldProperties = (OrderedHashtable)bulkinfo.fieldProperties.clone();
            fieldPropertiesByProperty = (HashMap)bulkinfo.fieldPropertiesByProperty.clone();
            findFieldProperties = (FieldPropertySet)bulkinfo.findFieldProperties.clone(this);
            FieldPropertySet fieldpropertyset;
            for(Iterator iterator = bulkinfo.columnSets.values().iterator(); iterator.hasNext(); columnSets.put(fieldpropertyset.getName(), fieldpropertyset.clone(this)))
                fieldpropertyset = (FieldPropertySet)iterator.next();

            FieldPropertySet fieldpropertyset1;
            for(Iterator iterator1 = bulkinfo.forms.values().iterator(); iterator1.hasNext(); forms.put(fieldpropertyset1.getName(), fieldpropertyset1.clone(this)))
                fieldpropertyset1 = (FieldPropertySet)iterator1.next();

            FieldPropertySet fieldpropertyset2;
            for(Iterator iterator2 = bulkinfo.printForms.values().iterator(); iterator2.hasNext(); printForms.put(fieldpropertyset2.getName(), fieldpropertyset2.clone(this)))
                fieldpropertyset2 = (FieldPropertySet)iterator2.next();

            FieldPropertySet fieldpropertyset3;
            for(Iterator iterator3 = bulkinfo.freeSearchSets.values().iterator(); iterator3.hasNext(); freeSearchSets.put(fieldpropertyset3.getName(), fieldpropertyset3.clone(this)))
                fieldpropertyset3 = (FieldPropertySet)iterator3.next();

        }else{
            fieldProperties = new OrderedHashtable();
            findFieldProperties = new FieldPropertySet(this);
        }
    }

    /**
     * Aggiunge una columnFieldProperty. Viene cercata la FieldProperty con lo stesso nome e 
     * se viene trovata vengono usati le sue propriet� quando non sono state definite dalla columnFieldProperty
     */
    public void addColumnFieldProperty(ColumnFieldProperty newProperty){
        FieldPropertySet fieldpropertyset = (FieldPropertySet)columnSets.get("default");
        if(fieldpropertyset == null)
            columnSets.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        fieldpropertyset.addFieldProperty(newProperty);
    }
    /**
     * Aggiunge un ColumnSet al ricevente.
     */
    public void addColumnSet(FieldPropertySet fieldpropertyset){
        addFieldPropertySet(columnSets, fieldpropertyset);
    }
    /**
     * Aggiunge una FieldProperty.
     */
    public void addFieldProperty(FieldProperty fieldproperty){
        fieldProperties.put(fieldproperty.getName(), fieldproperty);
        fieldPropertiesByProperty.put(fieldproperty.getProperty(), fieldproperty);
        fieldproperty.setBulkInfo(this);
    }
    /**
     * Aggiunge un FieldPropertySet all'elenco delle form del ricevente.
     */
    private void addFieldPropertySet(Map<String, Object> map, FieldPropertySet fieldpropertyset){
        map.put(fieldpropertyset.getName(), fieldpropertyset);
        fieldpropertyset.setBulkInfo(this);
    }
    /**
     * Aggiunge una FindFieldProperty. Viene cercata la FieldProperty con lo stesso nome e se 
     * viene trovata vengono usati le sue propriet� quando non sono state definite dalla findFieldProperty
     */
    public void addFindFieldProperty(FieldProperty fieldproperty){
        findFieldProperties.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }
    /**
     * Aggiunge una FormFieldProperty. Viene cercata la FieldProperty con lo stesso nome e se viene 
     * trovata vengono usati le sue propriet� quando non sono state definite dalla formFieldProperty
     */
    public void addForm(FieldPropertySet fieldpropertyset){
        addFieldPropertySet(forms, fieldpropertyset);
    }
    /**
     * Aggiunge una FormFieldProperty. Viene cercata la FieldProperty con lo stesso nome e se 
     * viene trovata vengono usati le sue propriet� quando non sono state definite dalla formFieldProperty
     */
    public void addFormFieldProperty(FieldProperty fieldproperty){
        FieldPropertySet fieldpropertyset = (FieldPropertySet)forms.get("default");
        if(fieldpropertyset == null)
            forms.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        fieldpropertyset.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }

    public void addFreeSearchSet(FieldPropertySet fieldpropertyset){
        addFieldPropertySet(freeSearchSets, fieldpropertyset);
    }
    /**
     * Aggiunge una PrintFieldProperty. Viene cercata la FieldProperty con lo stesso nome e se viene 
     * trovata vengono usati le sue propriet� quando non sono state definite dalla printFieldProperty
     */
    public void addPrintFieldProperty(FieldProperty fieldproperty){
        FieldPropertySet fieldpropertyset = (FieldPropertySet)printForms.get("default");
        if(fieldpropertyset == null)
            printForms.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        fieldpropertyset.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }
    /**
     * Aggiunge un FieldPropertySet all'elenco delle printForm del ricevente.
     */
    public void addPrintForm(FieldPropertySet fieldpropertyset){
        addFieldPropertySet(printForms, fieldpropertyset);
    }
    /**
     * Costruisce un albero logico di clausole di ricerca basate sui valori non nulli delle 
     * FindFieldProperties dell'OggettoBulk specificato. 
     * Le clausole vengono costruite mettendo in AND tutti i valori non 
     * nulli di tutte le FindFieldProperties.
     */
    public Criteria buildFindClausesFrom(OggettoBulk oggettobulk, Boolean freeSearch){
        try{
            Criteria compoundfindclause = CriteriaFactory.createCriteria(oggettobulk.getClass().getName());
            for(Enumeration<?> enumeration = getFindFieldProperties(); enumeration.hasMoreElements();){
                FieldProperty fieldproperty = (FieldProperty)enumeration.nextElement();
                if(fieldproperty.getProperty() != null && (freeSearch == null || fieldproperty.isEnabledOnFreeSearch() == freeSearch.booleanValue())){
                    String s = fieldproperty.getFindProperty() != null ? fieldproperty.getFindProperty() : fieldproperty.getProperty();
                    Object obj = Introspector.getPropertyValue(oggettobulk, s);
                    if(obj != null){
                    	SimpleExpression criterion = Restrictions.eq(s, obj);
                    	if (!fieldproperty.isCaseSensitiveSearch())
                    		criterion.ignoreCase();
                    	compoundfindclause.add(criterion);
                    }
                }
            }
            return compoundfindclause;
        }catch(Exception exception){
            throw new IntrospectionError(exception);
        }
    }
    /**
     * Aggiunge in cache un BulkInfo
     */
    protected static void cacheBulkInfo(Class<?> class1, BulkInfo bulkinfo){
        bulkInfos.put(class1, bulkinfo);
    }

    void completeFieldProperty(FieldProperty fieldproperty){
        FieldProperty fieldproperty1 = getFieldProperty(fieldproperty.getName());
        if(fieldproperty1 == null)
            addFieldProperty(fieldproperty1 = fieldproperty);
        else
            fieldproperty.fillNullsFrom(fieldproperty1);
        fieldproperty.setBulkInfo(this);
    }
    /**
     * Restituisce la classe di OggettoBulk che � rappresentata dal ricevente.
     */
    public Class<?> getBulkClass(){
        return bulkClass;
    }
    @SuppressWarnings("unchecked")
	public static List<Class> getClassHierarchy(Class targetClass, boolean reverse, boolean includeSelf) {
    	List<Class> hierarchy = new ArrayList<Class>();
    	if (!includeSelf) {
    		targetClass = targetClass.getSuperclass();
        }    	
    	while (targetClass != null) {
            if (reverse) {
                hierarchy.add(targetClass);
            } else {
                hierarchy.add(0, targetClass);
            }
            targetClass = targetClass.getSuperclass();
        }    	
    	return hierarchy;
    }

    public static String lowerFirst(String source){
		source = source.substring(0, 1).toLowerCase()+source.substring(1);
		return source;
	}
    @SuppressWarnings("unchecked")
	private static BulkInfo makeAnnotetedBulkInfo(Class class1) {
    	BulkInfo bulkinfo = new BulkInfo(class1);
    	BulkInfoAnnotation bulkInfoAnn =  (BulkInfoAnnotation) class1.getAnnotation(BulkInfoAnnotation.class);
    	bulkinfo.setShortDescription(bulkInfoAnn.shortDescription());
    	bulkinfo.setLongDescription(bulkInfoAnn.longDescription());
    	List<Class> classHierarchy = getClassHierarchy(class1, false, true);
    	//Carico prima i Field Property leggendoli dagli attributi
    	for (Iterator<Class> iteratorClass = classHierarchy.iterator(); iteratorClass.hasNext();) {
			Class target = iteratorClass.next();
        	List<Field> attributi = Arrays.asList(target.getDeclaredFields());
        	for (Iterator<Field> iterator = attributi.iterator(); iterator.hasNext();) {
    			Field field = iterator.next();
    			FieldPropertyAnnotation fieldPropertyAnnotation = field.getAnnotation(FieldPropertyAnnotation.class); 
    			if (fieldPropertyAnnotation != null){
    				FieldProperty fieldProperty = new FieldProperty();  
    				fieldProperty.createWithAnnotation(fieldPropertyAnnotation, field.getName());
    				bulkinfo.addFieldProperty(fieldProperty);
    			}
    		}
		}
    	//Ora carico i Field Property leggendoli dai metodi
    	for (Iterator<Class> iteratorClass = classHierarchy.iterator(); iteratorClass.hasNext();) {
			Class target = iteratorClass.next();
        	List<Method> metodi = Arrays.asList(target.getDeclaredMethods());
        	for (Iterator<Method> iterator = metodi.iterator(); iterator.hasNext();) {
        		Method method = iterator.next();
    			FieldPropertyAnnotation fieldPropertyAnnotation = method.getAnnotation(FieldPropertyAnnotation.class); 
    			if (fieldPropertyAnnotation != null){
    				FieldProperty fieldProperty = new FieldProperty();  
    				String attributo = lowerFirst(method.getName().substring(3));
    				fieldProperty.createWithAnnotation(fieldPropertyAnnotation, attributo);
    				bulkinfo.addFieldProperty(fieldProperty);
    			}
    		}
    	}
    	
    	//Carico i FormFieldProperty
    	for (Iterator<FormAnnotation> iterator = Arrays.asList(bulkInfoAnn.form()).iterator(); iterator.hasNext();) { 
    		FormAnnotation formAnnotation = iterator.next();
        	FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, formAnnotation.name());
        	for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(formAnnotation.value()).iterator(); iterator2.hasNext();) {
        		FieldPropertyAnnotation type = iterator2.next();
        		FieldProperty fieldProperty = new FieldProperty();  
				fieldProperty.createWithAnnotation(type);				
        		fieldpropertyset.addFormFieldProperty(fieldProperty);
			}
        	bulkinfo.addForm(fieldpropertyset);
		}
    	//Carico i ColumnFieldProperty
    	for (Iterator<ColumnSetAnnotation> iterator = Arrays.asList(bulkInfoAnn.columnSet()).iterator(); iterator.hasNext();) { 
    		ColumnSetAnnotation columnSetAnnotation = iterator.next();
        	FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, columnSetAnnotation.name());
        	for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(columnSetAnnotation.value()).iterator(); iterator2.hasNext();) {
        		FieldPropertyAnnotation type = iterator2.next();
        		ColumnFieldProperty fieldProperty = new ColumnFieldProperty();  
				fieldProperty.createWithAnnotation(type);				
        		fieldpropertyset.addColumnFieldProperty(fieldProperty);
			}
        	bulkinfo.addColumnSet(fieldpropertyset);
		}
    	//Carico i FindFieldProperty
    	for (Iterator<FreeSearchSetAnnotation> iterator = Arrays.asList(bulkInfoAnn.freeSearchSet()).iterator(); iterator.hasNext();) { 
    		FreeSearchSetAnnotation freeSearchSetAnnotation = iterator.next();
        	FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, freeSearchSetAnnotation.name());
        	for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(freeSearchSetAnnotation.value()).iterator(); iterator2.hasNext();) {
        		FieldPropertyAnnotation type = iterator2.next();
        		FieldProperty fieldProperty = new FieldProperty();  
				fieldProperty.createWithAnnotation(type);				
        		fieldpropertyset.addFindFieldProperty(fieldProperty);
			}
        	bulkinfo.addFreeSearchSet(fieldpropertyset);
		}
    	//Carico i PrintFieldProperty
    	for (Iterator<PrintFormAnnotation> iterator = Arrays.asList(bulkInfoAnn.printForm()).iterator(); iterator.hasNext();) { 
    		PrintFormAnnotation printFormAnnotation = iterator.next();
        	FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, printFormAnnotation.name());
        	for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(printFormAnnotation.value()).iterator(); iterator2.hasNext();) {
        		FieldPropertyAnnotation type = iterator2.next();
        		FieldProperty fieldProperty = new PrintFieldProperty();  
				fieldProperty.createWithAnnotation(type);				
        		fieldpropertyset.addPrintFieldProperty(fieldProperty);
			}
        	bulkinfo.addPrintForm(fieldpropertyset);
		}   
    	return bulkinfo;
	}    
    /**
     * Restituisce l'istanza di BulkInfo associata ad una classe.
     */
    public static BulkInfo getBulkInfo(Class<?> class1){
        BulkInfo bulkinfo = getCachedBulkInfo(class1);
        if(bulkinfo == null){
        	bulkinfo = makeAnnotetedBulkInfo(class1);
            cacheBulkInfo(class1, bulkinfo);
        }
        return bulkinfo;
    }
    /**
     * Restituisce l'istanza di BulkInfo associata ad una classe.
     */
    protected static BulkInfo getCachedBulkInfo(Class<?> class1){
        return (BulkInfo)bulkInfos.get(class1);
    }
    /**
     * Restituisce l'elenco delle columnFieldProperty
     */
    public Enumeration<?> getColumnFieldProperties(){
        return getColumnFieldProperties("default");
    }
    /**
     * Restituisce l'elenco delle columnFieldProperty.
     */
    public Enumeration<?> getColumnFieldProperties(String columnSetName){
        if(columnSetName == null)
            columnSetName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)columnSets.get(columnSetName);
        if(fieldpropertyset == null)
            return Collections.enumeration(Collections.emptyList());
        else
            return fieldpropertyset.getFieldProperties();
    }
    /**
     * Cerca una ColumnFieldProperty.
     */
    public ColumnFieldProperty getColumnFieldProperty(String name){
        return getColumnFieldProperty("default", name);
    }
    /**
     * Cerca una ColumnFieldProperty.
     */
    public ColumnFieldProperty getColumnFieldProperty(String columnSetName, String name){
        if(columnSetName == null)
            columnSetName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)columnSets.get(columnSetName);
        if(fieldpropertyset == null)
            return null;
        else
            return (ColumnFieldProperty)fieldpropertyset.getFieldProperty(name);
    }
    /**
     * Restituisce un dizionario contenente le ColumnFieldProperty del ColumnSet "default"
     */
    public Dictionary<?, ?> getColumnFieldPropertyDictionary(){
        return getColumnFieldPropertyDictionary("default");
    }
    /**
     * Restituisce un dizionario di ColumnFieldProperty
     */
    public Dictionary<?, ?> getColumnFieldPropertyDictionary(String columnSetName){
        if(columnSetName == null)
            columnSetName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)columnSets.get(columnSetName);
        if(fieldpropertyset == null)
            return OrderedHashtable.EMPTY_HASHTABLE;
        else
            return fieldpropertyset.getFieldPropertyDictionary();
    }
    /**
     * Restituisce il valore il nome della classe bulk privato del package
     * e del suffisso Bulk
     */
    private String getDefaultDescription(){
        String s = getBulkClass().getName();
        s = s.substring(s.lastIndexOf('.') + 1);
        if(s.endsWith("VBulk"))
            s = s.substring(0, s.length() - 5);
        else
        if(s.endsWith("Bulk"))
            s = s.substring(0, s.length() - 4);
        return s.replace('_', ' ');
    }
    /**
     * Restituisce l'elenco delle FieldProperty.
     */
    public Enumeration<?> getFieldProperties(){
        return fieldProperties.elements();
    }
    /**
     * Cerca una FieldProperty.
     */
    public FieldProperty getFieldProperty(String name){
        return (FieldProperty)fieldProperties.get(name);
    }
    /**
     * Cerca una FieldProperty in base alla property
     */
    public FieldProperty getFieldPropertyByProperty(String property){
        return (FieldProperty)fieldPropertiesByProperty.get(property);
    }
    /**
     * Restituisce l'elenco delle FindFieldProperty.
     */
    public Enumeration<?> getFindFieldProperties(){
        return findFieldProperties.getFieldProperties();
    }
    /**
     * Cerca una FindFieldProperty.
     */
    public FieldProperty getFindFieldProperty(String name){
        return findFieldProperties.getFieldProperty(name);
    }
    /**
     * Restituisce l'elenco delle FormFieldProperty.
     */
    public Enumeration<?> getFormFieldProperties(){
        return getFormFieldProperties("default");
    }
    /**
     * Restituisce l'elenco delle FormFieldProperty di una Form.
     */
    public Enumeration<?> getFormFieldProperties(String formName){
        if(formName == null)
            formName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)forms.get(formName);
        if(fieldpropertyset == null)
            return Collections.enumeration(Collections.emptyList());
        else
            return fieldpropertyset.getFieldProperties();
    }
    /**
     * Cerca una FormFieldProperty.
     */
    public FieldProperty getFormFieldProperty(String name){
        return getFormFieldProperty("default", name);
    }
    /**
     * Cerca una FormFieldProperty.
     */
    public FieldProperty getFormFieldProperty(String formName, String name){
        if(formName == null)
            formName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)forms.get(formName);
        if(fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldProperty(name);
    }
    /**
     * Restituisce l'elenco di FindFieldProperty abilitate alla ricerca libera.
     */
    public final Enumeration<?> getFreeSearchProperties(){
        return getFreeSearchProperties("default");
    }
    /**
     * Restituisce l'elenco di FindFieldProperty abilitate alla ricerca libera.
     */
    public final Enumeration<?> getFreeSearchProperties(String freeSearchSet){
        if(freeSearchSet == null)
            freeSearchSet = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)freeSearchSets.get(freeSearchSet);
        if(fieldpropertyset == null){
            fieldpropertyset = new FieldPropertySet(this, freeSearchSet);
            for(Enumeration<?> enumeration = getFindFieldProperties(); enumeration.hasMoreElements();){
                FieldProperty fieldproperty = (FieldProperty)enumeration.nextElement();
                if(fieldproperty.isEnabledOnFreeSearch())
                    fieldpropertyset.addFieldProperty(fieldproperty);
            }
            freeSearchSets.put(freeSearchSet, fieldpropertyset);
        }
        return fieldpropertyset.getFieldProperties();
    }
    /**
     * Restituisce il valore della propriet� 'longDescription'
     */
    public String getLongDescription(){
        if(longDescription == null)
            longDescription = getDefaultDescription();
        return longDescription;
    }
    /**
     * Restituisce l'elenco di PrintFieldProperty
     */
    public Enumeration<?> getPrintFieldProperties(String printFormName){
        if(printFormName == null)
            printFormName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)printForms.get(printFormName);
        if(fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldPropertyDictionary().elements();
    }
    /**
     * Cerca una PrintFieldProperty.
     */
    public FieldProperty getPrintFieldProperty(String name){
        return getPrintFieldProperty(null, name);
    }
    /**
     * Cerca una PrintFieldProperty.
     */
    public FieldProperty getPrintFieldProperty(String printFormName, String name){
        if(printFormName == null)
            printFormName = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet)printForms.get(printFormName);
        if(fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldProperty(name);
    }

    public Collection<Object> getPrintForms(){
        return printForms.values();
    }
    /**
     * Restituisce il valore della propriet� 'shortDescription'
     */
    public String getShortDescription(){
        if(shortDescription == null)
            shortDescription = getDefaultDescription();
        return shortDescription;
    }

    protected BulkInfo internalGetBulkInfo(Class<?> class1){
        return getBulkInfo(class1);
    }

    /**
     * Pulisce la cache dei BulkInfo e forza la rilettura dei file di definizione
     */
    public static void resetBulkInfos(){
        bulkInfos.clear();
    }
    /**
     * Pulisce la cache dei BulkInfo e forza la rilettura dei file di definizione.
     */
    public static void resetBulkInfos(Class<?> class1){
        bulkInfos.remove(class1);
    }
    /**
     * Imposta il valore della propriet� 'longDescription'
     */
    public void setLongDescription(String newLongDescription){
        longDescription = newLongDescription;
    }
    /**
     * Imposta il valore della propriet� 'shortDescription'
     */
    public void setShortDescription(String newShortDescription){
        shortDescription = newShortDescription;
    }
}