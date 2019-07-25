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

public class FieldValidationException extends DetailedException
        implements Serializable {

    private String text;
    private String prefix;
    private String fieldName;

    public FieldValidationException() {
    }

    public FieldValidationException(String s) {
        super(s);
    }

    public FieldValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FieldValidationException(String s, Throwable throwable, String s1, String s2, String s3) {
        super(s, throwable);
        prefix = s1;
        fieldName = s2;
        text = s3;
    }

    public FieldValidationException(Throwable throwable) {
        super(throwable);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String s) {
        fieldName = s;
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