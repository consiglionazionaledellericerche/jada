package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            OfflineStatementParameter

public abstract class AbstractOfflineStatementParameter
    implements OfflineStatementParameter, Serializable
{

    public AbstractOfflineStatementParameter(int i)
    {
        index = i;
    }

    public final int getIndex()
    {
        return index;
    }

    public abstract void set(PreparedStatement preparedstatement)
        throws SQLException;

    protected final int index;
}