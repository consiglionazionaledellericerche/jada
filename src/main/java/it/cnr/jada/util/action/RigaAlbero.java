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

import it.cnr.jada.util.NodoAlbero;

import java.io.Serializable;

public class RigaAlbero
        implements Serializable {

    private int livello;
    private NodoAlbero nodo;
    private boolean ultimo;

    public RigaAlbero() {
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int i) {
        livello = i;
    }

    public NodoAlbero getNodo() {
        return nodo;
    }

    public void setNodo(NodoAlbero nodoalbero) {
        nodo = nodoalbero;
    }

    public boolean isUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean flag) {
        ultimo = flag;
    }
}