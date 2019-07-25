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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;

public class ResetBulkInfos extends BusinessProcess implements Serializable {
    AdminSession adminSession = (AdminSession) EJBCommonServices.createEJB("JADAEJB_AdminSession");

    public ResetBulkInfos() {
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
            throws BusinessProcessException {
        BulkInfo.resetBulkInfos();
        Introspector.resetPropertiesCache();
        if (actioncontext instanceof HttpActionContext)
            ((HttpActionContext) actioncontext).resetActionMappings();
        Config.getHandler().reset();
        java.beans.Introspector.flushCaches();
        try {
            adminSession.resetPersistentInfos();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
    }
}