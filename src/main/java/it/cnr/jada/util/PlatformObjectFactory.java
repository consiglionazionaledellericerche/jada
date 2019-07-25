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

package it.cnr.jada.util;


public abstract class PlatformObjectFactory {

    private PlatformObjectFactory() {
    }

    public static final Object createInstance(Class class1, String s) {
        int i = class1.getName().lastIndexOf('.');
        try {
            return class1.getClassLoader().loadClass(class1.getName().substring(0, i) + "." + s + class1.getName().substring(i)).newInstance();
        } catch (ClassNotFoundException _ex) {
            return null;
        } catch (InstantiationException instantiationexception) {
            throw new InstantiationError(instantiationexception.getMessage());
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }
}