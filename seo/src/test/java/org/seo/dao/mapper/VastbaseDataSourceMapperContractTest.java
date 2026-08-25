package org.seo.dao.mapper;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VastbaseDataSourceMapperContractTest {

    @Test
    public void addDatabasePersistsSchemaName() throws Exception {
        Configuration configuration = mapperConfiguration();
        BoundSql boundSql = configuration
                .getMappedStatement("org.seo.dao.mapper.DataSourceMapper.addDataBase")
                .getBoundSql(parameters());

        String sql = normalize(boundSql.getSql());
        List<String> properties = boundSql.getParameterMappings().stream()
                .map(ParameterMapping::getProperty)
                .collect(Collectors.toList());

        assertTrue(sql.contains("INSERT INTO seo.seo_datasource_database"));
        assertTrue(sql.contains("DBNAME, SCHEMA_NAME"));
        assertTrue(properties.contains("params.SCHEMA_NAME"));
    }

    @Test
    public void duplicateLookupUsesDatabaseAndSchemaTuple() throws Exception {
        Configuration configuration = mapperConfiguration();
        BoundSql boundSql = configuration
                .getMappedStatement("org.seo.dao.mapper.DataSourceMapper.getDataBase")
                .getBoundSql(parameters());

        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("a.DBNAME = ?"));
        assertTrue(sql.contains("a.SCHEMA_NAME = ?"));
    }

    @Test
    public void datasourceEnumUsesStableMapKeysOnPostgresqlCompatibleDatabases() throws Exception {
        BoundSql boundSql = mapperConfiguration()
                .getMappedStatement("org.seo.dao.mapper.DataSourceMapper.getDataSourceEnum")
                .getBoundSql(parameters());

        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("a.URL AS \"URL\""));
        assertTrue(sql.contains("a.DRIVERCLASS AS \"DRIVERCLASS\""));
    }

    @Test
    public void vastbaseMetadataLookupUsesSchemaParameter() throws Exception {
        Map<String, Object> values = values();
        values.put("BASE_TYPE", "Vastbase");
        values.put("TABLE_SIGN", "fm_trs_guoku_base_table");

        BoundSql boundSql = boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataTableSelection",
                values);

        String sql = normalize(boundSql.getSql());
        List<String> properties = boundSql.getParameterMappings().stream()
                .map(ParameterMapping::getProperty)
                .collect(Collectors.toList());

        assertTrue(sql.contains("table_schema = ?"));
        assertTrue(sql.contains("table_type IN ('BASE TABLE', 'VIEW')"));
        assertTrue(properties.contains("params.SCHEMA_NAME"));
    }

    @Test
    public void mysqlMetadataLookupKeepsOnsiteInformationSchemaSemantics() throws Exception {
        Map<String, Object> values = values();
        values.put("BASE_TYPE", "Mysql");
        values.put("DATABASE", "edw");
        values.put("TABLE_SIGN", "trs_kyd_industry_copy4");

        BoundSql tableSql = boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataTableSelection",
                values);
        BoundSql commentSql = boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataTableComments",
                values);

        assertTrue(normalize(tableSql.getSql()).contains(
                "SELECT TABLE_NAME AS id,TABLE_NAME AS name FROM information_schema.TABLES"));
        assertTrue(normalize(tableSql.getSql()).contains("TABLE_SCHEMA = ?"));
        assertTrue(normalize(commentSql.getSql()).contains("TABLE_COMMENT"));
        assertTrue(normalize(commentSql.getSql()).contains("COLUMN_COMMENT"));
        assertTrue(tableSql.getParameterMappings().stream()
                .anyMatch(mapping -> "params.DATABASE".equals(mapping.getProperty())));
    }

    @Test
    public void databaseLabelsMatchOnsiteDbNameSemantics() throws Exception {
        String databaseSelectionSql = normalize(boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataBaseSelection",
                values()).getSql());
        String treeSql = normalize(boundSql(
                "mybatis/seo/DataTableMapper.xml",
                "org.seo.dao.mapper.DataTableMapper.getDataSourceTree",
                values()).getSql());

        assertTrue(databaseSelectionSql.contains("b.DBNAME AS name"));
        assertFalse(databaseSelectionSql.contains("THEN b.SCHEMA_NAME"));
        assertTrue(treeSql.contains("b.DBNAME AS lable"));
        assertFalse(treeSql.contains("THEN b.SCHEMA_NAME"));
    }

    @Test
    public void dataTableMaintenanceReturnsConfiguredSchemaWithoutChangingDatabaseLabels() throws Exception {
        String databaseSelectionSql = normalize(boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataBaseSelection",
                values()).getSql());
        String relationDataSql = normalize(boundSql(
                "mybatis/seo/DataTableMapper.xml",
                "org.seo.dao.mapper.DataTableMapper.getRelationData",
                values()).getSql());

        assertTrue(databaseSelectionSql.contains("b.DBNAME AS name"));
        assertTrue(databaseSelectionSql.contains("b.SCHEMA_NAME AS \"SCHEMA_NAME\""));
        assertTrue(relationDataSql.contains("b.DBNAME AS \"DBNAME\""));
        assertTrue(relationDataSql.contains("b.SCHEMA_NAME AS \"SCHEMA_NAME\""));
    }

    @Test
    public void mappersResolveVastbaseSchemaInternallyByDatabaseId() throws Exception {
        String auxiliarySql = normalize(boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataSourceInfo",
                values()).getSql());
        BoundSql databaseInfo = boundSql(
                "mybatis/seo/DataAuxiliaryMapper.xml",
                "org.seo.dao.mapper.DataAuxiliaryMapper.getDataBaseInfo",
                values());
        String typeSql = normalize(boundSql(
                "mybatis/seo/ComprehensiveQueryMapper.xml",
                "org.seo.dao.mapper.ComprehensiveQueryMapper.getType",
                values()).getSql());

        assertTrue(auxiliarySql.contains("AS \"NAMESPACE\""));
        assertTrue(auxiliarySql.contains("b.SCHEMA_NAME"));
        String databaseInfoSql = normalize(databaseInfo.getSql());
        assertTrue(databaseInfoSql.contains("b.DBNAME AS \"DBNAME\""));
        assertTrue(databaseInfoSql.contains("b.SCHEMA_NAME AS \"SCHEMA_NAME\""));
        assertTrue(databaseInfoSql.contains("b.ID = ?"));
        assertTrue(typeSql.contains("a.SCHEMA_NAME AS \"SCHEMA_NAME\""));
        assertTrue(typeSql.contains("AS \"DATABASE_NAME\""));
        assertTrue(typeSql.contains("AS \"DBNAME\""));
    }

    private Configuration mapperConfiguration() throws Exception {
        return mapperConfiguration("mybatis/seo/DataSourceMapper.xml");
    }

    private Configuration mapperConfiguration(String resource) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("Missing mapper resource: " + resource);
            }
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input, configuration, resource, configuration.getSqlFragments());
            builder.parse();
        }
        return configuration;
    }

    private BoundSql boundSql(String resource, String statement, Map<String, Object> values)
            throws Exception {
        Map<String, Object> wrapped = new HashMap<>();
        wrapped.put("params", values);
        return mapperConfiguration(resource).getMappedStatement(statement).getBoundSql(wrapped);
    }

    private Map<String, Object> parameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("params", values());
        return parameters;
    }

    private Map<String, Object> values() {
        Map<String, Object> values = new HashMap<>();
        values.put("ID", "database-id");
        values.put("SOURCE_ID", "source-id");
        values.put("DBNAME", "gk_data_analysis");
        values.put("SCHEMA_NAME", "edw");
        values.put("USERNAME", "test-user");
        values.put("PASSWORD", "test-password");
        values.put("STATE", "0");
        values.put("CREATE_TIME", "2026-07-27");
        values.put("CREATE_USER", "test-user-id");
        values.put("DRIVERCLASS_NAME", "org.postgresql.Driver");
        values.put("DATASOURCE_URL", "jdbc:postgresql://127.0.0.1:5432/gk_data_analysis?currentSchema=edw");
        values.put("DATABASE_ID", "database-id");
        values.put("TABLE_NAME", new String[0]);
        return values;
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}
