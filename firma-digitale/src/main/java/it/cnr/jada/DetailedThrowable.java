package it.cnr.jada;

import java.io.Serializable;

interface DetailedThrowable
    extends Serializable, Cloneable
{

    public abstract Throwable getDetail();
}