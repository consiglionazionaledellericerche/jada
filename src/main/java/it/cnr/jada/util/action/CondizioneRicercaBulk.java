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
        implements Serializable, NodoAlbero {

    private static final Dictionary logicalOperatorOptions;

    static {
        logicalOperatorOptions = new OrderedHashtable();
        logicalOperatorOptions.put("AND", "e");
        logicalOperatorOptions.put("OR", "o");
    }

    private String logicalOperator;
    private CondizioneComplessaBulk parent;

    public CondizioneRicercaBulk() {
    }

    public abstract FindClause creaFindClause();

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String s) {
        if ("".equals(s))
            logicalOperator = null;
        else
            logicalOperator = s;
    }

    public Dictionary getLogicalOperatorOptions() {
        return logicalOperatorOptions;
    }

    public Object getObject() {
        return this;
    }

    public CondizioneComplessaBulk getParent() {
        return parent;
    }

    public void setParent(CondizioneComplessaBulk condizionecomplessabulk) {
        parent = condizionecomplessabulk;
    }

    public boolean isPrimaCondizione() {
        return getParent() == null || getParent().getChildren().nextElement() == this;
    }

    public void validate()
            throws ValidationException {
        if (logicalOperator == null && !isPrimaCondizione())
            throw new ValidationException("E' necessario specificare un operatore logico (e,o)");
        else
            return;
    }

    public abstract String getDescrizioneNodo();

    public abstract Enumeration getFigliNodo();
}