/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.io.Serializable;
import java.util.*;
/**
 * Classe di utilit� che rappresenta un dizionario ordinato, ovvero un dizionario che permette di leggere 
 * l'elenco delle chiavi e dei valori nello stesso ordine in cui sono stati inseriti.
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class OrderedHashtable extends Dictionary implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
    private Vector keys;
    private Hashtable values;
    public static final Hashtable EMPTY_HASHTABLE = new Hashtable();
	
	public OrderedHashtable(){
        keys = new Vector();
        values = new Hashtable();
    }

    public OrderedHashtable(int i){
        keys = new Vector();
        values = new Hashtable(i);
    }

    public OrderedHashtable(int i, float f){
        keys = new Vector();
        values = new Hashtable(i, f);
    }

    private OrderedHashtable(Vector vector, Hashtable hashtable){
        keys = vector;
        values = hashtable;
    }

    public synchronized void clear(){
        values.clear();
        keys.removeAllElements();
    }

    public Object clone(){
        return new OrderedHashtable((Vector)keys.clone(), (Hashtable)values.clone());
    }

    public synchronized Enumeration elements(){
        return new OrderedHashtableEnumerator(this);
    }

    public synchronized Object get(int i){
        return values.get(keys.elementAt(i));
    }

    public synchronized Object get(Object obj){
        return values.get(obj);
    }

    public synchronized boolean isEmpty(){
        return values.isEmpty();
    }

    public synchronized Enumeration keys(){
        return keys.elements();
    }

    public synchronized Object put(Object obj, Object obj1){
        if(values.get(obj) == null)
            keys.addElement(obj);
        return values.put(obj, obj1);
    }

    public synchronized Object putFirst(Object obj, Object obj1){
        if(values.get(obj) == null)
            keys.add(0, obj);
        return values.put(obj, obj1);
    }

    public synchronized Object remove(Object obj){
        keys.removeElement(obj);
        return values.remove(obj);
    }

    public synchronized int size(){
        return values.size();
    }
}