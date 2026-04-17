package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.MonitorAlarmMapper;
import org.triber.analysis.service.MonitorAlarmService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/16 16:42
 * @Description
 */
@Service
public class MonitorAlarmServiceImpl implements MonitorAlarmService {

    @Autowired
    private MonitorAlarmMapper monitorAlarmMapper;

    @Override
    public List<Map<String, Object>> getAlarmIndexData(Map<String, Object> map) {
        return monitorAlarmMapper.getAlarmIndexData(map);
    }

    @Override
    public List<Map<String, Object>> getAlarmPlatformData(Map<String, Object> map) {
        return monitorAlarmMapper.getAlarmPlatformData(map);
    }

    @Override
    public List<Map<String, Object>> getAlarmResourceData(Map<String, Object> map) {
        return monitorAlarmMapper.getAlarmResourceData(map);
    }

    @Override
    public List<Map<String, Object>> getAlarmAnalysisData(Map<String, Object> map) {
        return monitorAlarmMapper.getAlarmAnalysisData(map);
    }
}
