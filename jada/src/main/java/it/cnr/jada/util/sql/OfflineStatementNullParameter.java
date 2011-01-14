package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementNullParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementNullParameter(int i, int j)
    {
        super(i);
        sqlType = j;
        typeName = null;
    }

    public OfflineStatementNullParameter(int i, int j, String s)
    {
        super(i);
        sqlType = j;
        typeName = s;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        if(typeName == null)
            preparedstatement.setNull(super.index, sqlType);
        else
            preparedstatement.setNull(super.index, sqlType, typeName);
    }

    private final int sqlType;
    private final String typeName;
}