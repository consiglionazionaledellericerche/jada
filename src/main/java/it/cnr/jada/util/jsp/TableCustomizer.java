package it.cnr.jada.util.jsp;


public interface TableCustomizer
{

    public abstract String getRowStyle(Object obj);

    public abstract boolean isRowEnabled(Object obj);

    public abstract boolean isRowReadonly(Object obj);
}