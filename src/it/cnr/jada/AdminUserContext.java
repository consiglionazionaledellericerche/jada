/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada;

import java.io.PrintWriter;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class AdminUserContext implements UserContext {
	private static final long serialVersionUID = 1320321812715720606L;
	private static AdminUserContext _instance;
	
	public static AdminUserContext getInstance(){
		if(_instance == null)
			_instance = new AdminUserContext();
		return _instance;
	}

	@Override
	public String getSessionId() {
		return null;
	}

	@Override
	public String getUser() {
		return "ADMIN";
	}

	@Override
	public void writeTo(PrintWriter printwriter) {
	}

}
