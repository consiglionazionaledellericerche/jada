package it.cnr.jada.comp;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ApplicationException

public class OptionRequestException extends ApplicationException
    implements Serializable
{

    public OptionRequestException(String s, String s1)
    {
        this(s, s1, 1);
    }

    public OptionRequestException(String s, String s1, int i)
    {
        super(s1);
        name = s;
        type = i;
    }

    public final String getName()
    {
        return name;
    }

    public final int getType()
    {
        return type;
    }

    public static final int CONFIRM = 1;
    public static final int CONFIRM_YES_NO = 2;
    public static final int CONFIRM_YES_NO_CANCEL = 3;
    private final int type;
    private final String name;
}