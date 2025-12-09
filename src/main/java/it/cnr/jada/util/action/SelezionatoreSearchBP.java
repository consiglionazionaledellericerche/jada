/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;
import java.util.Optional;

public class SelezionatoreSearchBP extends SelezionatoreListaBP implements SearchProvider {

    private String componentSessionName;

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        Optional.ofNullable(config.getInitParameter("bulkClassName"))
                .map(s -> {
                    try {
                        return BulkInfo.getBulkInfo(Class.forName(s));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }).ifPresent(bulkInfo1 -> setBulkInfo(bulkInfo1));

        Optional.ofNullable(config.getInitParameter("searchResultColumnSet"))
                .filter(s -> Optional.ofNullable(getBulkInfo()).isPresent())
                .ifPresent(s -> setColumns(getBulkInfo().getColumnFieldPropertyDictionary(s)));

        this.componentSessionName = Optional.ofNullable(config.getInitParameter("componentSessionName"))
                .orElse("JADAEJB_CRUDComponentSession");

        Optional.ofNullable(config.getInitParameter("openIterator"))
                .filter(s -> Boolean.valueOf(s))
                .ifPresent(s -> {
                    try {
                        openIterator(actioncontext);
                    } catch (BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
    }

    public void openIterator(it.cnr.jada.action.ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
        try {
            OggettoBulk model = Optional.ofNullable(getBulkInfo())
                    .map(BulkInfo::getBulkClass)
                    .map(aClass -> {
                        try {
                            return aClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    })
                    .filter(OggettoBulk.class::isInstance)
                    .map(OggettoBulk.class::cast)
                    .orElseThrow(() -> new BusinessProcessException("Cannot create OggettoBulk"));
            setModel(actioncontext, model);
            it.cnr.jada.util.RemoteIterator ri = search(
                    actioncontext,
                    null,
                    model
            );
            this.setIterator(actioncontext, ri);
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public it.cnr.jada.ejb.RicercaComponentSession createComponentSession(it.cnr.jada.action.ActionContext context, String jndiName) throws jakarta.ejb.EJBException, BusinessProcessException {
        return (it.cnr.jada.ejb.RicercaComponentSession)
                createComponentSession(jndiName, it.cnr.jada.ejb.RicercaComponentSession.class);
    }

    @Override
    public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
                    (actioncontext, createComponentSession(actioncontext, componentSessionName).cerca(actioncontext.getUserContext(), compoundfindclause, oggettobulk));
        } catch (RemoteException | ComponentException e) {
            throw handleException(e);
        }
    }
}
