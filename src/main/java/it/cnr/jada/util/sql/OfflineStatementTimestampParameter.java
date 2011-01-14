package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.*;
import java.util.Calendar;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementTimestampParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementTimestampParameter(int i, Timestamp timestamp)
    {
        super(i);
        value = timestamp;
        calendar = null;
    }

    public OfflineStatementTimestampParameter(int i, Timestamp timestamp, Calendar calendar1)
    {
        super(i);
        value = timestamp;
        calendar = calendar1;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        if(calendar == null)
            preparedstatement.setTimestamp(super.index, value);
        else
            preparedstatement.setTimestamp(super.index, value, calendar);
    }

    private final Timestamp value;
    private final Calendar calendar;
}