package org.jeecg.config.shiro.filters;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class LegacyPlatformBlockFilter extends BasicHttpAuthenticationFilter {

    private static final String OFFLINE_RESPONSE = "{\"success\":false,\"message\":\"vis旧平台入口已停用，请从分析平台访问\"}";

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_GONE);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(OFFLINE_RESPONSE);
        return false;
    }
}