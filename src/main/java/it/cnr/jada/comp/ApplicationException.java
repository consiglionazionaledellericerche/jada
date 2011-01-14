package it.cnr.jada.comp;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ComponentException

public class ApplicationException extends ComponentException
    implements Serializable
{

    public ApplicationException()
    {
    }

    public ApplicationException(String s)
    {
        super(s);
    }

    public ApplicationException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ApplicationException(Throwable throwable)
    {
        super(throwable);
    }
}