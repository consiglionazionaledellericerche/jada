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

package it.cnr.jada.comp;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDConstraintException

public class CRUDNotNullConstraintException extends CRUDConstraintException
        implements Serializable {

    public CRUDNotNullConstraintException(String s, OggettoBulk oggettobulk) {
        super(s, oggettobulk);
    }

    public String getUserMessage() {
        StringBuffer stringbuffer = new StringBuffer();
        FieldProperty fieldproperty = getFormFieldProperty();
        stringbuffer.append("Il campo \"");
        stringbuffer.append(fieldproperty != null && fieldproperty.getLabel() != null ? fieldproperty.getLabel() : getPropertyName());
        stringbuffer.append("\" non pu\362 essere vuoto.");
        return stringbuffer.toString();
    }
}