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
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            NestedFormController, FormController

public class CompoundPropertyController extends NestedFormController
        implements Serializable {

    private final BulkInfo bulkInfo;
    private final Class modelClass;
    private String propertyName;

    public CompoundPropertyController(String s, Class class1, String s1, FormController formcontroller) {
        super(s, formcontroller);
        propertyName = s1;
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
    }

    public BulkInfo getBulkInfo() {
        return bulkInfo;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String s) {
        propertyName = s;
    }

    public void resync(ActionContext actioncontext) {
        try {
            setModel(actioncontext, (OggettoBulk) Introspector.getPropertyValue(getParentModel(), propertyName));
            super.resync(actioncontext);
        } catch (Exception exception) {
            throw new IntrospectionError(exception);
        }
    }
}