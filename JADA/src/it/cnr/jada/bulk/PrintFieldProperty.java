/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;

/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PrintFieldProperty extends FieldProperty{

	private static final long serialVersionUID = 1L;
	public static final int TYPE_STRING = 0;
    public static final int TYPE_DATE = 1;
    public static final int TYPE_TIMESTAMP = 2;
    private int paramPosition;
    private int paramType;
    private String paramNameJR;
    private String paramTypeJR;

    public PrintFieldProperty(){
        paramType = TYPE_STRING;
    }

    void fillNullsFrom(FieldProperty fieldproperty){
        super.fillNullsFrom(fieldproperty);
        if(fieldproperty instanceof PrintFieldProperty){
            PrintFieldProperty printfieldproperty = (PrintFieldProperty)fieldproperty;
            if(paramPosition < 0)
                paramPosition = printfieldproperty.paramPosition;
            if(paramType == 0)
                paramType = printfieldproperty.paramType;
        }
    }

    public int getParameterPosition(){
        return getParamPosition();
    }

    public String getParameterType(){
        switch(paramPosition){
        case TYPE_DATE: // '\001'
            return "DATE";

        case TYPE_TIMESTAMP: // '\002'
            return "TIMESTAMP";

        case TYPE_STRING: // '\0'
        default:
            return "STRING";
        }
    }

    public int getParamPosition(){
        return paramPosition;
    }

    public int getParamType(){
        return paramType;
    }

    public void setParameterPosition(int i){
        setParamPosition(i);
    }

    public void setParameterType(String s){
        if("DATE".equalsIgnoreCase(s))
            setParamType(TYPE_DATE);
        if("STRING".equalsIgnoreCase(s))
            setParamType(TYPE_STRING);
        if("TIMESTAMP".equalsIgnoreCase(s))
            setParamType(TYPE_TIMESTAMP);
    }

    public void setParamPosition(int i){
        paramPosition = i;
    }

    public void setParamType(int i){
        paramType = i;
    }

	public String getParamNameJR() {
		return paramNameJR;
	}

	public void setParamNameJR(String paramNameJR) {
		this.paramNameJR = paramNameJR;
	}

	public String getParamTypeJR() {
		return paramTypeJR;
	}

	public void setParamTypeJR(String paramTypeJR) {
		this.paramTypeJR = paramTypeJR;
	}
	@Override
	public void createWithAnnotation(FieldPropertyAnnotation fieldPropertyAnnotation, String property) {
		super.createWithAnnotation(fieldPropertyAnnotation, property);
		setParamNameJR(nvl(fieldPropertyAnnotation.paramNameJR()));
		setParamTypeJR(nvl(fieldPropertyAnnotation.paramTypeJR()));
	}		
}