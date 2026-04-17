package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/14 17:56
 * @Description
 */
public interface ForecastAnalysisService {

    List<Map<String, Object>> getIndexData(Map<String, Object> map);

    List<Map<String, Object>> getResourceData(Map<String, Object> map);

    List<Map<String, Object>> getAnalysisDada(Map<String, Object> map);

    List<Map<String, Object>> getHistoryAnalysisDada(Map<String, Object> map);
}
