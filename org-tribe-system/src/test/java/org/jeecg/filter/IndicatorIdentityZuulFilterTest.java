package org.jeecg.filter;

import com.netflix.zuul.context.RequestContext;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndicatorIdentityZuulFilterTest {

    private IndicatorIdentityZuulFilter filter;
    private RedisUtil redisUtil;
    private ISysUserService userService;

    @Before
    public void setUp() {
        filter = new IndicatorIdentityZuulFilter();
        redisUtil = mock(RedisUtil.class);
        userService = mock(ISysUserService.class);
        ReflectionTestUtils.setField(filter, "redisUtil", redisUtil);
        ReflectionTestUtils.setField(filter, "sysUserService", userService);
    }

    @Test
    public void portalSubjectCodeWinsEvenWhenLocalMappingExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Access-Token", "token-1");
        when(redisUtil.get("PREFIX_NEXUS_PORTAL_SUBJECT_CODE_token-1")).thenReturn("BOOK-001");
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
        SysUser local = new SysUser();
        local.setGuokuId("GK-001");
        when(userService.getUserByName("local-name")).thenReturn(local);
        RequestContext context = new RequestContext();

        filter.addAuthenticatedHeaders(context, request, loginUser("local-id", "local-name"));

        assertEquals("local-id", context.getZuulRequestHeaders().get("x-analysis-user-id"));
        assertEquals("GK-001", context.getZuulRequestHeaders().get("x-analysis-guoku-id"));
    }

    private LoginUser loginUser(String id, String username) {
        LoginUser user = new LoginUser();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}
