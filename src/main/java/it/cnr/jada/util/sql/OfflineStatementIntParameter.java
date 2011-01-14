package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementIntParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementIntParameter(int i, int j)
    {
        super(i);
        value = j;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setInt(super.index, value);
    }

    private final int value;
}