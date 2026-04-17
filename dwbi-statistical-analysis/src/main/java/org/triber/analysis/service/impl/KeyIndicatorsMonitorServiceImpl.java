package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.KeyIndicatorsMonitorMapper;
import org.triber.analysis.service.KeyIndicatorsMonitorService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/17 15:10
 * @Description
 */
@Service
public class KeyIndicatorsMonitorServiceImpl implements KeyIndicatorsMonitorService {

    @Autowired
    private KeyIndicatorsMonitorMapper keyIndicatorsMonitorMapper;

    @Override
    public List<Map<String, Object>> getKeyIndicators(Map<String, Object> map) {
        return keyIndicatorsMonitorMapper.getKeyIndicators(map);
    }

    @Override
    public List<Map<String, Object>> getIndexPlatform(Map<String, Object> map) {
        return keyIndicatorsMonitorMapper.getIndexPlatform(map);
    }

    @Override
    public List<Map<String, Object>> getTableByDataBase(Map<String, Object> map) {
        return keyIndicatorsMonitorMapper.getTableByDataBase(map);
    }

    @Override
    public List<Map<String, Object>> getResourceTop(Map<String, Object> map) {
        return keyIndicatorsMonitorMapper.getResourceTop(map);
    }

    @Override
    public List<Map<String, Object>> getKeyIndicatorMonitorDada(Map<String, Object> map) {
        return keyIndicatorsMonitorMapper.getKeyIndicatorMonitorDada(map);
    }
}
