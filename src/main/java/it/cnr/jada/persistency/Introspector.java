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


// Referenced classes of package it.cnr.jada.persistency:
//            IntrospectionException, KeyedPersistent, PersistentInfo

public interface Introspector {

    String getOid(KeyedPersistent keyedpersistent)
            throws IntrospectionException;

    PersistentInfo getPersistentInfo(Class class1)
            throws IntrospectionException;

    Class getPropertyType(Class class1, String s)
            throws IntrospectionException;

    Object getPropertyValue(Object obj, String s)
            throws IntrospectionException;

    void setPropertyValue(Object obj, String s, Object obj1)
            throws IntrospectionException;
}