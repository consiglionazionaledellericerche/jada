/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import java.io.Serializable;
/**
 * Una eccezione di validazione su un campo di una Form. 
 * Viene generata quando non � possibile convertire il testo del campo nel formato compatibile con 
 * la property associata al campo o quando il tentativo di impostare il valore alla property genera 
 * una ValidationException
 *
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class FieldValidationException extends it.cnr.jada.DetailedException implements Serializable{
	private static final long serialVersionUID = 1L;
    private String text;
    private String prefix;
    private String fieldName;

	public FieldValidationException(){
    }

    public FieldValidationException(String s){
        super(s);
    }

    public FieldValidationException(String s, Throwable throwable){
        super(s, throwable);
    }

    public FieldValidationException(String s, Throwable throwable, String prefix, String fieldName, String text){
        super(s, throwable);
        this.prefix = prefix;
        this.fieldName = fieldName;
        this.text = text;
    }

    public FieldValidationException(Throwable throwable){
        super(throwable);
    }

    public String getFieldName(){
        return fieldName;
    }

    public String getPrefix(){
        return prefix;
    }

    public String getText(){
        return text;
    }

    public void setFieldName(String s){
        fieldName = s;
    }

    public void setPrefix(String s){
        prefix = s;
    }

    public void setText(String s){
        text = s;
    }
}