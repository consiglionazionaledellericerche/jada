package it.cnr.jada.persistency.sql;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CHARToBooleanConverter implements SQLConverter<java.lang.Boolean>, Serializable {
	private static final long serialVersionUID = 1L;

	public CHARToBooleanConverter() {
		super();
	}

	public Class<java.lang.Boolean> getTargetJavaType(int i, boolean flag) {
		return java.lang.Boolean.class;
	}

	public void javaToSql(LoggableStatement preparedstatement, Object obj,
			int i, int j) throws SQLException {
		if (obj == null)
			preparedstatement.setNull(i, j);
		else
			preparedstatement.setString(i, ((Boolean) obj).booleanValue() ? "Y"
					: "N");
	}

	public Object sqlToJava(ResultSet resultset, String s) throws SQLException {
		String s1 = resultset.getString(s);
		if (s1 == null || s1.length() == 0) {
			return null;
		} else {
			char c = Character.toUpperCase(s1.charAt(0));
			return new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == '1');
		}
	}

	public Object javaToSql(Object obj) {
		return ((Boolean) obj).booleanValue() ? "Y" : "N";
	}

	public Object sqlToJava(Object obj) {
		String s1 = (String) obj;
		if (s1 == null || s1.length() == 0) {
			return null;
		} else {
			char c = Character.toUpperCase(s1.charAt(0));
			return new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == '1');
		}
	}

	public String columnName(String columnName) {
		return columnName;
	}
}