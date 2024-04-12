/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created on Jan 26, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.excel.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.excel.bp.ExcelSpoolerBP;
import it.cnr.jada.excel.bp.OfflineExcelSpoolerBP;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.ejb.BframeExcelComponentSession;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * @author mspasiano
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExcelSpoolerAction extends SelezionatoreListaAction {

    BframeExcelComponentSession bframeExcelComponentSession = (BframeExcelComponentSession) EJBCommonServices.createEJB("BFRAMEEXCEL_EJB_BframeExcelComponentSession");

    /**
     *
     */
    public ExcelSpoolerAction() {
        super();
    }

    public Forward doDelete(ActionContext context) {
        try {
            ExcelSpoolerBP bp = (ExcelSpoolerBP) context.getBusinessProcess();
            bp.setSelection(context);
            Excel_spoolerBulk[] array = null;
            if (!bp.getSelection().isEmpty()) {
                array = new Excel_spoolerBulk[bp.getSelection().size()];
                int j = 0;
                for (it.cnr.jada.util.action.SelectionIterator i = bp.getSelection().iterator(); i.hasNext(); )
                    array[j++] = (Excel_spoolerBulk) bp.getElementAt(context, i.nextIndex());
            } else if (bp.getFocusedElement() != null) {
                array = new Excel_spoolerBulk[1];
                array[0] = (Excel_spoolerBulk) bp.getFocusedElement();
            }
            if (array != null) {
                bp.createComponentSession().deleteJobs(context.getUserContext(), array);
                for (int i = 0; i < array.length; i++) {
                    try {
                        Excel_spoolerBulk excel = array[i];
                        if (excel.getServer() == null || excel.getNome_file() == null)
                            continue;
                        StringBuffer reportServerURL = new StringBuffer(excel.getServer());
                        reportServerURL.append("?user=");
                        reportServerURL.append(java.net.URLEncoder.encode(context.getUserContext().getUser()));
                        reportServerURL.append("&file=");
                        reportServerURL.append(java.net.URLEncoder.encode(excel.getNome_file()));
                        reportServerURL.append("&command=delete");
                        java.net.URLConnection conn = new java.net.URL(reportServerURL.toString()).openConnection();
                        conn.setUseCaches(false);
                        conn.connect();
                        java.io.InputStream is = conn.getInputStream();
                        is.close();
                    } catch (java.io.IOException e) {
                        // Non posso fare molto... i file sono gi? stati cancellati dalla
                        // tabella della coda!!
                    }
                }
            } else {
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare almeno una riga.");
            }
            bp.refresh(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doRefresh(ActionContext context) {
        try {
            ExcelSpoolerBP bp = (ExcelSpoolerBP) context.getBusinessProcess();
            bp.refresh(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doSelection(ActionContext context, String name) {
        try {
            ExcelSpoolerBP bp = (ExcelSpoolerBP) context.getBusinessProcess();
            bp.setFocus(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doExcel(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            Object parent;
            if (selezionatorelistabp.getClass().getName().equalsIgnoreCase(SelezionatoreListaBP.class.getName()))
                parent = selezionatorelistabp.getParent();
            else
                parent = selezionatorelistabp;
            String longDescription = "Estrazione";
            try {
                longDescription = selezionatorelistabp.getBulkInfo().getLongDescription().replace('/', '-').replace('\'', '-');
                if (longDescription.length() > 30)
                    longDescription = longDescription.substring(0, 30);
            } catch (java.lang.NullPointerException e) {
            }
            RemoteIterator remoteiterator = selezionatorelistabp.getIterator();
            Query query = null;
            if (remoteiterator instanceof TransactionalBulkLoaderIterator) {
                query = ((TransactionalBulkLoaderIterator) remoteiterator).getQuery();
            } else if (remoteiterator instanceof BulkLoaderIterator) {
                query = ((BulkLoaderIterator) remoteiterator).getQuery();
            }
            OrderedHashtable columnLabel = new OrderedHashtable();
            OrderedHashtable columnHeaderLabel = new OrderedHashtable();
            OrderedHashtable colonnedaEstrarre = new OrderedHashtable();
            colonne:
            for (Enumeration enumeration = selezionatorelistabp.getColumns().keys(); enumeration.hasMoreElements(); ) {
                String columnName = (String) enumeration.nextElement();
                Dictionary hiddenColumns = actioncontext.getUserContext().getHiddenColumns();
                if (hiddenColumns != null && selezionatorelistabp.getPath() != null) {
                    String key = selezionatorelistabp.getPath().concat("-").concat(columnName);
                    if (hiddenColumns.get(key) != null)
                        continue colonne;
                }
                colonnedaEstrarre.put(columnName, selezionatorelistabp.getColumns().get(columnName));
            }

            colonne:
            for (Enumeration enumeration = selezionatorelistabp.getColumns().elements(); enumeration.hasMoreElements(); ) {
                ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration.nextElement();
                Dictionary hiddenColumns = actioncontext.getUserContext().getHiddenColumns();
                if (hiddenColumns != null && selezionatorelistabp.getPath() != null) {
                    String key = selezionatorelistabp.getPath().concat("-").concat(columnfieldproperty.getName());
                    if (hiddenColumns.get(key) != null)
                        continue colonne;
                }
                String label = columnfieldproperty.getLabel(parent);
                if (label != null)
                    columnLabel.put(columnfieldproperty, label);
                String headerlabel = columnfieldproperty.getHeaderLabel(parent);
                if (headerlabel != null)
                    columnHeaderLabel.put(columnfieldproperty, headerlabel);
            }

            actioncontext.closeBusinessProcess(selezionatorelistabp);
            bframeExcelComponentSession.addQueue(actioncontext.getUserContext(), columnLabel, columnHeaderLabel, longDescription, colonnedaEstrarre, query.toString(), null, query.getColumnMap(), (OggettoBulk) Introspector.newInstance(selezionatorelistabp.getBulkInfo().getBulkClass(), new Object[]{}));
            OfflineExcelSpoolerBP excelSpoolerBP = (OfflineExcelSpoolerBP) actioncontext.createBusinessProcess("OfflineExcelSpoolerBP");
            return actioncontext.addBusinessProcess(excelSpoolerBP);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    @Override
    public Forward doCloseForm(ActionContext actioncontext)
            throws BusinessProcessException {
        Forward appoForward = super.doCloseForm(actioncontext);
        if (actioncontext.getBusinessProcess() instanceof OfflineExcelSpoolerBP) {
            appoForward = super.doCloseForm(actioncontext);
        }
        return appoForward;
    }
}
