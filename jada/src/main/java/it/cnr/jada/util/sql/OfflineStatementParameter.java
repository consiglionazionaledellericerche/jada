package it.cnr.jada.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface OfflineStatementParameter
{

    public abstract int getIndex();

    public abstract void set(PreparedStatement preparedstatement)
        throws SQLException;
}