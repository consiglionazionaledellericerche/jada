package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementBigDecimalParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementBigDecimalParameter(int i, BigDecimal bigdecimal)
    {
        super(i);
        value = bigdecimal;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        preparedstatement.setBigDecimal(super.index, value);
    }

    private final BigDecimal value;
}