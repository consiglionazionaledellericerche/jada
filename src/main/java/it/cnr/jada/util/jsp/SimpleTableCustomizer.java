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

package it.cnr.jada.util.jsp;

import jakarta.servlet.jsp.JspWriter;

import java.io.IOException;
import java.io.Serializable;


public class SimpleTableCustomizer
        implements Serializable, TableCustomizer {

    public SimpleTableCustomizer() {
    }

    public String getRowStyle(Object obj) {
        return null;
    }

    @Override
    public String getRowCSSClass(Object obj, boolean even) {
        return null;
    }

    public boolean isRowEnabled(Object obj) {
        return false;
    }

    public boolean isRowReadonly(Object obj) {
        return false;
    }

    @Override
    public String getTableClass() {
        return null;
    }

    @Override
    public void writeTfoot(JspWriter jspwriter) throws IOException {

    }
}