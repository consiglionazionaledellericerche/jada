package it.cnr.jada.util.action;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.util.NodoAlbero;
import it.cnr.jada.util.OrderedHashtable;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.util.action:
//            CondizioneComplessaBulk

public abstract class CondizioneRicercaBulk extends OggettoBulk
    implements Serializable, NodoAlbero
{

    public CondizioneRicercaBulk()
    {
    }

    public abstract FindClause creaFindClause();

    public String getLogicalOperator()
    {
        return logicalOperator;
    }

    public Dictionary getLogicalOperatorOptions()
    {
        return logicalOperatorOptions;
    }

    public Object getObject()
    {
        return this;
    }

    public CondizioneComplessaBulk getParent()
    {
        return parent;
    }

    public boolean isPrimaCondizione()
    {
        return getParent() == null || getParent().getChildren().nextElement() == this;
    }

    public void setLogicalOperator(String s)
    {
        if("".equals(s))
            logicalOperator = null;
        else
            logicalOperator = s;
    }

    public void setParent(CondizioneComplessaBulk condizionecomplessabulk)
    {
        parent = condizionecomplessabulk;
    }

    public void validate()
        throws ValidationException
    {
        if(logicalOperator == null && !isPrimaCondizione())
            throw new ValidationException("E' necessario specificare un operatore logico (e,o)");
        else
            return;
    }

    public abstract String getDescrizioneNodo();

    public abstract Enumeration getFigliNodo();

    private static final Dictionary logicalOperatorOptions;
    private String logicalOperator;
    private CondizioneComplessaBulk parent;

    static 
    {
        logicalOperatorOptions = new OrderedHashtable();
        logicalOperatorOptions.put("AND", "e");
        logicalOperatorOptions.put("OR", "o");
    }
}