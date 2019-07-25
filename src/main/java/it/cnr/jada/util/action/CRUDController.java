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
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

// Referenced classes of package it.cnr.jada.util.action:
//            FormController, ListController, Selection

public interface CRUDController
        extends FormController, ListController {

    void add(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException;

    int countDetails();

    String getControllerName();

    int getModelIndex();

    void remove(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException;

    void removeAll(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException;

    void reset(ActionContext actioncontext);

    void resync(ActionContext actioncontext)
            throws BusinessProcessException;

    void save(ActionContext actioncontext)
            throws BusinessProcessException;

    void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause)
            throws BusinessProcessException;

    void setModelIndex(ActionContext actioncontext, int i)
            throws ValidationException;

    void setPageIndex(ActionContext actioncontext, int i)
            throws ValidationException, BusinessProcessException;

    Selection setSelection(ActionContext actioncontext)
            throws BusinessProcessException, ValidationException;

    void toggle(ActionContext actioncontext)
            throws ValidationException, BusinessProcessException;
}