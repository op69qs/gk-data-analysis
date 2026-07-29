package org.jeecg.modules.reporting.service;

import org.jeecg.modules.oauth.NexusPortalIdentitySupport;
import org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportingUserScopeServiceTest {

    private ISysUserService userService;
    private AgentTreasuryConfigMapper treasuryMapper;
    private NexusPortalIdentitySupport portalIdentitySupport;
    private ReportingUserScopeService service;

    @Before
    public void setUp() {
        userService = mock(ISysUserService.class);
        treasuryMapper = mock(AgentTreasuryConfigMapper.class);
        portalIdentitySupport = mock(NexusPortalIdentitySupport.class);
        service = new ReportingUserScopeService(userService, treasuryMapper, portalIdentitySupport);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Access-Token", "tok-1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void portalLoginResolvesGuokuAndPrefixFromBookorgcode() {
        when(portalIdentitySupport.resolveSubjectCode("tok-1")).thenReturn("BOOK-22");
        when(treasuryMapper.findGuokuIdByBookorgcode("BOOK-22")).thenReturn("220000");
        when(treasuryMapper.findScopePrefixByBookorgcode("BOOK-22")).thenReturn("22");

        assertEquals("220000", service.requireGuokuId("any-user"));
        assertEquals("22", service.requireTreasuryPrefix("any-user"));
        verify(userService, never()).getUserByName("any-user");
    }

    @Test
    public void localLoginUsesSysUserGuokuId() {
        when(portalIdentitySupport.resolveSubjectCode("tok-1")).thenReturn(null);
        SysUser user = new SysUser();
        user.setGuokuId("500000");
        when(userService.getUserByName("local")).thenReturn(user);
        when(treasuryMapper.findScopePrefix("500000")).thenReturn("50");

        assertEquals("500000", service.requireGuokuId("local"));
        assertEquals("50", service.requireTreasuryPrefix("local"));
    }

    @Test(expected = IllegalStateException.class)
    public void failsClosedWhenNeitherPortalNorLocalScopeExists() {
        when(portalIdentitySupport.resolveSubjectCode("tok-1")).thenReturn(null);
        when(userService.getUserByName("orphan")).thenReturn(new SysUser());

        service.requireGuokuId("orphan");
    }
}
