package org.seo.util;

import java.util.Arrays;

public final class DataSourceConnectionSupport {

    private static final String DB2_VALIDATION_QUERY = "SELECT 1 FROM SYSIBM.SYSDUMMY1";
    private static final String PORTABLE_VALIDATION_QUERY = "SELECT 1";

    private DataSourceConnectionSupport() {
    }

    public static String buildUrl(String template, String ip, String port,
                                  String database, String schema) {
        if (template.contains("SCHEMA_NAME") && isBlank(schema)) {
            throw new IllegalArgumentException("Vastbase Schema不能为空");
        }
        return template.replace("ip", ip)
                .replace("port", port)
                .replace("DBNAME", database)
                .replace("SCHEMA_NAME", schema == null ? "" : schema);
    }

    public static String validationQuery(String driverClassName) {
        String driver = driverClassName == null ? "" : driverClassName.toLowerCase();
        return driver.contains("db2") ? DB2_VALIDATION_QUERY : PORTABLE_VALIDATION_QUERY;
    }

    public static String namespace(String type, String database, String schema) {
        return "Vastbase".equals(type) ? schema : database;
    }

    public static String[] schemaValues(String csv, int databaseCount) {
        if (isBlank(csv)) {
            String[] schemas = new String[databaseCount];
            Arrays.fill(schemas, "");
            return schemas;
        }
        String[] schemas = csv.split(",", -1);
        if (schemas.length != databaseCount) {
            throw new IllegalArgumentException("数据库与Schema配置数量不一致");
        }
        return schemas;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
