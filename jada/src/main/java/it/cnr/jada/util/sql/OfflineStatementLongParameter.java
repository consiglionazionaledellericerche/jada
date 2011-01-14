package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementLongParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementLongParameter(int i, long l)
    {
        super(i);
        value = l;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setLong(super.index, value);
    }

    private final long value;
}