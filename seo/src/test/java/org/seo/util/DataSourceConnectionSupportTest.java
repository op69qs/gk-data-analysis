package org.seo.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DataSourceConnectionSupportTest {

    @Test
    public void buildsVastbaseUrlWithDatabaseAndSchema() {
        String actual = DataSourceConnectionSupport.buildUrl(
                "jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME",
                "100.71.11.54",
                "25432",
                "gk_data_analysis",
                "edw");

        assertEquals(
                "jdbc:postgresql://100.71.11.54:25432/gk_data_analysis?currentSchema=edw",
                actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingSchemaWhenTemplateRequiresIt() {
        DataSourceConnectionSupport.buildUrl(
                "jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME",
                "127.0.0.1",
                "5432",
                "gk_data_analysis",
                "");
    }

    @Test
    public void keepsLegacyUrlTemplateBehavior() {
        String actual = DataSourceConnectionSupport.buildUrl(
                "jdbc:mysql://ip:port/DBNAME?useSSL=false",
                "127.0.0.1",
                "3306",
                "ods",
                null);

        assertEquals("jdbc:mysql://127.0.0.1:3306/ods?useSSL=false", actual);
    }

    @Test
    public void choosesPortableValidationQueries() {
        assertEquals("SELECT 1",
                DataSourceConnectionSupport.validationQuery("org.postgresql.Driver"));
        assertEquals("SELECT 1",
                DataSourceConnectionSupport.validationQuery("ru.yandex.clickhouse.ClickHouseDriver"));
        assertEquals("SELECT 1 FROM SYSIBM.SYSDUMMY1",
                DataSourceConnectionSupport.validationQuery("com.ibm.db2.jcc.DB2Driver"));
    }

    @Test
    public void choosesSchemaOnlyForVastbaseNamespace() {
        assertEquals("edw",
                DataSourceConnectionSupport.namespace("Vastbase", "gk_data_analysis", "edw"));
        assertEquals("ods",
                DataSourceConnectionSupport.namespace("Mysql", "ods", null));
    }

    @Test
    public void alignsEditedSchemasWithDatabaseRows() {
        assertArrayEquals(
                new String[]{"edw", "ods"},
                DataSourceConnectionSupport.schemaValues("edw,ods", 2));
        assertArrayEquals(
                new String[]{"", ""},
                DataSourceConnectionSupport.schemaValues("", 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMisalignedEditedSchemaRows() {
        DataSourceConnectionSupport.schemaValues("edw", 2);
    }
}
