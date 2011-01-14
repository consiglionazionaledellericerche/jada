package it.cnr.jada.bulk;

import it.cnr.jada.util.OrderedHashtable;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;

public class FieldPropertySet implements Cloneable, Serializable{

    private String name;
    private OrderedHashtable properties;
    private BulkInfo bulkInfo;
    private String label;

    public FieldPropertySet(){
        properties = new OrderedHashtable();
    }

    public FieldPropertySet(BulkInfo bulkinfo){
    	this.properties = new OrderedHashtable();
        this.bulkInfo = bulkinfo;
    }

    public FieldPropertySet(BulkInfo bulkinfo, String name){
    	this.properties = new OrderedHashtable();
        this.bulkInfo = bulkinfo;
        this.name = name;
    }

    public void addColumnFieldProperty(ColumnFieldProperty columnfieldproperty){
        addFieldProperty(columnfieldproperty);
    }

    public void addFieldProperties(FieldPropertySet fieldpropertyset){
        FieldProperty fieldproperty;
        for(Enumeration enumeration = fieldpropertyset.properties.elements(); enumeration.hasMoreElements(); properties.put(fieldproperty.getName(), fieldproperty))
            fieldproperty = (FieldProperty)enumeration.nextElement();

    }

    public void addFieldProperty(FieldProperty fieldproperty){
        bulkInfo.completeFieldProperty(fieldproperty);
        properties.put(fieldproperty.getName(), fieldproperty);
    }

    public void addFindFieldProperty(FieldProperty fieldproperty){
        addFieldProperty(fieldproperty);
    }

    public void addFormFieldProperty(FieldProperty fieldproperty){
        addFieldProperty(fieldproperty);
    }

    public void addPrintFieldProperty(FieldProperty fieldproperty){
        addFieldProperty(fieldproperty);
    }

    protected Object clone(){
        return clone(null);
    }

    public Object clone(BulkInfo bulkinfo){
        try{
            FieldPropertySet fieldpropertyset = (FieldPropertySet)super.clone();
            fieldpropertyset.properties = (OrderedHashtable)properties.clone();
            fieldpropertyset.bulkInfo = bulkinfo;
            return fieldpropertyset;
        }catch(CloneNotSupportedException _ex){
            return null;
        }
    }

    public BulkInfo getBulkInfo(){
        return bulkInfo;
    }

    public Enumeration getFieldProperties(){
        return properties.elements();
    }

    public FieldProperty getFieldProperty(String name){
        return (FieldProperty)properties.get(name);
    }

    public Dictionary getFieldPropertyDictionary(){
        return properties;
    }

    public String getLabel(){
        return label;
    }

    public String getName(){
        return name;
    }

    public void setBulkInfo(BulkInfo bulkinfo){
        bulkInfo = bulkinfo;
    }

    public void setLabel(String s){
        label = s;
    }

    public void setName(String s){
        name = s;
    }

    public String toString(){
        StringBuffer stringbuffer = new StringBuffer();
        for(Enumeration enumeration = properties.elements(); enumeration.hasMoreElements(); stringbuffer.append('\n'))
            stringbuffer.append(enumeration.nextElement());
        return stringbuffer.toString();
    }
}
