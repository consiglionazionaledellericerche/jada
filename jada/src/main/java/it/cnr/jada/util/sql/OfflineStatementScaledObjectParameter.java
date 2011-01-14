package it.cnr.jada.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            OfflineStatementTypedObjectParameter, AbstractOfflineStatementParameter, OfflineStatementObjectParameter

public class OfflineStatementScaledObjectParameter extends OfflineStatementTypedObjectParameter
{

    public OfflineStatementScaledObjectParameter(int i, Object obj, int j, int k)
    {
        super(i, obj, j);
        scale = k;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setObject(super.index, super.value, super.targetSqlType, scale);
    }

    private final int scale;
}