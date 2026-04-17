package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.ForecastThresholdParameterMapper;
import org.triber.analysis.service.ForecastThresholdParameterService;
import org.triber.analysis.util.UuidUtil;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/10 13:46
 * @Description
 */
@Service
public class ForecastThresholdParameterServiceImpl implements ForecastThresholdParameterService {

    @Autowired
    private ForecastThresholdParameterMapper thresholdParameterMapper;

    @Override
    public List<Map<String, Object>> getMonitorIndexData(Map<String, Object> map) {
        return thresholdParameterMapper.getMonitorIndexData(map);
    }

    @Override
    public List<Map<String, Object>> getIndexResourceData(Map<String, Object> map) {
        return thresholdParameterMapper.getIndexResourceData(map);
    }

    @Override
    public int getThresholdCount(Map<String, Object> map) {
        return thresholdParameterMapper.getThresholdCount(map);
    }

    @Override
    public List<Map<String, Object>> getThresholdData(Map<String, Object> map) {
        return thresholdParameterMapper.getThresholdData(map);
    }

    @Override
    public void insertThresholdData(Map<String, Object> map) {
        thresholdParameterMapper.insertThresholdData(map);
    }

    @Override
    public void updateThresholdData(Map<String, Object> map) {
        thresholdParameterMapper.updateThresholdData(map);
    }

    @Override
    public void deleteThresholdData(Map<String, Object> map) {
        thresholdParameterMapper.deleteThresholdData(map);
    }

    @Override
    public void insertForecastData(Map<String, Object> map) {
        if (map.size() > 0) {
            map.put("ID", UuidUtil.get32UUID());
            thresholdParameterMapper.insertForecastData(map);
        }
    }
}
