// InspectionSupplementMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/3/4.
 */
public interface InspectionSupplementMapper {

    /**
     * 获取检查记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getSupplementLedgerInfo(@Param(value = "params")PageData params);

    /**
     * 编辑检查补录信息
     * @param params
     * @return
     */
    void editSupplementTask(@Param(value = "params")PageData params);

    /**
     * 获取检查补录任务信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getSupplementTask(@Param(value = "params")PageData params);

    /**
     * 获取检查补录任务信息
     * @param params
     * @return
     */
    int getSupplementTaskCount(@Param(value = "params")PageData params);

    /**
     * 新增检查补录信息
     * @param params
     * @return
     */
    void addSupplementTask(@Param(value = "params")PageData params);

    /**
     * 删除检查补录信息
     * @param params
     * @return
     */
    void delSupplementTask(@Param(value = "params")PageData params);

    /**
     * 新增检查补录台账
     * @param params
     * @return
     */
    void addSupplementLedger(@Param(value = "params")PageData params);

    /**
     * 新增制度依据
     * @param params
     * @return
     */
    void addSupplementRule(@Param(value = "params")PageData params);

    /**
     * 根据问题台账ID编辑
     * @param params
     * @return
     */
    void editSupplementLedgerById(@Param(value = "params")PageData params);


    /**
     * 根据台账ID删除
     * @param params
     * @return
     */
    void delSupplementLedgerById(@Param(value = "params")PageData params);

    /**
     * 删除问题台账制度依据
     * @param params
     * @return
     */
    void delSupplementRule(@Param(value = "params")PageData params);

}///:~
