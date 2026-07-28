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

public class TimsReportMapperXmlTest {
    @Test
    public void mapperUsesOnlyWhitelistedJarTargetsAndBoundParameters() throws IOException {
        String xml = readResource("org/jeecg/modules/reporting/mapper/xml/TimsReportMapper.xml").toLowerCase();
        for (String table : Arrays.asList(
                "stg.trs_tmis_budget_income",
                "stg.trs_tmis_budget_payout", "stg.trs_tmis_stock")) {
            assertTrue("缺少 JAR 目标表：" + table, xml.contains(table));
        }
        assertFalse(xml.contains("agent_key_file.tims_file_income"));
        assertFalse(xml.contains("agent_key_file.tims_file_payout"));
        assertFalse(xml.contains("agent_key_file.tims_file_stock"));
        assertTrue(xml.contains("tax_org_code"));
        assertTrue(xml.contains("f_debitamt"));
        assertTrue(xml.contains("f_loanamt"));
        assertTrue(xml.contains("#{row.daccttext}"));
        assertTrue(xml.contains("countstgincome"));
        assertTrue(xml.contains("countstgpayout"));
        assertTrue(xml.contains("countstgstock"));
        assertFalse(xml.contains("${"));
        assertFalse(xml.contains("`"));
    }

    private String readResource(String path) throws IOException {
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
