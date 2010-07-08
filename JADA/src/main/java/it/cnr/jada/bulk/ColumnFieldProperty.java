/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.util.Introspector;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
/**
 * Classe che descrive le proprietà di una colonna da visualizzare in una TABLE HTML per visualizzare un 
 * elenco tabulare di OggettiBulk. Le ColumnFieldProperty sono definite nel BulkInfo di un OggettoBulk e 
 * possono essere elencate nel file ".info" associato.
 * Una columnFieldProperty possiede, oltre a quelle di FieldProperty, le seguenti proprietà: 
 * columnStyle Stile CSS da applicare alle celle della tabella. 
 * headerStyle Stile CSS da applicare alla testata della colonna.
 * headerLabel Stile CSS da applicare al ragruppamento della testata della colonna.
 * See Also:Serialized Form
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ColumnFieldProperty extends FieldProperty implements Serializable{

	private static final long serialVersionUID = 1L;
	private String headerStyle;
	private String columnStyle;
	private String headerLabel;
    
    public ColumnFieldProperty(){
    }
    /**
     * Restituisce il valore della propriet� 'columnStyle'
     */
    public String getColumnStyle(){
        return columnStyle;
    }
    /**
     * Restituisce il valore della propriet� 'headerStyle'
     */
    public String getHeaderStyle(){
        return headerStyle;
    }
    /**
     * Imposta il valore della propriet� 'columnStyle'
     */
    public void setColumnStyle(String newColumnStyle){
        columnStyle = newColumnStyle;
    }
    /**
     * Imposta il valore della propriet� 'headerStyle'
     */
    public void setHeaderStyle(String newHeaderStyle){
        headerStyle = newHeaderStyle;
    }
    public boolean isNotTableHeader(){
    	return getHeaderLabel()==null;
    }
	/**
	 * @return
	 */
	public String getHeaderLabel() {
		return headerLabel;
	}

	/**
	 * @param string
	 */
	public void setHeaderLabel(String string) {
		headerLabel = string;
	}
	
	public String getLabel(Object bp){
		try {
			String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("columnLabel", getName()));
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
		return getLabel();
	}
	
	public String getHeaderLabel(Object bp){
		try {
			String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("headerLabel", getName()));
			return dLabel;
		}catch (InvocationTargetException e) {
		} catch (IntrospectionException e) {
		}
		return getHeaderLabel();
	}
	@Override
	public void createWithAnnotation( FieldPropertyAnnotation fieldPropertyAnnotation, String property) {
		super.createWithAnnotation(fieldPropertyAnnotation, property);
		setHeaderStyle(nvl(fieldPropertyAnnotation.headerStyle()));
		setColumnStyle(nvl(fieldPropertyAnnotation.columnStyle()));
		setHeaderLabel(nvl(fieldPropertyAnnotation.headerLabel()));
	}	
}