package it.cnr.jada.action;

import java.io.PrintWriter;
import java.util.Dictionary;

import it.cnr.jada.UserContext;

public class AdminUserContext implements UserContext {
	private static final long serialVersionUID = 7472364126199935204L;
	private static AdminUserContext instance = null;
	private java.lang.String sessionId;
	
	protected AdminUserContext() {
      // Exists only to defeat instantiation.
   	}
   	
	public AdminUserContext(String sessionId) {
		this.sessionId = sessionId;
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
	
	public Dictionary getHiddenColumns() {
		return null;
	}

	public String getSessionId() {
		return null;
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

}
