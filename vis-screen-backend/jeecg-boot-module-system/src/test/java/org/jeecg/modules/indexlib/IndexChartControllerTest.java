package org.jeecg.modules.indexlib;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.indexlib.controller.IndexBarLineController;
import org.jeecg.modules.indexlib.controller.IndexPieController;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
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
