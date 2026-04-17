// InspectionStatisticsTableServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionStatisticsTableMapper;
import org.inspect.service.InspectionStatisticsTableService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 被查国库统计表
 * @author Created by Samer on 2019/10/29.
 */
@Service
public class InspectionStatisticsTableServiceImpl implements InspectionStatisticsTableService{

    @Autowired
    private InspectionStatisticsTableMapper inspectionStatisticsTableMapper;

    /**
     * 查询当前任务问题台账中的一级问题分类
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getCurTaskQuestion_1(PageData params) {
        return inspectionStatisticsTableMapper.getCurTaskQuestion_1(params);
    }

    /**
     * 根据问题编码查询描述
     * @param QUESTION_ID
     * @return
     */
    @Override
    public String getQuestionDscrById(String QUESTION_ID) {
        return inspectionStatisticsTableMapper.getQuestionDscrById(QUESTION_ID);
    }

    /**
     * 获取被查国库统计表
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getStatisticsTable(PageData params) {
        return inspectionStatisticsTableMapper.getStatisticsTable(params);
    }

    /**
     * 检查统计表同步过程调用
     * @param params
     */
    @Override
    public void callStatisticsTableSYNC_TYPE(PageData params) {
        inspectionStatisticsTableMapper.callStatisticsTableSYNC_TYPE(params);
    }
} ///:~
