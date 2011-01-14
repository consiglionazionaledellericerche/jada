package it.cnr.jada.util.action;

import it.cnr.jada.bulk.FieldValidationMap;

import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.util.action:
//            Selection

public interface ListController
{

    public abstract String getControllerName();

    public abstract Enumeration getElements();

    public abstract FieldValidationMap getFieldValidationMap();

    public abstract String getInputPrefix();

    public abstract Selection getSelection();

    public abstract boolean isGrowable();

    public abstract boolean isShrinkable();
}