package it.cnr.jada.util.action;

import it.cnr.jada.bulk.OggettoBulk;

public interface ModelController
{

    public abstract OggettoBulk getModel();

    public abstract int getStatus();

    public abstract boolean isDirty();

    public abstract boolean isInputReadonly();

    public abstract void setDirty(boolean flag);
}