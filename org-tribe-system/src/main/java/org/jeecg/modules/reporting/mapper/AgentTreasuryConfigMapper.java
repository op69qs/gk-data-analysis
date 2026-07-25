package org.jeecg.modules.reporting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;

public interface AgentTreasuryConfigMapper extends BaseMapper<AgentTreasuryConfig> {
    String findScopePrefix(@Param("guokuId") String guokuId);
}
