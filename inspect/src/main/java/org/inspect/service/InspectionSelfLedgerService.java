// InspectionSelfLedgerService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 自查问题台账
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionSelfLedgerService {

    /**
     * 根据任务ID获取检查分类
     * @param params
     * @return
     */
    Map<String, String> getTaskInfoById(PageData params);

    /**
     * 获取国库信息
     * @param params
     * @return
     */
    Map<String, String> getGuokuInfo(PageData params);

    /**
     * 根据用户ID任务ID获取问题台账
     * @param params
     * @return
     */
    List<Map<String, String>> getSelfLedgerByUserIdTaskID(PageData params);

    /**
     * 根据检查任务ID获取该台账添加人
     * @param params
     * @return
     */
    List<Map<String, String>> getLedgerAddUserByTaskId(PageData params);

    /**
     * 根据台账ID获取台账信息
     * @param params
     * @return
     */
    List<Map<String, String>> getSelfLedgerByLedgerID(PageData params);

    /**
     * 问题台账新增
     * @param params
     * @return
     */
    void addSelfLedger(PageData params);

    /**
     * 根据问题台账ID编辑
     * @param params
     * @return
     */
    void editSelfLedgerByLedgerID(PageData params);

    /**
     * 根据台账ID删除
     * @param params
     * @return
     */
    void delSelfLedgerByLedgerId(PageData params);

    /**
     * 新增问题台账制度依据
     * @param params
     */
    void addQuestionLedgerRule(Map<String, Object> params);

    /**
     * 删除问题台账制度依据
     * @param params
     */
    void delQuestionLedgerRule(Map<String, Object> params);


} ///:~
