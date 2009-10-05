/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.servlet;

import it.cnr.jada.util.ejb.HttpEJBCleaner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ExpireSessionServlet extends HttpServlet implements HttpSessionListener {
	private static final long serialVersionUID = -670381598120618354L;

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpEJBCleaner httpejbcleaner = (HttpEJBCleaner)se.getSession().getAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner");
		if (httpejbcleaner != null)
			httpejbcleaner.remove();
	}
}
