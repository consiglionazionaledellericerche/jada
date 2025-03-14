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

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.servlet.jsp.JspWriter;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


public class ColumnFieldProperty extends FieldProperty implements Serializable {
    @JsonIgnore
    private String headerStyle;
    @JsonIgnore
    private String columnStyle;
    @JsonIgnore
    private String headerLabel;
    @JsonIgnore
    private Boolean textTruncate;

    public ColumnFieldProperty() {
        this.textTruncate = Boolean.FALSE;
    }

    public String getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(String s) {
        columnStyle = s;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String s) {
        headerStyle = s;
    }

    public Boolean isTextTruncate() {
        return textTruncate;
    }

    public void setTextTruncate(Boolean textTruncate) {
        this.textTruncate = textTruncate;
    }

    protected void writeCheckBox(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeCheckBox(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    public void writeColumnStyle(JspWriter jspwriter, Object obj, String s) throws IOException {
        writeStyle(jspwriter, s, columnStyle, obj);
    }

    public void writeColumnStyle(JspWriter jspwriter, Object obj, boolean flag, String s, int i) throws IOException {
        String cssClass = null;
        try {
            cssClass = (String) Introspector.getPropertyValue(obj, Introspector.buildMetodName("cssClass", getName()));
            Optional.ofNullable(Introspector.getPropertyValue(obj, this.getProperty()))
                    .ifPresent(o -> {
                        try {
                            jspwriter.println(" title=\"" + o + "\" ");
                        } catch (IOException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    });
        } catch (IntrospectionException e) {
        } catch (InvocationTargetException e) {
        }
        if (cssClass == null)
            cssClass = s;
        if (i == 5 || flag)
            writeStyle(jspwriter, cssClass, JSPUtils.mergeStyles(columnStyle, getStyle(), getFormatStyle(obj)), obj);
        else
            writeStyle(jspwriter, cssClass, columnStyle, obj);
    }

    protected void writeCRUDTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i != 5)
            super.writeCRUDTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    protected void writeVIEWTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i != 5)
            super.writeVIEWTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    public void writeHeaderStyle(JspWriter jspwriter, Object obj, String s) throws IOException {
        writeStyle(jspwriter, s, headerStyle, obj);
    }

    protected void writeInputStyle(JspWriter jspwriter, String s, String s1, Object obj) throws IOException {
        writeStyle(jspwriter, s != null ? s : "TableInput", s1, obj);
    }

    public void writeLabel(JspWriter jspwriter, Object obj, String s, boolean isBootstrap) throws IOException {
        if (getLabel() == null)
            jspwriter.print("&nbsp;");
        else
            super.writeLabel(jspwriter, obj, s, isBootstrap);
    }

    protected void writePassword(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writePassword(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap);
    }

    protected void writeRadioGroup(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeRadioGroup(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    public void writeReadonlyText(JspWriter jspwriter, Object obj, String cssClass, String attribute) throws IOException {
        try {
            String s2 = getStringValueFrom(obj);
            jspwriter.print("<span");
            writeInputStyle(jspwriter, cssClass, getStyle(), obj, s2);
            if (attribute != null) {
                jspwriter.print(' ');
                jspwriter.print(attribute);
            }
            jspwriter.print(">");
            String s3 = encodeHtmlText(s2).trim();
            if (s3.length() == 0)
                jspwriter.print("&nbsp;");
            else
                jspwriter.print(s3);
            jspwriter.print("</span>");
        } catch (Exception _ex) {
            jspwriter.print("&nbsp;");
        }
    }

    protected void writeReadonlyText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap) throws IOException, IntrospectionException, InvocationTargetException {
        writeReadonlyText(jspwriter, obj, s, s1);
    }

    protected void writeSearchTool(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i != 5)
            super.writeSearchTool(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
        else if (getPrintProperty() != null)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            jspwriter.print("&nbsp;");
    }

    protected void writeSearchToolWithLike(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i != 5)
            super.writeSearchToolWithLike(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
        else if (getPrintProperty() != null)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            jspwriter.print("&nbsp;");
    }

    protected void writeSelect(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeSelect(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    protected void writeText(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String cssClass, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5 || flag)
            writeReadonlyText(jspwriter, obj, cssClass, s1);
        else
            super.writeText(jspwriter, obj, flag, obj1, cssClass, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    protected void writeTextArea(JspWriter jspwriter, Object obj, boolean flag, Object obj1, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException, IntrospectionException, InvocationTargetException {
        if (i == 5 || flag)
            writeReadonlyText(jspwriter, obj, s, s1);
        else
            super.writeTextArea(jspwriter, obj, flag, obj1, s, s1, s2, i, fieldvalidationmap, isBootstrap);
    }

    public void writeLabel(Object bp, JspWriter jspwriter, Object obj, String s) throws IOException {
        if (getInputType().equals("BUTTON")) {
            return;
        } else {
            jspwriter.print("<span");
            writeLabelStyle(jspwriter, s, getLabelStyle(), obj);
            jspwriter.print(">");
            if (bp != null) {
                try {
                    String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("columnLabel", getName()));
                    jspwriter.print(encodeHtmlText(dLabel));
                } catch (InvocationTargetException e) {
                    jspwriter.print(encodeHtmlText(getLabel()));
                } catch (IntrospectionException e) {
                    try {
                        String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
                        jspwriter.print(encodeHtmlText(dLabel));
                    } catch (InvocationTargetException e1) {
                        jspwriter.print(encodeHtmlText(getLabel()));
                    } catch (IntrospectionException e1) {
                        jspwriter.print(encodeHtmlText(getLabel()));
                    }
                }
            } else
                jspwriter.print(encodeHtmlText(getLabel()));
            jspwriter.print("</span>");
            return;
        }
    }

    public void writeHeaderLabel(Object bp, JspWriter jspwriter, Object obj, String s) throws IOException {
        jspwriter.print("<span");
        writeLabelStyle(jspwriter, null, getLabelStyle(), obj);
        jspwriter.print(">");
        if (bp != null) {
            try {
                String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("headerLabel", getName()));
                jspwriter.print(encodeHtmlText(dLabel));
            } catch (InvocationTargetException e) {
                jspwriter.print(encodeHtmlText(s));
            } catch (IntrospectionException e) {
                jspwriter.print(encodeHtmlText(s));
            }
        } else
            jspwriter.print(encodeHtmlText(s));
        jspwriter.print("</span>");
    }

    @JsonIgnore
    public boolean isNotTableHeader() {
        return getHeaderLabel() == null;
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

    public String getLabel(Object bp) {
        try {
            String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("columnLabel", getName()));
            return dLabel;
        } catch (InvocationTargetException e) {
        } catch (IntrospectionException e) {
            try {
                String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
                return dLabel;
            } catch (IntrospectionException e1) {
            } catch (InvocationTargetException e1) {
            }
        }
        return getLabel();
    }

    public String getHeaderLabel(Object bp) {
        try {
            String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("headerLabel", getName()));
            return dLabel;
        } catch (InvocationTargetException e) {
        } catch (IntrospectionException e) {
        }
        return getHeaderLabel();
    }

    @Override
    public void createWithAnnotation(FieldPropertyAnnotation fieldPropertyAnnotation, String property) {
        super.createWithAnnotation(fieldPropertyAnnotation, property);
        setHeaderStyle(nvl(fieldPropertyAnnotation.headerStyle()));
        setColumnStyle(nvl(fieldPropertyAnnotation.columnStyle()));
        setHeaderLabel(nvl(fieldPropertyAnnotation.headerLabel()));
    }
    @Override
    public void writeLabel(Object bp, JspWriter jspwriter, Object obj, boolean isBootstrap)
            throws IOException {
        writeLabel(bp, jspwriter, obj, null, isBootstrap);
    }

    public void writeLabel(Object bp, JspWriter jspwriter, Object obj, String s, boolean isBootstrap)
            throws IOException {
        String label;
        if (getInputTypeIndex() == BUTTON) {
            return;
        } else {
            label = "";
            if (bp != null) {
                try {
                    String dLabel = (String) Introspector.getPropertyValue(bp, Introspector.buildMetodName("label", getName()));
                    label = encodeHtmlText(dLabel);
                } catch (InvocationTargetException | IntrospectionException e) {
                    label = encodeHtmlText(getLabel());
                }
            } else
                label = encodeHtmlText(getLabel());
        }
        jspwriter.print("<span");
        writeLabelStyle(jspwriter, s, getLabelStyle(), obj);
        if (isTextTruncate()) {
            jspwriter.print(" title=\"" + label +"\"");
        }
        jspwriter.print(">");
        jspwriter.print(label);
        jspwriter.print("</span>");
        return;
    }
}
