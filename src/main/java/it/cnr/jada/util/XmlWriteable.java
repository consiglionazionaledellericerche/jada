package it.cnr.jada.util;

import java.io.IOException;

// Referenced classes of package it.cnr.jada.util:
//            XmlWriter

public interface XmlWriteable
{

    public abstract void writeTo(XmlWriter xmlwriter)
        throws IOException;
}