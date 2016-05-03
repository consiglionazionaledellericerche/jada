package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;
import it.cnr.jada.util.XmlWriter;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchPolicy, FetchAllPolicy, FetchNonePolicy

class ComplexFetchPolicy
    implements Serializable, FetchPolicy
{
    class ComplexFetchPolicyIterator
        implements Serializable, Iterator
    {

        public boolean hasNext()
        {
            return fetchPolicy != null;
        }

        public Object next()
        {
            if(fetchPolicy instanceof ComplexFetchPolicy)
            {
                FetchPolicy fetchpolicy = ((ComplexFetchPolicy)fetchPolicy).fetchPolicy;
                fetchPolicy = ((ComplexFetchPolicy)fetchPolicy).nextFetchPolicy;
                return fetchpolicy;
            }
            if(fetchPolicy == null)
            {
                throw new NoSuchElementException();
            } else
            {
                FetchPolicy fetchpolicy1 = fetchPolicy;
                fetchPolicy = null;
                return fetchpolicy1;
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        private FetchPolicy fetchPolicy;

        ComplexFetchPolicyIterator()
        {
            fetchPolicy = ComplexFetchPolicy.this;
        }
    }


    ComplexFetchPolicy(FetchPolicy fetchpolicy, FetchPolicy fetchpolicy1)
    {
        fetchPolicy = fetchpolicy;
        nextFetchPolicy = fetchpolicy1;
    }

    public FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy == null)
            return this;
        if(fetchpolicy.equals(FetchAllPolicy.FETCHALL))
            return fetchpolicy;
        if(include(fetchpolicy))
            return this;
        if(fetchpolicy instanceof ComplexFetchPolicy)
        {
            FetchPolicy fetchpolicy1 = addFetchPolicy(((ComplexFetchPolicy)fetchpolicy).fetchPolicy);
            return fetchpolicy1.addFetchPolicy(((ComplexFetchPolicy)fetchpolicy).nextFetchPolicy);
        } else
        {
            return new ComplexFetchPolicy(fetchpolicy, this);
        }
    }

    public FetchPolicy addPrefix(String s)
    {
        if(s == null)
            return this;
        FetchPolicy fetchpolicy = fetchPolicy.addPrefix(s);
        if(fetchpolicy.equals(FetchNonePolicy.FETCHNONE))
            return nextFetchPolicy.addPrefix(s);
        if(nextFetchPolicy.equals(FetchAllPolicy.FETCHALL))
            return FetchAllPolicy.FETCHALL;
        FetchPolicy fetchpolicy1 = nextFetchPolicy.addPrefix(s);
        if(fetchpolicy1.equals(FetchNonePolicy.FETCHNONE))
            return fetchpolicy;
        if(fetchpolicy1.equals(FetchAllPolicy.FETCHALL))
            return FetchAllPolicy.FETCHALL;
        else
            return new ComplexFetchPolicy(fetchpolicy, fetchpolicy1);
    }

    public boolean excludePrefix(String s)
    {
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
            if(!((FetchPolicy)iterator1.next()).excludePrefix(s))
                return false;

        return true;
    }

    public boolean include(FetchPolicy fetchpolicy)
    {
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
            if(iterator1.next().equals(fetchpolicy))
                return true;

        return false;
    }

    public boolean include(String s)
    {
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
            if(((FetchPolicy)iterator1.next()).include(s))
                return true;

        return false;
    }

    public boolean includePrefix(String s)
    {
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
            if(((FetchPolicy)iterator1.next()).includePrefix(s))
                return true;

        return false;
    }

    public Iterator iterator()
    {
        return new ComplexFetchPolicyIterator();
    }

    public FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy)
    {
        if(fetchpolicy == null)
            return this;
        FetchPolicy fetchpolicy1 = null;
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
        {
            FetchPolicy fetchpolicy2 = (FetchPolicy)iterator1.next();
            if(!fetchpolicy.include(fetchpolicy2))
                if(fetchpolicy1 == null)
                    fetchpolicy1 = fetchpolicy2;
                else
                    fetchpolicy1 = fetchpolicy1.addFetchPolicy(fetchpolicy2);
        }

        return fetchpolicy1;
    }

    public String toString()
    {
        return XmlWriter.toString(this);
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("complexFetchPolicy");
        for(Iterator iterator1 = iterator(); iterator1.hasNext(); xmlwriter.println())
            ((FetchPolicy)iterator1.next()).writeTo(xmlwriter);

        xmlwriter.closeLastTag();
    }

    private FetchPolicy fetchPolicy;
    private FetchPolicy nextFetchPolicy;


}