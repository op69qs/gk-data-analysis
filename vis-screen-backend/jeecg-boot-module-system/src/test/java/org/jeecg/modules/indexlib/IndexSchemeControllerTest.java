package org.jeecg.modules.indexlib;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.indexlib.controller.IndexSchemeController;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
import org.jeecg.modules.util.PageData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndexSchemeControllerTest {
    @Mock
    private IndexSchemeService indexSchemeService;
    @Mock
    private IndexRelationService indexRelationService;

    private IndexSchemeController controller;

    @Before
    public void setUp() throws Exception {
        controller = new IndexSchemeController();
        setField("indexSchemeService", indexSchemeService);
        setField("indexRelationService", indexRelationService);
    }

    @Test
    public void listMapsProductionQueryFields() {
        JSONObject request = new JSONObject();
        request.put("pageNo", "2");
        request.put("pageSize", "10");
        request.put("name", "收入");
        request.put("begin_time", "2026-01-01");
        request.put("end_time", "2026-01-31");
        when(indexSchemeService.selectSchemeTable(any(PageData.class)))
                .thenReturn(Collections.emptyList());
        when(indexSchemeService.getSchemeCount(any(PageData.class))).thenReturn(0);

        Map<String, Object> response = controller.selectSchemeTable(request);

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(indexSchemeService).selectSchemeTable(captor.capture());
        assertEquals("收入", captor.getValue().getString("schemeDescr"));
        assertEquals(10, captor.getValue().get("page"));
        assertEquals(10, captor.getValue().get("pageSize"));
        assertEquals("2026-01-01", captor.getValue().getString("begin_time"));
        assertEquals("2026-01-31", captor.getValue().getString("end_time"));
        assertEquals("success", response.get("result"));
        assertEquals(0, response.get("total"));
        assertEquals(Collections.emptyList(), response.get("rows"));
    }

    @Test
    public void deletePassesProductionSchemeId() {
        JSONObject request = new JSONObject();
        request.put("schemeId", "scheme-1");

        Map<String, Object> response = controller.deleteScheme(request);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(indexSchemeService).deleteSchemeById(captor.capture());
        assertEquals("scheme-1", captor.getValue().get("schemeId"));
        assertEquals("success", response.get("result"));
        assertEquals("删除指标方案成功", response.get("msg"));
    }

    @Test
    public void getIndexInfoUsesProductionColumnsField() {
        JSONObject request = new JSONObject();
        request.put("SCHEME_COLUMS", "I1,I2");
        when(indexRelationService.getBatchIndexInfo(any(PageData.class)))
                .thenReturn(Collections.emptyList());

        Map<String, Object> response = controller.getIndexInfo(request);

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(indexRelationService).getBatchIndexInfo(captor.capture());
        assertEquals("I1,I2", captor.getValue().getString("SCHEME_COLUMS"));
        assertEquals("success", response.get("result"));
        assertEquals(Collections.emptyList(), response.get("indexInfoList"));
    }

    @Test
    public void serviceFailuresAreReportedAsFailed() {
        JSONObject listRequest = new JSONObject();
        listRequest.put("pageNo", "1");
        listRequest.put("pageSize", "10");
        doThrow(new IllegalStateException("db unavailable"))
                .when(indexSchemeService).getSchemeCount(any(PageData.class));

        Map<String, Object> response = controller.selectSchemeTable(listRequest);

        assertEquals("failed", response.get("result"));
        assertEquals("查询指标方案失败", response.get("msg"));
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = IndexSchemeController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
}
