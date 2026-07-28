package org.jeecg.modules.reporting.service;

import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper;
import org.springframework.stereotype.Service;

/** Resolves treasury scope from the authenticated server-side user record, never from request input. */
@Service
public class ReportingUserScopeService {
    private final ISysUserService userService;
    private final AgentTreasuryConfigMapper treasuryMapper;

    public ReportingUserScopeService(ISysUserService userService, AgentTreasuryConfigMapper treasuryMapper) {
        this.userService = userService;
        this.treasuryMapper = treasuryMapper;
    }

    public String requireGuokuId(String username) {
        SysUser user = userService.getUserByName(username);
        if (user == null || user.getGuokuId() == null || user.getGuokuId().trim().isEmpty()) {
            throw new IllegalStateException("当前用户未配置所属国库，无法确定数据范围");
        }
        return user.getGuokuId().trim();
    }

    public String requireTreasuryPrefix(String username) {
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
}
