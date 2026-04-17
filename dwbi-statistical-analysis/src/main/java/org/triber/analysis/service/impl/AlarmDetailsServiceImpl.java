package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.AlarmDetailsMapper;
import org.triber.analysis.service.AlarmDetailsService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:46
 * @Description
 */
@Service
public class AlarmDetailsServiceImpl implements AlarmDetailsService {

    @Autowired
    private AlarmDetailsMapper alarmDetailsMapper;

    @Override
    public int getAlarmDetailsDataTotal(Map<String, Object> map) {
        return alarmDetailsMapper.getAlarmDetailsDataTotal(map);
    }

    @Override
    public List<Map<String, Object>> getAlarmDetailsData(Map<String, Object> map) {
        return alarmDetailsMapper.getAlarmDetailsData(map);
    }
}
