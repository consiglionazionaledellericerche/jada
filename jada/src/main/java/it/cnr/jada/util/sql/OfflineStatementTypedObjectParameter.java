package it.cnr.jada.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            OfflineStatementObjectParameter, AbstractOfflineStatementParameter

class OfflineStatementTypedObjectParameter extends OfflineStatementObjectParameter
{

    public OfflineStatementTypedObjectParameter(int i, Object obj, int j)
    {
        super(i, obj);
        targetSqlType = j;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setObject(super.index, super.value, targetSqlType);
    }

    protected final int targetSqlType;
}