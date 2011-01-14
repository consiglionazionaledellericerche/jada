package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.*;
import it.cnr.jada.util.jsp.Button;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreAlberoBP, CondizioneRicercaBulk, CondizioneComplessaBulk, CondizioneSempliceBulk, 
//            SearchProvider, FindBP

public class RicercaLiberaBP extends SelezionatoreAlberoBP
    implements Serializable
{
    class FindBPSearchProvider
        implements Serializable, SearchProvider
    {

        public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
            throws BusinessProcessException
        {
            if(context == null)
                return findBp.find(actioncontext, (CompoundFindClause)condizioneRadice.creaFindClause(), getPrototype());
            else
                return findBp.find(actioncontext, (CompoundFindClause)condizioneRadice.creaFindClause(), getPrototype(), context, optionsProperty);
        }

        private final FindBP findBp;

        public FindBPSearchProvider(FindBP findbp)
        {
            findBp = findbp;
        }
    }


    public RicercaLiberaBP()
    {
        super("Th");
        showSearchResult = true;
        setMostraRadice(true);
    }

    public void apriParentesi()
    {
        CondizioneRicercaBulk condizionericercabulk = condizioneCorrente;
        if(nuovaCondizione)
        {
            condizioneCorrente.getParent().sostituisciCondizione(condizionericercabulk, condizioneCorrente = new CondizioneComplessaBulk());
            nuovaCondizione = false;
        } else
        {
            if(condizioneCorrente instanceof CondizioneComplessaBulk)
                ((CondizioneComplessaBulk)condizionericercabulk).aggiungiCondizione(condizioneCorrente = new CondizioneComplessaBulk());
            else
                condizionericercabulk.getParent().aggiungiCondizioneDopo(condizioneCorrente = new CondizioneComplessaBulk(), condizionericercabulk);
            rigaSelezionata++;
        }
        aggiornaRighe();
    }

    public void cancellaCondizioneCorrente(ActionContext actioncontext)
    {
        if(getCondizioneCorrente().getParent() == null)
            return;
        getCondizioneCorrente().getParent().rimuoviCondizione(condizioneCorrente);
        aggiornaRighe();
        if(rigaSelezionata >= size())
            rigaSelezionata = size() - 1;
        setCondizioneCorrente((CondizioneRicercaBulk)getElementAt(actioncontext, rigaSelezionata));
        nuovaCondizione = false;
    }

    public void cancellaCondizioni()
    {
        condizioneCorrente = condizioneRadice = new CondizioneComplessaBulk();
        setRadice(condizioneRadice);
        rigaSelezionata = 0;
        nuovaCondizione = false;
        nuovaCondizione();
    }

    public void chiudiParentesi()
    {
        CondizioneRicercaBulk condizionericercabulk = condizioneCorrente;
        condizionericercabulk.getParent().getParent().aggiungiCondizioneDopo(condizioneCorrente = new CondizioneSempliceBulk(prototype, getFreeSearchSet()), condizionericercabulk.getParent());
        if(isNuovaCondizione())
            condizionericercabulk.getParent().rimuoviCondizione(condizionericercabulk);
        else
            rigaSelezionata++;
        nuovaCondizione = true;
        aggiornaRighe();
    }

    protected Button[] createToolbar()
    {
        Button abutton[] = new Button[7];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.newClause");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.saveClause");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.removeClause");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.removeAllClauses");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.search");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.openParenthesis");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.closeParenthesis");
        return abutton;
    }

    public String getColumnSet()
    {
        return columnSet;
    }

    public CondizioneRicercaBulk getCondizioneCorrente()
    {
        return condizioneCorrente;
    }

    public CondizioneComplessaBulk getCondizioneRadice()
    {
        return condizioneRadice;
    }

    public String getFreeSearchSet()
    {
        return freeSearchSet;
    }

    public OggettoBulk getPrototype()
    {
        return prototype;
    }

    public int getRigaSelezionata()
    {
        return rigaSelezionata;
    }

    public SearchProvider getSearchProvider()
    {
        return searchProvider;
    }

    public boolean isCanPerformSearchWithoutClauses()
    {
        return canPerformSearchWithoutClauses;
    }

    public boolean isMultiSelection()
    {
        return multiSelection;
    }

    public boolean isNuovaCondizione()
    {
        return nuovaCondizione;
    }

    public boolean isShowSearchResult()
    {
        return showSearchResult;
    }

    public void nuovaCondizione()
    {
        CondizioneRicercaBulk condizionericercabulk = condizioneCorrente;
        if(nuovaCondizione)
        {
            condizioneCorrente.getParent().sostituisciCondizione(condizionericercabulk, condizioneCorrente = new CondizioneSempliceBulk(getPrototype(), getFreeSearchSet()));
        } else
        {
            if(condizioneCorrente instanceof CondizioneComplessaBulk)
                ((CondizioneComplessaBulk)condizionericercabulk).aggiungiCondizione(condizioneCorrente = new CondizioneSempliceBulk(getPrototype(), getFreeSearchSet()));
            else
                condizionericercabulk.getParent().aggiungiCondizioneDopo(condizioneCorrente = new CondizioneSempliceBulk(getPrototype(), getFreeSearchSet()), condizionericercabulk);
            rigaSelezionata++;
            nuovaCondizione = true;
        }
        aggiornaRighe();
    }

    public void setCanPerformSearchWithoutClauses(boolean flag)
    {
        canPerformSearchWithoutClauses = flag;
    }

    public void setColumnSet(String s)
    {
        columnSet = s;
    }

    public void setCondizioneCorrente(CondizioneRicercaBulk condizionericercabulk)
    {
        condizioneCorrente = condizionericercabulk;
    }

    public void setCondizioneRadice(CondizioneComplessaBulk condizionecomplessabulk)
    {
        condizioneRadice = condizionecomplessabulk;
    }

    public void setFindbp(FindBP findbp)
    {
        setSearchProvider(new FindBPSearchProvider(findbp));
    }

    public void setFreeSearchSet(String s)
    {
        freeSearchSet = s;
    }

    public void setMultiSelection(boolean flag)
    {
        multiSelection = flag;
    }

    public void setNuovaCondizione(boolean flag)
    {
        nuovaCondizione = flag;
    }

    public void setPrototype(OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        setPrototype(oggettobulk, null, null);
    }

    public void setPrototype(OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws BusinessProcessException
    {
        setPrototype(oggettobulk, oggettobulk1, s, freeSearchSet);
    }

    public void setPrototype(OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s, String s1)
        throws BusinessProcessException
    {
        prototype = oggettobulk;
        context = oggettobulk1;
        optionsProperty = s;
        freeSearchSet = s1;
        condizioneCorrente = condizioneRadice = new CondizioneComplessaBulk();
        rigaSelezionata = 0;
        for(Enumeration enumeration = oggettobulk.getBulkInfo().getFreeSearchProperties(s1); enumeration.hasMoreElements();)
        {
            FieldProperty fieldproperty = (FieldProperty)enumeration.nextElement();
            try
            {
                Object obj = Introspector.getPropertyValue(oggettobulk, fieldproperty.getProperty());
                if(obj != null)
                {
                    CondizioneSempliceBulk condizionesemplicebulk = new CondizioneSempliceBulk(oggettobulk, s1);
                    condizionesemplicebulk.setFindFieldProperty(fieldproperty);
                    condizionesemplicebulk.setOperator(new Integer(8192));
                    if(rigaSelezionata > 0)
                        condizionesemplicebulk.setLogicalOperator("AND");
                    condizionesemplicebulk.setValue(obj);
                    if(condizioneCorrente != condizioneRadice)
                        condizioneRadice.aggiungiCondizioneDopo(condizionesemplicebulk, condizioneCorrente);
                    else
                        condizioneRadice.aggiungiCondizione(condizionesemplicebulk);
                    condizioneCorrente = condizionesemplicebulk;
                    rigaSelezionata++;
                }
            }
            catch(IntrospectionException _ex) { }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw new BusinessProcessException(invocationtargetexception);
            }
        }

        setRadice(condizioneRadice);
        nuovaCondizione();
    }

    public void setRigaSelezionata(ActionContext actioncontext, int i)
    {
        if(rigaSelezionata == i)
            return;
        if(nuovaCondizione)
        {
            condizioneCorrente.getParent().rimuoviCondizione(condizioneCorrente);
            if(i > rigaSelezionata)
                i--;
            nuovaCondizione = false;
            aggiornaRighe();
        }
        setCondizioneCorrente((CondizioneRicercaBulk)getElementAt(actioncontext, rigaSelezionata = i));
    }

    public void setSearchProvider(SearchProvider searchprovider)
    {
        searchProvider = searchprovider;
    }

    public void setShowSearchResult(boolean flag)
    {
        showSearchResult = flag;
    }

    private OggettoBulk prototype;
    private CondizioneRicercaBulk condizioneCorrente;
    private CondizioneComplessaBulk condizioneRadice;
    private int rigaSelezionata;
    private boolean nuovaCondizione;
    private OggettoBulk context;
    private String optionsProperty;
    private boolean showSearchResult;
    private boolean multiSelection;
    private SearchProvider searchProvider;
    private boolean canPerformSearchWithoutClauses;
    private String freeSearchSet;
    private String columnSet;



}