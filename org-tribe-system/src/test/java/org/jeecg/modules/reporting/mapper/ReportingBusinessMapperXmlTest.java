package org.jeecg.modules.reporting.mapper;

import org.junit.Test;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportingBusinessMapperXmlTest {
    @Test
    public void monitoringSqlUsesActiveJarSchemasAndDoesNotReferenceInactiveEdwChanges() throws IOException {
        String xml = read("org/jeecg/modules/reporting/mapper/xml/ReportingBusinessMapper.xml").toLowerCase();
        for (String object : Arrays.asList(
                "agent_key_file.agent_treatury_config", "agent_key_file.agent_keyfile_pending",
                "agent_key_file.tims_file_pending", "edw.cm_guoku_dimnsn")) {
            assertTrue("缺少 JAR 对象：" + object, xml.contains(object));
        }
        assertTrue(xml.contains("count(distinct tp.biz_type)"));
        assertFalse(xml.contains("edw.income_report_detail_stat"));
        assertFalse(xml.contains("edw.payout_report_detail_stat"));
        assertFalse(xml.contains("edw.reprot_update_record"));
        assertFalse(xml.contains("insertchange"));
        assertFalse(xml.contains("${"));
        assertFalse(xml.contains("ifnull"));
        assertFalse(xml.contains("group_concat"));
        assertFalse(xml.contains("find_in_set"));
        assertFalse(xml.contains("`"));
    }

    @Test
    public void mapperCanBeParsedByTheProjectMybatisRuntime() throws IOException {
        parseAndAssert("org/jeecg/modules/reporting/mapper/xml/ReportingBusinessMapper.xml",
                "org.jeecg.modules.reporting.mapper.ReportingBusinessMapper.queryTimsMonitoring");
        parseAndAssert("org/jeecg/modules/reporting/mapper/xml/AgentTreasuryConfigMapper.xml",
                "org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper.findScopePrefix");
    }

    private void parseAndAssert(String path, String statement) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull("Missing resource " + path, input);
        try (InputStream stream = input) {
            Configuration configuration = new Configuration();
            new XMLMapperBuilder(stream, configuration, path, configuration.getSqlFragments()).parse();
            assertTrue(configuration.hasStatement(statement));
        }
    }

    private String read(String path) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull("Missing resource " + path, input);
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = stream.read(buffer)) >= 0) output.write(buffer, 0, length);
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
