package org.jeecg.modules.reporting.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KeyReportMapperXmlTest {

    @Test
    public void mapperUsesFixedJarTablesAndVastbaseDateConversion() throws IOException {
        String xml = readResource("org/jeecg/modules/reporting/mapper/xml/KeyReportMapper.xml").toLowerCase();

        for (String table : Arrays.asList(
                "agent_key_file.agent_file_income",
                "agent_key_file.agent_file_payout",
                "agent_key_file.agent_file_stock",
                "agent_key_file.agent_file_back")) {
            assertTrue("缺少原 JAR 表：" + table, xml.contains(table));
        }
        assertTrue(xml.contains("cast(#{row.dacct} as date)"));
        assertTrue(xml.contains("acount_code"));
        assertTrue(xml.contains("bckreason"));
        assertFalse(xml.contains("date_format("));
        assertFalse(xml.contains("${"));
    }

    private String readResource(String path) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull("Missing resource " + path, input);
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = stream.read(buffer)) >= 0) {
                output.write(buffer, 0, length);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
