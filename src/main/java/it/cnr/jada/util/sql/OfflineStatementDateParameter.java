package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.*;
import java.util.Calendar;

// Referenced classes of package it.cnr.jada.util.sql:
//            AbstractOfflineStatementParameter

class OfflineStatementDateParameter extends AbstractOfflineStatementParameter
    implements Serializable
{

    public OfflineStatementDateParameter(int i, Date date)
    {
        super(i);
        value = date;
        calendar = null;
    }

    public OfflineStatementDateParameter(int i, Date date, Calendar calendar1)
    {
        super(i);
        value = date;
        calendar = calendar1;
    }

    public void set(PreparedStatement preparedstatement)
        throws SQLException
    {
        if(calendar == null)
            preparedstatement.setDate(super.index, value);
        else
            preparedstatement.setDate(super.index, value, calendar);
    }

    private final Date value;
    private final Calendar calendar;
}