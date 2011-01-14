package it.cnr.jada.bulk;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

public class FieldValidationException extends DetailedException
    implements Serializable
{

    public FieldValidationException()
    {
    }

    public FieldValidationException(String s)
    {
        super(s);
    }

    public FieldValidationException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FieldValidationException(String s, Throwable throwable, String s1, String s2, String s3)
    {
        super(s, throwable);
        prefix = s1;
        fieldName = s2;
        text = s3;
    }

    public FieldValidationException(Throwable throwable)
    {
        super(throwable);
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getText()
    {
        return text;
    }

    public void setFieldName(String s)
    {
        fieldName = s;
    }

    public void setPrefix(String s)
    {
        prefix = s;
    }

    public void setText(String s)
    {
        text = s;
    }

    private String text;
    private String prefix;
    private String fieldName;
}