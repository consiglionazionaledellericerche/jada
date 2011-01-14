package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

public abstract class FetchPattern
    implements XmlWriteable, Serializable
{

    public FetchPattern()
    {
    }

    public FetchPattern(String s)
    {
        this();
        setPattern(s);
    }

    public abstract boolean excludePrefix(String s, boolean flag);

    public String getPattern()
    {
        if(starred)
            return pattern + "*";
        else
            return pattern;
    }

    public abstract boolean includePrefix(String s, boolean flag);

    public boolean match(String s)
    {
        if(starred)
            return s.startsWith(pattern);
        else
            return s.equals(pattern);
    }

    public abstract boolean match(String s, boolean flag);

    public boolean matchPrefix(String s)
    {
        if(starred)
            return s.startsWith(pattern);
        else
            return pattern.startsWith(s);
    }

    void newMethod()
    {
    }

    public void setPattern(String s)
    {
        pattern = s;
        starred = s.charAt(s.length() - 1) == '*';
        if(starred)
            pattern = s.substring(0, s.length() - 1);
    }

    public String toString()
    {
        return XmlWriter.toString(this);
    }

    public abstract void writeTo(XmlWriter xmlwriter)
        throws IOException;

    protected String pattern;
    protected boolean starred;
}