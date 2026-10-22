package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageInfo;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.SchemeInfo;
import org.jeecg.modules.visualScreen.model.SchemeRel;
import org.jeecg.modules.visualScreen.service.PageInfoService;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.jeecg.modules.visualScreen.service.SchemeInfoService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Real controllers and transaction advice; all storage is local and snapshotted. */
public class SchemeInfoControllerTest {
    private final LocalSchemes store = new LocalSchemes();
    private SchemeInfoController controller;
    private PageInfoService pages;
    private PageSubService modules;
    private LocalTransactions transactions;

    @Before
    public void setUp() {
        SchemeInfoController target = new SchemeInfoController();
        pages = mock(PageInfoService.class);
        modules = mock(PageSubService.class);
        ReflectionTestUtils.setField(target, "schemeInfoService", store);
        ReflectionTestUtils.setField(target, "pageInfoService", pages);
        ReflectionTestUtils.setField(target, "pageSubService", modules);
        transactions = new LocalTransactions(store::snapshot, store::restore);
        ProxyFactory proxy = new ProxyFactory(target);
        proxy.addAdvice(new TransactionInterceptor(transactions, new AnnotationTransactionAttributeSource()));
        controller = (SchemeInfoController) proxy.getProxy();
    }

    @Test
    public void paginatedQueryFailureIsNotSuccessfulEmptyData() {
        store.failReads = true;
        assertEquals("failed", controller.getPage(json("pageNo", "1", "pageSize", "10")).get("result"));
    }

    @Test
    public void allSchemeQueryFailureIsReported() {
        store.failReads = true;
        assertEquals("failed", controller.getAll(new JSONObject()).get("result"));
    }

    @Test
    public void relationshipQueryFailureIsReported() {
        store.failReads = true;
        assertEquals("failed", controller.getAllRel(json("scheme_id", "existing")).get("result"));
    }

    @Test
    public void previewQueryFailureIsReported() {
        store.failReads = true;
        assertEquals("failed", controller.getAllPage(json("scheme_id", "existing")).get("result"));
    }

    @Test
    public void missingSchemeIdCannotLoadRelationshipsFromOtherSchemes() {
        seed("existing", "原方案", "page-1");
        assertEquals("failed", controller.getAllRel(new JSONObject()).get("result"));
        assertEquals("failed", controller.getAllPage(new JSONObject()).get("result"));
    }

    @Test
    public void emptySchemePreviewHasAnArrayAndZeroCharts() {
        Map<String, Object> result = controller.getAllPage(json("scheme_id", "empty"));
        assertEquals("success", result.get("result"));
        assertEquals(Collections.emptyList(), result.get("data"));
        assertEquals(0, result.get("count"));
    }

    @Test
    public void previewPreservesPageOrderSkipsDeletedPagesAndCountsChartQueries() {
        seed("existing", "原方案", "page-2", "deleted", "page-1");
        when(pages.getPageInfo(any(PageData.class))).thenAnswer(call -> {
            String id = ((PageData) call.getArgument(0)).getString("id");
            if ("deleted".equals(id)) return null;
            PageInfo page = new PageInfo(); page.setId(id); return page;
        });
        PageSub chart = new PageSub(); chart.setQuery_path("/queryData/getData");
        PageSub html = new PageSub(); html.setQuery_path("null");
        when(modules.getPageSub(any(PageData.class))).thenReturn(Arrays.asList(chart, html));
        Map<String, Object> result = controller.getAllPage(json("scheme_id", "existing"));
        List<PageInfo> resultPages = (List<PageInfo>) result.get("data");
        assertEquals(Arrays.asList("page-2", "page-1"), resultPages.stream().map(PageInfo::getId).collect(Collectors.toList()));
        assertEquals(2, result.get("count"));
        assertEquals(2, resultPages.get(0).getPage_sub().size());
    }

    @Test
    public void failureLoadingPreviewModulesCannotReportSuccess() {
        seed("existing", "原方案", "page-1");
        when(pages.getPageInfo(any(PageData.class))).thenReturn(new PageInfo());
        when(modules.getPageSub(any(PageData.class))).thenThrow(new IllegalStateException("module query failed"));
        assertEquals("failed", controller.getAllPage(json("scheme_id", "existing")).get("result"));
    }

