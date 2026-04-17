// InspectionWorkingPaperService.java

package org.inspect.service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/22.
 */
public interface InspectionWorkingPaperService {

    /**
     * 根据底稿ID获取底稿信息
     * @param params
     * @return
     */
    List<Map<String, String>> getWorkingPapersByPaperId(Map<String, Object> params);

    /**
     * 根据底稿ID更新底稿信息
     * @param params
     * @return
     */
    void updateWorkingPapersByPaperId(Map<String, Object> params);

    /**
     * 根据任务ID用户ID获取台账信息
     * @param params
     * @return
     */
    Map<String, String> getWorkingPapersByTaskIdUserId(Map<String, Object> params);

    /**
     * 获取当前用户姓名
     * @param params
     * @return
     */
    Map<String, String> getUserInfoByUserId(Map<String, Object> params);

    /**
     * 根据任务ID获取任务信息
     * @param params
     * @return
     */
    Map<String, String> getTaskInfoByTaskId(Map<String, Object> params);

    /**
     * 根据当前用户任务ID获取台账一级二级
     * @param params
     * @return
     */
    List<Map<String, String>> getQuestionLedgerLvById(Map<String, Object> params);

    /**
     * 根据当前用户任务ID获取末级台账制度及整改方式信息
     * @param params
     * @return
     */
    List<Map<String, String>> getRuleOpinionById(Map<String, Object> params);

    /**
     * 工作底稿新增
     * @param params
     * @return
     */
    void addWorkingPapers(Map<String, Object> params);

}///:~
