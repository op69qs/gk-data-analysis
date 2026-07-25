package org.jeecg.modules.reporting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;
import org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AgentTreasuryService {
    private final AgentTreasuryConfigMapper mapper;

    public AgentTreasuryService(AgentTreasuryConfigMapper mapper) {
        this.mapper = mapper;
    }

    public IPage<AgentTreasuryConfig> page(int pageNo, int pageSize, String code, String name, String state) {
        QueryWrapper<AgentTreasuryConfig> query = new QueryWrapper<AgentTreasuryConfig>().orderByDesc("add_time");
        if (!blank(code)) query.like("tre_code", code.trim());
        if (!blank(name)) query.like("tre_name", name.trim());
        if (!blank(state)) query.eq("state", state.trim());
        return mapper.selectPage(new Page<>(Math.max(1, pageNo), Math.min(100, Math.max(1, pageSize))), query);
    }

    @Transactional
    public void add(AgentTreasuryConfig record, String userId) {
        validate(record);
        if (mapper.selectById(record.getTreCode()) != null) throw new IllegalArgumentException("代理国库代码已存在");
        record.setAddUserid(userId);
        record.setAddTime(new Date());
        mapper.insert(record);
    }

    @Transactional
    public void update(String treasuryCode, AgentTreasuryConfig record, String userId) {
        AgentTreasuryConfig existing = mapper.selectById(treasuryCode);
        if (existing == null) throw new IllegalArgumentException("代理国库配置不存在");
        record.setTreCode(treasuryCode);
        validate(record);
        existing.setTreName(record.getTreName());
        existing.setStartDate(record.getStartDate());
        existing.setEndDate(record.getEndDate());
        existing.setState(record.getState());
        existing.setModUserid(userId);
        existing.setModTime(new Date());
        mapper.updateById(existing);
    }

    private void validate(AgentTreasuryConfig record) {
        if (record == null || blank(record.getTreCode()) || blank(record.getTreName())
                || record.getStartDate() == null || record.getEndDate() == null) {
            throw new IllegalArgumentException("国库代码、名称、开始日期和结束日期均不能为空");
        }
        if (record.getStartDate().after(record.getEndDate())) throw new IllegalArgumentException("开始日期不能晚于结束日期");
        if (!"0".equals(record.getState()) && !"1".equals(record.getState())) {
            throw new IllegalArgumentException("状态只能是 0（启用）或 1（停用）");
        }
    }

    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }
}
