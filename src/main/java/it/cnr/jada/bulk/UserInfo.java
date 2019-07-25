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

import it.cnr.jada.UserTransaction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;

import java.io.Serializable;

public class UserInfo extends OggettoBulk implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserTransaction userTransaction;
    private String userid;

    public UserInfo() {
    }

    public BusinessProcess createBusinessProcess(ActionContext actioncontext,
                                                 String s) throws BusinessProcessException {
        return actioncontext.createBusinessProcess(s);
    }

    public BusinessProcess createBusinessProcess(ActionContext actioncontext,
                                                 String s, Object aobj[]) throws BusinessProcessException {
        return actioncontext.createBusinessProcess(s, aobj);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String s) {
        userid = s;
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public void setUserTransaction(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

}