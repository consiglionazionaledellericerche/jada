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

// Referenced classes of package it.cnr.jada.util.action:
//            NestedFormController, FormController

public class SimpleNestedFormController extends NestedFormController {

    private final BulkInfo bulkInfo;
    private final Class modelClass;

    public SimpleNestedFormController(String s, Class class1, FormController formcontroller) {
        super(s, formcontroller);
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
    }

    public BulkInfo getBulkInfo() {
        return bulkInfo;
    }

    public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) {
        super.setModel(actioncontext, oggettobulk);
        resetChildren(actioncontext);
    }
}