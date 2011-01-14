package it.cnr.jada.persistency;

import java.io.Serializable;

public interface Persistent
    extends Serializable
{

    public abstract boolean equals(Object obj);
}