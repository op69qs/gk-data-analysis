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

public class QueryDataMapperContractTest {
    private String mapperXml;

    @Before
    public void loadMapper() throws Exception {
        String resource = "org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml";
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
        Pattern pattern = Pattern.compile("<select\\s+id=\\\"" + Pattern.quote(id)
                + "\\\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mapperXml);
        assertTrue("Missing select " + id, matcher.find());
        return matcher.group();
    }

    private void assertUppercaseSeriesFields(String id) {
        String sql = select(id);
        assertTrue(id + " must expose DACCT", sql.contains("AS \"DACCT\""));
        assertTrue(id + " must expose INDEX_VALUE", sql.contains("AS \"INDEX_VALUE\""));
    }

    @Test
    public void controllerUppercaseKeysHaveQuotedAliases() {
        String[] queries = {
                "getTransferIncome", "getTransferIncomeRate",
                "getLocalFinancial", "getLocalFinancialRate",
                "getLandTransfer", "getLandTransferRate",
                "getPublicBudget", "getPublicBudgetRate",
                "getThreeBudget", "getThreeBudgetRate",
                "getTaxRevenue", "getTaxRevenueRate",
                "getIncomePayoutGap", "getIndustryTax",
                "getCustomsRevenue", "getCustomsRevenueRate",
                "getCustomsNonTax", "getCustomsNonTaxRate",
                "getCustomsImportDuties", "getCustomsImportDutiesRate",
                "getCustomsImportVat", "getCustomsImportVatRate",
                "getImportDutyArticles", "getImportDutyArticlesRate",
                "getIndustryMain", "getTreasuryIndex",
                "getInventoryBalance", "getInventoryForm",
                "getCustomsIncomeSituationTb", "getCustomsIncomeSituation"
        };
        for (String query : queries) {
            assertUppercaseSeriesFields(query);
        }
        String structure = select("getStructure");
        assertTrue(structure.contains("AS \"INDEX_NAME\""));
        assertTrue(structure.contains("AS \"INDEX_VALUE\""));
    }

    @Test
    public void sourceLowercaseKeysRemainLowercase() {
        String[] queries = {
                "getMunicipalitiesDirectly", "getIndustryType", "getIndustryName",
                "getIndustryTop10", "getSubjectPay", "getSubjectPaySub",
                "getPurposePay", "getAccountToGuoku", "getGuokuToAccount",
                "getGuokuToOrg", "getOrgToGuoku"
        };
        for (String query : queries) {
            String sql = select(query);
            assertTrue(query + " must expose lowercase name", sql.contains("AS \"name\""));
            assertTrue(query + " must expose lowercase value", sql.contains("AS \"value\""));
            assertFalse(query + " must not change REST item keys", sql.contains("AS \"NAME\""));
        }
    }

    @Test
    public void annualDistinctQueriesOrderByTheirSelectedYear() {
        for (String query : new String[]{"getTransferIncomeRate", "getLocalFinancialRate",
                "getLandTransferRate", "getThreeBudgetRate", "getTaxRevenueRate"}) {
            String sql = select(query);
            assertTrue(query + " must order annual rows by selected year",
                    sql.contains("ORDER BY LEFT(a.DACCT, 4)"));
            assertTrue(query + " must retain non-annual full-date ordering",
                    sql.contains("ORDER BY a.DACCT"));
        }
    }

    @Test
    public void groupedIndustryNameOrdersByItsGroupedColumn() {
        String sql = select("getIndustryName");
        assertTrue(sql.contains("GROUP BY a.industrial_name"));
        assertTrue(sql.contains("ORDER BY a.industrial_name"));
        assertFalse(sql.contains("ORDER BY a.industrial_type"));
    }
}
