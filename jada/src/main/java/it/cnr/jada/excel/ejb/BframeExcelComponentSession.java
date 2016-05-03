package it.cnr.jada.excel.ejb;

import java.util.List;

import it.cnr.jada.UserContext;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;

import javax.ejb.Remote;
@Remote
public interface BframeExcelComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
	Excel_spoolerBulk addQueue(it.cnr.jada.UserContext param0,  it.cnr.jada.util.OrderedHashtable param1,   it.cnr.jada.util.OrderedHashtable param2,   String param3,  java.util.Dictionary param4,  String param5,   it.cnr.jada.persistency.sql.ColumnMap param6,   it.cnr.jada.bulk.OggettoBulk param7)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
  void deleteJobs(it.cnr.jada.UserContext param0,it.cnr.jada.excel.bulk.Excel_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
  it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaSchedulazione(UserContext userContext, Long long1, String resource) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Excel_spoolerBulk findExcelSpooler(UserContext userContext, Long pg)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void modifyQueue(UserContext userContext, Excel_spoolerBulk bulk)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
