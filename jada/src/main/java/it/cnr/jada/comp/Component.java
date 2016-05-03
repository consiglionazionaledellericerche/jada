package it.cnr.jada.comp;

import java.sql.Connection;

import it.cnr.jada.UserContext;

import javax.ejb.EJBContext;

public interface Component{

    public abstract void release();
	
    public abstract void release(UserContext usercontext);

    public abstract void initialize();
    
    public abstract Connection getConnection(UserContext usercontext) throws ComponentException;

}