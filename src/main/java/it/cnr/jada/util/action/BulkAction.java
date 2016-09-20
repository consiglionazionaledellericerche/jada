package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.TransactionClosedException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

// Referenced classes of package it.cnr.jada.util.action:
//            FormAction, FormField, ModelController, CRUDController, 
//            BulkBP, FormBP, CRUDBP, UnsortableException, 
//            FormController, RicercaLiberaBP, SelezionatoreListaBP, SelectionListener

public class BulkAction extends FormAction
    implements Serializable
{

    public BulkAction()
    {
    }

    protected void blankSearch(ActionContext actioncontext, FormField formfield)
    {
        blankSearch(actioncontext, formfield, createEmptyModelForSearchTool(actioncontext, formfield));
    }

    protected void blankSearch(ActionContext actioncontext, FormField formfield, OggettoBulk oggettobulk)
    {
        try
        {
            OggettoBulk oggettobulk1 = formfield.getModel();
            if(oggettobulk1 != null)
            {
                formfield.getField().setValueIn(oggettobulk1, oggettobulk);
                formfield.getFormController().setDirty(true);
            }
        }
        catch(Exception exception)
        {
            throw new ActionPerformingError(exception);
        }
    }

    protected OggettoBulk createEmptyModelForSearchTool(ActionContext actioncontext, FormField formfield)
    {
        try
        {
            return (OggettoBulk)formfield.getField().getPropertyType().newInstance();
        }
        catch(InstantiationException instantiationexception)
        {
            throw new IntrospectionError(instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new IntrospectionError(illegalaccessexception);
        }
    }

    public Forward doAddToCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            fillModel(actioncontext);
            try
            {
                return (Forward)Introspector.invoke(this, "doAddToCRUD", s, actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                getController(actioncontext, s).add(actioncontext);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doBlankSearch(ActionContext actioncontext, String s)
    {
        try
        {
            BulkBP _tmp = (BulkBP)actioncontext.getBusinessProcess();
            try
            {
                fillModel(actioncontext);
            }
            catch(FillException _ex) { }
            FormField formfield = getFormField(actioncontext, s);
            OggettoBulk oggettobulk = formfield.getModel();
            oggettobulk.setToBeUpdated();
            try
            {
                return (Forward)Introspector.invoke(this, "doBlankSearch", formfield.getField().getName(), actioncontext, oggettobulk);
            }
            catch(NoSuchMethodException _ex) { }
            try
            {
                blankSearch(actioncontext, formfield, createEmptyModelForSearchTool(actioncontext, formfield));
                return actioncontext.findDefaultForward();
            }
            catch(Exception exception1)
            {
                return handleException(actioncontext, exception1);
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doBringBackCRUD(ActionContext actioncontext)
        throws RemoteException
    {
        HookForward hookforward = (HookForward)actioncontext.getCaller();
        OggettoBulk oggettobulk = (OggettoBulk)hookforward.getParameter("bringback");
        FormField formfield = (FormField)hookforward.getParameter("field");
        OggettoBulk oggettobulk1 = formfield.getFormController().getModel();
        if(oggettobulk != null)
        {
            try
            {
                return (Forward)Introspector.invoke(this, "doBringBackCRUD", formfield.getField().getName(), actioncontext, oggettobulk1, oggettobulk);
            }
            catch(NoSuchMethodException _ex) { }
            catch(Exception exception)
            {
                return handleException(actioncontext, exception);
            }
            try
            {
                formfield.getField().setValueIn(oggettobulk1, oggettobulk);
            }
            catch(Throwable throwable)
            {
                return handleException(actioncontext, throwable);
            }
        }
        return actioncontext.findDefaultForward();
    }

    public Forward doBringBackFilter(ActionContext actioncontext)
        throws RemoteException
    {
        HookForward hookforward = (HookForward)actioncontext.getCaller();
        return doBringBackFilter(actioncontext, (CRUDController)hookforward.getParameter("controller"), (CompoundFindClause)hookforward.getParameter("filter"));
    }

    protected Forward doBringBackFilter(ActionContext actioncontext, CRUDController crudcontroller, CompoundFindClause compoundfindclause)
        throws RemoteException
    {
        try
        {
            return (Forward)Introspector.invoke(this, "doBringBackFilter", crudcontroller.getControllerName(), compoundfindclause);
        }
        catch(NoSuchMethodException _ex) { }
        catch(Exception exception1)
        {
            return handleException(actioncontext, exception1);
        }
        try
        {
            if(compoundfindclause != null)
            {
                crudcontroller.setFilter(actioncontext, compoundfindclause);
                if(crudcontroller.countDetails() == 0)
                {
                    ((BulkBP)actioncontext.getBusinessProcess()).setMessage("La ricerca non ha fornito alcun risultato. Il filtro sar\340 rimosso.");
                    crudcontroller.setFilter(actioncontext, null);
                }
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doBringBackSearchResult(ActionContext actioncontext)
        throws RemoteException
    {
        HookForward hookforward = (HookForward)actioncontext.getCaller();
        return doBringBackSearchResult(actioncontext, (FormField)hookforward.getParameter("field"), (OggettoBulk)hookforward.getParameter("focusedElement"));
    }

    protected Forward doBringBackSearchResult(ActionContext actioncontext, FormField formfield, OggettoBulk oggettobulk)
        throws RemoteException
    {
        OggettoBulk oggettobulk1 = formfield.getFormController().getModel();
        oggettobulk1.setToBeUpdated();
        try
        {
            return (Forward)Introspector.invoke(this, "doBringBackSearch", formfield.getField().getName(), actioncontext, oggettobulk1, oggettobulk);
        }
        catch(NoSuchMethodException _ex) { }
        catch(Exception exception1)
        {
            return handleException(actioncontext, exception1);
        }
        try
        {
            if(oggettobulk != null)
            {
                formfield.getField().setValueIn(oggettobulk1, oggettobulk);
                formfield.getFormController().setDirty(true);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            try
            {
                fillModel(actioncontext);
            }
            catch(FillException _ex) { }
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmCloseForm");
            else
                return doConfirmCloseForm(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfirmCloseForm(ActionContext actioncontext, int i)
        throws BusinessProcessException
    {
        if(i == 4)
        {
            actioncontext.closeBusinessProcess();
            HookForward hookforward = (HookForward)actioncontext.findForward("close");
            if(hookforward != null)
                return hookforward;
        }
        return actioncontext.findDefaultForward();
    }

    public Forward doConfirmPrint(ActionContext actioncontext, int i)
    {
        try
        {
            if(i == 4)
            {
                BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess(bulkbp.getPrintbp());
                bulkbp.initializePrintBP(actioncontext, businessprocess);
                if (bulkbp.getTransactionPolicy()!= BusinessProcess.IGNORE_TRANSACTION)
                	actioncontext.closeBusinessProcess(bulkbp);
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    public Forward doCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            bulkbp.fillModel(actioncontext);
            FormField formfield = getFormField(actioncontext, s);
            try
            {
                return (Forward)Introspector.invoke(this, "doCRUD", formfield.getField().getName(), actioncontext);
            }
            catch(NoSuchMethodException _ex) { }
            try
            {
                formfield.getModel();
                CRUDBP crudbp = (CRUDBP)actioncontext.getUserInfo().createBusinessProcess(actioncontext, formfield.getField().getCRUDBusinessProcessName(), new Object[] {
                    bulkbp.isEditable() ? "MR" : "R"
                });
                actioncontext.addHookForward("bringback", this, "doBringBackCRUD");
                HookForward hookforward = (HookForward)actioncontext.findForward("bringback");
                hookforward.addParameter("field", formfield);
                return actioncontext.addBusinessProcess(crudbp);
            }
            catch(Exception exception1)
            {
                return handleException(actioncontext, exception1);
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doDefault(ActionContext actioncontext)
        throws RemoteException
    {
        try
        {
            fillModel(actioncontext);
            return super.doDefault(actioncontext);
        }
        catch(FillException fillexception)
        {
            return handleException(actioncontext, fillexception);
        }
    }

    public Forward doFilterCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            fillModel(actioncontext);
            try
            {
                return (Forward)Introspector.invoke(this, "doFilterCRUD", s, actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                return filter(actioncontext, getController(actioncontext, s));
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doFreeSearch(ActionContext actioncontext, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            bulkbp.fillModel(actioncontext);
            FormField formfield = getFormField(actioncontext, s);
            try
            {
                return (Forward)Introspector.invoke(this, "doFreeSearch", formfield.getField().getName(), actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                return freeSearch(actioncontext, formfield);
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doNavigate(ActionContext actioncontext, String s, int i)
    {
        try
        {
            fillModel(actioncontext);
            CRUDController crudcontroller = getController(actioncontext, s);
            try
            {
                return (Forward)Introspector.invoke(this, "doNavigate", crudcontroller.getControllerName(), actioncontext, new Integer(i));
            }
            catch(NoSuchMethodException _ex)
            {
                crudcontroller.setPageIndex(actioncontext, i);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doPrint(ActionContext actioncontext)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmPrint");
            else
                return doConfirmPrint(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRemoveAllFromCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            fillModel(actioncontext);
            try
            {
                return (Forward)Introspector.invoke(this, "doRemoveAllFromCRUD", s, actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                getController(actioncontext, s).removeAll(actioncontext);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doRemoveFilterCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            fillModel(actioncontext);
            try
            {
                return (Forward)Introspector.invoke(this, "doRemoveFilterCRUD", s, actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                return removeFilter(actioncontext, getController(actioncontext, s));
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doRemoveFromCRUD(ActionContext actioncontext, String s)
    {
        try
        {
            fillModel(actioncontext);
            try
            {
                return (Forward)Introspector.invoke(this, "doRemoveFromCRUD", s, actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                getController(actioncontext, s).remove(actioncontext);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSearch(ActionContext actioncontext, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            bulkbp.fillModel(actioncontext);
            FormField formfield = getFormField(actioncontext, s);
            try
            {
                return (Forward)Introspector.invoke(this, "doSearch", formfield.getField().getName(), actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                return search(actioncontext, formfield, formfield.getField().getColumnSet());
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
        throws BusinessProcessException
    {
        try
        {
            fillModel(actioncontext);
            CRUDController crudcontroller = getController(actioncontext, s);
            try
            {
                return (Forward)Introspector.invoke(this, "doSelect", crudcontroller.getControllerName(), actioncontext);
            }
            catch(NoSuchMethodException _ex)
            {
                crudcontroller.setSelection(actioncontext);
            }
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSort(ActionContext actioncontext, String s, String s1)
        throws BusinessProcessException
    {
        try
        {
            fillModel(actioncontext);
            CRUDController crudcontroller = getController(actioncontext, s);
            if(crudcontroller instanceof Orderable)
            {
                Orderable orderable = (Orderable)crudcontroller;
                int i = orderable.getOrderBy(s1);
                try
                {
                    switch(i)
                    {
                    case 0: // '\0'
                        orderable.setOrderBy(actioncontext, s1, 1);
                        break;

                    case 1: // '\001'
                        orderable.setOrderBy(actioncontext, s1, -1);
                        break;

                    case -1: 
                        orderable.setOrderBy(actioncontext, s1, 0);
                        break;
                    }
                }
                catch(UnsortableException _ex)
                {
                    setErrorMessage(actioncontext, "Tabella non ordinabile");
                    orderable.setOrderBy(actioncontext, s1, 0);
                }
            }
        }
        catch(FillException fillexception)
        {
            return handleException(actioncontext, fillexception);
        }
        return actioncontext.findDefaultForward();
    }

    public Forward doTab(ActionContext actioncontext, String s, String s1)
    {
        try
        {
            fillModel(actioncontext);
            return super.doTab(actioncontext, s, s1);
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    protected boolean fillModel(ActionContext actioncontext)
        throws FillException
    {
        BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
        return bulkbp.fillModel(actioncontext);
    }

    protected Forward filter(ActionContext actioncontext, CRUDController crudcontroller)
    {
        try
        {
            return filter(actioncontext, crudcontroller, (OggettoBulk)crudcontroller.getBulkInfo().getBulkClass().newInstance());
        }
        catch(InstantiationException instantiationexception)
        {
            return handleException(actioncontext, instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            return handleException(actioncontext, illegalaccessexception);
        }
    }

    protected Forward filter(ActionContext actioncontext, CRUDController crudcontroller, OggettoBulk oggettobulk)
    {
        try
        {
            BulkBP _tmp = (BulkBP)actioncontext.getBusinessProcess();
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.createBusinessProcess("RicercaLibera");
            ricercaliberabp.setPrototype(oggettobulk);
            actioncontext.addHookForward("filter", this, "doBringBackFilter");
            HookForward hookforward = (HookForward)actioncontext.findForward("filter");
            hookforward.addParameter("controller", crudcontroller);
            return actioncontext.addBusinessProcess(ricercaliberabp);
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    protected Forward freeSearch(ActionContext actioncontext, FormField formfield)
    {
        try
        {
            return freeSearch(actioncontext, formfield, (OggettoBulk)formfield.getField().getPropertyType().newInstance());
        }
        catch(InstantiationException instantiationexception)
        {
            return handleException(actioncontext, instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            return handleException(actioncontext, illegalaccessexception);
        }
    }

    protected Forward freeSearch(ActionContext actioncontext, FormField formfield, OggettoBulk oggettobulk)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            OggettoBulk oggettobulk1 = formfield.getModel();
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.createBusinessProcess("RicercaLibera");
            ricercaliberabp.setSearchProvider(bulkbp.getSearchProvider(oggettobulk1, formfield.getField().getProperty()));
            ricercaliberabp.setFreeSearchSet(formfield.getField().getFreeSearchSet());
            ricercaliberabp.setPrototype(oggettobulk);
            ricercaliberabp.setColumnSet(formfield.getField().getColumnSet());
            actioncontext.addHookForward("seleziona", this, "doBringBackSearchResult");
            HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
            hookforward.addParameter("field", formfield);
            return actioncontext.addBusinessProcess(ricercaliberabp);
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    protected CRUDController getController(ActionContext actioncontext, String s)
    {
        BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
        if(!s.startsWith(bulkbp.getInputPrefix()))
            return null;
        s = s.substring(bulkbp.getInputPrefix().length());
        Object obj = bulkbp;
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreTokens();)
            obj = ((FormController) (obj)).getChildController(stringtokenizer.nextToken());

        return (CRUDController)obj;
    }

    protected FormField getFormField(ActionContext actioncontext, String s)
    {
        BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
        if(!s.startsWith(bulkbp.getInputPrefix()))
        {
            return null;
        } else
        {
            s = s.substring(bulkbp.getInputPrefix().length() + 1);
            return bulkbp.getFormField(s);
        }
    }

    protected Forward handleException(ActionContext actioncontext, Throwable throwable)
    {
        try
        {
            throw throwable;
        }
        catch(ValidationException validationexception)
        {
            setErrorMessage(actioncontext, validationexception.getMessage());
            return actioncontext.findDefaultForward();
        }
        catch(FillException fillexception)
        {
            setErrorMessage(actioncontext, fillexception.getMessage());
            return actioncontext.findDefaultForward();
        }
        catch(TransactionClosedException _ex) { }
        catch(Throwable throwable1)
        {
            return super.handleException(actioncontext, throwable1);
        }
        try
        {
            doCloseForm(actioncontext);
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, ((Throwable) (businessprocessexception)));
        }
        setErrorMessage(actioncontext, "Tempo a disposizione scaduto.");
        return actioncontext.findDefaultForward();
    }

    protected Forward removeFilter(ActionContext actioncontext, CRUDController crudcontroller)
    {
        try
        {
            crudcontroller.setFilter(actioncontext, null);
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward search(ActionContext actioncontext, FormField formfield, BulkInfo bulkinfo, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            OggettoBulk oggettobulk = formfield.getModel();
            OggettoBulk oggettobulk1 = (OggettoBulk)formfield.getField().getValueFrom(oggettobulk);
            if(oggettobulk1 == null)
                oggettobulk1 = createEmptyModelForSearchTool(actioncontext, formfield);
            return selectFromSearchResult(actioncontext, formfield, bulkinfo, bulkbp.find(actioncontext, null, oggettobulk1, oggettobulk, formfield.getField().getProperty()), s);
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward search(ActionContext actioncontext, FormField formfield, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            OggettoBulk oggettobulk = formfield.getModel();
            OggettoBulk oggettobulk1 = (OggettoBulk)formfield.getField().getValueFrom(oggettobulk);
            if(oggettobulk1 == null)
                oggettobulk1 = createEmptyModelForSearchTool(actioncontext, formfield);
            return selectFromSearchResult(actioncontext, formfield, bulkbp.find(actioncontext, null, oggettobulk1, oggettobulk, formfield.getField().getProperty()), s);
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    protected SelezionatoreListaBP select(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1)
        throws BusinessProcessException
    {
        return select(actioncontext, remoteiterator, bulkinfo, s, s1, null);
    }

    protected SelezionatoreListaBP select(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1, ObjectReplacer objectreplacer)
        throws BusinessProcessException
    {
        return select(actioncontext, remoteiterator, bulkinfo, s, s1, objectreplacer, null);
    }

    protected SelezionatoreListaBP select(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1, ObjectReplacer objectreplacer, SelectionListener selectionlistener)
        throws BusinessProcessException
    {
        try
        {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
            selezionatorelistabp.setObjectReplacer(objectreplacer);
            selezionatorelistabp.setSelectionListener(actioncontext, selectionlistener);
            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
            selezionatorelistabp.setBulkInfo(bulkinfo);
            selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
            actioncontext.addHookForward("seleziona", this, s1);
            HookForward _tmp = (HookForward)actioncontext.findForward("seleziona");
            actioncontext.addBusinessProcess(selezionatorelistabp);
            return selezionatorelistabp;
        }
        catch(RemoteException remoteexception)
        {
            throw new BusinessProcessException(remoteexception);
        }
    }

    protected Forward selectFromSearchResult(ActionContext actioncontext, FormField formfield, BulkInfo bulkinfo, RemoteIterator remoteiterator, String s)
    {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
            if(remoteiterator == null || remoteiterator.countElements() == 0)
            {
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                bulkbp.setMessage("La ricerca non ha fornito alcun risultato.");
                return actioncontext.findDefaultForward();
            }
            if(remoteiterator.countElements() == 1)
            {
                doBringBackSearchResult(actioncontext, formfield, (OggettoBulk)remoteiterator.nextElement());
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                return actioncontext.findDefaultForward();
            } else
            {
                SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
                selezionatorelistabp.setIterator(actioncontext, remoteiterator);
                selezionatorelistabp.setBulkInfo(bulkinfo);
                selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
                actioncontext.addHookForward("seleziona", this, "doBringBackSearchResult");
                HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
                hookforward.addParameter("field", formfield);
                return actioncontext.addBusinessProcess(selezionatorelistabp);
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    protected Forward selectFromSearchResult(ActionContext actioncontext, FormField formfield, RemoteIterator remoteiterator, String s)
    {
        return selectFromSearchResult(actioncontext, formfield, BulkInfo.getBulkInfo(formfield.getField().getPropertyType()), remoteiterator, s);
    }
}