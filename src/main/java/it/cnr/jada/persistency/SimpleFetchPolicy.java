package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy, FetchAllPolicy, ComplexFetchPolicy, FetchNonePolicy, 
//            PrefixedFetchPolicy, FetchPattern

public class SimpleFetchPolicy
    implements XmlWriteable, Serializable, FetchPolicy
{

    public SimpleFetchPolicy()
    {
        patterns = new ArrayList();
    }

    public SimpleFetchPolicy(String s)
    {
        patterns = new ArrayList();
        name = s;
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy == null)
            return this;
        if(fetchpolicy.equals(FetchAllPolicy.FETCHALL))
            return fetchpolicy;
        if(fetchpolicy instanceof ComplexFetchPolicy)
            return fetchpolicy.addFetchPolicy(this);
        if(fetchpolicy.equals(this))
            return this;
        else
            return new ComplexFetchPolicy(this, fetchpolicy);
    }

    public void addPattern(FetchPattern fetchpattern)
    {
        patterns.add(fetchpattern);
    }

    public FetchPolicy addPrefix(String s)
    {
        if(s == null)
            return this;
        if(includePrefix(s))
            return FetchAllPolicy.FETCHALL;
        if(excludePrefix(s))
            return FetchNonePolicy.FETCHNONE;
        else
            return new PrefixedFetchPolicy(this, s);
    }

    public boolean excludePrefix(String s)
    {
        s = s + '.';
        boolean flag = false;
        for(Iterator iterator = patterns.iterator(); iterator.hasNext();)
            flag = ((FetchPattern)iterator.next()).excludePrefix(s, flag);

        return flag;
    }

    public String getName()
    {
        return name;
    }

    public boolean include(FetchPolicy fetchpolicy)
    {
        return this == fetchpolicy;
    }

    public boolean include(String s)
    {
        boolean flag = true;
        for(Iterator iterator = patterns.iterator(); iterator.hasNext();)
            flag = ((FetchPattern)iterator.next()).match(s, flag);

        return flag;
    }

    public boolean includePrefix(String s)
    {
        s = s + '.';
        boolean flag = true;
        for(Iterator iterator = patterns.iterator(); iterator.hasNext();)
            flag = ((FetchPattern)iterator.next()).includePrefix(s, flag);

        return flag;
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy != null && fetchpolicy.include(this))
            return null;
        else
            return this;
    }

    public void setName(String s)
    {
        name = s;
    }

    public String toString()
    {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("fetchPolicy");
        xmlwriter.printAttribute("name", getName(), "default");
        for(Iterator iterator = patterns.iterator(); iterator.hasNext(); ((FetchPattern)iterator.next()).writeTo(xmlwriter));
        xmlwriter.closeLastTag();
    }

    private List patterns;
    private String name;
}