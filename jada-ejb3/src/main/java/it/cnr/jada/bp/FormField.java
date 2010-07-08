/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bp;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
/**
 * Oggetto che contiene il riferimento ad una FormFieldProperty e un FormController che ne fa uso
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class FormField implements Serializable{

	private static final long serialVersionUID = 1L;
    private FormController formController;
    private FieldProperty field;
    private OggettoBulk model;

	public FormField(FormController formcontroller, FieldProperty fieldproperty, OggettoBulk oggettobulk){
        formController = formcontroller;
        field = fieldproperty;
        model = oggettobulk;
    }

    public FieldProperty getField(){
        return field;
    }

    public FormController getFormController(){
        return formController;
    }

    public OggettoBulk getModel(){
        return model;
    }
}