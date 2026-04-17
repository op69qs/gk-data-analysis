package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/16 16:42
 * @Description
 */
public interface MonitorAlarmService {

    List<Map<String, Object>> getAlarmIndexData(Map<String, Object> map);

    List<Map<String, Object>> getAlarmPlatformData(Map<String, Object> map);

    List<Map<String, Object>> getAlarmResourceData(Map<String, Object> map);

    List<Map<String, Object>> getAlarmAnalysisData(Map<String, Object> map);
}
