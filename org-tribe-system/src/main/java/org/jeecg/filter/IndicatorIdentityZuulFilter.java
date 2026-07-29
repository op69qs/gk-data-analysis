package org.jeecg.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.oauth.NexusPortalIdentitySupport;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Supplies indicatorsLib with identity values derived after Shiro authentication.
 * Client-provided values with the same names are overwritten here.
 */
@Component
public class IndicatorIdentityZuulFilter extends ZuulFilter {

    public static final String USER_ID_HEADER = "X-Analysis-User-Id";
    public static final String SUBJECT_CODE_HEADER = "X-Analysis-Subject-Code";
    public static final String GUOKU_ID_HEADER = "X-Analysis-Guoku-Id";

    @Autowired
    private NexusPortalIdentitySupport portalIdentitySupport;

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String uri = request == null ? null : request.getRequestURI();
        return uri != null && (uri.equals("/indicatorsLib")
                || uri.startsWith("/indicatorsLib/")
                || uri.equals("/seo/seoController/executeSqlFromFont"));
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (!(principal instanceof LoginUser)) {
            throw new IllegalStateException("指标服务请求缺少已认证用户");
        }
        addAuthenticatedHeaders(context, request, (LoginUser) principal);
        return null;
    }

    void addAuthenticatedHeaders(
            RequestContext context,
            HttpServletRequest request,
            LoginUser loginUser) {
        context.addZuulRequestHeader(USER_ID_HEADER, requireValue("当前用户ID", loginUser.getId()));

        String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
        String subjectCode = portalIdentitySupport.resolveSubjectCode(token);
        if (StringUtils.isNotBlank(subjectCode)) {
            context.addZuulRequestHeader(SUBJECT_CODE_HEADER, subjectCode);
            context.addZuulRequestHeader(GUOKU_ID_HEADER, "");
            return;
        }

        SysUser localUser = sysUserService.getUserByName(loginUser.getUsername());
        String guokuId = localUser == null ? null : localUser.getGuokuId();
        context.addZuulRequestHeader(SUBJECT_CODE_HEADER, "");
        context.addZuulRequestHeader(GUOKU_ID_HEADER, requireValue("当前用户所属国库", guokuId));
    }

    private String requireValue(String name, String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException(name + "缺失");
        }
        return value;
    }
}
