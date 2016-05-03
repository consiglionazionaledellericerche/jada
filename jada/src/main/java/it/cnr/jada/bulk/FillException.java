package it.cnr.jada.bulk;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            FieldProperty

public class FillException extends DetailedException
    implements Serializable
{

    public FillException()
    {
    }

    public FillException(String s)
    {
        super(s);
    }

    public FillException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FillException(String s, Throwable throwable, String s1, FieldProperty fieldproperty, String s2)
    {
        super(s, throwable);
        prefix = s1;
        text = s2;
        field = fieldproperty;
    }

    public FillException(Throwable throwable)
    {
        super(throwable);
    }

    public FieldProperty getField()
    {
        return field;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getText()
    {
        return text;
    }

    public void setField(FieldProperty fieldproperty)
    {
        field = fieldproperty;
    }

    public void setPrefix(String s)
    {
        prefix = s;
    }

    public void setText(String s)
    {
        text = s;
    }

    private FieldProperty field;
    private String prefix;
    private String text;
}