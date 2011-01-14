package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;

public interface FetchPolicy
    extends XmlWriteable
{

    public abstract FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy);

    public abstract FetchPolicy addPrefix(String s);

    public abstract boolean excludePrefix(String s);

    public abstract boolean include(FetchPolicy fetchpolicy);

    public abstract boolean include(String s);

    public abstract boolean includePrefix(String s);

    public abstract FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy);
}