package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementBooleanParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementBooleanParameter(int i, boolean flag)
    {
        super(i);
        value = flag;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setBoolean(super.index, value);
    }

    private final boolean value;
}