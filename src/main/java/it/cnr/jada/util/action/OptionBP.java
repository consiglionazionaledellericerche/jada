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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package it.cnr.jada.util.action:
//            FormBP

public class OptionBP extends FormBP
        implements Serializable {

    public static final int MESSAGE = 0;
    public static final int CONFIRM = 1;
    public static final int CONFIRM_YES_NO = 2;
    public static final int CONFIRM_YES_NO_CANCEL = 3;
    public static final int INPUT = 4;
    public static final int OK_BUTTON = 1;
    public static final int CANCEL_BUTTON = 2;
    public static final int YES_BUTTON = 4;
    public static final int NO_BUTTON = 8;
    public static final int CLOSE_BUTTON = 16;
    private boolean nowrap;
    private int type;
    private int icon;
    private int buttons;
    private int option;
    private Map attributes;
    public OptionBP() {
        type = 0;
    }

    public void addAttribute(String s, Object obj) {
        if (attributes == null)
            attributes = new HashMap();
        attributes.put(s, obj);
    }

    public Object getAttribute(String s) {
        if (attributes == null)
            return null;
        else
            return attributes.get(s);
    }

    public int getOption() {
        return option;
    }

    public void setOption(int i) {
        option = i;
    }

    public boolean hasButton(int i) {
        return (buttons & i) != 0;
    }

    public void hideButton(int i) {
        buttons &= 0xffff ^ i;
    }

    public void setType(int i) {
        type = i;
        switch (i) {
            case 0: // '\0'
                buttons = 16;
                break;

            case 1: // '\001'
                buttons = 3;
                break;

            case 2: // '\002'
                buttons = 12;
                break;

            case 3: // '\003'
                buttons = 14;
                break;
        }
    }

    public void showButton(int i) {
        buttons |= i;
    }

    /**
     * @return
     */
    public boolean isNowrap() {
        return nowrap;
    }

    /**
     * @param b
     */
    public void setNowrap(boolean b) {
        nowrap = b;
    }

}