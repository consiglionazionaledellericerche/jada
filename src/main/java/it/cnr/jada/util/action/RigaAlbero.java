package it.cnr.jada.util.action;

import it.cnr.jada.util.NodoAlbero;

import java.io.Serializable;

public class RigaAlbero
    implements Serializable
{

    public RigaAlbero()
    {
    }

    public int getLivello()
    {
        return livello;
    }

    public NodoAlbero getNodo()
    {
        return nodo;
    }

    public boolean isUltimo()
    {
        return ultimo;
    }

    public void setLivello(int i)
    {
        livello = i;
    }

    public void setNodo(NodoAlbero nodoalbero)
    {
        nodo = nodoalbero;
    }

    public void setUltimo(boolean flag)
    {
        ultimo = flag;
    }

    private int livello;
    private NodoAlbero nodo;
    private boolean ultimo;
}