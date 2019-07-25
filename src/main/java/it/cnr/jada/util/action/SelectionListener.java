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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.BitSet;

public interface SelectionListener {

    void clearSelection(ActionContext actioncontext)
            throws BusinessProcessException;

    void deselectAll(ActionContext actioncontext);

    BitSet getSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset)
            throws BusinessProcessException;

    void initializeSelection(ActionContext actioncontext)
            throws BusinessProcessException;

    void selectAll(ActionContext actioncontext)
            throws BusinessProcessException;

    BitSet setSelection(ActionContext actioncontext, OggettoBulk aoggettobulk[], BitSet bitset, BitSet bitset1)
            throws BusinessProcessException;
}