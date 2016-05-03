package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPattern

public class IncludeFetchPattern extends FetchPattern
    implements Serializable
{

    public IncludeFetchPattern()
    {
    }

    public IncludeFetchPattern(String s)
    {
        super(s);
    }

    public boolean excludePrefix(String s, boolean flag)
    {
        if(flag)
        {
            if(super.starred)
                return !super.pattern.startsWith(s) && !s.startsWith(super.pattern);
            else
                return !super.pattern.startsWith(s);
        } else
        {
            return false;
        }
    }

    public boolean includePrefix(String s, boolean flag)
    {
        if(!flag)
            return super.starred && s.startsWith(super.pattern);
        else
            return true;
    }

    public boolean match(String s, boolean flag)
    {
        if(!flag)
            return match(s);
        else
            return true;
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openInlineTag("include");
        xmlwriter.printAttribute("pattern", super.starred ? super.pattern + "*" : super.pattern, "");
        xmlwriter.closeLastTag();
    }
}