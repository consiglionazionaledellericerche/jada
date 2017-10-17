package it.cnr.jada.util.jsp;


import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public interface TableCustomizer
{

    public abstract String getRowStyle(Object obj);

    public abstract boolean isRowEnabled(Object obj);

    public abstract boolean isRowReadonly(Object obj);

    public abstract String getTableClass();

    void writeTfoot(JspWriter jspwriter) throws IOException;
}