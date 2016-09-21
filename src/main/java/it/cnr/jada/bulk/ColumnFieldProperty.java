package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.jsp.JSPUtils;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspWriter;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class ColumnFieldProperty extends FieldProperty implements Serializable{
	@JsonIgnore
	private String headerStyle;
	@JsonIgnore
	private String columnStyle;
	@JsonIgnore
	private String headerLabel;
    
    public ColumnFieldProperty(){
    }

    public String getColumnStyle(){
        return columnStyle;
    }

    public String getHeaderStyle(){
        return headerStyle;
    }

    public void setColumnStyle(String s){
        columnStyle = s;
    }

    public void setHeaderStyle(String s){
        headerStyle = s;
    }

    protected void writeCheckBox(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeCheckBox(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    public void writeColumnStyle(JspWriter jspwriter, Object obj, String s) throws IOException{
        writeStyle(jspwriter, s, columnStyle, obj);
    }
    
    public void writeColumnStyle(JspWriter jspwriter, Object obj, boolean flag, String s, int i) throws IOException{
    	String cssClass=null; 
    	try {
			 cssClass=(String)Introspector.getPropertyValue(obj, Introspector.buildMetodName("cssClass", getName()));
		} catch (IntrospectionException e) {		
		} catch (InvocationTargetException e) {
		}
		if(cssClass==null)
			cssClass=s;
        if(i == 5 || flag)
            writeStyle(jspwriter, cssClass, JSPUtils.mergeStyles(columnStyle, getStyle(), getFormatStyle(obj)), obj);
        else
            writeStyle(jspwriter, cssClass, columnStyle, obj);
    }

    protected void writeCRUDTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i != 5)
            super.writeCRUDTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    public void writeHeaderStyle(JspWriter jspwriter, Object obj, String s) throws IOException{
        writeStyle(jspwriter, s, headerStyle, obj);
    }

    protected void writeInputStyle(JspWriter jspwriter, String s, String s1, Object obj) throws IOException{
        writeStyle(jspwriter, s != null ? s : "TableInput", s1, obj);
    }

    public void writeLabel(JspWriter jspwriter, Object obj, String s) throws IOException{
        if(getLabel() == null)
            jspwriter.print("&nbsp;");
        else
            super.writeLabel(jspwriter, obj, s);
    }
    
    protected void writePassword(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writePassword(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    protected void writeRadioGroup(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeRadioGroup(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    public void writeReadonlyText(JspWriter jspwriter, Object obj, String s, String s1) throws IOException{
        try{
            String s2 = getStringValueFrom(obj);
            jspwriter.print("<span");
            writeInputStyle(jspwriter, s, getStyle(), obj, s2);
            if(s1 != null){
                jspwriter.print(' ');
                jspwriter.print(s1);
            }
            jspwriter.print(">");
            String s3 = encodeHtmlText(s2).trim();
            if(s3.length() == 0)
                jspwriter.print("&nbsp;");
            else
                jspwriter.print(s3);
            jspwriter.print("</span>");
        }catch(Exception _ex){
            jspwriter.print("&nbsp;");
        }
    }

    protected void writeReadonlyText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        writeReadonlyText(jspwriter, obj, s, s1);
    }

    protected void writeSearchTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i != 5)
            super.writeSearchTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
        else
        if(getPrintProperty() != null)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            jspwriter.print("&nbsp;");
    }

    protected void writeSelect(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2,  int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeSelect(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    protected void writeText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5 || flag)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeText(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    protected void writeTextArea(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException{
        if(i == 5 || flag)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeTextArea(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }
	public void writeLabel(Object bp, JspWriter jspwriter, Object obj, String s) throws IOException{
		if(getInputType().equals("BUTTON")){
			return;
		} else{
			jspwriter.print("<span");
			writeLabelStyle(jspwriter, s, getLabelStyle(), obj);
			jspwriter.print(">");
			if (bp != null){
				try {
					String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("columnLabel", getName()));
					jspwriter.print(encodeHtmlText(dLabel));
				}catch (InvocationTargetException e) {
					jspwriter.print(encodeHtmlText(getLabel()));
				}catch (IntrospectionException e) {
					try {
						String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
						jspwriter.print(encodeHtmlText(dLabel));
					}catch (InvocationTargetException e1) {
						jspwriter.print(encodeHtmlText(getLabel()));
					}catch (IntrospectionException e1) {
						jspwriter.print(encodeHtmlText(getLabel()));
					}			
				}
			}else
			  jspwriter.print(encodeHtmlText(getLabel()));
			jspwriter.print("</span>");
			return;
		}
	}
	public void writeHeaderLabel(Object bp, JspWriter jspwriter, Object obj, String s) throws IOException{
		jspwriter.print("<span");
		writeLabelStyle(jspwriter, null , getLabelStyle(), obj);
		jspwriter.print(">");
		if (bp != null){
				try {
					String dLabel = (String)Introspector.getPropertyValue(bp, Introspector.buildMetodName("headerLabel", getName()));
					jspwriter.print(encodeHtmlText(dLabel));
				}catch (InvocationTargetException e) {
					jspwriter.print(encodeHtmlText(s));
				} catch (IntrospectionException e) {
					jspwriter.print(encodeHtmlText(s));
				}
			}else
			  jspwriter.print(encodeHtmlText(s));
		jspwriter.print("</span>");
	}
	@JsonIgnore
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
