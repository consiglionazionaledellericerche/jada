package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementDoubleParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementDoubleParameter(int i, double d)
    {
        super(i);
        value = d;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setDouble(super.index, value);
    }

    private final double value;
}