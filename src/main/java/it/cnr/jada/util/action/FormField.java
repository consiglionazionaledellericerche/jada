package it.cnr.jada.util.action;

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            FormController

public class FormField
    implements Serializable
{

    public FormField(FormController formcontroller, FieldProperty fieldproperty, OggettoBulk oggettobulk)
    {
        formController = formcontroller;
        field = fieldproperty;
        model = oggettobulk;
    }

    public FieldProperty getField()
    {
        return field;
    }

    public FormController getFormController()
    {
        return formController;
    }

    public OggettoBulk getModel()
    {
        return model;
    }

    private FormController formController;
    private FieldProperty field;
    private OggettoBulk model;
}