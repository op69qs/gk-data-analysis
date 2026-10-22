package org.seo.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComprehensiveQueryDataScopeSqlTest {

    @Test
    public void portalTreasuryJoinUsesClickHouseLowercaseDmcodeColumns() {
        String sql = ComprehensiveQueryDataScopeSql.portalTreasuryJoin(
                "PT_TK_SUMMARY.DRAW_BACK_TRE_CODE", "220000000002");

        assertTrue(sql.contains("root.bookorgcode = '220000000002'"));
        assertTrue(sql.contains("scope.PID = root.guoku_id"));
        assertTrue(sql.contains("PT_TK_SUMMARY.DRAW_BACK_TRE_CODE = scope_rows.CID"));
        assertEquals(false, sql.contains("BOOKORGCODE") || sql.contains("GUOKU_ID"));
    }

    @Test
    public void portalTreasuryInFilterAvoidsJoinOnTreasuryColumn() {
        String sql = ComprehensiveQueryDataScopeSql.portalTreasuryInFilter(
                "tv_fin_income_detail.S_ORGCODE", "220000000002");

        assertTrue(sql.contains(
                "tv_fin_income_detail.S_ORGCODE in (select distinct scope.CID"));
        assertTrue(sql.contains("root.bookorgcode = '220000000002'"));
        assertFalse(sql.contains("scope_rows"));
    }
}
