package org.jeecg.modules.indexlib;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.indexlib.controller.IndexBarLineController;
import org.jeecg.modules.indexlib.controller.IndexPieController;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
import org.jeecg.modules.util.IndexChartsHelper;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.service.GalleryService;
import org.jeecg.modules.visualScreen.service.PageWhereService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexChartControllerTest {
    @Mock
    private IndexRelationService indexRelationService;
    @Mock
    private IndexSchemeService indexSchemeService;
    @Mock
    private GalleryService galleryService;
    @Mock
    private PageWhereService pageWhereService;

    private IndexBarLineController barLineController;
    private IndexPieController pieController;

    @Before
    public void setUp() throws Exception {
        barLineController = new IndexBarLineController();
        pieController = new IndexPieController();
        inject(barLineController);
        inject(pieController);
    }

    @Test
    public void saveParsesConditionAndUsesProductionGalleryFields() {
        JSONObject condition = baseRequest("bar");
        condition.put("scheme_id", "scheme-1");
        JSONObject request = new JSONObject();
        request.put("condition", condition.toJSONString());
        request.put("content", "{\"series\":[]}");
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_DESCR", "收入方案"));

        Map<String, Object> response = barLineController.saveIndexBarLine(request);

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(galleryService).add(captor.capture());
        PageData saved = captor.getValue();
        assertEquals("success", response.get("result"));
        assertEquals("添加成功", response.get("msg"));
        assertEquals("收入方案", saved.getString("scheme_name"));
        assertEquals("0", saved.getString("state"));
        assertEquals("1010", saved.getString("business_id"));
        assertEquals("{\"series\":[]}", saved.getString("content"));
        assertEquals("scheme-1", saved.getString("scheme_id"));
        assertNotNull(saved.getString("id"));
        assertNotNull(saved.getString("add_time"));
        assertEquals(condition, JSONObject.parseObject(saved.getString("condition")));
    }

    @Test
    public void barLinePreviewCoversBarLineAndCombinedTypesWithProductionSql() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_COLUMS", "I1"));
        when(indexRelationService.getIndexDetails(any(PageData.class)))
                .thenReturn(indexInfo("收入", "0", "T_INDEX"));
        when(indexSchemeService.getAllTrsInfo()).thenReturn("A,B");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>singletonList(row("CODE", "A", "I1", "12.345")));

        for (String type : Arrays.asList("bar", "line", "barAndLine")) {
            Map<String, Object> response =
                    barLineController.getIndexBarLineData(baseRequest(type));
            assertEquals("success", response.get("result"));
            assertEquals(type, response.get("type"));
            assertEquals(Arrays.asList("A", "B"), response.get("x"));
            assertEquals(Collections.singletonList(Arrays.asList("12.35", "")),
                    response.get("data"));
            assertEquals(1, ((List<?>) response.get("indexInfoList")).size());
        }

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(indexSchemeService, times(3)).execSchemeSql(captor.capture());
        for (PageData query : captor.getAllValues()) {
            String sql = query.getString("schemeSql");
            assertTrue(sql.contains("T_INDEX"));
            assertTrue(sql.contains("I1"));
            assertTrue(sql.contains("2026-01-01"));
            assertTrue(sql.contains("2026-01-31"));
        }
    }

    @Test
    public void barLineDateAxisUsesDailyScaleAndProductionSql() {
        stubSchemeAndIndex("I1", "0");
        JSONObject request = baseRequest("bar");
        request.put("xTurn", "0");
        request.put("direction", "D1");
        request.put("startDate", "2026-01-01");
        request.put("endDate", "2026-01-02");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>singletonList(
                        row("ACCOUNT_DATE", "2026-01-01", "I1", "4")));

        Map<String, Object> response = barLineController.getIndexBarLineData(request);

        assertEquals(Arrays.asList("2026-01-01", "2026-01-02"), response.get("x"));
        assertEquals(Collections.singletonList(Arrays.asList("4.00", "")), response.get("data"));
        String sql = capturedSql();
        assertTrue(sql.contains("ROUND(tt.`I1`/1, 2) AS `I1`"));
        assertTrue(sql.contains("AND bb.CODE = 'D1'"));
        assertTrue(sql.contains("GROUP BY bb.ACCOUNT_DATE"));
        assertTrue(sql.contains("ORDER BY tt.`ACCOUNT_DATE` ASC"));
    }

    @Test
    public void barLineAreaDimensionUsesOnlyAreaScale() {
        stubSchemeAndIndex("I1", "0");
        when(indexSchemeService.getAllAreaInfo()).thenReturn("东部,西部");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());
        JSONObject request = baseRequest("bar");
        request.put("dimensionFlag", "2");

        Map<String, Object> response = barLineController.getIndexBarLineData(request);

        assertEquals(Arrays.asList("东部", "西部"), response.get("x"));
        verify(indexSchemeService).getAllAreaInfo();
        verify(indexSchemeService, never()).getAllTrsInfo();
    }

    @Test
    public void barLineInvalidDimensionReturnsNullWithoutScaleServiceCalls() {
        stubSchemeAndIndex("I1", "0");
        JSONObject request = baseRequest("bar");
        request.put("dimensionFlag", "unexpected");

        assertNull(barLineController.getIndexBarLineData(request));
        verify(indexSchemeService, never()).getAllTrsInfo();
        verify(indexSchemeService, never()).getAllAreaInfo();
        verify(indexSchemeService, never()).execSchemeSql(any(PageData.class));
    }

    @Test
    public void literalNullIndexTypeUsesProductionMultiplyBranch() {
        stubSchemeAndIndex("I1", "null");
        when(indexSchemeService.getAllTrsInfo()).thenReturn("A");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());

        barLineController.getIndexBarLineData(baseRequest("bar"));

        String sql = capturedSql();
        assertTrue(sql.contains("ROUND(tt.`I1` * 100, 2) AS `I1`"));
    }

    @Test
    public void barLineSchemeColumnsSelectOnlyRequestedChartIds() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_COLUMS", "I1,I2"));
        when(indexRelationService.getIndexDetails(any(PageData.class)))
                .thenReturn(indexInfo("利润率", "1", "T_RATE"));
        when(indexSchemeService.getAllTrsInfo()).thenReturn("A");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());
        JSONObject request = baseRequest("bar");
        request.put("schemecolumns", Collections.singletonList(
                Collections.singletonMap("chartId", "I2")));

        Map<String, Object> response = barLineController.getIndexBarLineData(request);

        assertEquals(1, ((List<?>) response.get("indexInfoList")).size());
        String sql = capturedSql();
        assertTrue(sql.contains("ROUND(tt.`I2` * 100, 2) AS `I2`"));
        assertTrue(sql.contains("find_in_set(INDEX_ID, 'I2')"));
        assertTrue(!sql.contains("tt.`I1`"));
    }

    @Test
    public void quarterScaleMatchesProductionBranchBoundaries() {
        assertQuarterScale("2025Q1", "2025Q3",
                "2025年第一季度", "2025年第二季度", "2025年第三季度");
        assertQuarterScale("2025Q3", "2025Q1",
                "2025年第三季度", "2025年第四季度", "2026年第一季度");
        assertQuarterScale("2025Q3", "2026Q1",
                "2025年第三季度", "2025年第四季度", "2026年第一季度",
                "2026年第二季度", "2026年第三季度", "2026年第四季度");
        assertQuarterScale("2025Q3", "2026Q2",
                "2025年第三季度", "2025年第四季度",
                "2026年第一季度", "2026年第二季度");
        assertQuarterScale("2025Q3", "2026Q4",
                "2025年第三季度", "2025年第四季度", "2026年第一季度",
                "2026年第二季度", "2026年第三季度", "2026年第四季度");
    }

    @Test
    public void galleryAndPageWhereOverrideBarLineQuarterCondition() {
        JSONObject condition = baseRequest("bar");
        condition.put("xTurn", "0");
        condition.put("periodFlag", "3");
        condition.put("direction", "D1");
        condition.put("unit", "10");
        condition.put("time_type", "q");
        when(galleryService.getAll(any(PageData.class))).thenReturn(Collections.singletonList(
                row("condition", condition.toJSONString(), "unused", "")));
        when(pageWhereService.getAll(any(PageData.class))).thenReturn(Collections.singletonList(
                row("where_value", "2025-Q3,2026-Q2", "unused", "")));
        stubSchemeAndIndex("I1", "0");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());
        JSONObject request = new JSONObject();
        request.put("gallery_id", "G1");
        request.put("sub_id", "S1");

        Map<String, Object> response = barLineController.getIndexBarLineData(request);

        assertEquals(Arrays.asList("2025年第三季度", "2025年第四季度",
                "2026年第一季度", "2026年第二季度"), response.get("x"));
        assertEquals(Collections.singletonList(Arrays.asList("", "", "", "")),
                response.get("data"));
        String sql = capturedSql();
        assertTrue(sql.contains("ROUND(tt.`I1`/10, 2) AS `I1`"));
        assertTrue(sql.contains("bb.ACCOUNT_DATE >= '2025Q3'"));
        assertTrue(sql.contains("bb.ACCOUNT_DATE <= '2026Q2'"));
        verify(galleryService).getAll(any(PageData.class));
        verify(pageWhereService).getAll(any(PageData.class));
    }

    @Test
    public void piePreviewReturnsProductionLegendDataAndSql() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_COLUMS", "I1"));
        when(indexRelationService.getIndexDetails(any(PageData.class)))
                .thenReturn(indexInfo("收入", "0", "T_INDEX"));
        Map<String, Object> resultRow = row("name", "地区A", "value", "8.50");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.singletonList(resultRow));
        JSONObject request = baseRequest("pie");
        request.put("direction", "Y");
        request.put("indexName", "I1");

        Map<String, Object> response = pieController.getIndexPieData(request);

        assertEquals("success", response.get("result"));
        assertEquals("pie", response.get("type"));
        assertEquals(Collections.singletonList(resultRow), response.get("data"));
        assertTrue(Arrays.equals(new String[]{"地区A"}, (String[]) response.get("legend")));
        assertEquals(1, ((List<?>) response.get("indexInfoList")).size());
        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(indexSchemeService).execSchemeSql(captor.capture());
        assertTrue(captor.getValue().getString("schemeSql").contains("aa.COLID ='I1'"));
        assertTrue(captor.getValue().getString("schemeSql").contains("T_INDEX"));
    }

    @Test
    public void pieXDirectionAndRateUseProductionSqlAndDateOverride() {
        stubSchemeAndIndex("I1", "0");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());
        JSONObject request = baseRequest("pie");
        request.put("direction", "X");
        request.put("GK", "ORG-1");
        request.put("isRate", "true");
        request.put("endDate", "2026-02-28");

        Map<String, Object> response = pieController.getIndexPieData(request);

        assertEquals("success", response.get("result"));
        assertEquals(0, ((String[]) response.get("legend")).length);
        assertEquals(Collections.emptyList(), response.get("data"));
        String sql = capturedSql();
        assertTrue(sql.contains("aa.CODE ='ORG-1'"));
        assertTrue(sql.contains("GROUP BY aa.COLID"));
        assertTrue(sql.contains("ACCOUNT_PERIOD >='2026-02-28'"));
        assertTrue(sql.contains("ACCOUNT_PERIOD <='2026-02-28'"));
    }

    @Test
    public void galleryAndPageWhereOverridePieCondition() {
        JSONObject condition = baseRequest("pie");
        condition.put("direction", "Y");
        condition.put("indexName", "I1");
        condition.put("unit", "10");
        condition.put("time_type", "q");
        when(galleryService.getAll(any(PageData.class))).thenReturn(Collections.singletonList(
                row("condition", condition.toJSONString(), "unused", "")));
        when(pageWhereService.getAll(any(PageData.class))).thenReturn(Collections.singletonList(
                row("where_value", "2025-Q3,2026-Q2", "unused", "")));
        stubSchemeAndIndex("I1", "0");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenReturn(Collections.<Map<String, Object>>emptyList());
        JSONObject request = new JSONObject();
        request.put("gallery_id", "G1");
        request.put("sub_id", "S1");

        pieController.getIndexPieData(request);

        String sql = capturedSql();
        assertTrue(sql.contains("ROUND(V.value/10"));
        assertTrue(sql.contains("ACCOUNT_PERIOD >='2025Q3'"));
        assertTrue(sql.contains("ACCOUNT_PERIOD <='2026Q2'"));
    }

    @Test
    public void pieEmptySchemeAndExecutionExceptionMatchProduction() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.<String, String>emptyMap());
        JSONObject request = baseRequest("pie");
        request.put("direction", "Y");
        assertNull(pieController.getIndexPieData(request));

        stubSchemeAndIndex("I1", "0");
        when(indexSchemeService.execSchemeSql(any(PageData.class)))
                .thenThrow(new IllegalStateException("query failed"));
        request.put("indexName", "I1");
        Map<String, Object> failed = pieController.getIndexPieData(request);
        assertEquals("failed", failed.get("result"));
        assertEquals("操作失败", failed.get("msg"));
    }

    @Test
    public void emptySchemeColumnsReturnsNullLikeProduction() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.<String, String>emptyMap());

        assertNull(barLineController.getIndexBarLineData(baseRequest("bar")));
    }

    @Test
    public void serviceExceptionsReturnProductionFailureResponse() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenThrow(new IllegalStateException("db unavailable"));

        Map<String, Object> response =
                barLineController.getIndexBarLineData(baseRequest("bar"));

        assertEquals("failed", response.get("result"));
        assertEquals("操作失败", response.get("msg"));
    }

    @Test
    public void invalidPieDirectionIsRejectedBeforeSqlExecution() {
        JSONObject request = baseRequest("pie");
        request.put("direction", "map");

        Map<String, Object> response = pieController.getIndexPieData(request);

        assertEquals("failed", response.get("result"));
        assertEquals("统计方向未选择，查询失败", response.get("msg"));
    }

    @Test
    public void saveFailureUsesProductionMessage() {
        JSONObject request = new JSONObject();
        request.put("condition", baseRequest("pie").toJSONString());
        doThrow(new IllegalStateException("write failed"))
                .when(galleryService).add(any(PageData.class));
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_DESCR", "收入方案"));

        Map<String, Object> response = pieController.saveIndexPie(request);

        assertEquals("failed", response.get("result"));
        assertEquals("添加失败", response.get("msg"));
    }

    @Test
    public void pieSaveSuccessAndBarSaveFailureUseProductionResponses() {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_DESCR", "方案"));
        JSONObject pieRequest = saveRequest("pie");
        assertEquals("success", pieController.saveIndexPie(pieRequest).get("result"));

        doThrow(new IllegalStateException("write failed"))
                .when(galleryService).add(any(PageData.class));
        Map<String, Object> failed = barLineController.saveIndexBarLine(saveRequest("bar"));
        assertEquals("failed", failed.get("result"));
        assertEquals("添加失败", failed.get("msg"));
    }

    private JSONObject baseRequest(String type) {
        JSONObject request = new JSONObject();
        request.put("scheme_id", "scheme-1");
        request.put("type", type);
        request.put("periodFlag", "1");
        request.put("timeType", "2");
        request.put("startDate", "2026-01-01");
        request.put("endDate", "2026-01-31");
        request.put("price", "1");
        request.put("dimensionFlag", "1");
        request.put("xTurn", "1");
        return request;
    }

    private Map<String, Object> indexInfo(
            String name, String type, String table) {
        Map<String, Object> info = new HashMap<>();
        info.put("INDEX_NAME", name);
        info.put("INDEX_TYPE", type);
        info.put("INDEX_CORRE_TABLE", table);
        return info;
    }

    private void stubSchemeAndIndex(String column, String type) {
        when(indexSchemeService.getSchemeInfoById(any(PageData.class)))
                .thenReturn(Collections.singletonMap("SCHEME_COLUMS", column));
        when(indexRelationService.getIndexDetails(any(PageData.class)))
                .thenReturn(indexInfo("指标", type, "T_INDEX"));
    }

    private String capturedSql() {
        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(indexSchemeService).execSchemeSql(captor.capture());
        return captor.getValue().getString("schemeSql");
    }

    private void assertQuarterScale(String start, String end, String... expected) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("periodFlag", "3");
        condition.put("startDate", start);
        condition.put("endDate", end);
        assertEquals(Arrays.asList(expected), IndexChartsHelper.setEChartsScale(condition));
    }

    private JSONObject saveRequest(String type) {
        JSONObject request = new JSONObject();
        request.put("condition", baseRequest(type).toJSONString());
        request.put("content", "{}");
        return request;
    }

    private Map<String, Object> row(
            String key1, Object value1, String key2, Object value2) {
        Map<String, Object> row = new HashMap<>();
        row.put(key1, value1);
        row.put(key2, value2);
        return row;
    }

    private void inject(Object controller) throws Exception {
        setField(controller, "indexRelationService", indexRelationService);
        setField(controller, "indexSchemeService", indexSchemeService);
        setField(controller, "galleryService", galleryService);
        setField(controller, "pageWhereService", pageWhereService);
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
