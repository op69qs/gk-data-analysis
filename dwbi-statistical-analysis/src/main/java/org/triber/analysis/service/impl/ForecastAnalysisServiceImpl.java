package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.ForecastAnalysisMapper;
import org.triber.analysis.service.ForecastAnalysisService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/14 17:56
 * @Description
 */
@Service
public class ForecastAnalysisServiceImpl implements ForecastAnalysisService {

    @Autowired
    private ForecastAnalysisMapper analysisMapper;

    @Override
    public List<Map<String, Object>> getIndexData(Map<String, Object> map) {
        return analysisMapper.getIndexData(map);
    }

    @Override
    public List<Map<String, Object>> getResourceData(Map<String, Object> map) {
        return analysisMapper.getResourceData(map);
    }

    @Override
    public List<Map<String, Object>> getAnalysisDada(Map<String, Object> map) {
        return analysisMapper.getAnalysisData(map);
    }

    @Override
    public List<Map<String, Object>> getHistoryAnalysisDada(Map<String, Object> map) {
        return analysisMapper.getHistoryAnalysisDada(map);
    }
}
