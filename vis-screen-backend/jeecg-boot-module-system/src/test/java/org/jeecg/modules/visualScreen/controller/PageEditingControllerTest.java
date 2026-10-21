package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageInfo;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.service.*;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PageEditingControllerTest {
    private PageInfoService pages;
    private PageSubService modules;
    private PageWhereService conditions;
    private PageInfoController controller;
    private RecordingTransactions transactions;

    @Before
    public void setUp() {
        pages = mock(PageInfoService.class);
        modules = mock(PageSubService.class);
        conditions = mock(PageWhereService.class);
        PageInfoController target = new PageInfoController();
        ReflectionTestUtils.setField(target, "pageInfoService", pages);
        ReflectionTestUtils.setField(target, "pageSubService", modules);
        ReflectionTestUtils.setField(target, "pageWhereService", conditions);
        transactions = new RecordingTransactions();
        ProxyFactory proxy = new ProxyFactory(target);
        proxy.addAdvice(new TransactionInterceptor(transactions, new AnnotationTransactionAttributeSource()));
        controller = (PageInfoController) proxy.getProxy();
    }

    @Test
    public void failedModuleInsertRollsBackTheWholePageEditAndReportsFailure() {
        doThrow(new IllegalStateException("module insert failed")).when(modules).add(any(PageData.class));
        assertEquals("failed", controller.edit(page()).get("result"));
        assertEquals(1, transactions.rollbacks);
        assertEquals(0, transactions.commits);
    }

    @Test
    public void failedPageCreationRollsBackInsteadOfLeavingAnEmptyPage() {
        doThrow(new IllegalStateException("module insert failed")).when(modules).add(any(PageData.class));
        assertEquals("failed", controller.add(page()).get("result"));
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void successfulEditCommitsPageModulesAndConditions() {
        assertEquals("success", controller.edit(page()).get("result"));
        verify(pages).edit(any(PageData.class));
        verify(modules).del(any(PageData.class));
        verify(conditions).del(any(PageData.class));
        verify(modules).add(any(PageData.class));
        assertEquals(1, transactions.commits);
        assertEquals(0, transactions.rollbacks);
    }

    @Test
    public void failedDeleteCannotReportSuccessOrCommitPartialDeletion() {
        doThrow(new IllegalStateException("condition delete failed")).when(conditions).del(any(PageData.class));
        assertEquals("failed", controller.del(json("id", "page-1")).get("result"));
        assertEquals(1, transactions.rollbacks);
    }

    @Test
    public void missingPageIdNeverReachesAnUnrestrictedDeleteOrUpdate() {
        assertEquals("failed", controller.del(new JSONObject()).get("result"));
        assertEquals("failed", controller.editState(json("state", "0")).get("result"));
        PageInfo value = page();
        value.setId(null);
        assertEquals("failed", controller.edit(value).get("result"));
        verify(pages, never()).del(any(PageData.class));
        verify(pages, never()).edit(any(PageData.class));
    }

    @Test
    public void databaseFailureWhenPublishingIsReportedAsFailure() {
        doThrow(new IllegalStateException("update failed")).when(pages).edit(any(PageData.class));
        JSONObject request = json("id", "page-1");
        request.put("state", "0");
        assertEquals("failed", controller.editState(request).get("result"));
    }

    @Test
    public void emptyPageHasAnArrayAndRepeatedGalleryModulesKeepSeparateConditions() {
        PageSubController target = new PageSubController();
        ReflectionTestUtils.setField(target, "pageSubService", modules);
        ReflectionTestUtils.setField(target, "pageWhereService", conditions);
        when(modules.getAll(any(PageData.class))).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), target.getAll(json("page_id", "page-1")).get("rows"));

        Map<String, Object> first = new HashMap<>();
        first.put("id", "sub-1"); first.put("gallery_id", "gallery-1"); first.put("page_id", "page-1");
        Map<String, Object> second = new HashMap<>(first);
        second.put("id", "sub-2");
        when(modules.getAll(any(PageData.class))).thenReturn(Arrays.asList(first, second));
        List<String> subIds = new ArrayList<>();
        when(conditions.getAll(any(PageData.class))).thenAnswer(call -> {
            subIds.add(((PageData) call.getArgument(0)).getString("sub_id"));
            return Collections.emptyList();
        });
        assertEquals("success", target.getAll(json("page_id", "page-1")).get("result"));
        assertEquals(Arrays.asList("sub-1", "sub-2"), subIds);
    }

    @Test
    public void legacyGalleryRequestWithoutPaginationStillLoadsCharts() {
        GalleryController target = new GalleryController();
        GalleryService gallery = mock(GalleryService.class);
        ReflectionTestUtils.setField(target, "galleryService", gallery);
        when(gallery.getPage(any(PageData.class))).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), target.getPage(json("state", "0")).get("rows"));
        verify(gallery).getPage(any(PageData.class));
    }

    private PageInfo page() {
        PageInfo value = new PageInfo();
        value.setId("page-1"); value.setName("收入页"); value.setTemplate("6");
        value.setPage_sub(Collections.singletonList(new PageSub()));
        return value;
    }

    private JSONObject json(String key, String value) {
        JSONObject request = new JSONObject(); request.put(key, value); return request;
    }

    private static class RecordingTransactions extends AbstractPlatformTransactionManager {
        int commits;
        int rollbacks;
        protected Object doGetTransaction() { return new Object(); }
        protected void doBegin(Object tx, TransactionDefinition definition) {}
        protected void doCommit(DefaultTransactionStatus status) { commits++; }
        protected void doRollback(DefaultTransactionStatus status) { rollbacks++; }
    }
}
