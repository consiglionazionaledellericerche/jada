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

package it.cnr.jada.bulk;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.bulk.annotation.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.XMLObjectFiller;
import it.cnr.jada.util.action.FormField;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.jsp.JspWriter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class BulkInfo implements Serializable {

    private static final Hashtable bulkInfos = new Hashtable();
    private OrderedHashtable fieldProperties;
    private Map forms;
    private Map columnSets;
    private Map freeSearchSets;
    private Map printForms;
    private FieldPropertySet findFieldProperties;
    private Class bulkClass;
    private String shortDescription;
    private String longDescription;
    private HashMap fieldPropertiesByProperty;

    public BulkInfo() {
        forms = new HashMap();
        columnSets = new HashMap();
        freeSearchSets = new HashMap();
        printForms = new HashMap();
        fieldPropertiesByProperty = new HashMap();
    }

    protected BulkInfo(Class class1) {
        forms = new HashMap();
        columnSets = new HashMap();
        freeSearchSets = new HashMap();
        printForms = new HashMap();
        fieldPropertiesByProperty = new HashMap();
        if (!it.cnr.jada.bulk.OggettoBulk.class.isAssignableFrom(class1))
            throw new RuntimeException("Tentativo di costruire un BulkInfo per una classe che non \350 un OggettoBulk");
        bulkClass = class1;
        if (class1 != it.cnr.jada.bulk.OggettoBulk.class) {
            BulkInfo bulkinfo = internalGetBulkInfo(class1.getSuperclass());
            fieldProperties = (OrderedHashtable) bulkinfo.fieldProperties.clone();
            fieldPropertiesByProperty = (HashMap) bulkinfo.fieldPropertiesByProperty.clone();
            findFieldProperties = (FieldPropertySet) bulkinfo.findFieldProperties.clone(this);
            FieldPropertySet fieldpropertyset;
            for (Iterator iterator = bulkinfo.columnSets.values().iterator(); iterator.hasNext(); columnSets.put(fieldpropertyset.getName(), fieldpropertyset.clone(this)))
                fieldpropertyset = (FieldPropertySet) iterator.next();

            FieldPropertySet fieldpropertyset1;
            for (Iterator iterator1 = bulkinfo.forms.values().iterator(); iterator1.hasNext(); forms.put(fieldpropertyset1.getName(), fieldpropertyset1.clone(this)))
                fieldpropertyset1 = (FieldPropertySet) iterator1.next();

            FieldPropertySet fieldpropertyset2;
            for (Iterator iterator2 = bulkinfo.printForms.values().iterator(); iterator2.hasNext(); printForms.put(fieldpropertyset2.getName(), fieldpropertyset2.clone(this)))
                fieldpropertyset2 = (FieldPropertySet) iterator2.next();

            FieldPropertySet fieldpropertyset3;
            for (Iterator iterator3 = bulkinfo.freeSearchSets.values().iterator(); iterator3.hasNext(); freeSearchSets.put(fieldpropertyset3.getName(), fieldpropertyset3.clone(this)))
                fieldpropertyset3 = (FieldPropertySet) iterator3.next();

        } else {
            fieldProperties = new OrderedHashtable();
            findFieldProperties = new FieldPropertySet(this);
        }
    }

    protected BulkInfo(Class class1, InputStream inputstream) {
        this(class1);
        String encoding = System.getProperty("SIGLA_ENCODING", "UTF-8");
        try {
            readInfo(new InputStreamReader(inputstream, encoding));
        } catch (UnsupportedEncodingException e) {
            System.out.print("Encoding " + encoding + " non supportato");
            readInfo(new InputStreamReader(inputstream));
        }
    }

    protected BulkInfo(Class class1, Reader reader) {
        this(class1);
        readInfo(reader);
    }

    protected static void cacheBulkInfo(Class class1, BulkInfo bulkinfo) {
        bulkInfos.put(class1, bulkinfo);
    }

    public static BulkInfo getBulkInfo(Class class1) {
        BulkInfo bulkinfo = getCachedBulkInfo(class1);
        if (bulkinfo == null) {
            if (class1.getAnnotation(it.cnr.jada.bulk.annotation.BulkInfoAnnotation.class) != null) {
                bulkinfo = makeAnnotetedBulkInfo(class1);
            } else {
                InputStream inputstream = null;
                try {
                    URL resurl = class1.getResource("/" + class1.getPackage().getName().replace('.', '/') + "/" + class1.getSimpleName() + "Info.xml");
                    if (resurl != null)
                        inputstream = resurl.openStream();
                } catch (IOException e1) {
                }
                if (inputstream == null)
                    bulkinfo = new BulkInfo(class1);
                else
                    bulkinfo = new BulkInfo(class1, inputstream);
            }
            cacheBulkInfo(class1, bulkinfo);
        }
        return bulkinfo;
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

    public static String lowerFirst(String source) {
        source = source.substring(0, 1).toLowerCase() + source.substring(1);
        return source;
    }

    @SuppressWarnings("unchecked")
    private static BulkInfo makeAnnotetedBulkInfo(Class class1) {
        BulkInfo bulkinfo = new BulkInfo(class1);
        BulkInfoAnnotation bulkInfoAnn = (BulkInfoAnnotation) class1.getAnnotation(BulkInfoAnnotation.class);
        bulkinfo.setShortDescription(bulkInfoAnn.shortDescription());
        bulkinfo.setLongDescription(bulkInfoAnn.longDescription());
        List<Class> classHierarchy = getClassHierarchy(class1, false, true);
        //Carico prima i Field Property leggendoli dagli attributi
        for (Iterator<Class> iteratorClass = classHierarchy.iterator(); iteratorClass.hasNext(); ) {
            Class target = iteratorClass.next();
            List<Field> attributi = Arrays.asList(target.getDeclaredFields());
            for (Iterator<Field> iterator = attributi.iterator(); iterator.hasNext(); ) {
                Field field = iterator.next();
                FieldPropertyAnnotation fieldPropertyAnnotation = field.getAnnotation(FieldPropertyAnnotation.class);
                if (fieldPropertyAnnotation != null) {
                    FieldProperty fieldProperty = new FieldProperty();
                    fieldProperty.createWithAnnotation(fieldPropertyAnnotation, field.getName());
                    bulkinfo.addFieldProperty(fieldProperty);
                }
            }
        }
        //Ora carico i Field Property leggendoli dai metodi
        for (Iterator<Class> iteratorClass = classHierarchy.iterator(); iteratorClass.hasNext(); ) {
            Class target = iteratorClass.next();
            List<Method> metodi = Arrays.asList(target.getDeclaredMethods());
            for (Iterator<Method> iterator = metodi.iterator(); iterator.hasNext(); ) {
                Method method = iterator.next();
                FieldPropertyAnnotation fieldPropertyAnnotation = method.getAnnotation(FieldPropertyAnnotation.class);
                if (fieldPropertyAnnotation != null) {
                    FieldProperty fieldProperty = new FieldProperty();
                    String attributo = lowerFirst(method.getName().substring(3));
                    fieldProperty.createWithAnnotation(fieldPropertyAnnotation, attributo);
                    bulkinfo.addFieldProperty(fieldProperty);
                }
            }
        }

        //Carico i FormFieldProperty
        for (Iterator<FormAnnotation> iterator = Arrays.asList(bulkInfoAnn.form()).iterator(); iterator.hasNext(); ) {
            FormAnnotation formAnnotation = iterator.next();
            FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, formAnnotation.name());
            for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(formAnnotation.value()).iterator(); iterator2.hasNext(); ) {
                FieldPropertyAnnotation type = iterator2.next();
                FieldProperty fieldProperty = new FieldProperty();
                fieldProperty.createWithAnnotation(type);
                fieldpropertyset.addFormFieldProperty(fieldProperty);
            }
            bulkinfo.addForm(fieldpropertyset);
        }
        //Carico i ColumnFieldProperty
        for (Iterator<ColumnSetAnnotation> iterator = Arrays.asList(bulkInfoAnn.columnSet()).iterator(); iterator.hasNext(); ) {
            ColumnSetAnnotation columnSetAnnotation = iterator.next();
            FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, columnSetAnnotation.name());
            for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(columnSetAnnotation.value()).iterator(); iterator2.hasNext(); ) {
                FieldPropertyAnnotation type = iterator2.next();
                ColumnFieldProperty fieldProperty = new ColumnFieldProperty();
                fieldProperty.createWithAnnotation(type);
                fieldpropertyset.addColumnFieldProperty(fieldProperty);
            }
            bulkinfo.addColumnSet(fieldpropertyset);
        }
        //Carico i FindFieldProperty
        for (Iterator<FreeSearchSetAnnotation> iterator = Arrays.asList(bulkInfoAnn.freeSearchSet()).iterator(); iterator.hasNext(); ) {
            FreeSearchSetAnnotation freeSearchSetAnnotation = iterator.next();
            FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, freeSearchSetAnnotation.name());
            for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(freeSearchSetAnnotation.value()).iterator(); iterator2.hasNext(); ) {
                FieldPropertyAnnotation type = iterator2.next();
                FieldProperty fieldProperty = new FieldProperty();
                fieldProperty.createWithAnnotation(type);
                fieldpropertyset.addFindFieldProperty(fieldProperty);
            }
            bulkinfo.addFreeSearchSet(fieldpropertyset);
        }
        //Carico i PrintFieldProperty
        for (Iterator<PrintFormAnnotation> iterator = Arrays.asList(bulkInfoAnn.printForm()).iterator(); iterator.hasNext(); ) {
            PrintFormAnnotation printFormAnnotation = iterator.next();
            FieldPropertySet fieldpropertyset = new FieldPropertySet(bulkinfo, printFormAnnotation.name());
            for (Iterator<FieldPropertyAnnotation> iterator2 = Arrays.asList(printFormAnnotation.value()).iterator(); iterator2.hasNext(); ) {
                FieldPropertyAnnotation type = iterator2.next();
                FieldProperty fieldProperty = new PrintFieldProperty();
                fieldProperty.createWithAnnotation(type);
                fieldpropertyset.addPrintFieldProperty(fieldProperty);
            }
            bulkinfo.addPrintForm(fieldpropertyset);
        }

        return bulkinfo;
    }

    protected static BulkInfo getCachedBulkInfo(Class class1) {
        return (BulkInfo) bulkInfos.get(class1);
    }

    public static void resetBulkInfos() {
        bulkInfos.clear();
    }

    public static void resetBulkInfos(Class class1) {
        bulkInfos.remove(class1);
    }

    public void addColumnFieldProperty(ColumnFieldProperty columnfieldproperty) {
        FieldPropertySet fieldpropertyset = (FieldPropertySet) columnSets.get("default");
        if (fieldpropertyset == null)
            columnSets.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        fieldpropertyset.addFieldProperty(columnfieldproperty);
    }

    public void addColumnSet(FieldPropertySet fieldpropertyset) {
        addFieldPropertySet(columnSets, fieldpropertyset);
    }

    public void addFieldProperty(FieldProperty fieldproperty) {
        fieldProperties.put(fieldproperty.getName(), fieldproperty);
        fieldPropertiesByProperty.put(fieldproperty.getProperty(), fieldproperty);
        fieldproperty.setBulkInfo(this);
    }

    private void addFieldPropertySet(Map map, FieldPropertySet fieldpropertyset) {
        map.put(fieldpropertyset.getName(), fieldpropertyset);
        fieldpropertyset.setBulkInfo(this);
    }

    public void addFindFieldProperty(FieldProperty fieldproperty) {
        findFieldProperties.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }

    public void addForm(FieldPropertySet fieldpropertyset) {
        addFieldPropertySet(forms, fieldpropertyset);
    }

    public void addFormFieldProperty(FieldProperty fieldproperty) {
        FieldPropertySet fieldpropertyset = (FieldPropertySet) forms.get("default");
        if (fieldpropertyset == null)
            forms.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        completeFieldProperty(fieldproperty);
        fieldpropertyset.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }

    public void addFreeSearchSet(FieldPropertySet fieldpropertyset) {
        addFieldPropertySet(freeSearchSets, fieldpropertyset);
    }

    public void addPrintFieldProperty(FieldProperty fieldproperty) {
        FieldPropertySet fieldpropertyset = (FieldPropertySet) printForms.get("default");
        if (fieldpropertyset == null)
            printForms.put("default", fieldpropertyset = new FieldPropertySet(this, "default"));
        fieldpropertyset.addFieldProperty(fieldproperty);
        fieldproperty.setBulkInfo(this);
    }

    public void addPrintForm(FieldPropertySet fieldpropertyset) {
        addFieldPropertySet(printForms, fieldpropertyset);
    }

    public CompoundFindClause buildFindClausesFrom(OggettoBulk oggettobulk, Boolean boolean1) {
        try {
            CompoundFindClause compoundfindclause = null;
            for (Enumeration enumeration = getFindFieldProperties(); enumeration.hasMoreElements(); ) {
                FieldProperty fieldproperty = (FieldProperty) enumeration.nextElement();
                if (fieldproperty.getProperty() != null && (boolean1 == null || fieldproperty.isEnabledOnFreeSearch() == boolean1.booleanValue())) {
                    String s = fieldproperty.getFindProperty() != null ? fieldproperty.getFindProperty() : fieldproperty.getProperty();
                    Object obj = Introspector.getPropertyValue(oggettobulk, s);
                    if (obj != null) {
                        if (compoundfindclause == null)
                            compoundfindclause = new CompoundFindClause();
                        SimpleFindClause simplefindclause = new SimpleFindClause();
                        simplefindclause.setPropertyName(s);
                        simplefindclause.setLogicalOperator("AND");
                        simplefindclause.setOperator(8192);
                        simplefindclause.setValue(obj);
                        simplefindclause.setCaseSensitive(fieldproperty.isCaseSensitiveSearch());
                        compoundfindclause.addChild(simplefindclause);
                    }
                }
            }
            return compoundfindclause;
        } catch (Exception exception) {
            throw new IntrospectionError(exception);
        }
    }

    public CompoundFindClause buildFindClausesLikeFrom(OggettoBulk oggettobulk, Boolean boolean1, int operator, String nameSearchtool, CompoundFindClause compoundfindclause) {
        try {
            Enumeration enumeration = getFindFieldProperties();

            while(true) {
                FieldProperty fieldproperty;
                do {
                    do {
                        if (!enumeration.hasMoreElements()) {
                            return compoundfindclause;
                        }

                        fieldproperty = (FieldProperty)enumeration.nextElement();
                    } while(fieldproperty.getProperty() == null);
                } while(boolean1 != null && fieldproperty.isEnabledOnFreeSearch() != boolean1);

                String s = fieldproperty.getFindProperty() != null ? fieldproperty.getFindProperty() : fieldproperty.getProperty();
                Object obj = Introspector.getPropertyValue(oggettobulk, s);
                if (obj != null) {
                    if (compoundfindclause == null) {
                        compoundfindclause = new CompoundFindClause();
                    }

                    Enumeration enumerationColumnsLike = getFormFieldProperties(nameSearchtool);
                    if (enumerationColumnsLike != null){
                        while(true) {
                            FieldProperty fieldpropertyLike;
                            do {
                                do {
                                    if (!enumerationColumnsLike.hasMoreElements()) {
                                        return compoundfindclause;
                                    }

                                    fieldpropertyLike = (FieldProperty)enumerationColumnsLike.nextElement();
                                } while(fieldpropertyLike.getProperty() == null);
                            } while(boolean1 != null && fieldpropertyLike.isEnabledOnFreeSearch() != boolean1);

                            String propertyNameLike = fieldpropertyLike.getFindProperty() != null ? fieldpropertyLike.getFindProperty() : fieldpropertyLike.getProperty();
                            SimpleFindClause simplefindclauseLike = new SimpleFindClause();
                            simplefindclauseLike.setPropertyName(propertyNameLike);
                            simplefindclauseLike.setLogicalOperator("OR");
                            simplefindclauseLike.setOperator(operator);
                            simplefindclauseLike.setValue(obj);
                            simplefindclauseLike.setCaseSensitive(fieldpropertyLike.isCaseSensitiveSearch());
                            compoundfindclause.addChild(simplefindclauseLike);
                        }
                    }
                }
            }
        } catch (Exception var9) {
            throw new IntrospectionError(var9);
        }
    }

    void completeFieldProperty(FieldProperty fieldproperty) {
        FieldProperty fieldproperty1 = getFieldProperty(fieldproperty.getName());
        if (fieldproperty1 == null)
            addFieldProperty(fieldproperty1 = fieldproperty);
        else
            fieldproperty.fillNullsFrom(fieldproperty1);
        fieldproperty.setBulkInfo(this);
    }

    public Class getBulkClass() {
        return bulkClass;
    }

    public Enumeration getColumnFieldProperties() {
        return getColumnFieldProperties("default");
    }

    public Enumeration getColumnFieldProperties(String s) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) columnSets.get(s);
        if (fieldpropertyset == null)
            return Collections.enumeration(Collections.EMPTY_LIST);
        else
            return fieldpropertyset.getFieldProperties();
    }

    public ColumnFieldProperty getColumnFieldProperty(String s) {
        return getColumnFieldProperty("default", s);
    }

    public ColumnFieldProperty getColumnFieldProperty(String s, String s1) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) columnSets.get(s);
        if (fieldpropertyset == null)
            return null;
        else
            return (ColumnFieldProperty) fieldpropertyset.getFieldProperty(s1);
    }

    public Dictionary getColumnFieldPropertyDictionary() {
        return getColumnFieldPropertyDictionary("default");
    }

    public Dictionary getColumnFieldPropertyDictionary(String s) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) columnSets.get(s);
        if (fieldpropertyset == null)
            return OrderedHashtable.EMPTY_HASHTABLE;
        else
            return fieldpropertyset.getFieldPropertyDictionary();
    }

    private String getDefaultDescription() {
        String s = getBulkClass().getName();
        s = s.substring(s.lastIndexOf('.') + 1);
        if (s.endsWith("VBulk"))
            s = s.substring(0, s.length() - 5);
        else if (s.endsWith("Bulk"))
            s = s.substring(0, s.length() - 4);
        return s.replace('_', ' ');
    }

    public Enumeration getFieldProperties() {
        return fieldProperties.elements();
    }

    public FieldProperty getFieldProperty(String s) {
        return (FieldProperty) fieldProperties.get(s);
    }

    public FieldProperty getFieldPropertyByProperty(String s) {
        return (FieldProperty) fieldPropertiesByProperty.get(s);
    }

    public Enumeration getFindFieldProperties() {
        return findFieldProperties.getFieldProperties();
    }

    public FieldProperty getFindFieldProperty(String s) {
        return findFieldProperties.getFieldProperty(s);
    }

    public Enumeration getFormFieldProperties() {
        return getFormFieldProperties("default");
    }

    public Enumeration getFormFieldProperties(String s) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) forms.get(s);
        if (fieldpropertyset == null)
            return Collections.enumeration(Collections.EMPTY_LIST);
        else
            return fieldpropertyset.getFieldProperties();
    }

    public FieldProperty getFormFieldProperty(String s) {
        return getFormFieldProperty("default", s);
    }

    public FieldProperty getFormFieldProperty(String s, String s1) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) forms.get(s);
        if (fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldProperty(s1);
    }

    public final Enumeration getFreeSearchProperties() {
        return getFreeSearchProperties("default");
    }

    public final Enumeration getFreeSearchProperties(String s) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) freeSearchSets.get(s);
        if (fieldpropertyset == null) {
            fieldpropertyset = new FieldPropertySet(this, s);
            for (Enumeration enumeration = getFindFieldProperties(); enumeration.hasMoreElements(); ) {
                FieldProperty fieldproperty = (FieldProperty) enumeration.nextElement();
                if (fieldproperty.isEnabledOnFreeSearch())
                    fieldpropertyset.addFieldProperty(fieldproperty);
            }
            freeSearchSets.put(s, fieldpropertyset);
        }
        return fieldpropertyset.getFieldProperties();
    }

    public String getLongDescription() {
        if (longDescription == null)
            longDescription = getDefaultDescription();
        return longDescription;
    }

    public void setLongDescription(String s) {
        longDescription = s;
    }

    public Enumeration getPrintFieldProperties(String s) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) printForms.get(s);
        if (fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldPropertyDictionary().elements();
    }

    public FieldProperty getPrintFieldProperty(String s) {
        return getPrintFieldProperty(null, s);
    }

    public FieldProperty getPrintFieldProperty(String s, String s1) {
        if (s == null)
            s = "default";
        FieldPropertySet fieldpropertyset = (FieldPropertySet) printForms.get(s);
        if (fieldpropertyset == null)
            return null;
        else
            return fieldpropertyset.getFieldProperty(s1);
    }

    public Collection getPrintForms() {
        return printForms.values();
    }

    public String getShortDescription() {
        if (shortDescription == null)
            shortDescription = getDefaultDescription();
        return shortDescription;
    }

    public void setShortDescription(String s) {
        shortDescription = s;
    }

    protected BulkInfo internalGetBulkInfo(Class class1) {
        return getBulkInfo(class1);
    }

    private void readInfo(Reader reader) {
        try {
            XMLObjectFiller xmlobjectfiller = new XMLObjectFiller(this);
            xmlobjectfiller.mapElementToClass("bulkInfo", getClass());
            xmlobjectfiller.mapElement("fieldProperty", it.cnr.jada.bulk.FieldProperty.class, "addFieldProperty");
            xmlobjectfiller.mapElement("findFieldProperty", it.cnr.jada.bulk.FieldProperty.class, "addFindFieldProperty");
            xmlobjectfiller.mapElement("formFieldProperty", it.cnr.jada.bulk.FieldProperty.class, "addFormFieldProperty");
            xmlobjectfiller.mapElement("printFieldProperty", it.cnr.jada.bulk.PrintFieldProperty.class, "addPrintFieldProperty");
            xmlobjectfiller.mapElement("columnFieldProperty", it.cnr.jada.bulk.ColumnFieldProperty.class, "addColumnFieldProperty");
            xmlobjectfiller.mapElement("form", it.cnr.jada.bulk.FieldPropertySet.class, "addForm");
            xmlobjectfiller.mapElement("printForm", it.cnr.jada.bulk.FieldPropertySet.class, "addPrintForm");
            xmlobjectfiller.mapElement("columnSet", it.cnr.jada.bulk.FieldPropertySet.class, "addColumnSet");
            xmlobjectfiller.mapElement("freeSearchSet", it.cnr.jada.bulk.FieldPropertySet.class, "addFreeSearchSet");
            xmlobjectfiller.parse(new InputSource(reader));
        } catch (SAXException saxexception) {
            throw new IntrospectionError(saxexception);
        } catch (IOException ioexception) {
            throw new DetailedRuntimeException(ioexception);
        } catch (ParserConfigurationException e) {
            throw new IntrospectionError(e);
        }
    }

    /**
     * Scrive una form HTML standard con tutte le FormFieldProperties.
     */
    public void writeForm(JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, int j, boolean showLabels, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeForm(null, out, bean, formName, labelClass, inputClass, prefix, status, readonly, j, showLabels, fieldvalidationmap, isBootstrap);
    }

    public void writeFormForSearchTool(JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, int j, boolean showLabels, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeFormForSearchTool(null, out, bean, formName, labelClass, inputClass, prefix, status, readonly, j, showLabels, fieldvalidationmap, isBootstrap);
    }

    public void writeFormForSearchToolWithLike(JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, int j, boolean showLabels, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeFormForSearchTool(null, out, bean, formName, labelClass, inputClass, prefix, status, readonly, j, showLabels, fieldvalidationmap, isBootstrap);
    }

    /**
     * Scrive una form HTML standard con tutte le formFieldProperties.
     */
    public void writeForm(Object bp, JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, int layout, boolean showLabels, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        if (layout == 1)
            out.println("<tr>");
        for (Enumeration enumeration = getFormFieldProperties(formName); enumeration.hasMoreElements(); ) {
            FieldProperty fieldproperty = (FieldProperty) enumeration.nextElement();
            if (layout == 0)
                out.println("<tr>");
            if (showLabels) {
                out.print("<td>");
                fieldproperty.writeLabel(bp, out, bean, labelClass, isBootstrap);
                out.println("</td>");
            }
            out.print("<td>");
            fieldproperty.writeInput(out, bean, readonly, Optional.ofNullable(inputClass).orElseGet(() -> isBootstrap ? "form-control" : "FormInput"), null, prefix, status, fieldvalidationmap, isBootstrap);
            out.println("</td>");
            if (layout == 0)
                out.println("</tr>");
        }
        if (layout == 1)
            out.println("</tr>");
    }

    /**
     * Scrive una form HTML standard con tutte le formFieldProperties.
     */
    public void writeFormForSearchTool(Object bp, JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, int layout, boolean showLabels, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        for (Enumeration enumeration = getFormFieldProperties(formName); enumeration.hasMoreElements(); ) {
            FieldProperty fieldproperty = (FieldProperty) enumeration.nextElement();
            fieldproperty.writeInput(out, bean, readonly,
                    Optional.ofNullable(inputClass)
                            .map(s -> s.concat(" input-title"))
                            .orElse("form-control input-title"),
                    null, prefix, status, fieldvalidationmap, isBootstrap);
        }
    }

    /**
     * Scrive una form HTML standard con tutte le formFieldProperties.
     */
    public void writeForm(Object bp, JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeForm(bp, out, bean, formName, labelClass, inputClass, prefix, status, readonly, 0, true, fieldvalidationmap, isBootstrap);
    }

    /**
     * Scrive una form HTML standard con tutte le formFieldProperties.
     */
    public void writeForm(JspWriter out, Object bean, String formName, String labelClass, String inputClass, String prefix, int status, boolean readonly, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeForm(out, bean, formName, labelClass, inputClass, prefix, status, readonly, 0, true, fieldvalidationmap, isBootstrap);
    }

    /**
     * Scrive un campo di input HTML con la sua label associato a una formFieldProperty
     */
    public void writeFormField(JspWriter out, Object bean, String formName, String name, String prefix, int labelColspan, int inputColspan, int status, boolean readonly, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        writeFormField(null, out, bean, formName, name, prefix, labelColspan, inputColspan, status, readonly, fieldvalidationmap, isBootstrap);
    }

    /**
     * Scrive un campo di input HTML con la sua label associato a una formFieldProperty
     */
    public void writeFormField(Object bp, JspWriter out, Object bean, String formName, String name, String prefix, int labelColspan, int inputColspan, int status, boolean readonly, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        FieldProperty fieldproperty = getFormFieldProperty(formName, name);
        if (fieldproperty != null) {
            out.print("<td");
            if (labelColspan > 1) {
                out.print(" colspan=\"");
                out.print(labelColspan);
                out.print("\"");
            }
            out.print(">");
            fieldproperty.writeLabel(bp, out, bean, "FormLabel", isBootstrap);
            out.print("</td><td");
            if (inputColspan > 1) {
                out.print(" colspan=\"");
                out.print(inputColspan);
                out.print("\"");
            }
            out.print(">");
            fieldproperty.writeInput(out, bean, readonly, isBootstrap ? "form-control" : "FormInput", null, prefix, status, fieldvalidationmap, isBootstrap);
            out.print("</td>");
        }
    }

    /**
     * Scrive un campo di input HTML senza label associato a una formFieldProperty
     */
    public void writeFormInput(JspWriter out, Object bean, String formName, String name, boolean readonly, String cssClass, String attributes, String prefix, int status, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException {
        FieldProperty fieldproperty = getFormFieldProperty(formName, name);
        if (fieldproperty != null)
            fieldproperty.writeInput(out, bean, readonly, Optional.ofNullable(cssClass).orElseGet(() -> isBootstrap ? "form-control" : "FormInput"), attributes, prefix, status, fieldvalidationmap, isBootstrap);
    }

    /**
     * Scrive la label di un campo di input HTML associato a una formFieldProperty
     */
    public void writeFormLabel(JspWriter out, Object bean, String formName, String name, String cssClass, boolean isBootstrap) throws IOException {
        writeFormLabel(null, out, bean, formName, name, cssClass, isBootstrap);
    }

    /**
     * Scrive la label di un campo di input HTML associato a una formFieldProperty
     */
    public void writeFormLabel(Object bp, JspWriter out, Object bean, String formName, String name, String cssClass, boolean isBootstrap) throws IOException {
        FieldProperty fieldproperty = getFormFieldProperty(formName, name);
        if (fieldproperty != null)
            fieldproperty.writeLabel(bp, out, bean, cssClass, isBootstrap);
    }
}