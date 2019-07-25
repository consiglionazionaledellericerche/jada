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

package it.cnr.jada.bulk;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            FieldProperty

public class FillException extends DetailedException
        implements Serializable {

    private FieldProperty field;
    private String prefix;
    private String text;

    public FillException() {
    }

    public FillException(String s) {
        super(s);
    }

    public FillException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FillException(String s, Throwable throwable, String s1, FieldProperty fieldproperty, String s2) {
        super(s, throwable);
        prefix = s1;
        text = s2;
        field = fieldproperty;
    }

    public FillException(Throwable throwable) {
        super(throwable);
    }

    public FieldProperty getField() {
        return field;
    }

    public void setField(FieldProperty fieldproperty) {
        field = fieldproperty;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String s) {
        prefix = s;
    }

    public String getText() {
        return text;
    }

    public void setText(String s) {
        text = s;
    }
}