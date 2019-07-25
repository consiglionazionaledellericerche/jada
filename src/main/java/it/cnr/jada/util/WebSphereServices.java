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

import javax.servlet.ServletContext;

public abstract class WebSphereServices {

    private static final WebSphereServices instance;

    static {
        instance = (WebSphereServices) PlatformObjectFactory.createInstance(it.cnr.jada.util.WebSphereServices.class, "was35");
    }

    public WebSphereServices() {
    }

    public static final WebSphereServices getInstance() {
        return instance;
    }

    public abstract String getCloneIndex();

    //public abstract void suspendTransaction();

    public abstract String getQueueName();

    public abstract String getResourcePath(ServletContext servletcontext, String s);
}