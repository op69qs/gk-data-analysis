package org.jeecg.modules.reporting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;
import org.jeecg.modules.reporting.mapper.AgentTreasuryConfigMapper;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AgentTreasuryServiceTest {

    @Test
    @SuppressWarnings("unchecked")
    public void preservesOriginalDateFiltersAndCurrentUserTreasuryScope() {
        AgentTreasuryConfigMapper mapper = mock(AgentTreasuryConfigMapper.class);
        when(mapper.findScopePrefix("2201000000")).thenReturn("2201");
        when(mapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(new Page<AgentTreasuryConfig>());
        AgentTreasuryService service = new AgentTreasuryService(mapper);

        service.page(1, 10, "220", "代理", "0",
                Date.valueOf("2026-01-01"), Date.valueOf("2026-12-31"), "2201000000");

        verify(mapper).findScopePrefix("2201000000");
        ArgumentCaptor<QueryWrapper> query = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(mapper).selectPage(any(Page.class), query.capture());
        String sql = query.getValue().getSqlSegment().toLowerCase();
        assertTrue(sql.contains("tre_code"));
        assertTrue(sql.contains("start_date"));
        assertTrue(sql.contains("end_date"));
    }
}
