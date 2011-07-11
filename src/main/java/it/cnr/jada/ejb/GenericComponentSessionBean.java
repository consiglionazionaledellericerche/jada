package it.cnr.jada.ejb;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.Component;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.sql.*;

import javax.annotation.Resource;
import javax.ejb.*;
@Stateless
public abstract class GenericComponentSessionBean implements GenericComponentSession{
	@Resource private SessionContext context;
	
    public GenericComponentSessionBean(){
    }

    protected final void component_invocation_failure(UserContext usercontext, Component component) throws EJBException{
        if(usercontext != null && usercontext.isTransactional()){
            try{
                Statement statement = component.getConnection(usercontext).createStatement();
                statement.execute("ROLLBACK TO SAVEPOINT " + getSavepointName());
                statement.close();
            }catch(Throwable _ex) { 
            }
        }else{
        	context.setRollbackOnly();
        }
        component.release(usercontext);
    }

    protected final void component_invocation_succes(UserContext usercontext, Component component) throws EJBException{
    	if(usercontext == null || !usercontext.isTransactional()){
            try{
                EJBCommonServices.unlockTransaction();
            }catch(EJBException ejbexception){
                throw new EJBException("Can't unlock transaction", ejbexception);
            }
    	}
    	component.release(usercontext);    
    }

    private String getSavepointName(){
        return "SP" + String.valueOf(Math.abs(getClass().getName().hashCode()));
    }

    protected final void pre_component_invocation(UserContext usercontext, Component component) throws EJBException{
        if(usercontext != null)
            if(usercontext.isTransactional()){
                try{
                    Statement statement = component.getConnection(usercontext).createStatement();
                    statement.execute("SAVEPOINT " + getSavepointName());
                    statement.close();
                }catch(EJBException ejbexception){
                    throw new EJBException("Can't initialize SAVEPOINT", ejbexception);
                }catch(SQLException sqlexception){
                    throw new EJBException("Can't initialize SAVEPOINT", sqlexception);
                } catch (ComponentException e) {
                    throw new EJBException("Can't initialize SAVEPOINT", e);
				}
            }else{
                try{
                    EJBCommonServices.lockTransaction();
                }catch(EJBException ejbexception1){
                    throw new EJBException("Can't lock transaction", ejbexception1);
                }
            }
        component.initialize();
    }
    protected final Error uncaughtError(UserContext usercontext, Component component, Error error) throws EJBException, Error{
        component_invocation_failure(usercontext, component);
        if(usercontext.isTransactional())
            throw new DetailedRuntimeException(error);
        else
            return error;
    }

    protected final RuntimeException uncaughtRuntimeException(UserContext usercontext, Component component, RuntimeException runtimeexception) throws EJBException, RuntimeException{
        component_invocation_failure(usercontext, component);
        if(usercontext.isTransactional())
            return new DetailedRuntimeException(runtimeexception);
        else
            throw runtimeexception;
    }
    @Remove
    public abstract void ejbRemove() throws javax.ejb.EJBException;

    public String getTransactionalInterface(){
    	String name = this.getClass().getName();
    	name = name.substring(0,name.lastIndexOf("Bean"));
		int i = name.lastIndexOf('.');
		name = name.substring(0, i + 1) + "Transactional" + name.substring(i + 1);
    	return name;
    }
}