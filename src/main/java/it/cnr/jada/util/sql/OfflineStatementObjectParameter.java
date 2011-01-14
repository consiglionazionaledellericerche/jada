package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementObjectParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementObjectParameter(int i, Object obj)
    {
        super(i);
        value = obj;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setObject(super.index, value);
    }

    protected final Object value;
}