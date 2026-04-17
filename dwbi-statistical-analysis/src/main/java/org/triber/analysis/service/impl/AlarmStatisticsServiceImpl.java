package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.AlarmStatisticsMapper;
import org.triber.analysis.service.AlarmStatisticsService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:46
 * @Description
 */
@Service
public class AlarmStatisticsServiceImpl implements AlarmStatisticsService {

    @Autowired
    private AlarmStatisticsMapper alarmStatisticsMapper;

    @Override
    public int getAlarmStatisticsDataTotal(Map<String, Object> map) {
        return alarmStatisticsMapper.getAlarmStatisticsDataTotal(map);
    }

    @Override
    public List<Map<String, Object>> getAlarmStatisticsData(Map<String, Object> map) {
        return alarmStatisticsMapper.getAlarmStatisticsData(map);
    }
}
