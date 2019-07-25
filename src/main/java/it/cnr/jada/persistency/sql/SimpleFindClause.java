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

package it.cnr.jada.persistency.sql;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            FindClause

public class SimpleFindClause
        implements FindClause, Serializable {

    private int operator;
    private Object value;
    private String logicalOperator;
    private String propertyName;
    private boolean caseSensitive;
    private String sqlClause;

    public SimpleFindClause() {
    }

    public SimpleFindClause(String s, int i, Object obj) {
        this(null, s, i, obj, true);
    }

    public SimpleFindClause(String s, String s1, int i, Object obj) {
        this(s, s1, i, obj, true);
    }

    public SimpleFindClause(String s, String s1, int i, Object obj, boolean flag) {
        logicalOperator = s;
        propertyName = s1;
        operator = i;
        value = obj;
        caseSensitive = flag;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String s) {
        logicalOperator = s;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int i) {
        operator = i;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String s) {
        propertyName = s;
    }

    public String getSqlClause() {
        return sqlClause;
    }

    public void setSqlClause(String s) {
        sqlClause = s;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object obj) {
        value = obj;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean flag) {
        caseSensitive = flag;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append('\t');
        if (logicalOperator != null) {
            stringbuffer.append(logicalOperator);
            stringbuffer.append(' ');
        }
        stringbuffer.append(propertyName);
        stringbuffer.append(' ');
        switch (operator) {
            case 8201:
                stringbuffer.append("IS NULL )");
                return stringbuffer.toString();

            case 8202:
                stringbuffer.append("IS NOT NULL )");
                return stringbuffer.toString();

            case 8192:
                stringbuffer.append("=");
                break;

            case 16386:
                stringbuffer.append("<");
                break;

            case 16388:
                stringbuffer.append(">");
                break;

            case 16387:
                stringbuffer.append("<=");
                break;

            case 16389:
                stringbuffer.append(">=");
                break;

            case 8193:
                stringbuffer.append("<>");
                break;

            case 40968:
                stringbuffer.append("STARTSWITH");
                break;

            case 40967:
                stringbuffer.append("LIKE");
                break;
        }
        stringbuffer.append(' ');
        stringbuffer.append(value);
        return stringbuffer.toString();
    }
}