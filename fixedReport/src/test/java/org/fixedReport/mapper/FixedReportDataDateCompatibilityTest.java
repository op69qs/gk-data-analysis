package org.fixedReport.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class FixedReportDataDateCompatibilityTest {

    @Test
    public void nationwideComparisonTrimsStoredDataDateAtAllCallSites() throws Exception {
        String monthly = read("mybatis/fixedReport/NewsFlashMapper.xml");
        String quarterly = read("mybatis/fixedReport/NewsFlashQuarterMapper.xml");

        assertEquals(2, occurrences(monthly, "RTRIM(src.DATA_DATE) ="));
        assertEquals(4, occurrences(quarterly, "RTRIM(a.DATA_DATE) ="));
        assertFalse(monthly.contains("AND src.DATA_DATE =REPLACE"));
        assertFalse(quarterly.contains("AND a.DATA_DATE =REPLACE"));
    }

    private static String read(String resource) throws Exception {
        try (InputStream input = FixedReportDataDateCompatibilityTest.class
                .getClassLoader().getResourceAsStream(resource)) {
            assertNotNull("Missing classpath resource: " + resource, input);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static int occurrences(String value, String token) {
        int count = 0;
        for (int index = 0; (index = value.indexOf(token, index)) >= 0; index += token.length()) {
            count++;
        }
        return count;
    }
}
