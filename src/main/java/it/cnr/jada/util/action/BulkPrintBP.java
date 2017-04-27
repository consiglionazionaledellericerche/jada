package it.cnr.jada.util.action;

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

// Referenced classes of package it.cnr.jada.util.action:
//            AbstractPrintBP, FormBP

public class BulkPrintBP extends AbstractPrintBP
    implements Serializable
{

    public BulkPrintBP()
    {
    }

    public OggettoBulk getBulk()
    {
        return bulk;
    }

    public void print(PageContext pagecontext)
        throws ServletException, IOException, BusinessProcessException
    {
        bulk.writeForm(pagecontext.getOut(), 2, getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    public void setBulk(OggettoBulk oggettobulk)
    {
        bulk = oggettobulk;
    }

    private OggettoBulk bulk;
}