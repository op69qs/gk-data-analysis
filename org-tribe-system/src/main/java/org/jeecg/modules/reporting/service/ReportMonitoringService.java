package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.ReportingBusinessMapper;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;
import org.jeecg.modules.reporting.vo.ReportingPageResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportMonitoringService {
    private final ReportingBusinessMapper mapper;

    public ReportMonitoringService(ReportingBusinessMapper mapper) {
        this.mapper = mapper;
    }

    public ReportingPageResult<Map<String, Object>> key(ReportingBusinessQuery query,
                                                        int pageNo, int pageSize) {
        prepare(query, pageNo, pageSize);
        return new ReportingPageResult<>(mapper.countKeyMonitoring(query), mapper.queryKeyMonitoring(query));
    }

    public ReportingPageResult<Map<String, Object>> tims(ReportingBusinessQuery query,
                                                         int pageNo, int pageSize) {
        prepare(query, pageNo, pageSize);
        return new ReportingPageResult<>(mapper.countTimsMonitoring(query), mapper.queryTimsMonitoring(query));
    }

    public List<Map<String, Object>> treasuries() {
        return mapper.queryTreasuries();
    }

    void prepare(ReportingBusinessQuery query, int pageNo, int pageSize) {
        if (query == null) throw new IllegalArgumentException("查询条件不能为空");
        int size = Math.min(100, Math.max(1, pageSize));
        query.setLimit(size);
        query.setOffset((Math.max(1, pageNo) - 1) * size);
    }
}
