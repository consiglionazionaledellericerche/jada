package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.*;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementArrayParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementArrayParameter(int i, Array array)
    {
        super(i);
        value = array;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setArray(super.index, value);
    }

    private final Array value;
}