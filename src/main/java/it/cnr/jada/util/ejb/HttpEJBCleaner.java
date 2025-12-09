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

package it.cnr.jada.util.ejb;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HttpEJBCleaner implements Serializable {
    private transient static final Logger logger = LoggerFactory.getLogger(HttpEJBCleaner.class);
    private final Set objects = new HashSet<Object>();

    private HttpEJBCleaner() {
    }

    private static HttpEJBCleaner bindToHttpSession(HttpSession httpsession) {
        if (httpsession == null)
            return null;
        HttpEJBCleaner httpejbcleaner = (HttpEJBCleaner) httpsession.getAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner");
        if (httpejbcleaner == null)
            httpsession.setAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner", httpejbcleaner = new HttpEJBCleaner());
        return httpejbcleaner;
    }

    public static void register(ActionContext actioncontext, Object obj) {
        Optional.ofNullable(actioncontext)
                .filter(HttpActionContext.class::isInstance)
                .map(HttpActionContext.class::cast)
                .flatMap(actionContext -> Optional.ofNullable(actionContext.getSession(false)))
                .ifPresent(httpSession -> register(httpSession, obj));
    }

    public static void unregister(ActionContext actioncontext, Object obj) {
        Optional.ofNullable(actioncontext)
                .filter(HttpActionContext.class::isInstance)
                .map(HttpActionContext.class::cast)
                .flatMap(actionContext -> Optional.ofNullable(actionContext.getSession(false)))
                .ifPresent(httpSession -> unregister(httpSession, obj));
    }

    public static void register(HttpSession httpsession, Object obj) {
        Optional.ofNullable(bindToHttpSession(httpsession))
                .ifPresent(httpEJBCleaner -> httpEJBCleaner.register(obj));
    }

    public static void unregister(HttpSession httpsession, Object obj) {
        Optional.ofNullable(bindToHttpSession(httpsession))
                .ifPresent(httpEJBCleaner -> httpEJBCleaner.unregister(obj));
    }

    public void register(Object obj) {
        Optional.ofNullable(obj)
                .ifPresent(object -> objects.add(obj));
    }

    public void unregister(Object obj) {
        Optional.ofNullable(obj)
                .ifPresent(object -> objects.remove(obj));
    }

    public void remove(Object obj) {
        try {
            if (obj instanceof BulkLoaderIterator) {
                ((BulkLoaderIterator) obj).ejbRemove();
            } else if (obj instanceof TransactionalBulkLoaderIterator) {
                ((TransactionalBulkLoaderIterator) obj).ejbRemove();
            } else if (obj instanceof it.cnr.jada.UserTransaction) {
                ((it.cnr.jada.UserTransaction) obj).remove();
            }
        } catch (jakarta.ejb.NoSuchEJBException ex) {
        } catch (Throwable _ex) {
            logger.warn("Cannot remove object: ", obj, _ex);
        }
    }

    public void remove() {
        objects.stream()
                .distinct()
                .forEach(obj -> remove(obj));
        objects.clear();
        logger.info("ActiveBulkLoaderIterator: {} ActiveUserTransaction: {}",
                EJBTracer.getInstance().getActiveBulkLoaderIteratorCounter(),
                EJBTracer.getInstance().getActiveUserTransactionCounter()
        );
    }
}