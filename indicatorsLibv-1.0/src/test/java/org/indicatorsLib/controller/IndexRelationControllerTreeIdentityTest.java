package org.indicatorsLib.controller;

import com.alibaba.fastjson.JSONObject;
import org.indicatorsLib.BaseController;
import org.indicatorsLib.service.IndexRelationService;
import org.indicatorsLib.util.PageData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexRelationControllerTreeIdentityTest {

    private IndexRelationController controller;
    private IndexRelationService relationService;

    @Before
    public void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(BaseController.ANALYSIS_USER_ID_HEADER, "current-user");
        request.addHeader(BaseController.ANALYSIS_GUOKU_ID_HEADER, "GK-001");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        relationService = mock(IndexRelationService.class);
        when(relationService.selectIndexRelationTree(any(PageData.class)))
                .thenReturn(Collections.emptyList());
        controller = new IndexRelationController();
        ReflectionTestUtils.setField(controller, "indexRelationService", relationService);
    }

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void publicIndicatorTreeDoesNotFilterByCreator() {
        JSONObject request = new JSONObject();
        request.put("personalFlag", "1");
        request.put("userId", "spoofed-user");

        controller.selectIndexRelationTree(request);

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(relationService).selectIndexRelationTree(captor.capture());
        assertNull(captor.getValue().get("userId"));
    }

    @Test
    public void personalIndicatorTreeUsesAuthenticatedUser() {
        JSONObject request = new JSONObject();
        request.put("personalFlag", "0");
        request.put("userId", "spoofed-user");

        controller.selectIndexRelationTree(request);

        ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
        verify(relationService).selectIndexRelationTree(captor.capture());
        assertEquals("current-user", captor.getValue().getString("userId"));
    }
}
