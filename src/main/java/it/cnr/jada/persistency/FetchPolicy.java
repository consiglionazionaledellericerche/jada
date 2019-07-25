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

package it.cnr.jada.persistency;

import it.cnr.jada.util.XmlWriteable;

public interface FetchPolicy
        extends XmlWriteable {

    FetchPolicy addFetchPolicy(FetchPolicy fetchpolicy);

    FetchPolicy addPrefix(String s);

    boolean excludePrefix(String s);

    boolean include(FetchPolicy fetchpolicy);

    boolean include(String s);

    boolean includePrefix(String s);

    FetchPolicy removeFetchPolicy(FetchPolicy fetchpolicy);
}