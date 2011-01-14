package it.cnr.jada.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.persistency.beans.BeanIntrospector;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.EventTracer;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.EJBTracer;

import java.beans.Introspector;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;
import javax.ejb.Stateless;

@Stateless (name="JADAEJB_AdminSession")
public class AdminSessionBean implements AdminSession{
    static final long serialVersionUID = 0x2c7e5503d9bf9553L;

    public void addSQLTraceUser(String s){
        EJBCommonServices.getSqlEventTracer().addTraceUser(s);
    }

    public long getActiveBulkLoaderIteratorCounter(){
        return EJBTracer.getInstance().getActiveBulkLoaderIteratorCounter();
    }

    public long getActiveComponentCounter(){
        return EJBTracer.getInstance().getActiveComponentCounter();
    }

    public long getActiveUserTransactionCounter(){
        return EJBTracer.getInstance().getActiveUserTransactionCounter();
    }

    public String[] getTraceUsers(){
        if(EJBCommonServices.getSqlEventTracer() == null)
            return null;
        else
            return EJBCommonServices.getSqlEventTracer().getTraceUsers();
    }

    public byte[] getZippedTrace(UserContext usercontext){
        EventTracer eventtracer = EJBCommonServices.getSqlEventTracer();
        if(eventtracer == null)
            return null;
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try{
            PrintWriter printwriter = new PrintWriter(new GZIPOutputStream(bytearrayoutputstream));
            eventtracer.getTrace(usercontext, 100, printwriter);
            printwriter.close();
        }catch(IOException _ex) { }
        return bytearrayoutputstream.toByteArray();
    }

    public boolean isDumpStackTraceEnabled(){
        if(EJBCommonServices.getSqlEventTracer() == null)
            return false;
        else
            return EJBCommonServices.getSqlEventTracer().isDumpStackTraceEnabled();
    }

    public boolean isSqlTracerEnabled(){
        return EJBCommonServices.isSqlTracerEnabled();
    }

    public void removeSQLTraceUser(String s){
        EJBCommonServices.getSqlEventTracer().removeTraceUser(s);
    }

	public void loadBulkInfos(Class class1){
		try {		
		  BulkInfo.getBulkInfo(class1);
		} catch (Exception e) {
			System.out.println("Errore nel loadBulkInfos() - " + e.getMessage());
		}	
	}

	public void loadPersistentInfos(Class class1){
		try {
			BeanIntrospector.getSQLInstance().getPersistentInfo(class1);
		} catch (Exception e) {
			System.out.println("Errore nel loadPersistentInfos() - " + class1);
		}	
	}
	
    public void resetPersistentInfos(){
        BeanIntrospector.getSQLInstance().resetPersistentInfos();
        BeanIntrospector.getSQLInstance().resetPropertyCaches();
        BulkInfo.resetBulkInfos();
        Introspector.flushCaches();
        it.cnr.jada.util.Introspector.resetPropertiesCache();
        Config.getHandler().reset();
        BulkInfo.resetBulkInfos();
    }

    public void setDumpStackTraceEnabled(boolean flag){
        if(EJBCommonServices.getSqlEventTracer() == null){
            return;
        } else{
            EJBCommonServices.getSqlEventTracer().setDumpStackTraceEnabled(flag);
            return;
        }
    }

    public void setSqlTracerEnabled(boolean flag){
        EJBCommonServices.setSqlTracerEnabled(flag);
    }

}
