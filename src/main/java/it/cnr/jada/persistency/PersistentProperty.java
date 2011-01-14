package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

public class PersistentProperty
    implements Serializable
{

    public PersistentProperty()
    {
    }

    public PersistentProperty(String s)
    {
        name = s;
    }

    public String getName()
    {
        return name;
    }

    public boolean isPartOfOid()
    {
        return partOfOid;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setPartOfOid(boolean flag)
    {
        partOfOid = flag;
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("persistentProperty");
        xmlwriter.printAttribute("name", getName(), null);
        xmlwriter.printAttribute("partOfOid", isPartOfOid(), false);
        xmlwriter.closeLastTag();
    }

    private String name;
    private boolean partOfOid;
}