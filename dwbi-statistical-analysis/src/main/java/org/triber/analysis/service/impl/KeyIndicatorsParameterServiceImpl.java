package org.triber.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triber.analysis.dao.mapper.KeyIndicatorsParameterMapper;
import org.triber.analysis.service.KeyIndicatorsParameterService;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/29 15:37
 * @Description
 */
@Service
public class KeyIndicatorsParameterServiceImpl implements KeyIndicatorsParameterService {

    @Autowired
    private KeyIndicatorsParameterMapper parameterMapper;

    @Override
    public int getKeyIndicatorsDataTotal(Map<String, Object> map) {
        return parameterMapper.getKeyIndicatorsDataTotal(map);
    }

    @Override
    public List<Map<String, Object>> getKeyIndicatorsData(Map<String, Object> map) {
        return parameterMapper.getKeyIndicatorsData(map);
    }

    @Override
    public void updateKeyIndicatorsData(Map<String, Object> map) {
        parameterMapper.updateKeyIndicatorsData(map);
    }
}
