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
//            CRUDValidationException, CRUDException

public abstract class CRUDConstraintException extends CRUDValidationException
        implements Serializable {

    private final String propertyName;

    public CRUDConstraintException(String s, OggettoBulk oggettobulk) {
        super(s, null, oggettobulk);
        propertyName = s;
    }

    public FieldProperty getFormFieldProperty() {
        String s = propertyName;
        do {
            FieldProperty fieldproperty = getBulk().getBulkInfo().getFieldPropertyByProperty(s);
            if (fieldproperty != null)
                return fieldproperty;
            int i = s.lastIndexOf('.');
            if (i < 1)
                return null;
            s = s.substring(0, i);
        } while (true);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public abstract String getUserMessage();
}