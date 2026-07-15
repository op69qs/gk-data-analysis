package org.jeecg.modules.visualScreen.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueryMapDataMapperContractTest {
    private String mapperXml;

    @Before
    public void loadMapper() throws Exception {
        String resource = "org/jeecg/modules/visualScreen/mapper/xml/QueryMapDataMapper.xml";
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        assertTrue("Mapper resource must exist", input != null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        mapperXml = new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private String select(String id) {
        Matcher matcher = Pattern.compile("<select\\s+id=\\\"" + Pattern.quote(id)
                + "\\\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE).matcher(mapperXml);
        assertTrue("Missing select " + id, matcher.find());
        return matcher.group();
    }

    @Test
    public void budgetRevenueMapPreservesSourceFieldCasing() {
        for (String query : new String[]{"getBudgetRevenue", "getBudgetRevenueRate"}) {
            String sql = select(query);
            assertTrue(query + " must expose lowercase name", sql.contains("AS \"name\""));
            assertTrue(query + " must expose lowercase value", sql.contains("AS \"value\""));
        }
        for (String query : new String[]{"getBudgetRevenueTitle", "getBudgetRevenueRateTitle"}) {
            String sql = select(query);
            assertTrue(query + " must expose controller key AREA_DSCR", sql.contains("AS \"AREA_DSCR\""));
            assertTrue(query + " must expose controller key INDEX_VALUE", sql.contains("AS \"INDEX_VALUE\""));
            assertFalse(query + " must not combine DISTINCT with ORDER BY AREA_CODE",
                    Pattern.compile("SELECT\\s+DISTINCT", Pattern.CASE_INSENSITIVE).matcher(sql).find());
        }
        String extrema = select("getBudgetRevenueMax");
        assertTrue(extrema.contains("AS \"max_value\""));
        assertTrue(extrema.contains("AS \"min_value\""));
    }
}
