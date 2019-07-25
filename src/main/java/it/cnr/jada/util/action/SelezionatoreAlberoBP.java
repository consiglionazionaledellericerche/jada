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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.NodoAlbero;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package it.cnr.jada.util.action:
//            AbstractSelezionatoreBP, RigaAlbero

public class SelezionatoreAlberoBP extends AbstractSelezionatoreBP
        implements Serializable {

    private NodoAlbero radice;
    private int numeroLivelli;
    private Vector righe;
    private boolean mostraRadice;

    public SelezionatoreAlberoBP() {
    }

    public SelezionatoreAlberoBP(String s) {
        super(s);
    }

    protected void aggiornaRighe() {
        numeroLivelli = 0;
        righe = new Vector();
        if (mostraRadice) {
            RigaAlbero rigaalbero = new RigaAlbero();
            righe.addElement(rigaalbero);
            rigaalbero.setLivello(0);
            rigaalbero.setNodo(radice);
            rigaalbero.setUltimo(true);
            numeroLivelli++;
        }
        costruisciRighe(radice.getFigliNodo(), numeroLivelli);
        numeroLivelli++;
    }

    private void costruisciRighe(Enumeration enumeration, int i) {
        if (enumeration == null)
            return;
        if (numeroLivelli < i)
            numeroLivelli = i;
        Enumeration enumeration1;
        for (; enumeration.hasMoreElements(); costruisciRighe(enumeration1, i + 1)) {
            NodoAlbero nodoalbero = (NodoAlbero) enumeration.nextElement();
            RigaAlbero rigaalbero = new RigaAlbero();
            righe.addElement(rigaalbero);
            rigaalbero.setLivello(i);
            rigaalbero.setNodo(nodoalbero);
            rigaalbero.setUltimo(!enumeration.hasMoreElements());
            enumeration1 = nodoalbero.getFigliNodo();
        }

    }

    public Object getElementAt(ActionContext actioncontext, int i) {
        return ((RigaAlbero) righe.elementAt(i)).getNodo().getObject();
    }

    public int getNumeroLivelli() {
        return numeroLivelli;
    }

    public NodoAlbero getRadice() {
        return radice;
    }

    public void setRadice(NodoAlbero nodoalbero) {
        radice = nodoalbero;
        aggiornaRighe();
    }

    public Enumeration getRighe() {
        return righe.elements();
    }

    public int indexOf(Object obj) {
        int i = 0;
        for (Enumeration enumeration = righe.elements(); enumeration.hasMoreElements(); ) {
            if (((RigaAlbero) enumeration.nextElement()).getNodo().getObject() == obj)
                return i;
            i++;
        }

        return -1;
    }

    public boolean isMostraRadice() {
        return mostraRadice;
    }

    public void setMostraRadice(boolean flag) {
        mostraRadice = flag;
    }

    public int size() {
        return righe.size();
    }
}