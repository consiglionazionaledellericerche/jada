package it.cnr.jada.util.action;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.excel.bp.OfflineExcelSpoolerBP;
import it.cnr.jada.excel.ejb.BframeExcelComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.TransactionClosedException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Optional;

// Referenced classes of package it.cnr.jada.util.action:
//			  SelezionatoreAction, SelezionatoreListaBP, AbstractSelezionatoreBP, Selection, 
//			  FormBP, OptionBP, BulkListPrintBP, FormAction, 
//			  BulkAction

public class SelezionatoreListaAction extends SelezionatoreAction
        implements Serializable {
    BframeExcelComponentSession bframeExcelComponentSession = (BframeExcelComponentSession) EJBCommonServices.createEJB("BFRAMEEXCEL_EJB_BframeExcelComponentSession");

    public SelezionatoreListaAction() {
    }

    public Forward basicDoBringBack(ActionContext actioncontext)
            throws BusinessProcessException {
        SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
        HookForward hookforward = (HookForward) actioncontext.findForward("seleziona");
        if (selezionatorelistabp.getSelectionListener() == null) {
            hookforward.addParameter("selectedElements", selezionatorelistabp.getSelectedElements(actioncontext));
            hookforward.addParameter("selection", selezionatorelistabp.getSelection());
            hookforward.addParameter("focusedElement", selezionatorelistabp.getFocusedElement(actioncontext));
        }
        actioncontext.closeBusinessProcess();
        return hookforward;
    }

    public Forward doBringBack(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            if (selezionatorelistabp.getSelection().isEmpty() && selezionatorelistabp.getSelection().getFocus() < 0) {
                selezionatorelistabp.setMessage(2, "E' necessario selezionare almeno un elemento");
                return actioncontext.findDefaultForward();
            } else {
                return basicDoBringBack(actioncontext);
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.clearSelection(actioncontext);
            return super.doCloseForm(actioncontext);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfirmSort(ActionContext actioncontext, OptionBP optionbp)
            throws BusinessProcessException {
        try {
            AbstractSelezionatoreBP _tmp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
            if (optionbp.getOption() == 4)
                return doConfirmSort(actioncontext, (String) optionbp.getAttribute("name"), (String) optionbp.getAttribute("feature"));
            else
                return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfirmSort(ActionContext actioncontext, String s, String s1)
            throws BusinessProcessException {
        try {
            AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
            int i = abstractselezionatorebp.getOrderBy(s1);
            switch (i) {
                case 0: // '\0'
                    abstractselezionatorebp.setOrderBy(actioncontext, s1, 1);
                    break;

                case 1: // '\001'
                    abstractselezionatorebp.setOrderBy(actioncontext, s1, -1);
                    break;

                case -1:
                    abstractselezionatorebp.setOrderBy(actioncontext, s1, 0);
                    break;
            }
            abstractselezionatorebp.reset(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doGotoPage(ActionContext actioncontext, int i) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            selezionatorelistabp.goToPage(actioncontext, i);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doMultipleSelection(ActionContext actioncontext)
            throws BusinessProcessException {
        return doBringBack(actioncontext);
    }

    public Forward doNextFrame(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            selezionatorelistabp.goToPage(actioncontext, selezionatorelistabp.getCurrentPage() + selezionatorelistabp.getPageFrameSize());
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNextPage(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            selezionatorelistabp.goToPage(actioncontext, selezionatorelistabp.getCurrentPage() + 1);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPreviousFrame(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            selezionatorelistabp.goToPage(actioncontext, selezionatorelistabp.getCurrentPage() - selezionatorelistabp.getPageFrameSize());
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPreviousPage(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            selezionatorelistabp.goToPage(actioncontext, selezionatorelistabp.getCurrentPage() - 1);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPrint(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            BulkListPrintBP bulklistprintbp = (BulkListPrintBP) actioncontext.createBusinessProcess(selezionatorelistabp.getPrintbp());
            bulklistprintbp.setColumns(selezionatorelistabp.getColumns());
            bulklistprintbp.setIterator(actioncontext, selezionatorelistabp.detachIterator());
            if (selezionatorelistabp.getBulkInfo() != null)
                bulklistprintbp.setTitle(selezionatorelistabp.getBulkInfo().getLongDescription());
            else
                bulklistprintbp.setTitle("Lista");
            actioncontext.closeBusinessProcess();
            return actioncontext.addBusinessProcess(bulklistprintbp);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (RemoteException remoteexception) {
            return handleException(actioncontext, remoteexception);
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
            OggettoBulk bulk = bframeExcelComponentSession.addQueue(actioncontext.getUserContext(), columnLabel, columnHeaderLabel, longDescription, colonnedaEstrarre, query.toString(), query.getColumnMap(), (OggettoBulk) Introspector.newInstance(selezionatorelistabp.getBulkInfo().getBulkClass(), new Object[]{}));
            OfflineExcelSpoolerBP excelSpoolerBP = (OfflineExcelSpoolerBP) actioncontext.createBusinessProcess("OfflineExcelSpoolerBP");
            excelSpoolerBP.setModel(actioncontext, bulk);
            return actioncontext.addBusinessProcess(excelSpoolerBP);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSelectAll(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.selectAll(actioncontext);
            return basicDoBringBack(actioncontext);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
            throws BusinessProcessException {
        try {
            if (!s.startsWith("mainTable")) {
                return super.doSelection(actioncontext, s);
            } else {
                AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
                abstractselezionatorebp.setFocus(actioncontext);
                return doBringBack(actioncontext);
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSort(ActionContext actioncontext, String s, String s1)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            selezionatorelistabp.saveSelection(actioncontext);
            if (selezionatorelistabp.getSelection().isEmpty()) {
                return doConfirmSort(actioncontext, s, s1);
            } else {
                OptionBP optionbp = openConfirm(actioncontext, "L'operazione richiesta comporta la perdita degli elementi selezionati. Vuoi continuare?", 2, "doConfirmSort");
                optionbp.addAttribute("name", s);
                optionbp.addAttribute("feature", s1);
                return optionbp;
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doMostraColonneNascoste(ActionContext actioncontext) throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            for (Enumeration columns = actioncontext.getUserContext().getHiddenColumns().keys(); columns.hasMoreElements(); ) {
                String name = (String) columns.nextElement();
                if (name.startsWith(selezionatorelistabp.getPath())) {
                    actioncontext.getUserContext().getHiddenColumns().remove(name);
                }
            }
            selezionatorelistabp.setHiddenColumnButtonHidden(true);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doHiddenColumn(ActionContext actioncontext, String s, String s1) throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            String key = selezionatorelistabp.getPath().concat("-").concat(s1);
            actioncontext.getUserContext().getHiddenColumns().put(key, selezionatorelistabp.getColumns().get(s1));
            selezionatorelistabp.setHiddenColumnButtonHidden(false);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    protected Forward handleException(ActionContext actioncontext, Throwable throwable) {
        try {
            throw throwable;
        } catch (TransactionClosedException _ex) {
        } catch (Throwable throwable1) {
            return super.handleException(actioncontext, throwable1);
        }
        try {
            doCloseForm(actioncontext);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, ((Throwable) (businessprocessexception)));
        }
        setErrorMessage(actioncontext, "Tempo a disposizione per la ricerca scaduto.");
        return actioncontext.findDefaultForward();
    }

    public Forward doCancellaFiltro(ActionContext context) {
        SelezionatoreListaBP bp = Optional.ofNullable(context.getBusinessProcess())
                .filter(SelezionatoreListaBP.class::isInstance)
                .map(SelezionatoreListaBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business process non trovato!"));
        bp.setCondizioneCorrente(null);
        Optional<CRUDBP> crudbp = Optional.ofNullable(bp.getParent())
                .filter(CRUDBP.class::isInstance)
                .map(CRUDBP.class::cast);
        if (crudbp.isPresent()) {
            try {
                bp.setIterator(context, crudbp.get().getSearchProvider().search(context, null, crudbp.get().createEmptyModelForSearch(context)));
            } catch (RemoteException|BusinessProcessException e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    public Forward doRicercaLibera(ActionContext context) {
        try {
            SelezionatoreListaBP bp = Optional.ofNullable(context.getBusinessProcess())
                    .filter(SelezionatoreListaBP.class::isInstance)
                    .map(SelezionatoreListaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business process non trovato!"));
            Optional<CRUDBP> crudbp = Optional.ofNullable(bp.getParent())
                    .filter(CRUDBP.class::isInstance)
                    .map(CRUDBP.class::cast);

            if (crudbp.isPresent()) {
                SearchProvider searchProvider = Optional.ofNullable(bp.getFormField())
                        .map(formField -> crudbp.get().getSearchProvider(crudbp.get().getModel(), formField.getField().getProperty()))
                        .map(SearchProvider.class::cast)
                        .orElseGet(() -> crudbp.get().getSearchProvider());
                OggettoBulk oggettoBulk = Optional.ofNullable(bp.getFormField())
                        .map(formField -> bp.getBulkInfo())
                        .map(bulkInfo -> bulkInfo.getBulkClass())
                        .map(aClass -> {
                            try {
                                return aClass.newInstance();
                            } catch (InstantiationException | IllegalAccessException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        })
                        .filter(OggettoBulk.class::isInstance)
                        .map(OggettoBulk.class::cast)
                        .orElse(crudbp.get().createEmptyModelForFreeSearch(context));

                RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
                ricercaLiberaBP.setSearchProvider(searchProvider);
                ricercaLiberaBP.setFreeSearchSet("default");
                ricercaLiberaBP.setShowSearchResult(false);
                ricercaLiberaBP.setCanPerformSearchWithoutClauses(true);
                ricercaLiberaBP.setPrototype(oggettoBulk);
                Optional.ofNullable(bp.getCondizioneCorrente())
                        .filter(condizioneComplessaBulk -> !condizioneComplessaBulk.children.isEmpty())
                        .ifPresent(condizioneRicercaBulk -> {
                            ricercaLiberaBP.setCondizioneRadice(condizioneRicercaBulk);
                            ricercaLiberaBP.setCondizioneCorrente(
                                    (CondizioneRicercaBulk)condizioneRicercaBulk.children.stream()
                                            .filter(CondizioneSempliceBulk.class::isInstance)
                                            .map(CondizioneSempliceBulk.class::cast)
                                            .reduce((first, second) -> second)
                                            .orElse(null)
                            );
                            ricercaLiberaBP.setRadice(condizioneRicercaBulk);
                        });
                context.addHookForward("searchResult",this,"doRigheSelezionate");
                context.addHookForward("close", this, "doCloseRicercaLibera");
                return context.addBusinessProcess(ricercaLiberaBP);
            } else {
                return context.findDefaultForward();
            }
        } catch(Throwable e) {
            return handleException(context,e);
        }
    }

    public Forward doCloseRicercaLibera(ActionContext context) {
        return context.findDefaultForward();
    }

    public Forward doRigheSelezionate(ActionContext context) {
        try {
            SelezionatoreListaBP bp = Optional.ofNullable(context.getBusinessProcess())
                    .filter(SelezionatoreListaBP.class::isInstance)
                    .map(SelezionatoreListaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Business process non trovato!"));
            HookForward hook = (HookForward)context.getCaller();
            Optional.ofNullable(hook.getParameter("remoteIterator"))
                    .filter(RemoteIterator.class::isInstance)
                    .map(RemoteIterator.class::cast)
                    .ifPresent(ri -> {
                        try {
                            bp.setIterator(context,ri);
                        } catch (RemoteException|BusinessProcessException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    });
            Optional.ofNullable(hook.getParameter("condizioneRadice"))
                    .filter(CondizioneComplessaBulk.class::isInstance)
                    .map(CondizioneComplessaBulk.class::cast)
                    .ifPresent(condizioneRicercaBulk -> {
                        bp.setCondizioneCorrente(condizioneRicercaBulk);
                    });
            bp.setDirty(true);
            return context.findDefaultForward();
        } catch(Throwable e) {
            return handleException(context,e);
        }
    }
}