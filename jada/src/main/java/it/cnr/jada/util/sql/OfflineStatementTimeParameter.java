package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.*;
import java.util.Calendar;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementTimeParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementTimeParameter(int i, Time time)
    {
        super(i);
        value = time;
        calendar = null;
    }

    public OfflineStatementTimeParameter(int i, Time time, Calendar calendar1)
    {
        super(i);
        value = time;
        calendar = calendar1;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        if(calendar == null)
            preparedstatement.setTime(super.index, value);
        else
            preparedstatement.setTime(super.index, value, calendar);
    }

    private final Time value;
    private final Calendar calendar;
}