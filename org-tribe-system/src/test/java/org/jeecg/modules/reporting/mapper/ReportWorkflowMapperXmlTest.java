package org.jeecg.modules.reporting.mapper;

import org.junit.Test;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportWorkflowMapperXmlTest {
    @Test
    public void processCallKeepsOriginalJarObjectsAndUsesBoundValues() throws IOException {
        String xml = read("org/jeecg/modules/reporting/mapper/xml/ReportWorkflowMapper.xml").toLowerCase();
        assertTrue(xml.contains("etl.guoku_lib_report_all_log"));
        assertTrue(xml.contains("adm.p_guoku_lib_report_all"));
        assertTrue(xml.contains("agent_key_file.report_process_call"));
        assertTrue(xml.contains("state = '1'"));
        assertTrue(xml.contains("lease_owner"));
        assertTrue(xml.contains("lease_until"));
        assertTrue(xml.contains("lease_owner = #{leaseowner}"));
        assertTrue(xml.contains("id=\"completeownedtask\""));
        assertFalse(xml.contains("heartbeatownedtasks"));
        assertFalse(xml.contains("${"));
        assertFalse(xml.contains("`"));
    }

    @Test
    public void workflowMapperParsesInProjectMybatisRuntime() throws IOException {
        String path = "org/jeecg/modules/reporting/mapper/xml/ReportWorkflowMapper.xml";
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull(input);
        try (InputStream stream = input) {
            Configuration configuration = new Configuration();
            new XMLMapperBuilder(stream, configuration, path, configuration.getSqlFragments()).parse();
            assertTrue(configuration.hasStatement(
                    "org.jeecg.modules.reporting.mapper.ReportWorkflowMapper.claimTask"));
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
