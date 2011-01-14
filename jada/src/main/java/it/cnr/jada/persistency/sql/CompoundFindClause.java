package it.cnr.jada.persistency.sql;

import java.io.*;
import java.util.*;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            FindClause, SimpleFindClause

public class CompoundFindClause
    implements FindClause, Serializable
{
    class LocalItr
        implements Serializable
    {

        public LocalItr parent;
        public Iterator iterator;

        LocalItr()
        {
            iterator = clauses.iterator();
        }
    }

    class CompoundFindClauseIterator
        implements Serializable, Iterator
    {

        private FindClause nextFindClause()
        {
            while(i != null){ 
                if(i.iterator.hasNext())
                {
                    FindClause findclause = (FindClause)i.iterator.next();
                    if(findclause instanceof SimpleFindClause)
                        return findclause;
                    i = ((CompoundFindClause)findclause).createLocalItr(i);
                } else
                {
                    i = i.parent;
                }
            }    
            return null;
        }

        public boolean hasNext()
        {
            return nextFindClause != null;
        }

        public Object next()
        {
            if(nextFindClause == null)
            {
                throw new NoSuchElementException();
            } else
            {
                FindClause findclause = nextFindClause;
                nextFindClause = nextFindClause();
                return findclause;
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        LocalItr i;
        FindClause nextFindClause;

        public CompoundFindClauseIterator()
        {
            i = new LocalItr();
            nextFindClause = nextFindClause();
        }
    }


    public CompoundFindClause()
    {
        clauses = new Vector();
    }

    public CompoundFindClause(FindClause findclause)
    {
        clauses = new Vector(2);
        clauses.add(findclause);
    }

    public CompoundFindClause(FindClause findclause, FindClause findclause1)
    {
        clauses = new Vector(2);
        clauses.add(findclause);
        clauses.add(findclause1);
    }

    public void addChild(FindClause findclause)
    {
        clauses.addElement(findclause);
    }

    public void addClause(String s, String s1, int i, Object obj)
    {
        addChild(new SimpleFindClause(s, s1, i, obj));
    }

    public void addClause(String s, String s1, int i, Object obj, boolean flag)
    {
        addChild(new SimpleFindClause(s, s1, i, obj, flag));
    }

    public static CompoundFindClause and(FindClause findclause, FindClause findclause1)
    {
        if(findclause == null)
            return identity(findclause1);
        if(findclause1 == null)
        {
            return identity(findclause);
        } else
        {
            findclause1.setLogicalOperator("AND");
            return new CompoundFindClause(findclause, findclause1);
        }
    }

    private LocalItr createLocalItr(LocalItr localitr)
    {
        LocalItr localitr1 = new LocalItr();
        localitr1.parent = localitr;
        return localitr1;
    }

    public Enumeration getClauses()
    {
        return clauses.elements();
    }

    public String getLogicalOperator()
    {
        return logicalOperator;
    }

    public static CompoundFindClause identity(FindClause findclause)
    {
        if(findclause == null)
            return null;
        if(findclause instanceof CompoundFindClause)
            return (CompoundFindClause)findclause;
        else
            return new CompoundFindClause(findclause);
    }

    public Iterator iterator()
    {
        return new CompoundFindClauseIterator();
    }

    public static CompoundFindClause or(FindClause findclause, FindClause findclause1)
    {
        if(findclause == null)
            return identity(findclause1);
        if(findclause1 == null)
        {
            return identity(findclause);
        } else
        {
            findclause1.setLogicalOperator("OR");
            return new CompoundFindClause(findclause, findclause1);
        }
    }

    public void setLogicalOperator(String s)
    {
        logicalOperator = s;
    }

    public String toString()
    {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        printwriter.println("AND (");
        for(Enumeration enumeration = clauses.elements(); enumeration.hasMoreElements(); printwriter.println(enumeration.nextElement()));
        return stringwriter.getBuffer().toString();
    }

    private Vector clauses;
    private String logicalOperator;


}