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
import java.util.List;
import java.util.Map;

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

    private void assertTree(List<Map<String, Object>> rows) {
        assertFalse("Acceptance database must contain dimension rows", rows.isEmpty());
        assertTrue("JDBC must return the case-sensitive pId key consumed by TreeFilterHeaper",
                rows.get(0).containsKey("pId"));
        List<TreeNodeNews> tree = TreeFilterHeaper.definedTreeFilter(rows);
        assertTrue("Dimension hierarchy must not collapse into a flat list", tree.size() < rows.size());
        assertTrue(tree.stream().anyMatch(node -> !node.getChildren().isEmpty()));
    }
}
