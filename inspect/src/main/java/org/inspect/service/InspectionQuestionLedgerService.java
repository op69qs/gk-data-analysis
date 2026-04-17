// InspectionQuestionLedgerService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账服务接口
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionQuestionLedgerService {

    /*问题台账内容填充*/
    Map<String, Object> assembleContent(PageData pd);

    /*问题台账内容填充HTML*/
    Map<String, Object> assembleContent_html(PageData pd);

    /*问题台账问题描述与制度依据拼接*/
    List<Map<String, Object>> assembleContentAndRule(PageData pd);

    /**
     * 问题台账问题类型树形
     * @return
     */
    List<Map<String, String>> getQuestionBankTreeForQuestionLedger(Map<String, Object> params);

    /**
     * 根据任务ID获取检查分类
     * @param params
     * @return
     */
    String getQuestionTypeByTaskId(Map<String, Object> params);

    /**
     * 根据用户ID任务ID获取问题台账
     * @param params params.TASK_ID 当前检查任务ID
     *               params.ADD_USERID 添加人ID
     * @return
     */
    List<Map<String, String>> getQuestionLedgerByUserIdTaskID(Map<String, Object> params);

    /**
     * 根据检查任务ID获取该台账添加人
     * @param params params.TASK_ID 检查任务ID
     * @return
     */
    List<Map<String, String>> getLedgerAddUserByTaskId(Map<String, Object> params);

    /**
     * 根据台账ID获取台账信息
     * @param params params.LEDGER_ID 问题台账ID
     * @return
     */
    List<Map<String, String>> getQuestionLedgerByLedgerID(Map<String, Object> params);

    /**
     * 问题台账新增
     * @param params
     */
    void addQuestionLedger(Map<String, Object> params);

    /**
     * 根据问题台账ID编辑
     * @param params
     */
    void editQuestionLedgerByLedgerID(Map<String, Object> params);

    /**
     * 根据台账ID删除
     * @param params
     */
    void delQuestionLedgerByLedgerId(Map<String, Object> params);

    /**
     * 新增问题台账制度依据
     * @param params
     */
    void addQuestionLedgerRule(Map<String, Object> params);

    /**
     * 编辑问题台账制度依据
     * @param params
     */
    void editQuestionLedgerRule(Map<String, Object> params);

    /**
     * 删除问题台账制度依据
     * @param params
     */
    void delQuestionLedgerRule(Map<String, Object> params);

    /**
     * 根据当前任务ID获取末级台账问题清单制度信息
     * @return
     */
    List<Map<String, String>> getRuleById(Map<String, Object> params);

    /**
     * 获取整改意见
     * @param params
     */
    List<Map<String, String>> getQuestionOpinionById(Map<String, Object> params);


} ///:~
