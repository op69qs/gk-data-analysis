package org.jeecg.modules.reporting.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LegacyPendingMapperXmlTest {
    @Test
    public void compatibilityWritesUseExactJarTablesAndColumnsWithoutSqlSubstitution() throws IOException {
        String xml = read("org/jeecg/modules/reporting/mapper/xml/LegacyPendingMapper.xml").toLowerCase();
        assertTrue(xml.contains("agent_key_file.agent_keyfile_pending"));
        assertTrue(xml.contains("agent_key_file.tims_file_pending"));
        assertTrue(xml.contains("sr_name_state"));
        assertTrue(xml.contains("file_exception"));
        assertFalse(xml.contains("${"));
        assertFalse(xml.contains("date_format"));
        assertFalse(xml.contains("ifnull"));
        assertFalse(xml.contains("`"));
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
