package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;

// Referenced classes of package it.cnr.jada.util.action:
//            NestedFormController, FormController

public class SimpleNestedFormController extends NestedFormController
{

    public SimpleNestedFormController(String s, Class class1, FormController formcontroller)
    {
        super(s, formcontroller);
        modelClass = class1;
        bulkInfo = BulkInfo.getBulkInfo(class1);
    }

    public BulkInfo getBulkInfo()
    {
        return bulkInfo;
    }

    public void setModel(ActionContext actioncontext, OggettoBulk oggettobulk)
    {
        super.setModel(actioncontext, oggettobulk);
        resetChildren(actioncontext);
    }

    private final BulkInfo bulkInfo;
    private final Class modelClass;
}