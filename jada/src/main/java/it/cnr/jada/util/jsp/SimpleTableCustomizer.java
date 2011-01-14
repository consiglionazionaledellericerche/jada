package it.cnr.jada.util.jsp;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.jsp:
//            TableCustomizer

public class SimpleTableCustomizer
    implements Serializable, TableCustomizer
{

    public SimpleTableCustomizer()
    {
    }

    public String getRowStyle(Object obj)
    {
        return null;
    }

    public boolean isRowEnabled(Object obj)
    {
        return false;
    }

    public boolean isRowReadonly(Object obj)
    {
        return false;
    }
}