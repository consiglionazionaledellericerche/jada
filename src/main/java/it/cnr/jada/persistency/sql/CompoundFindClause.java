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

package it.cnr.jada.persistency.sql;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            FindClause, SimpleFindClause

public class CompoundFindClause
        implements FindClause, Serializable {
    private Vector clauses;
    private String logicalOperator;


    public CompoundFindClause() {
        clauses = new Vector();
    }

    public CompoundFindClause(FindClause findclause) {
        clauses = new Vector(2);
        clauses.add(findclause);
    }

    public CompoundFindClause(FindClause findclause, FindClause findclause1) {
        clauses = new Vector(2);
        clauses.add(findclause);
        clauses.add(findclause1);
    }

    public static CompoundFindClause and(FindClause findclause, FindClause findclause1) {
        if (findclause == null)
            return identity(findclause1);
        if (findclause1 == null) {
            return identity(findclause);
        } else {
            findclause1.setLogicalOperator("AND");
            return new CompoundFindClause(findclause, findclause1);
        }
    }

    public static CompoundFindClause identity(FindClause findclause) {
        if (findclause == null)
            return null;
        if (findclause instanceof CompoundFindClause)
            return (CompoundFindClause) findclause;
        else
            return new CompoundFindClause(findclause);
    }

    public static CompoundFindClause or(FindClause findclause, FindClause findclause1) {
        if (findclause == null)
            return identity(findclause1);
        if (findclause1 == null) {
            return identity(findclause);
        } else {
            findclause1.setLogicalOperator("OR");
            return new CompoundFindClause(findclause, findclause1);
        }
    }

    public void addChild(FindClause findclause) {
        clauses.addElement(findclause);
    }

    public void addClause(String s, String s1, int i, Object obj) {
        addChild(new SimpleFindClause(s, s1, i, obj));
    }

    public void addClause(String s, String s1, int i, Object obj, boolean flag) {
        addChild(new SimpleFindClause(s, s1, i, obj, flag));
    }

    private LocalItr createLocalItr(LocalItr localitr) {
        LocalItr localitr1 = new LocalItr();
        localitr1.parent = localitr;
        return localitr1;
    }

    public Enumeration getClauses() {
        return clauses.elements();
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String s) {
        logicalOperator = s;
    }

    public Iterator iterator() {
        return new CompoundFindClauseIterator();
    }

    public String toString() {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        printwriter.println("AND (");
        for (Enumeration enumeration = clauses.elements(); enumeration.hasMoreElements(); printwriter.println(enumeration.nextElement()))
            ;
        return stringwriter.getBuffer().toString();
    }

    class LocalItr
            implements Serializable {

        public LocalItr parent;
        public Iterator iterator;

        LocalItr() {
            iterator = clauses.iterator();
        }
    }

    class CompoundFindClauseIterator
            implements Serializable, Iterator {

        LocalItr i;
        FindClause nextFindClause;

        public CompoundFindClauseIterator() {
            i = new LocalItr();
            nextFindClause = nextFindClause();
        }

        private FindClause nextFindClause() {
            while (i != null) {
                if (i.iterator.hasNext()) {
                    FindClause findclause = (FindClause) i.iterator.next();
                    if (findclause instanceof SimpleFindClause)
                        return findclause;
                    i = ((CompoundFindClause) findclause).createLocalItr(i);
                } else {
                    i = i.parent;
                }
            }
            return null;
        }

        public boolean hasNext() {
            return nextFindClause != null;
        }

        public Object next() {
            if (nextFindClause == null) {
                throw new NoSuchElementException();
            } else {
                FindClause findclause = nextFindClause;
                nextFindClause = nextFindClause();
                return findclause;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}