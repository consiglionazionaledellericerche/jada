package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementBytesParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementBytesParameter(int i, byte abyte0[])
    {
        super(i);
        value = abyte0;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setBytes(super.index, value);
    }

    private final byte value[];
}