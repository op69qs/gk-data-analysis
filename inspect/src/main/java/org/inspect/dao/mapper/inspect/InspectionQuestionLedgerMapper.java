// InspectionQuestionLedgerMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionQuestionLedgerMapper {

    /**
     * 获取问题描述信息
     * @param params
     * @return
     */
    List<Map<String, String>> getQuestionContentInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据当前任务ID获取台账问题清单一级
     * @return
     */
    List<Map<String, String>> getLedgerLv_1ById(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据当前任务ID获取台账问题清单二级
     * @return
     */
    List<Map<String, String>> getLedgerLv_2ById(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据当前任务ID问题分类ID获取问题描述及整改方式信息
     * @return
     */
    List<Map<String, String>> getQuestionLedgerById(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据当前任务ID获取末级台账问题清单制度信息
     * @return
     */
    List<Map<String, String>> getRuleById(@Param(value = "params")Map<String, Object> params);

    /**
     * 问题台账问题类型树形
     * @return
     */
    List<Map<String, String>> getQuestionBankTreeForQuestionLedger(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据任务ID获取检查分类
     * @param params
     * @return
     */
    String getQuestionTypeByTaskId(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据用户ID任务ID获取问题台账
     * @param params params.TASK_ID 当前检查任务ID
     *               params.ADD_USERID 添加人ID
     * @return
     */
    List<Map<String, String>> getQuestionLedgerByUserIdTaskID(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据检查任务ID获取该台账添加人
     * @param params params.TASK_ID 检查任务ID
     * @return
     */
    List<Map<String, String>> getLedgerAddUserByTaskId(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据台账ID获取台账信息
     * @param params params.LEDGER_ID 问题台账ID
     * @return
     */
    List<Map<String, String>> getQuestionLedgerByLedgerID(@Param(value = "params")Map<String, Object> params);

    /**
     * 问题台账新增
     * @param params
     */
    void addQuestionLedger(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据问题台账ID编辑
     * @param params
     */
    void editQuestionLedgerByLedgerID(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据台账ID删除
     * @param params
     */
    void delQuestionLedgerByLedgerId(@Param(value = "params")Map<String, Object> params);

    /**
     * 新增问题台账制度依据
     * @param params
     */
    void addQuestionLedgerRule(@Param(value = "params")Map<String, Object> params);

    /**
     * 编辑问题台账制度依据
     * @param params
     */
    void editQuestionLedgerRule(@Param(value = "params")Map<String, Object> params);

    /**
     * 删除问题台账制度依据
     * @param params
     */
    void delQuestionLedgerRule(@Param(value = "params")Map<String, Object> params);

    /**
     * 获取整改意见
     * @param params
     */
    List<Map<String, String>> getQuestionOpinionById(@Param(value = "params")Map<String, Object> params);

}///:~
