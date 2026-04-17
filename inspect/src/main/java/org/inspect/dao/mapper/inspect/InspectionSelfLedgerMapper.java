// InspectionSelfLedgerMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 自查问题台账
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionSelfLedgerMapper {

    /**
     * 根据任务ID获取检查分类
     * @param params
     * @return
     */
    Map<String, String> getTaskInfoById(@Param(value = "params")PageData params);

    /**
     * 获取国库信息
     * @param params
     * @return
     */
    Map<String, String> getGuokuInfo(@Param(value = "params")PageData params);

    /**
     * 根据用户ID任务ID获取问题台账
     * @param params
     * @return
     */
    List<Map<String, String>> getSelfLedgerByUserIdTaskID(@Param(value = "params")PageData params);

    /**
     * 根据检查任务ID获取该台账添加人
     * @param params
     * @return
     */
    List<Map<String, String>> getLedgerAddUserByTaskId(@Param(value = "params")PageData params);

    /**
     * 根据台账ID获取台账信息
     * @param params
     * @return
     */
    List<Map<String, String>> getSelfLedgerByLedgerID(@Param(value = "params")PageData params);

    /**
     * 问题台账新增
     * @param params
     * @return
     */
    void addSelfLedger(@Param(value = "params")PageData params);

    /**
     * 根据问题台账ID编辑
     * @param params
     * @return
     */
    void editSelfLedgerByLedgerID(@Param(value = "params")PageData params);

    /**
     * 根据台账ID删除
     * @param params
     * @return
     */
    void delSelfLedgerByLedgerId(@Param(value = "params")PageData params);

    /**
     * 新增问题台账制度依据
     * @param params
     */
    void addQuestionLedgerRule(@Param(value = "params")Map<String, Object> params);

    /**
     * 删除问题台账制度依据
     * @param params
     */
    void delQuestionLedgerRule(@Param(value = "params")Map<String, Object> params);

}///:~
