// InspectionTaskIssueListService.java

package org.inspect.service;

import java.util.List;
import java.util.Map;

/**
 * 问题清单
 * @author Created by Samer on 2019/10/24.
 */
public interface InspectionIssueListService {

    /**
     * 获取当前任务组组员信息
     * @param params
     * @return
     */
    List<Map<String, String>> getTaskGroupUserInfo(Map<String, Object> params);

    /**
     * 根据ID更新问题清单责任热
     * @param params
     * @return
     */
    void updateIssueListById(Map<String, Object> params);

    /**
     * 根据问题清单ID获取问题清单信息
     * @param params
     * @return
     */
    Map<String, String> getIssueListById(Map<String, Object> params);

    /**
     * 根据任务ID获取问题清单部门负责人
     * @param params
     * @return
     */
    Map<String, String> getDepartmentHeadByTaskId(Map<String, Object> params);

    /**
     * 根据当前任务ID获取台账问题清单一级二级
     * @param params
     * @return
     */
    List<Map<String, String>> getIssueLedgerLvById(Map<String, Object> params);

    /**
     * 根据当前任务ID获取末级台账问题清单制度及整改方式信息
     * @param params
     * @return
     */
    List<Map<String, String>> getIssueRuleOpinionById(Map<String, Object> params);

    /**
     * 新增问题清单
     * @param params
     * @return
     */
    void addIssueList(Map<String, Object> params);

    /**
     * 新增国债业务实地检查记录表
     * @param params
     * @return
     */
    void addDebtRecordIssue(Map<String, Object> params);

    /**
     * 删除国债业务实地检查记录表
     * @param params
     * @return
     */
    void delDebtRecordIssue(Map<String, Object> params);

    /**
     * 获取国债业务实地检查记录表
     * @param params
     * @return
     */
    List<Map<String, String>> getDebtRecordIssue(Map<String, Object> params);

}///:~
