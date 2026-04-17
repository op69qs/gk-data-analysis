// InspectionTaskIssueListServiceImpl.java

package org.inspect.service.impl;


import org.inspect.dao.mapper.inspect.InspectionIssueListMapper;
import org.inspect.service.InspectionIssueListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 问题清单
 * @author Created by Samer on 2019/10/24.
 */
@Service
public class InspectionIssueListServiceImpl implements InspectionIssueListService {

    @Autowired
    private InspectionIssueListMapper inspectionIssueListMapper;

    /**
     * 获取当前任务组组员信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getTaskGroupUserInfo(Map<String, Object> params){
        return inspectionIssueListMapper.getTaskGroupUserInfo(params);
    }

    /**
     * 根据ID更新问题清单责任热
     * @param params
     * @return
     */
    @Override
    public void updateIssueListById(Map<String, Object> params){
        inspectionIssueListMapper.updateIssueListById(params);
    }

    /**
     * 根据问题清单ID获取问题清单信息
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getIssueListById(Map<String, Object> params){
        return inspectionIssueListMapper.getIssueListById(params);
    }

    /**
     * 根据任务ID获取问题清单部门负责人
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getDepartmentHeadByTaskId(Map<String, Object> params){
        return inspectionIssueListMapper.getDepartmentHeadByTaskId(params);
    }

    /**
     * 根据当前任务ID获取台账问题清单一级二级
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getIssueLedgerLvById(Map<String, Object> params) {
        return inspectionIssueListMapper.getIssueLedgerLvById(params);
    }

    /**
     * 根据当前任务ID获取末级台账问题清单制度及整改方式信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getIssueRuleOpinionById(Map<String, Object> params) {
        return inspectionIssueListMapper.getIssueRuleOpinionById(params);
    }

    /**
     * 新增问题清单
     * @param params
     */
    @Override
    public void addIssueList(Map<String, Object> params) {
        inspectionIssueListMapper.addIssueList(params);
    }

    /**
     * 新增国债业务实地检查记录表
     * @param params
     * @return
     */
    @Override
    public void addDebtRecordIssue(Map<String, Object> params){
        inspectionIssueListMapper.addDebtRecordIssue(params);
    }

    /**
     * 删除国债业务实地检查记录表
     * @param params
     * @return
     */
    @Override
    public void delDebtRecordIssue(Map<String, Object> params){
        inspectionIssueListMapper.delDebtRecordIssue(params);
    }

    /**
     * 获取国债业务实地检查记录表
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getDebtRecordIssue(Map<String, Object> params){
        return inspectionIssueListMapper.getDebtRecordIssue(params);
    }


} ///:~
