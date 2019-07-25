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

import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;

public class PrintFieldProperty extends FieldProperty {

    public static final int TYPE_STRING = 0;
    public static final int TYPE_DATE = 1;
    public static final int TYPE_TIMESTAMP = 2;
    private int paramPosition;
    private int paramType;
    private String paramNameJR;
    private String paramTypeJR;

    public PrintFieldProperty() {
        paramType = 0;
    }

    void fillNullsFrom(FieldProperty fieldproperty) {
        super.fillNullsFrom(fieldproperty);
        if (fieldproperty instanceof PrintFieldProperty) {
            PrintFieldProperty printfieldproperty = (PrintFieldProperty) fieldproperty;
            if (paramPosition < 0)
                paramPosition = printfieldproperty.paramPosition;
            if (paramType == 0)
                paramType = printfieldproperty.paramType;
        }
    }

    public int getParameterPosition() {
        return getParamPosition();
    }

    public void setParameterPosition(int i) {
        setParamPosition(i);
    }

    public String getParameterType() {
        switch (paramPosition) {
            case 1: // '\001'
                return "DATE";

            case 2: // '\002'
                return "TIMESTAMP";

            case 0: // '\0'
            default:
                return "STRING";
        }
    }

    public void setParameterType(String s) {
        if ("DATE".equalsIgnoreCase(s))
            setParamType(1);
        if ("STRING".equalsIgnoreCase(s))
            setParamType(0);
        if ("TIMESTAMP".equalsIgnoreCase(s))
            setParamType(2);
    }

    public int getParamPosition() {
        return paramPosition;
    }

    public void setParamPosition(int i) {
        paramPosition = i;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int i) {
        paramType = i;
    }

    public String getParamNameJR() {
        return paramNameJR;
    }

    public void setParamNameJR(String paramNameJR) {
        this.paramNameJR = paramNameJR;
    }

    public String getParamTypeJR() {
        return paramTypeJR;
    }

    public void setParamTypeJR(String paramTypeJR) {
        this.paramTypeJR = paramTypeJR;
    }

    @Override
    public void createWithAnnotation(FieldPropertyAnnotation fieldPropertyAnnotation, String property) {
        super.createWithAnnotation(fieldPropertyAnnotation, property);
        setParamNameJR(nvl(fieldPropertyAnnotation.paramNameJR()));
        setParamTypeJR(nvl(fieldPropertyAnnotation.paramTypeJR()));
    }
}
