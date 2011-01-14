/*
 * Created on Oct 12, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.servlet;

/**
 * @author marco
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import it.cnr.jada.util.upload.*;

public class MultipartWrapper extends HttpServletRequestWrapper {

  MultipartRequest mreq = null;

  public MultipartWrapper(HttpServletRequest req, String dir)
									 throws IOException {
	super(req);
	mreq = new MultipartRequest(req, dir);
  }
  // Methods to replace HSR methods
  public Enumeration getParameterNames() {
	return mreq.getParameterNames();
  }
  public String getParameter(String name) {
	return mreq.getParameter(name);
  }
  public String[] getParameterValues(String name) {
	return mreq.getParameterValues(name);
  }
  public Map getParameterMap() {
	Map map = new HashMap();
	Enumeration numerazione = getParameterNames();
	while (numerazione.hasMoreElements()) {
	  String name = (String) numerazione.nextElement();
	  map.put(name, mreq.getParameterValues(name));
	}
	return map;
  }

  // Methods only in MultipartRequest
  public Enumeration getFileNames() {
	return mreq.getFileNames();
  }
  public String getFilesystemName(String name) {
	return mreq.getFilesystemName(name);
  }
  public String getContentType(String name) {
	return mreq.getContentType(name);
  }
  public UploadedFile getFile(String name) {
	return mreq.getFile(name);
  }
}