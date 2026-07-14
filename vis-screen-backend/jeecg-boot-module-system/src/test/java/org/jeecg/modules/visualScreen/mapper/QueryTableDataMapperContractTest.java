package org.jeecg.modules.visualScreen.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import static org.junit.Assert.assertTrue;

public class QueryTableDataMapperContractTest {
    private String mapperXml;

    @Before
    public void loadMapper() throws Exception {
        String resource = "org/jeecg/modules/visualScreen/mapper/xml/QueryTableDataMapper.xml";
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
    public void regionPayAcceptsLegacyVastbasePeriodEncoding() {
        String compatiblePeriod = "CONCAT('b''', #{params.PERIOD_FLAG}, '''')";
        for (String query : new String[]{"getRegionPay", "getRegionPayRate"}) {
            String sql = select(query);
            assertTrue(query + " must accept the migrated b-quoted period value",
                    sql.contains(compatiblePeriod));
        }
    }

    @Test
    public void mapperXmlIsWellFormed() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.newDocumentBuilder().parse(new InputSource(new StringReader(mapperXml)));
    }

    @Test
    public void tableSeriesExposeTheFrontendFieldNames() {
        String[] queries = {
                "getGrowthBudget", "getGrowthBudgetRate", "getGrowthTax", "getGrowthTaxRate",
                "getAmountRegion", "getRankingGrowth", "getTaxRegional", "getTaxRegion",
                "getFiveProvinces", "getMunicipalities", "getRegionPay", "getRegionPayRate",
                "getInventoryBalance", "getInventoryBalanceRate"
        };
        for (String query : queries) {
            String sql = select(query);
            assertTrue(query + " must expose AREA_DSCR", sql.contains("AS \"AREA_DSCR\""));
            assertTrue(query + " must expose at least one uppercase value field",
                    sql.contains("AS \"INDEX_VALUE\"") || sql.contains("AS \"GROWTH_INDEX_VALUE\""));
        }
        assertTrue(select("getAmountRegionCQ").contains("AS \"rownum\""));
    }
}
