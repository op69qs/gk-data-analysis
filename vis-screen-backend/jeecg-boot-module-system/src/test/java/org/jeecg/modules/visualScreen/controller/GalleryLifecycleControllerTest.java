package org.jeecg.modules.visualScreen.controller;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.service.GalleryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.jeecg.modules.visualScreen.controller.SchemeInfoControllerTest.json;
import static org.jeecg.modules.visualScreen.controller.SchemeInfoControllerTest.row;
import static org.junit.Assert.*;

public class GalleryLifecycleControllerTest {
    private final LocalGallery store = new LocalGallery();
    private GalleryController controller;
    private SchemeInfoControllerTest.LocalTransactions transactions;

    @Before
    public void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("{}".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        GalleryController target = new GalleryController();
        ReflectionTestUtils.setField(target, "galleryService", store);
        transactions = new SchemeInfoControllerTest.LocalTransactions(store::snapshot, store::restore);
        ProxyFactory proxy = new ProxyFactory(target);
        proxy.addAdvice(new TransactionInterceptor(transactions, new AnnotationTransactionAttributeSource()));
        controller = (GalleryController) proxy.getProxy();
    }

    @After
    public void clearRequest() { RequestContextHolder.resetRequestAttributes(); }

    @Test
    public void failedGalleryQueryCannotReportSuccess() {
        store.fail = true;
        assertEquals("failed", controller.getAll().get("result"));
    }

    @Test
    public void failedGalleryEditCannotReportSuccess() {
        store.fail = true;
        assertEquals("failed", controller.edit(json("id", "chart-1", "title", "修改图库")).get("result"));
    }

    @Test
    public void missingIdCannotReachAnUnrestrictedGalleryDeleteOrUpdate() {
        store.charts.put("chart-1", row("id", "chart-1", "title", "原图库"));
        assertEquals("failed", controller.del(json()).get("result"));
        assertEquals("failed", controller.edit(json("title", "修改图库")).get("result"));
        assertEquals(1, store.charts.size());
        assertEquals(0, store.writes);
    }

    @Test
    public void failedBatchGalleryDeleteRestoresEarlierRows() {
        store.charts.put("chart-1", row("id", "chart-1"));
        store.charts.put("bad", row("id", "bad"));
        assertEquals("failed", controller.del(json("id", "chart-1,bad")).get("result"));
        assertEquals(2, store.charts.size());
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void blankBatchGalleryIdCannotDeleteUnrelatedRows() {
        store.charts.put("chart-1", row("id", "chart-1"));
        store.charts.put("chart-2", row("id", "chart-2"));
        assertEquals("failed", controller.del(json("id", "chart-1,,chart-2")).get("result"));
        assertEquals(2, store.charts.size());
    }

    @Test
    public void successfulGalleryEditPreservesOtherFields() {
        store.charts.put("chart-1", row("id", "chart-1", "title", "原图库", "option", "chart-options"));
        assertEquals("success", controller.edit(json("id", "chart-1", "title", "修改图库")).get("result"));
        assertEquals("修改图库", store.charts.get("chart-1").get("title"));
        assertEquals("chart-options", store.charts.get("chart-1").get("option"));
    }

    private static class LocalGallery implements GalleryService {
        Map<String, Map<String, Object>> charts = new LinkedHashMap<>();
        boolean fail;
        int writes;
        public List<Map<String, Object>> getPage(PageData pd) { return getAll(pd); }
        public Integer getCount(PageData pd) { return charts.size(); }
        public List<Map<String, Object>> getAll(PageData pd) {
            if (fail) throw new IllegalStateException("gallery query failed");
            return new ArrayList<>(charts.values());
        }
        public void add(PageData pd) { writes++; charts.put(pd.getString("id"), new HashMap<>(pd)); }
        public void edit(PageData pd) {
            if (fail) throw new IllegalStateException("gallery update failed");
            writes++;
            if (charts.containsKey(pd.getString("id"))) charts.get(pd.getString("id")).putAll(pd);
        }
        public void del(PageData pd) {
            if ("bad".equals(pd.getString("id"))) throw new IllegalStateException("gallery delete failed");
            writes++;
            if (!pd.containsKey("id") || "".equals(pd.getString("id"))) charts.clear();
            else charts.remove(pd.getString("id"));
        }
        Object snapshot() {
            Map<String, Map<String, Object>> saved = new LinkedHashMap<>();
            charts.forEach((id, row) -> saved.put(id, new HashMap<>(row)));
            return saved;
        }
        void restore(Object saved) { charts = (Map<String, Map<String, Object>>) saved; }
    }
}
