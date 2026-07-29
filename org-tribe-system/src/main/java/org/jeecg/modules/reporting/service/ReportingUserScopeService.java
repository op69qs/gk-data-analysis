package org.jeecg.modules.reporting.service;

import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.oauth.NexusPortalIdentitySupport;
import org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Resolves treasury scope from portal subject_code (bookorgcode) or local sys_user.guoku_id.
 * Never trusts client-supplied guokuId.
 */
@Service
public class ReportingUserScopeService {
    private final ISysUserService userService;
    private final AgentTreasuryConfigMapper treasuryMapper;
    private final NexusPortalIdentitySupport portalIdentitySupport;

    public ReportingUserScopeService(ISysUserService userService,
                                     AgentTreasuryConfigMapper treasuryMapper,
                                     NexusPortalIdentitySupport portalIdentitySupport) {
        this.userService = userService;
        this.treasuryMapper = treasuryMapper;
        this.portalIdentitySupport = portalIdentitySupport;
    }

    public String requireGuokuId(String username) {
        String subjectCode = resolvePortalSubjectCode();
        if (StringUtils.isNotBlank(subjectCode)) {
            String guokuId = treasuryMapper.findGuokuIdByBookorgcode(subjectCode);
            if (StringUtils.isBlank(guokuId)) {
                throw new IllegalStateException("当前用户的门户国库无法映射到本地国库维表");
            }
            return guokuId.trim();
        }
        SysUser user = userService.getUserByName(username);
        if (user == null || user.getGuokuId() == null || user.getGuokuId().trim().isEmpty()) {
            throw new IllegalStateException("当前用户未配置所属国库，无法确定数据范围");
        }
        return user.getGuokuId().trim();
    }

    public String requireTreasuryPrefix(String username) {
        String subjectCode = resolvePortalSubjectCode();
        if (StringUtils.isNotBlank(subjectCode)) {
            String prefix = treasuryMapper.findScopePrefixByBookorgcode(subjectCode);
            if (StringUtils.isBlank(prefix)) {
                throw new IllegalStateException("当前用户的国库层级无法转换为上报范围");
            }
            return prefix.trim();
        }
        String guokuId = requireGuokuId(username);
        String prefix = treasuryMapper.findScopePrefix(guokuId);
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalStateException("当前用户的国库层级无法转换为上报范围");
        }
        return prefix.trim();
    }

    public void assertTreasuryAllowed(String treasuryCode, String username) {
        String prefix = requireTreasuryPrefix(username);
        if (treasuryCode == null || !treasuryCode.startsWith(prefix)) {
            throw new IllegalArgumentException("上报国库不在当前用户数据范围内");
        }
    }

    private String resolvePortalSubjectCode() {
        String token = currentAccessToken();
        return portalIdentitySupport.resolveSubjectCode(token);
    }

    private String currentAccessToken() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes)) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(DefContants.X_ACCESS_TOKEN);
    }
}
