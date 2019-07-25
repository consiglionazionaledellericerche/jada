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

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            AbstractPrintBP, FormBP

public class BulkPrintBP extends AbstractPrintBP
        implements Serializable {

    private OggettoBulk bulk;

    public BulkPrintBP() {
    }

    public OggettoBulk getBulk() {
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }

    public void print(PageContext pagecontext)
            throws ServletException, IOException, BusinessProcessException {
        bulk.writeForm(pagecontext.getOut(), 2, getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }
}