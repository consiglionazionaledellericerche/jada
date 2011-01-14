package it.cnr.jada.bulk;

import it.cnr.jada.action.*;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.bulk:
//            OggettoBulk

public class UserInfo extends OggettoBulk
    implements Serializable
{

    public UserInfo()
    {
    }

    public BusinessProcess createBusinessProcess(ActionContext actioncontext, String s)
        throws BusinessProcessException
    {
        return actioncontext.createBusinessProcess(s);
    }

    public BusinessProcess createBusinessProcess(ActionContext actioncontext, String s, Object aobj[])
        throws BusinessProcessException
    {
        return actioncontext.createBusinessProcess(s, aobj);
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String s)
    {
        userid = s;
    }

    private String userid;
}