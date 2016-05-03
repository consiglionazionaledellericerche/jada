package it.cnr.jada.util.action;

import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.action:
//            CondizioneRicercaBulk

public class CondizioneComplessaBulk extends CondizioneRicercaBulk
    implements Serializable
{

    public CondizioneComplessaBulk()
    {
        children = new Vector();
    }

    public void aggiungiCondizione(CondizioneRicercaBulk condizionericercabulk)
    {
        children.insertElementAt(condizionericercabulk, 0);
        condizionericercabulk.setParent(this);
    }

    public void aggiungiCondizioneDopo(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1)
    {
        children.insertElementAt(condizionericercabulk, children.indexOf(condizionericercabulk1) + 1);
        condizionericercabulk.setParent(this);
    }

    public void aggiungiCondizionePrima(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1)
    {
        children.insertElementAt(condizionericercabulk, children.indexOf(condizionericercabulk1));
        condizionericercabulk.setParent(this);
    }

    public int contaCondizioneSemplici()
    {
        int i = 0;
        for(Enumeration enumeration = children.elements(); enumeration.hasMoreElements();)
        {
            Object obj = enumeration.nextElement();
            if(obj instanceof CondizioneComplessaBulk)
                i += ((CondizioneComplessaBulk)obj).contaCondizioneSemplici();
            else
                i++;
        }

        return i;
    }

    public FindClause creaFindClause()
    {
        CompoundFindClause compoundfindclause = new CompoundFindClause();
        compoundfindclause.setLogicalOperator(getLogicalOperator());
        for(Enumeration enumeration = children.elements(); enumeration.hasMoreElements(); compoundfindclause.addChild(((CondizioneRicercaBulk)enumeration.nextElement()).creaFindClause()));
        return compoundfindclause;
    }

    public Enumeration getChildren()
    {
        return children.elements();
    }

    public String getDescrizioneNodo()
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(getLogicalOperator() != null)
        {
            stringbuffer.append(getLogicalOperatorOptions().get(getLogicalOperator()));
            stringbuffer.append(' ');
        }
        stringbuffer.append("(...)");
        return stringbuffer.toString();
    }

    public Enumeration getFigliNodo()
    {
        return children.elements();
    }

    public void rimuoviCondizione(CondizioneRicercaBulk condizionericercabulk)
    {
        children.removeElement(condizionericercabulk);
        condizionericercabulk.setParent(null);
    }

    public void sostituisciCondizione(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1)
    {
        children.setElementAt(condizionericercabulk1, children.indexOf(condizionericercabulk));
        condizionericercabulk1.setParent(this);
    }

    public Vector children;
}