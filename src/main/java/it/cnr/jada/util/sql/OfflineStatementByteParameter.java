package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementByteParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementByteParameter(int i, byte byte0)
    {
        super(i);
        value = byte0;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setByte(super.index, value);
    }

    private final byte value;
}