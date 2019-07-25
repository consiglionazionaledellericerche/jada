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

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ApplicationException

public class OptionRequestException extends ApplicationException
        implements Serializable {

    public static final int CONFIRM = 1;
    public static final int CONFIRM_YES_NO = 2;
    public static final int CONFIRM_YES_NO_CANCEL = 3;
    private final int type;
    private final String name;
    public OptionRequestException(String s, String s1) {
        this(s, s1, 1);
    }
    public OptionRequestException(String s, String s1, int i) {
        super(s1);
        name = s;
        type = i;
    }

    public final String getName() {
        return name;
    }

    public final int getType() {
        return type;
    }
}