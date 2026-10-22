package org.jeecg.modules.visualScreen.controller;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.jeecg.modules.dimnsnSetting.mapper.GuokuMapper;
import org.jeecg.modules.dimnsnSetting.model.TreeNodeNews;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.TreeFilterHeaper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/** Opt in explicitly; every connection is forced read-only and rolled back. */
public class VisEditorReadOnlyIntegrationTest {
    private SqlSession session;
    private Connection connection;

    @Before
    public void connect() throws Exception {
        assumeTrue("Explicit live read-only acceptance only", Boolean.getBoolean("vis.editor.readonly.integration"));
        String configPath = System.getProperty("vis.editor.config");
        assertNotNull("Provide -Dvis.editor.config with a local application YAML", configPath);
        Map config;
        try (InputStream in = Files.newInputStream(Paths.get(configPath))) {
            config = (Map) new Yaml().load(in);
        }
        Map master = (Map) ((Map) ((Map) ((Map) ((Map) config.get("spring")).get("datasource"))
                .get("dynamic")).get("datasource")).get("master");
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setUrl(master.get("url").toString());
        source.setUser(master.get("username").toString());
        source.setPassword(master.get("password").toString());
        source.setOptions("-c default_transaction_read_only=on -c statement_timeout=15000");
        connection = source.getConnection();
        connection.setAutoCommit(false);
        connection.setReadOnly(true);
        Configuration mybatis = new Configuration(new Environment("readonly", new JdbcTransactionFactory(), source));
        String mapper = "org/jeecg/modules/dimnsnSetting/mapper/xml/GuokuMapper.xml";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(mapper)) {
            assertNotNull(in);
            new XMLMapperBuilder(in, mybatis, mapper, mybatis.getSqlFragments()).parse();
        }
        session = new SqlSessionFactoryBuilder().build(mybatis).openSession(connection);
    }

    @After
    public void close() throws Exception {
        if (connection != null) connection.rollback();
        if (session != null) session.close();
    }

    @Test
    public void treasuryRowsRetainTheParentKeyRequiredByTheEditor() {
        assertTree(session.getMapper(GuokuMapper.class).getGuokuTree(new PageData()));
    }

    @Test
    public void areaRowsRetainTheParentKeyRequiredByTheEditor() {
        assertTree(session.getMapper(GuokuMapper.class).getAreaTree(new PageData()));
    }

    @Test
    public void legacyBlankPageQueryPathsFallBackToTheGalleryEndpoint() throws Exception {
        Configuration configuration = session.getConfiguration();
        String mapper = "org/jeecg/modules/visualScreen/mapper/xml/PageSubMapper.xml";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(mapper)) {
            assertNotNull(in);
            new XMLMapperBuilder(in, configuration, mapper, configuration.getSqlFragments()).parse();
        }
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("params", new PageData());
        String sql = configuration.getMappedStatement("org.jeecg.modules.visualScreen.mapper.PageSubMapper.getPageSub")
                .getBoundSql(arguments).getSql();
        Matcher projection = Pattern.compile("(?is)(COALESCE\\(.*?\\))\\s+AS\\s+query_path").matcher(sql);
        assertTrue("Page preview must resolve its query endpoint", projection.find());
        // Evaluate the real mapper expression over literals: no business table is read or written.
        String check = "SELECT " + projection.group(1)
                + " FROM (VALUES (0, NULL::text), (1, ''), (2, 'null'), (3, '/saved/path')) AS a(seq, query_path)"
                + " CROSS JOIN (VALUES ('/gallery/path')) AS g(query_path) ORDER BY a.seq";
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(check)) {
            for (String expected : new String[]{"/gallery/path", "/gallery/path", "/gallery/path", "/saved/path"}) {
                assertTrue(result.next());
                assertEquals(expected, result.getString(1));
            }
            assertFalse(result.next());
        }
    }

    private void assertTree(List<Map<String, Object>> rows) {
        assertFalse("Acceptance database must contain dimension rows", rows.isEmpty());
        assertTrue("JDBC must return the case-sensitive pId key consumed by TreeFilterHeaper",
                rows.get(0).containsKey("pId"));
        List<TreeNodeNews> tree = TreeFilterHeaper.definedTreeFilter(rows);
        assertTrue("Dimension hierarchy must not collapse into a flat list", tree.size() < rows.size());
        assertTrue(tree.stream().anyMatch(node -> !node.getChildren().isEmpty()));
    }
}
