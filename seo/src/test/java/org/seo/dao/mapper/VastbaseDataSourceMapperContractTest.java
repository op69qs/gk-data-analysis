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

    private Configuration mapperConfiguration() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "mybatis/seo/DataSourceMapper.xml";
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

    private Map<String, Object> parameters() {
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

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("params", values);
        return parameters;
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}
