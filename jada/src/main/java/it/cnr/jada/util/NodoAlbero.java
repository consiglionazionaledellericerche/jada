package it.cnr.jada.util;

import java.util.Enumeration;

public interface NodoAlbero
{

    public abstract String getDescrizioneNodo();

    public abstract Enumeration getFigliNodo();

    public abstract Object getObject();
}