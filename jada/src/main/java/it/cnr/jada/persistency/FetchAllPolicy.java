package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy

public class FetchAllPolicy
    implements Serializable, XmlWriteable, FetchPolicy
{

    private FetchAllPolicy()
    {
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy)
    {
        return this;
    }

    public FetchPolicy addPrefix(String s)
    {
        return this;
    }

    public boolean excludePrefix(String s)
    {
        return false;
    }

    public boolean include(FetchPolicy fetchpolicy)
    {
        return true;
    }

    public boolean include(String s)
    {
        return true;
    }

    public boolean includePrefix(String s)
    {
        return true;
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy == this)
            return null;
        else
            return this;
    }

    public String toString()
    {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("fetchPolicy");
        xmlwriter.printAttribute("name", "FETCHALL", null);
        xmlwriter.openInlineTag("include");
        xmlwriter.printAttribute("pattern", "*", null);
        xmlwriter.closeLastTag();
        xmlwriter.closeLastTag();
    }

    public static final FetchAllPolicy FETCHALL = new FetchAllPolicy();

}