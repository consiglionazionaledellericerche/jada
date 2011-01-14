package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy

public class FetchNonePolicy
    implements Serializable, XmlWriteable, FetchPolicy
{

    private FetchNonePolicy()
    {
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy)
    {
        return fetchpolicy;
    }

    public FetchPolicy addPrefix(String s)
    {
        return this;
    }

    public boolean excludePrefix(String s)
    {
        return true;
    }

    public boolean include(FetchPolicy fetchpolicy)
    {
        return false;
    }

    public boolean include(String s)
    {
        return false;
    }

    public boolean includePrefix(String s)
    {
        return false;
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy == null)
            return this;
        else
            return null;
    }

    public String toString()
    {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("fetchPolicy");
        xmlwriter.printAttribute("name", "FETCHNONE", null);
        xmlwriter.openInlineTag("exclude");
        xmlwriter.printAttribute("pattern", "*", null);
        xmlwriter.closeLastTag();
        xmlwriter.closeLastTag();
    }

    public static final FetchNonePolicy FETCHNONE = new FetchNonePolicy();

}