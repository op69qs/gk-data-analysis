package org.jeecg.modules.reporting.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportRuntimeLockMapperXmlTest {
    @Test
    public void leaseSqlSupportsExpiryOwnershipFencingAndRowLock() throws Exception {
        String xml = read("org/jeecg/modules/reporting/mapper/xml/ReportRuntimeLockMapper.xml").toLowerCase();
        assertTrue(xml.contains("lease_until &lt; #{now}"));
        assertTrue(xml.contains("lease_owner = #{owner}"));
        assertTrue(xml.contains("for update"));
        assertFalse(xml.contains("${"));
    }

    private String read(String path) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull(path, input);
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = stream.read(buffer)) >= 0) output.write(buffer, 0, length);
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
