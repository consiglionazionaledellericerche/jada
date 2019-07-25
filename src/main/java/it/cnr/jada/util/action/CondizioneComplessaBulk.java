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

package it.cnr.jada.util.action;

import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package it.cnr.jada.util.action:
//            CondizioneRicercaBulk

public class CondizioneComplessaBulk extends CondizioneRicercaBulk
        implements Serializable {

    public Vector children;

    public CondizioneComplessaBulk() {
        children = new Vector();
    }

    public void aggiungiCondizione(CondizioneRicercaBulk condizionericercabulk) {
        children.insertElementAt(condizionericercabulk, 0);
        condizionericercabulk.setParent(this);
    }

    public void aggiungiCondizioneDopo(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1) {
        children.insertElementAt(condizionericercabulk, children.indexOf(condizionericercabulk1) + 1);
        condizionericercabulk.setParent(this);
    }

    public void aggiungiCondizionePrima(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1) {
        children.insertElementAt(condizionericercabulk, children.indexOf(condizionericercabulk1));
        condizionericercabulk.setParent(this);
    }

    public int contaCondizioneSemplici() {
        int i = 0;
        for (Enumeration enumeration = children.elements(); enumeration.hasMoreElements(); ) {
            Object obj = enumeration.nextElement();
            if (obj instanceof CondizioneComplessaBulk)
                i += ((CondizioneComplessaBulk) obj).contaCondizioneSemplici();
            else
                i++;
        }

        return i;
    }

    public FindClause creaFindClause() {
        CompoundFindClause compoundfindclause = new CompoundFindClause();
        compoundfindclause.setLogicalOperator(getLogicalOperator());
        for (Enumeration enumeration = children.elements(); enumeration.hasMoreElements(); compoundfindclause.addChild(((CondizioneRicercaBulk) enumeration.nextElement()).creaFindClause()))
            ;
        return compoundfindclause;
    }

    public Enumeration getChildren() {
        return children.elements();
    }

    public String getDescrizioneNodo() {
        StringBuffer stringbuffer = new StringBuffer();
        if (getLogicalOperator() != null) {
            stringbuffer.append(getLogicalOperatorOptions().get(getLogicalOperator()));
            stringbuffer.append(' ');
        }
        stringbuffer.append("(...)");
        return stringbuffer.toString();
    }

    public Enumeration getFigliNodo() {
        return children.elements();
    }

    public void rimuoviCondizione(CondizioneRicercaBulk condizionericercabulk) {
        children.removeElement(condizionericercabulk);
        condizionericercabulk.setParent(null);
    }

    public void sostituisciCondizione(CondizioneRicercaBulk condizionericercabulk, CondizioneRicercaBulk condizionericercabulk1) {
        children.setElementAt(condizionericercabulk1, children.indexOf(condizionericercabulk));
        condizionericercabulk1.setParent(this);
    }
}