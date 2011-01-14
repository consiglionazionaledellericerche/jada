package it.cnr.jada.bulk;

import java.io.Serializable;
import java.text.ParseException;

public class ValidationParseException extends ParseException
    implements Serializable
{

    public ValidationParseException(String s, int i)
    {
        super(s, i);
        errorMessage = null;
        errorMessage = s;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    private String errorMessage;
}