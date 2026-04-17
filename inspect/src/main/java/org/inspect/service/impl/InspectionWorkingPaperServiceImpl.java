// InspectionWorkingPaperServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionWorkingPaperMapper;
import org.inspect.service.InspectionWorkingPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/22.
 */
@Service
public class InspectionWorkingPaperServiceImpl implements InspectionWorkingPaperService {

    @Autowired
    private InspectionWorkingPaperMapper inspectionWorkingPaperMapper;

    /**
     * 根据底稿ID获取底稿信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getWorkingPapersByPaperId(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getWorkingPapersByPaperId(params);
    }

    @Override
    public void updateWorkingPapersByPaperId(Map<String, Object> params) {
        inspectionWorkingPaperMapper.updateWorkingPapersByPaperId(params);
    }

    /**
     * 根据任务ID用户ID获取台账信息
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getWorkingPapersByTaskIdUserId(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getWorkingPapersByTaskIdUserId(params);
    }

    /**
     * 获取当前用户姓名
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getUserInfoByUserId(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getUserInfoByUserId(params);
    }

    /**
     * 根据任务ID获取任务信息
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getTaskInfoByTaskId(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getTaskInfoByTaskId(params);
    }

    /**
     * 根据当前用户任务ID获取台账一级二级
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getQuestionLedgerLvById(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getQuestionLedgerLvById(params);
    }

    /**
     * 根据当前用户任务ID获取末级台账制度及整改方式信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getRuleOpinionById(Map<String, Object> params) {
        return inspectionWorkingPaperMapper.getRuleOpinionById(params);
    }

    /**
     * 工作底稿新增
     * @param params
     */
    @Override
    public void addWorkingPapers(Map<String, Object> params) {
        inspectionWorkingPaperMapper.addWorkingPapers(params);
    }

} ///:~
