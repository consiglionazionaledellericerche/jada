/*
 * Created on Jan 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.excel.comp;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenericComponent;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.bulk.Excel_spooler_paramBulk;
import it.cnr.jada.excel.bulk.Excel_spooler_param_columnBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CHARToBooleanConverter;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.SendMail;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExcelComponent extends GenericComponent {
	public ExcelComponent() {
		super();
	}
	public Excel_spoolerBulk addQueue(UserContext usercontext,OrderedHashtable columnLabel, OrderedHashtable columnHeaderLabel, String longDescription,Dictionary columns,String query,it.cnr.jada.persistency.sql.ColumnMap columnMap,it.cnr.jada.bulk.OggettoBulk oggettoBulk) throws ComponentException{
		try {
			Excel_spoolerBulk excel_spooler = new Excel_spoolerBulk();
			excel_spooler.setSheet_name(longDescription);
			//excel_spooler.setStato(Excel_spoolerBulk.STATO_IN_CODA);
			//excel_spooler.setDt_prossima_esecuzione(excel_spooler.getDt_partenza());
			excel_spooler.setToBeCreated();
			insertBulk(usercontext,excel_spooler);
			BulkHome home = (BulkHome)homeCache.getHome(excel_spooler);

			oracle.sql.CLOB clob = (oracle.sql.CLOB)homeCache.getHome(excel_spooler).getSQLClob(excel_spooler,"QUERY");
			java.io.OutputStream os;
			try {
				os = new java.io.BufferedOutputStream(clob.getAsciiOutputStream());
				os.write(query.getBytes());
				os.close();				
			} catch (SQLException e) {
				throw new ComponentException(e);
			} catch (IOException e) {
				throw new ComponentException(e);
			}
			home.update(excel_spooler, usercontext);

			for(Enumeration enumeration = columns.elements(); enumeration.hasMoreElements();)
			{
			  ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration.nextElement();
			  ColumnMapping columnMapping = columnMap.getMappingForProperty(columnfieldproperty.getProperty());
			  if (columnMapping != null){
				Excel_spooler_paramBulk excel_param = new Excel_spooler_paramBulk();
				excel_param.setExcel_spooler(excel_spooler);
				excel_param.setColumn_name(columnMapping.getColumnName());
				excel_param.setColumn_type(columnMapping.getSqlTypeName(columnMapping.getSqlType()));
				excel_param.setColumn_label((String)columnLabel.get(columnfieldproperty));
				if(!columnfieldproperty.isNotTableHeader()){
				  excel_param.setHeader_label((String)columnHeaderLabel.get(columnfieldproperty));
				}
				insertBulk(usercontext,excel_param);
				if (columnMapping.getConverter() != null &&
				    columnMapping.getConverter().getClass().equals(CHARToBooleanConverter.class)){
					Excel_spooler_param_columnBulk excel_param_column = new Excel_spooler_param_columnBulk();
					excel_param_column.setExcel_spooler_param(excel_param);
					excel_param_column.setId_key("Y");
					excel_param_column.setValue("Si");
					insertBulk(usercontext,excel_param_column);
					
					excel_param_column = new Excel_spooler_param_columnBulk();
					excel_param_column.setExcel_spooler_param(excel_param);
					excel_param_column.setId_key("N");
					excel_param_column.setValue("No");
					insertBulk(usercontext,excel_param_column);															
				}
				if (columnfieldproperty.getKeysProperty() != null){
					Dictionary dictionary = (Dictionary)Introspector.getPropertyValue(oggettoBulk, columnfieldproperty.getKeysProperty());
					if (dictionary != null){
						for(Enumeration enumerationKey = dictionary.keys(); enumerationKey.hasMoreElements();){
							String id_key = enumerationKey.nextElement().toString();
							String value = null;
							if(dictionary.get(id_key) != null)
							  value = dictionary.get(id_key).toString();
							Excel_spooler_param_columnBulk excel_param_column = new Excel_spooler_param_columnBulk();
							excel_param_column.setExcel_spooler_param(excel_param);
							excel_param_column.setId_key(id_key);
							excel_param_column.setValue(value);
							insertBulk(usercontext,excel_param_column);
						}
					}
				}				
			  }
			}
			return excel_spooler; 
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (InvocationTargetException e) {
			throw new ComponentException(e);
		}
	}


	public void deleteJobs(it.cnr.jada.UserContext userContext, Excel_spoolerBulk[] excel_spooler) throws it.cnr.jada.comp.ComponentException {
		try {
			for (int i = 0;i < excel_spooler.length;i++) {
				excel_spooler[i] = (Excel_spoolerBulk)getHome(userContext,excel_spooler[i]).findAndLock(excel_spooler[i]);
				if (excel_spooler[i] == null)
					throw new ApplicationException("Una o pi? file sono stati cancellati da altri utenti.");
				if (Excel_spoolerBulk.STATO_IN_ESECUZIONE.equals(excel_spooler[i].getStato()))
					throw new ApplicationException("Una o pi? file sono attualmente in esecuzione e non possono essere cancellati.");
				deleteBulk(userContext,excel_spooler[i]);
			}
		} catch(PersistencyException e) {
		} catch(BusyResourceException e) {
		} catch(OutdatedResourceException e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			SQLBuilder sql = getHome(userContext,Excel_spoolerBulk.class,"V_EXCEL_SPOOLER").createSQLBuilder();
			sql.addClause("and","utcr",SQLBuilder.EQUALS, userContext.getUser());
			sql.addOrderBy("DACR DESC");
			return iterator(userContext,sql,Excel_spoolerBulk.class,null);
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public void cancellaSchedulazione(it.cnr.jada.UserContext userContext, Long pg, String indirizzoEMail) throws it.cnr.jada.comp.ComponentException {
		try {
			ArrayList<String> nuoviIndirizzi = new ArrayList<String>();
			StringBuffer bufferIndirizzi = new StringBuffer();
			Excel_spoolerBulk excelSpooler = (Excel_spoolerBulk)getHome(userContext,Excel_spoolerBulk.class).findByPrimaryKey(new Excel_spoolerBulk(pg));
			if (excelSpooler == null)
				return;
			StringTokenizer indirizzi = new StringTokenizer(excelSpooler.getEmail_a(),",");
			if (indirizzi.countTokens() == 1 && excelSpooler.getEmail_a().equalsIgnoreCase(indirizzoEMail)){
				deleteBulk(userContext,excelSpooler);
			}else{
				while(indirizzi.hasMoreElements()){
					String indirizzo = (String)indirizzi.nextElement();
					if (!indirizzo.equalsIgnoreCase(indirizzoEMail))
						nuoviIndirizzi.add(indirizzo);
}
				for (Iterator<String> iteratorIndirizzi = nuoviIndirizzi.iterator();iteratorIndirizzi.hasNext();){
					String ind = iteratorIndirizzi.next();
					bufferIndirizzi.append(ind);
					bufferIndirizzi.append(",");
				}
				bufferIndirizzi.deleteCharAt(bufferIndirizzi.length()-1);
				excelSpooler.setEmail_a(bufferIndirizzi.toString());
				excelSpooler.setToBeUpdated();
				updateBulk(userContext, excelSpooler);
			}
			SendMail.sendMail("Rimozione dalla lista di distribuzione di SIGLA", "Le confermiamo la rimozione dalla lista di distribuzione della \""+excelSpooler.getNome_file()+"\".", InternetAddress.parse(indirizzoEMail));
		} catch(PersistencyException e) {
			throw handleException(e);
		} catch (AddressException e) {
			throw handleException(e);
		}
	}
	public Excel_spoolerBulk findExcelSpooler(UserContext userContext, Long pg) throws ComponentException {
		try {
			return (Excel_spoolerBulk)getHome(userContext,Excel_spoolerBulk.class).findByPrimaryKey(new Excel_spoolerBulk(pg));
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	public void modifyQueue(UserContext userContext, Excel_spoolerBulk excelSpooler)throws ComponentException {
		try {
				excelSpooler.setToBeUpdated();
				updateBulk(userContext, excelSpooler);
		} catch (Exception e) {
			throw handleException(e);
		}
	}
}
