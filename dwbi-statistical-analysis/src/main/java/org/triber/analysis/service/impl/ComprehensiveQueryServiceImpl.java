package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.triber.analysis.dao.mapper.ComprehensiveQueryMapper;
import org.triber.analysis.service.ComprehensiveQueryService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:46
 * @Description
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ComprehensiveQueryServiceImpl implements ComprehensiveQueryService {

    @Autowired
    private ComprehensiveQueryMapper comprehensiveQueryMapper;

    @Override
    public List<Map<String, Object>> getPlatformData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getPlatformData(map);
    }

    @Override
    public List<Map<String, Object>> getDevOpsIndicatorsData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getDevOpsIndicatorsData(map);
    }

    @Override
    public List<Map<String, Object>> getNetworkIndicatorsData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getNetworkIndicatorsData(map);
    }

    @Override
    public List<Map<String, Object>> getDevOpsResourceData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getDevOpsResourceData(map);
    }

    @Override
    public List<Map<String, Object>> getNetworkResourceData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getNetworkResourceData(map);
    }

    @Override
    public List<Map<String, Object>> getComprehensiveQueryData(Map<String, Object> map) {
        return comprehensiveQueryMapper.getComprehensiveQueryData(map);
    }
}
