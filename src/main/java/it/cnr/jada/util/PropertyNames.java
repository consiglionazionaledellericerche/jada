package it.cnr.jada.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Properties;

public class PropertyNames {
    private static final Log logger = Log.getInstance(PropertyNames.class);
    private static Properties properties;

    static {
        try {
            properties = loadFromFile(Optional.ofNullable(System.getenpom.xmlv("SIGLA_POSTGRES_ENABLE"))
                    .map(s -> Boolean.valueOf(s))
                    .filter(aBoolean -> aBoolean.equals(Boolean.TRUE))
                    .map(aBoolean -> "postgres.properties")
                    .orElseGet(() ->
                            Optional.ofNullable(System.getenv("SIGLA_ORACLE_ENABLE"))
                                    .map(s -> Boolean.valueOf(s))
                                    .filter(aBoolean -> aBoolean.equals(Boolean.TRUE))
                                    .map(aBoolean -> "oracle.properties")
                                    .orElse("oracle.properties")
                    ));
        } catch (IOException e) {
            logger.error("Cannot load property file", e);
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
