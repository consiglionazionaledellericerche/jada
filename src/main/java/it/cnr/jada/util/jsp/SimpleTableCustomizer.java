package it.cnr.jada.util.jsp;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Serializable;


public class SimpleTableCustomizer
        implements Serializable, TableCustomizer {

    public SimpleTableCustomizer() {
    }

    public String getRowStyle(Object obj) {
        return null;
    }

    public boolean isRowEnabled(Object obj) {
        return false;
    }

    public boolean isRowReadonly(Object obj) {
        return false;
    }

    @Override
    public String getTableClass() {
        return null;
    }

    @Override
    public void writeTfoot(JspWriter jspwriter) throws IOException {

    }
}