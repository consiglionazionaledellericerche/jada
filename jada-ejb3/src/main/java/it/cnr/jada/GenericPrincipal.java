/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada;

import java.security.Principal;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class GenericPrincipal implements Principal {
	private static final long serialVersionUID = 1320321812715720606L;
	private static GenericPrincipal _instance;
	private final String name;
	
	public static GenericPrincipal getInstance(String name){
		if(_instance == null)
			_instance = new GenericPrincipal(name);
		return _instance;
	}

	public GenericPrincipal(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
