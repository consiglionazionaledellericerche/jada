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

package it.cnr.jada.excel.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.excel.bp.ExcelSpoolerBP;
import it.cnr.jada.excel.bp.OfflineExcelSpoolerBP;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;

public class OfflineExcelSpoolerAction extends ExcelSpoolerAction {
    /**
     * OfflineReportPrintAction constructor comment.
     */
    public OfflineExcelSpoolerAction() {
        super();
    }

    public Forward doSendEMail(ActionContext context) {
        OfflineExcelSpoolerBP bp = (OfflineExcelSpoolerBP) context.getBusinessProcess();
        try {
            bp.fillModel(context);
        } catch (FillException e) {
            return handleException(context, e);
        }

        return context.findDefaultForward();
    }

    protected Forward handleException(ActionContext context, Throwable ex) {
        try {
            throw ex;
        } catch (it.cnr.jada.bulk.ValidationException e) {
            setErrorMessage(context, e.getMessage());
            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.FillException e) {
            setErrorMessage(context, e.getMessage());
            return context.findDefaultForward();
        } catch (it.cnr.jada.comp.CRUDConstraintException e) {
            ((OfflineExcelSpoolerBP) context.getBusinessProcess()).setErrorMessage(e.getUserMessage());
            return context.findDefaultForward();
        } catch (it.cnr.jada.comp.CRUDException e) {
            ((OfflineExcelSpoolerBP) context.getBusinessProcess()).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    public Forward doExcel(ActionContext actioncontext) {
        try {
            OfflineExcelSpoolerBP bp = (OfflineExcelSpoolerBP) actioncontext.getBusinessProcess();
            bp.fillModel(actioncontext);
            bp.controllaCampiEMail();
            Excel_spoolerBulk bulkmodel = (Excel_spoolerBulk) bp.getModel();
            bulkmodel.validate();
            if (bulkmodel != null) {
                bulkmodel.setStato(Excel_spoolerBulk.STATO_IN_CODA);
                bulkmodel.setDt_prossima_esecuzione(bulkmodel.getDt_partenza());
            } else {
                bp.setMessage("Bisogna indicare sia la data e ora, sia l'intervallo e l'unita' dell'intervallo");
            }
            bframeExcelComponentSession.modifyQueue(actioncontext.getUserContext(), bulkmodel);
            ExcelSpoolerBP excelSpoolerBP = (ExcelSpoolerBP) actioncontext.createBusinessProcess("ExcelSpoolerBP");
            excelSpoolerBP.setMessage("Consultazione accodata con successo");
            return actioncontext.addBusinessProcess(excelSpoolerBP);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doAnnulla(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            OfflineExcelSpoolerBP bp = (OfflineExcelSpoolerBP) actioncontext.getBusinessProcess();
            Excel_spoolerBulk bulkmodel = (Excel_spoolerBulk) bp.getModel();
            Excel_spoolerBulk[] array = null;
            array = new Excel_spoolerBulk[1];
            array[0] = bulkmodel;
            bframeExcelComponentSession.deleteJobs(actioncontext.getUserContext(), array);
            return actioncontext.closeBusinessProcess();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    @Override
    public Forward doCloseForm(ActionContext actioncontext)
            throws BusinessProcessException {
        OfflineExcelSpoolerBP bp = (OfflineExcelSpoolerBP) actioncontext.getBusinessProcess();
        Excel_spoolerBulk bulkmodel = (Excel_spoolerBulk) bp.getModel();
        Excel_spoolerBulk[] array = null;
        array = new Excel_spoolerBulk[1];
        array[0] = bulkmodel;
        try {
            bframeExcelComponentSession.deleteJobs(actioncontext.getUserContext(), array);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
        return super.doCloseForm(actioncontext);
    }
}