/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import it.cnr.jada.AdminUserContext;
import it.cnr.jada.ejb.entity.LogErrore;
import it.cnr.jada.ejb.session.remote.LogComponentSession;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 * Session Bean implementation class LogComponentSessionBean
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Stateless(name="JADA_LogComponentSession")
@ExcludeDefaultInterceptors
@Remote(LogComponentSession.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class LogComponentSessionBean extends CRUDComponentSessionBean implements LogComponentSession {
	@PersistenceContext(unitName="SYSTEM")
	protected EntityManager manager;

	/**
     * @see CRUDComponentSessionBean#CRUDComponentSessionBean()
     */
    public LogComponentSessionBean() {
        super();
    }

	@ExcludeDefaultInterceptors	
	public void elaboraErrore(Throwable t) throws ComponentException {
		LogErrore errore = new LogErrore(getExceptionName(t));
		errore.setTrace(getExceptionStack(t));		
		errore.setToBeCreated();
		persist(AdminUserContext.getInstance(), errore);		
	}
	/** ritorna il nome della prima eccezione nello stack */
	private String getExceptionName(Throwable t) {
		String name = t.getClass().getSimpleName();
		Throwable cause = t.getCause();
		while(cause != null) {
			name = cause.getClass().getSimpleName();
			cause = cause.getCause();
		}		
		return name;
	}
	/** ritorna gli stacktrace dell'eccezione indicata in formato stringa */
	private String getExceptionStack(Throwable t) {
		StringBuilder sb = new StringBuilder(t.toString());
		StackTraceElement[] ste = t.getStackTrace();
		if (null !=ste) {
			for (int i = 0; i < ste.length; i++) {
				sb.append("\n\tat " + ste[i]);
			}
		}		
		Throwable parent = t;
        Throwable child;

        // Print the stack trace for each nested exception.
        while((child = parent.getCause()) != null) {
            if (child != null) {
                sb.append("\nCaused by: "  + child.toString());
                ste = child.getStackTrace();
        		if (null !=ste) {
        			for (int i = 0; i < ste.length; i++) {
        				sb.append("\n\tat " + ste[i]);
        			}
        		}
                parent = child;
            }
        }
		return sb.toString();
	}

}
