package it.cnr.jada.comp;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ComponentException

public class NoRollbackException extends ComponentException
    implements Serializable
{

    public NoRollbackException()
    {
    }

    public NoRollbackException(String s)
    {
        super(s);
    }

    public NoRollbackException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public NoRollbackException(Throwable throwable)
    {
        super(throwable);
    }
}