    @Test
    public void failedCreationRollsBackSchemeAndAlreadyInsertedRelationships() {
        store.failOnPage = "bad";
        assertEquals("failed", controller.add(scheme(null, "新增方案", "page-1", "bad")).get("result"));
        assertTrue(store.schemes.isEmpty());
        assertTrue(store.relations.isEmpty());
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void failedEditRestoresNameIntervalAndOriginalRelationshipOrder() {
        seed("existing", "原方案", "page-2", "page-1");
        store.failOnPage = "bad";
        assertEquals("failed", controller.edit(scheme("existing", "修改方案", "new-page", "bad")).get("result"));
        assertEquals("原方案", store.schemes.get("existing").get("name"));
        assertEquals("8000", store.schemes.get("existing").get("rotation_interval"));
        assertEquals(Arrays.asList("page-2", "page-1"), pageIds());
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void successfulEditRetainsAnExplicitDeletedPageRelationship() {
        seed("existing", "原方案", "page-1");
        assertEquals("success", controller.edit(scheme("existing", "修改方案", "deleted", "page-2")).get("result"));
        assertEquals("修改方案", store.schemes.get("existing").get("name"));
        assertEquals("9000", store.schemes.get("existing").get("rotation_interval"));
        assertEquals(Arrays.asList("deleted", "page-2"), pageIds());
        assertEquals("0", store.relations.get(0).get("sort"));
        assertEquals("1", store.relations.get(1).get("sort"));
        assertEquals(1, transactions.commits);
    }

    @Test
    public void failedBatchDeleteRestoresEverySchemeAndRelationship() {
        seed("first", "方案一", "page-1");
        seed("second", "方案二", "page-2");
        store.failOnRelationDelete = "second";
        assertEquals("failed", controller.del(json("id", "first,second")).get("result"));
        assertEquals(2, store.schemes.size());
        assertEquals(Arrays.asList("page-1", "page-2"), pageIds());
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void missingEditIdCannotClearEveryRelationship() {
        seed("existing", "原方案", "page-1");
        assertEquals("failed", controller.edit(scheme(null, "修改方案", "page-2")).get("result"));
        assertEquals(Collections.singletonList("page-1"), pageIds());
        assertEquals(0, store.writes);
    }

    @Test
    public void blankSchemeNameIsRejectedBeforeAnyWrite() {
        assertEquals("failed", controller.add(scheme(null, " ", "page-1")).get("result"));
        assertTrue(store.schemes.isEmpty());
        assertEquals(0, store.writes);
    }

    @Test
    public void missingDeleteIdIsRejectedBeforeAnyWrite() {
        seed("existing", "原方案", "page-1");
        assertEquals("failed", controller.del(new JSONObject()).get("result"));
        assertEquals(1, store.schemes.size());
        assertEquals(0, store.writes);
    }

    @Test
    public void blankIdInsideBatchCannotTriggerAnUnrestrictedDelete() {
        seed("first", "方案一", "page-1");
        seed("second", "方案二", "page-2");
        assertEquals("failed", controller.del(json("id", "first,,second")).get("result"));
        assertEquals(2, store.schemes.size());
        assertEquals(2, store.relations.size());
    }

    private List<String> pageIds() {
        return store.relations.stream().map(row -> (String) row.get("page_id")).collect(Collectors.toList());
    }

    private void seed(String id, String name, String... pageIds) {
        store.schemes.put(id, row("id", id, "name", name, "rotation_interval", "8000"));
        for (int index = 0; index < pageIds.length; index++) {
            store.relations.add(row("scheme_id", id, "page_id", pageIds[index], "sort", String.valueOf(index)));
        }
    }

    private static SchemeInfo scheme(String id, String name, String... pageIds) {
        SchemeInfo value = new SchemeInfo();
        value.setId(id); value.setName(name); value.setRotation_interval("9000");
        List<SchemeRel> relations = new ArrayList<>();
        for (int index = 0; index < pageIds.length; index++) {
            SchemeRel relation = new SchemeRel();
            relation.setPage_id(pageIds[index]); relation.setSort(String.valueOf(index));
            relations.add(relation);
        }
        value.setSchemeRel(relations);
        return value;
    }

    static JSONObject json(String... values) {
        JSONObject result = new JSONObject();
        for (int i = 0; i < values.length; i += 2) result.put(values[i], values[i + 1]);
        return result;
    }

    static Map<String, Object> row(String... values) {
        return new HashMap<>(json(values));
    }

    private static class LocalSchemes implements SchemeInfoService {
        Map<String, Map<String, Object>> schemes = new LinkedHashMap<>();
        List<Map<String, Object>> relations = new ArrayList<>();
        boolean failReads;
        String failOnPage, failOnRelationDelete;
        int writes;

        public List<Map<String, Object>> getPage(PageData pd) { return getAll(pd); }
        public Integer getCount(PageData pd) { return getAll(pd).size(); }
        public List<Map<String, Object>> getAll(PageData pd) {
            if (failReads) throw new IllegalStateException("scheme query failed");
            return new ArrayList<>(schemes.values());
        }
        public List<Map<String, Object>> checkRepeat(PageData pd) {
            return schemes.values().stream().filter(row -> Objects.equals(row.get("name"), pd.get("name"))
                    && !Objects.equals(row.get("id"), pd.get("id"))).collect(Collectors.toList());
        }
        public void add(PageData pd) { writes++; schemes.put(pd.getString("id"), new HashMap<>(pd)); }
        public void edit(PageData pd) {
            writes++;
            if (schemes.containsKey(pd.getString("id"))) schemes.get(pd.getString("id")).putAll(pd);
        }
        public void del(PageData pd) {
            writes++;
            if (!pd.containsKey("id") || "".equals(pd.getString("id"))) schemes.clear();
            else schemes.remove(pd.getString("id"));
        }
        public List<Map<String, Object>> getAllRel(PageData pd) {
            if (failReads) throw new IllegalStateException("relationship query failed");
            return relations.stream().filter(row -> !pd.containsKey("scheme_id")
                    || Objects.equals(row.get("scheme_id"), pd.get("scheme_id"))).collect(Collectors.toList());
        }
        public void addRel(PageData pd) {
            if (Objects.equals(failOnPage, pd.get("page_id"))) throw new IllegalStateException("relationship insert failed");
            writes++; relations.add(new HashMap<>(pd));
        }
        public void delRel(PageData pd) {
            if (Objects.equals(failOnRelationDelete, pd.get("scheme_id")) && failOnRelationDelete != null)
                throw new IllegalStateException("relationship delete failed");
            writes++;
            relations.removeIf(row -> pd.get("scheme_id") == null || "".equals(pd.get("scheme_id"))
                    || Objects.equals(row.get("scheme_id"), pd.get("scheme_id")));
        }
        Object snapshot() {
            Map<String, Map<String, Object>> savedSchemes = new LinkedHashMap<>();
            schemes.forEach((id, row) -> savedSchemes.put(id, new HashMap<>(row)));
            List<Map<String, Object>> savedRelations = relations.stream().map(HashMap::new).collect(Collectors.toList());
            return new Object[]{savedSchemes, savedRelations};
        }
        void restore(Object saved) {
            schemes = (Map<String, Map<String, Object>>) ((Object[]) saved)[0];
            relations = (List<Map<String, Object>>) ((Object[]) saved)[1];
        }
    }

    static class LocalTransactions extends AbstractPlatformTransactionManager {
        private final java.util.function.Supplier<Object> snapshot;
        private final java.util.function.Consumer<Object> restore;
        private Object saved;
        int commits, rollbacks;
        LocalTransactions(java.util.function.Supplier<Object> snapshot, java.util.function.Consumer<Object> restore) {
            this.snapshot = snapshot; this.restore = restore;
        }
        protected Object doGetTransaction() { return new Object(); }
        protected void doBegin(Object tx, TransactionDefinition definition) { saved = snapshot.get(); }
        protected void doCommit(DefaultTransactionStatus status) { commits++; }
        protected void doRollback(DefaultTransactionStatus status) { rollbacks++; restore.accept(saved); }
    }
}
