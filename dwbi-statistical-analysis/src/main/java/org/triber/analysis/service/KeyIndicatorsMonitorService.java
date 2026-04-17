package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/17 15:10
 * @Description
 */
public interface KeyIndicatorsMonitorService {

    List<Map<String, Object>> getKeyIndicators(Map<String, Object> map);

    List<Map<String, Object>> getIndexPlatform(Map<String, Object> map);

    List<Map<String, Object>> getTableByDataBase(Map<String, Object> map);

    List<Map<String, Object>> getResourceTop(Map<String, Object> map);

    List<Map<String, Object>> getKeyIndicatorMonitorDada(Map<String, Object> map);
}
