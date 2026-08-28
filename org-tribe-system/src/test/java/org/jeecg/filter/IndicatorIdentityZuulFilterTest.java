package org.jeecg.filter;

import com.netflix.zuul.context.RequestContext;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.oauth.NexusPortalIdentitySupport;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndicatorIdentityZuulFilterTest {

    private IndicatorIdentityZuulFilter filter;
    private NexusPortalIdentitySupport portalIdentitySupport;
    private ISysUserService userService;

    @Before
    public void setUp() {
        filter = new IndicatorIdentityZuulFilter();
        portalIdentitySupport = mock(NexusPortalIdentitySupport.class);
        userService = mock(ISysUserService.class);
        ReflectionTestUtils.setField(filter, "portalIdentitySupport", portalIdentitySupport);
        ReflectionTestUtils.setField(filter, "sysUserService", userService);
    }

    @Test
    public void portalSubjectCodeWinsEvenWhenLocalMappingExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Access-Token", "token-1");
        when(portalIdentitySupport.resolveSubjectCode("token-1")).thenReturn("BOOK-001");
        RequestContext context = new RequestContext();

        filter.addAuthenticatedHeaders(context, request, loginUser("portal-id", "portal-name"));

        assertEquals("portal-id", context.getZuulRequestHeaders().get("x-analysis-user-id"));
        assertEquals("BOOK-001", context.getZuulRequestHeaders().get("x-analysis-subject-code"));
        verify(userService, never()).getUserByName("portal-name");
    }

    @Test
    public void localLoginUsesConfiguredGuoku() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Access-Token", "token-2");
        when(portalIdentitySupport.resolveSubjectCode("token-2")).thenReturn(null);
        SysUser local = new SysUser();
        local.setGuokuId("GK-001");
        when(userService.getUserByName("local-name")).thenReturn(local);
        RequestContext context = new RequestContext();

        filter.addAuthenticatedHeaders(context, request, loginUser("local-id", "local-name"));

        assertEquals("local-id", context.getZuulRequestHeaders().get("x-analysis-user-id"));
        assertEquals("GK-001", context.getZuulRequestHeaders().get("x-analysis-guoku-id"));
    }

    @Test
    public void manualComprehensiveQueryReceivesAuthenticatedIdentity() {
        assertTrue(shouldFilter("/seo/seoController/executeSql"));
        assertTrue(shouldFilter("/seo/seoController/executeSql/"));
        assertTrue(shouldFilter("/seo/seoController/executeSql;v=1"));
        assertTrue(shouldFilter("/seo;v=1/seoController/executeSql"));
    }

    @Test
    public void comprehensiveQueryDownloadReceivesAuthenticatedIdentity() {
        assertTrue(shouldFilter("/seo/seoController/download"));
        assertTrue(shouldFilter("/seo/seoController/download/"));
        assertTrue(shouldFilter("/seo/seoController/download;v=1"));
        assertFalse(shouldFilter("/seo/seoController/downloadPreview"));
    }

    private boolean shouldFilter(String uri) {
        RequestContext context = RequestContext.getCurrentContext();
        context.clear();
        context.setRequest(new MockHttpServletRequest("POST", uri));
        try {
            return filter.shouldFilter();
        } finally {
            context.clear();
        }
    }

    private LoginUser loginUser(String id, String username) {
        LoginUser user = new LoginUser();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}
