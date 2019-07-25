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

package it.cnr.jada.action;

import it.cnr.jada.UserContext;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class AdminUserContext implements UserContext {
    private static final long serialVersionUID = 7472364126199935204L;
    private static AdminUserContext instance = null;
    private Hashtable<String, Serializable> attributes = new Hashtable<String, Serializable>();

    protected AdminUserContext() {
        this(String.valueOf(serialVersionUID));
    }

    public AdminUserContext(String sessionId) {
        attributes.put("sessionId", sessionId);
    }

    public static AdminUserContext getInstance() {
        if (instance == null) {
            instance = new AdminUserContext();
        }
        return instance;
    }

    public static AdminUserContext getInstance(String sessionId) {
        if (instance == null) {
            instance = new AdminUserContext(sessionId);
        }
        return instance;
    }

    public Dictionary<Object, Object> getHiddenColumns() {
        return null;
    }

    public String getSessionId() {
        return (String) attributes.get("sessionId");
    }

    public String getUser() {
        return "ADMIN";
    }

    public boolean isTransactional() {
        return false;
    }

    public void setTransactional(boolean flag) {

    }

    public void writeTo(PrintWriter printwriter) {
        printwriter.print("USER: ADMIN");
    }

    public Hashtable<String, Serializable> getAttributes() {
        return attributes;
    }
}
