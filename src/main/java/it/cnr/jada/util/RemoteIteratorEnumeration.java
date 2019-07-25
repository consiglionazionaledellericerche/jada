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

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator, RemotePagedIterator

public class RemoteIteratorEnumeration
        implements Serializable, Enumeration {

    private final RemoteIterator iterator;
    private Object[] page;
    private int position;

    public RemoteIteratorEnumeration(RemoteIterator remoteiterator) {
        iterator = remoteiterator;
        try {
            remoteiterator.moveTo(0);
            fetchPage();
        } catch (RemoteException _ex) {
        }
    }

    private void fetchPage()
            throws RemoteException {
        if ((iterator instanceof RemotePagedIterator) && (page == null || position >= page.length)) {
            if (((RemotePagedIterator) iterator).hasMorePages())
                page = ((RemotePagedIterator) iterator).nextPage();
            else
                page = new OggettoBulk[0];
            position = 0;
        }
    }

    public boolean hasMoreElements() {
        try {
            if (page == null)
                return iterator.hasMoreElements();
            return position < page.length;
        } catch (RemoteException _ex) {
            throw new NoSuchElementException();
        }
    }

    public Object nextElement() {
        try {
            if (page == null) {
                return iterator.nextElement();
            } else {
                Object obj = page[position++];
                fetchPage();
                return obj;
            }
        } catch (RemoteException _ex) {
            throw new NoSuchElementException();
        }
    }
}