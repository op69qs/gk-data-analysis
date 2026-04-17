package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/29 15:37
 * @Description
 */
public interface KeyIndicatorsParameterService {

    int getKeyIndicatorsDataTotal(Map<String, Object> map);

    List<Map<String, Object>> getKeyIndicatorsData(Map<String, Object> map);

    void updateKeyIndicatorsData(Map<String, Object> map);
}
