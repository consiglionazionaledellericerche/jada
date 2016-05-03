package it.cnr.jada.action;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

import it.cnr.jada.UserContext;
import it.cnr.jada.UserTransaction;

public class AdminUserContext implements UserContext {
	private static final long serialVersionUID = 7472364126199935204L;
	private static AdminUserContext instance = null;
	private Hashtable<String, Serializable> attributes = new Hashtable<String, Serializable>();
	
	protected AdminUserContext() {
		this(String.valueOf(serialVersionUID));
   	}
   	
	public AdminUserContext(String sessionId) {
		attributes.put("sessionId", sessionId);
	}

	public static AdminUserContext getInstance() {
      if(instance == null) {
         instance = new AdminUserContext();
      }
      return instance;
   	}

	public static AdminUserContext getInstance(String sessionId) {
	      if(instance == null) {
	         instance = new AdminUserContext(sessionId);
	      }
	      return instance;
	   	}
	
	public Dictionary<Object, Object> getHiddenColumns() {
		return null;
	}

	public String getSessionId() {
		return (String) attributes.get("sessionId");
	}

	public String getUser() {
		return "ADMIN";
	}

	public boolean isTransactional() {
		return false;
	}

	public void setTransactional(boolean flag) {

	}

	public void writeTo(PrintWriter printwriter) {
		printwriter.print("USER: ADMIN");
	}

	public Hashtable<String, Serializable> getAttributes() {
		return attributes;
	}
}
