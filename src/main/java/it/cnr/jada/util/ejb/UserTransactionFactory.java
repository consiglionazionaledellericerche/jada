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

import it.cnr.jada.UserTransaction;
import it.cnr.jada.util.PlatformObjectFactory;

public abstract class UserTransactionFactory {

    private static final UserTransactionFactory factory;

    static {
        factory = (UserTransactionFactory) PlatformObjectFactory.createInstance(it.cnr.jada.util.ejb.UserTransactionFactory.class, "was35");
    }

    public UserTransactionFactory() {
    }

    public static final UserTransactionFactory getFactory() {
        return factory;
    }

    public abstract UserTransaction createUserTransaction(Object obj);
}