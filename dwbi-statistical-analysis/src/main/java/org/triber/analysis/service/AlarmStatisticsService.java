package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:45
 * @Description 监测告警统计表
 */
public interface AlarmStatisticsService {

    /**
     * @Author haojiang
     * @Date 2020/11/18 11:25
     * @Description 获取监测告警统计表数据total
     */
    int getAlarmStatisticsDataTotal(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/12 09:45
     * @Description 监测告警统计表
     */
    List<Map<String, Object>> getAlarmStatisticsData(Map<String, Object> map);
}
