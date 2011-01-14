package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementStringParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementStringParameter(int i, String s)
    {
        super(i);
        value = s;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setString(super.index, value);
    }

    private final String value;
}