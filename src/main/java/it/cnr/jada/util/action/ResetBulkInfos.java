package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;

import javax.ejb.EJB;

public class ResetBulkInfos extends BusinessProcess implements Serializable{
	AdminSession adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");

    public ResetBulkInfos()
    {
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
        throws BusinessProcessException
    {
        BulkInfo.resetBulkInfos();
        Introspector.resetPropertiesCache();
        if(actioncontext instanceof HttpActionContext)
            ((HttpActionContext)actioncontext).resetActionMappings();
        Config.getHandler().reset();
        java.beans.Introspector.flushCaches();
        try{
        	adminSession.resetPersistentInfos();
        }catch(Throwable throwable){
            handleException(throwable);
        }
    }
}