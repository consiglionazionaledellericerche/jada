package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementShortParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementShortParameter(int i, short word0)
    {
        super(i);
        value = word0;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setShort(super.index, value);
    }

    private final short value;
}