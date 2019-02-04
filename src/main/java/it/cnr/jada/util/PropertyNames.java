package it.cnr.jada.util;

import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

public class PropertyNames {
    private static final Logger logger = LoggerFactory.getLogger(PropertyNames.class);
    private static Properties properties;

    public static final String ORACLE_PROPERTIES = "oracle.properties";
    public static final String POSTGRES_PROPERTIES = "postgres.properties";
    public static final String POSTGRE_SQL = "PostgreSQL";
    public static final String H2 = "H2";
    public static final String ORACLE = "Oracle";

    static {
        Connection connection = null;
        try {
            connection = EJBCommonServices.getConnection();
            final String databaseProductName = Optional.ofNullable(connection.getMetaData().getDatabaseProductName())
                    .orElse(ORACLE);
            switch (databaseProductName) {
                case POSTGRE_SQL: {
                    properties = loadFromFile(POSTGRES_PROPERTIES);
                    break;
                }
                case H2: {
                    properties = loadFromFile(POSTGRES_PROPERTIES);
                    break;
                }
                case ORACLE: {
                    properties = loadFromFile(ORACLE_PROPERTIES);
                    break;
                }
                default: {
                    properties = loadFromFile(ORACLE_PROPERTIES);
                    break;
                }
            }
        } catch (IOException|SQLException e) {
            logger.error("Cannot load property file", e);
        } finally {
            Optional.ofNullable(connection)
                    .ifPresent(conn -> {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            logger.error("Cannot close connection", e);
                        }
                    });
        }
    }

    private static Properties loadFromFile(String filename) throws IOException {
        logger.info("Config location: ", filename);
        try (InputStream stream = PropertyNames.class.getResourceAsStream(filename)) {
            Properties config = new Properties();
            config.load(stream);
            return config;
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, Object... placeholder) {
        return MessageFormat.format(properties.getProperty(key), placeholder);
    }
}
