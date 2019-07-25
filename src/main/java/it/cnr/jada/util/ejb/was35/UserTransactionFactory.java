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

package it.cnr.jada.util.ejb.was35;

import it.cnr.jada.UserTransaction;

// Referenced classes of package it.cnr.jada.util.ejb.was35:
//            UserTransaction

public class UserTransactionFactory extends it.cnr.jada.util.ejb.UserTransactionFactory {

    public UserTransactionFactory() {
    }

    public UserTransaction createUserTransaction(Object obj) {
        return new it.cnr.jada.util.ejb.was35.UserTransaction(obj);
    }
}