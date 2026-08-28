package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.seo.service.ComprehensiveQueryService;
import org.seo.util.PageData;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComprehensiveQueryControllerPortalScopeTest {

    private ComprehensiveQueryController controller;
    private ComprehensiveQueryService service;

    @Before
    public void setUp() {
        controller = new ComprehensiveQueryController();
        service = mock(ComprehensiveQueryService.class);
        ReflectionTestUtils.setField(controller, "comprehensiveQueryService", service);
        ReflectionTestUtils.setField(controller, "saveDir", "/tmp/comprehensive-query-portal-scope-test/");

        Map<String, String> type = new HashMap<>();
        type.put("DBNAME", "adm");
        type.put("TYPE", "Vastbase");
        when(service.getType(any(PageData.class))).thenReturn(type);

        Map<String, Object> treasuryColumn = new HashMap<>();
        treasuryColumn.put("FIELD_SIGN", "trs_comprehensive_payout_month_payoutmm.S_TRECODE▲T");
        when(service.getColumn(any(PageData.class)))
                .thenReturn(Collections.singletonList(treasuryColumn));
    }

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void portalSubjectScopesRowsAndCountWithTheSameTreasuryRelation() {
        portalRequest("BOOK-001");
        AtomicReference<String> rowSql = new AtomicReference<>();
        AtomicReference<String> countSql = new AtomicReference<>();
        when(service.executeSql(any(PageData.class), eq("database-1"))).thenAnswer(invocation -> {
            rowSql.set(((PageData) invocation.getArgument(0)).getString("sql"));
            return Collections.<Map<String, Object>>emptyList();
        });
        when(service.countSql(any(PageData.class), eq("database-1"))).thenAnswer(invocation -> {
            countSql.set(((PageData) invocation.getArgument(0)).getString("countSql"));
            return 1865445;
        });

        Map<String, Object> result = execute("CLIENT-GUOKU-MUST-NOT-BE-USED");

        assertEquals("success", result.get("result"));
        assertEquals(1865445, result.get("total"));
        assertPortalScope(rowSql.get());
        assertPortalScope(countSql.get());
        assertFalse(rowSql.get().contains("CLIENT-GUOKU-MUST-NOT-BE-USED"));
        assertFalse(rowSql.get().contains("PID = 'null'"));
    }

    @Test
    public void missingPortalSubjectFailsBeforeExecutingDatasourceSql() {
        portalRequest(null);

        Map<String, Object> result = execute("CLIENT-GUOKU-MUST-NOT-BE-USED");

        assertEquals("false", result.get("result"));
        assertEquals("门户所属国库编码缺失", result.get("msg"));
        verify(service, never()).executeSql(any(PageData.class), any(String.class));
        verify(service, never()).countSql(any(PageData.class), any(String.class));
    }

    @Test
    public void invalidPortalSubjectFailsBeforeExecutingDatasourceSql() {
        portalRequest("BOOK-001' OR '1'='1");

        Map<String, Object> result = execute(null);

        assertEquals("false", result.get("result"));
        assertEquals("门户所属国库编码格式非法", result.get("msg"));
        verify(service, never()).executeSql(any(PageData.class), any(String.class));
        verify(service, never()).countSql(any(PageData.class), any(String.class));
    }

    @Test
    public void downloadUsesTheAuthenticatedPortalSubjectScope() {
        portalRequest("BOOK-001");
        AtomicReference<String> rowSql = new AtomicReference<>();
        AtomicReference<String> countSql = new AtomicReference<>();
        when(service.countSql(any(PageData.class), eq("database-1"))).thenAnswer(invocation -> {
            countSql.set(((PageData) invocation.getArgument(0)).getString("countSql"));
            return 0;
        });
        when(service.executeSql(any(PageData.class), eq("database-1"))).thenAnswer(invocation -> {
            rowSql.set(((PageData) invocation.getArgument(0)).getString("sql"));
            return Collections.<Map<String, Object>>emptyList();
        });
        JSONObject params = downloadParams();

        controller.download(params, new MockHttpServletResponse());

        assertPortalScope(rowSql.get());
        assertPortalScope(countSql.get());
    }

    @Test
    public void downloadRejectsMissingPortalSubjectBeforeDatasourceSql() {
        portalRequest(null);
        assertDownloadRejected("门户所属国库编码缺失");

        verify(service, never()).executeSql(any(PageData.class), any(String.class));
        verify(service, never()).countSql(any(PageData.class), any(String.class));
    }

    @Test
    public void downloadRejectsInvalidPortalSubjectBeforeDatasourceSql() {
        portalRequest("BOOK-001' OR '1'='1");
        assertDownloadRejected("门户所属国库编码格式非法");

        verify(service, never()).executeSql(any(PageData.class), any(String.class));
        verify(service, never()).countSql(any(PageData.class), any(String.class));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> execute(String clientGuokuId) {
        return (Map<String, Object>) controller.executeSql(
                "database-1▲trs_comprehensive_payout_month_payoutmm",
                null,
                null,
                null,
                null,
                null,
                null,
                1,
                100,
                "portal-user",
                null,
                null,
                null,
                null,
                null,
                clientGuokuId);
    }

    private void portalRequest(String subjectCode) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (subjectCode != null) {
            request.addHeader("X-Analysis-Subject-Code", subjectCode);
        }
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    private JSONObject downloadParams() {
        JSONObject params = new JSONObject();
        params.put("table", "database-1▲trs_comprehensive_payout_month_payoutmm");
        params.put("COLUMN_EN", "S_TRECODE");
        params.put("COLUMN_CN", "国库编码");
        params.put("tableName", "综合查询支出月表");
        params.put("tableName_en", "trs_comprehensive_payout_month_payoutmm");
        params.put("userId", "portal-user");
        return params;
    }

    private void assertDownloadRejected(String expectedMessage) {
        try {
            controller.download(downloadParams(), new MockHttpServletResponse());
            fail("下载请求应在访问业务数据源前被拒绝");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private void assertPortalScope(String sql) {
        assertTrue(sql.contains("scope.PID = root.GUOKU_ID"));
        assertTrue(sql.contains("root.BOOKORGCODE = 'BOOK-001'"));
        assertTrue(sql.contains("trs_comprehensive_payout_month_payoutmm.S_TRECODE = scope_rows.CID"));
    }
}
