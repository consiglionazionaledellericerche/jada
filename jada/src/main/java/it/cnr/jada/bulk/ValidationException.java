package it.cnr.jada.bulk;

import java.io.Serializable;

public class ValidationException extends Exception
    implements Serializable
{

    public ValidationException()
    {
    }

    public ValidationException(String s)
    {
        super(s);
    }

    public ValidationException(String s, String s1)
    {
        super(s);
        fieldPropertyName = s1;
    }

    public String getFieldPropertyName()
    {
        return fieldPropertyName;
    }

    public void setFieldPropertyName(String s)
    {
        fieldPropertyName = s;
    }

    private String fieldPropertyName;
}