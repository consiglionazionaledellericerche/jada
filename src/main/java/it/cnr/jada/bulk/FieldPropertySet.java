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

import it.cnr.jada.util.OrderedHashtable;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Optional;

public class FieldPropertySet implements Cloneable, Serializable {

    private String name;
    private OrderedHashtable<String, FieldProperty> properties;
    private BulkInfo bulkInfo;
    private String label;

    public FieldPropertySet() {
        properties = new OrderedHashtable<String, FieldProperty>();
    }

    public FieldPropertySet(BulkInfo bulkinfo) {
        this.properties = new OrderedHashtable<String, FieldProperty>();
        this.bulkInfo = bulkinfo;
    }

    public FieldPropertySet(BulkInfo bulkinfo, String name) {
        this.properties = new OrderedHashtable<String, FieldProperty>();
        this.bulkInfo = bulkinfo;
        this.name = name;
    }

    public void addColumnFieldProperty(ColumnFieldProperty columnfieldproperty) {
        addFieldProperty(columnfieldproperty);
    }

    public void addFieldProperties(FieldPropertySet fieldpropertyset) {
        FieldProperty fieldproperty;
        for (Enumeration<FieldProperty> enumeration = fieldpropertyset.properties.elements(); enumeration.hasMoreElements(); properties.put(fieldproperty.getName(), fieldproperty))
            fieldproperty = (FieldProperty) enumeration.nextElement();

    }

    public void addFieldProperty(FieldProperty fieldproperty) {
        if (Optional.ofNullable(fieldproperty.getExtend()).isPresent()) {
            Optional.ofNullable(getFieldProperty(fieldproperty.getExtend()))
                    .ifPresent(fieldPropertyFrom -> {
                        fieldproperty.fillNullsFrom(fieldPropertyFrom);
                    });
        }
        bulkInfo.completeFieldProperty(fieldproperty);
        properties.put(fieldproperty.getName(), fieldproperty);
    }

    public void addFindFieldProperty(FieldProperty fieldproperty) {
        addFieldProperty(fieldproperty);
    }

    public void addFormFieldProperty(FieldProperty fieldproperty) {
        addFieldProperty(fieldproperty);
    }

    public void addPrintFieldProperty(FieldProperty fieldproperty) {
        addFieldProperty(fieldproperty);
    }

    protected Object clone() {
        return clone(null);
    }

    public Object clone(BulkInfo bulkinfo) {
        try {
            FieldPropertySet fieldpropertyset = (FieldPropertySet) super.clone();
            fieldpropertyset.properties = (OrderedHashtable) properties.clone();
            fieldpropertyset.bulkInfo = bulkinfo;
            return fieldpropertyset;
        } catch (CloneNotSupportedException _ex) {
            return null;
        }
    }

    public BulkInfo getBulkInfo() {
        return bulkInfo;
    }

    public void setBulkInfo(BulkInfo bulkinfo) {
        bulkInfo = bulkinfo;
    }

    public Enumeration<FieldProperty> getFieldProperties() {
        return properties.elements();
    }

    public FieldProperty getFieldProperty(String name) {
        return (FieldProperty) properties.get(name);
    }

    public Dictionary getFieldPropertyDictionary() {
        return properties;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String s) {
        label = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer();
        for (Enumeration enumeration = properties.elements(); enumeration.hasMoreElements(); stringbuffer.append('\n'))
            stringbuffer.append(enumeration.nextElement());
        return stringbuffer.toString();
    }
}
