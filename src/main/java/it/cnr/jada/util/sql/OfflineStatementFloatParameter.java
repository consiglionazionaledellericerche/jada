package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementFloatParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementFloatParameter(int i, float f)
    {
        super(i);
        value = f;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setFloat(super.index, value);
    }

    private final float value;
}