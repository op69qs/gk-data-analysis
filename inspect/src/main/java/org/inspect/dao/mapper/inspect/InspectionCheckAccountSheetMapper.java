// InspectionCheckAccountSheetMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/18.
 */
public interface InspectionCheckAccountSheetMapper {

    /**
     * 获取子表信息
     * @return
     */
    List<Map<String, String>> getSheetSubInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据登记表名查找表名
     * @return
     */
    List<Map<String, String>> getSheetNameByName(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据被查库ID任务ID查找对账单位ID
     * @return
     */
    List<Map<String, String>> getTaxOrgIdById(@Param(value = "params")Map<String, Object> params);

    /**
     * 获取征收机构信息
     * @return
     */
    List<Map<String, String>> getTaxOrgInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据当前任务ID获取核算主体包含的国库
     * @return
     */
    List<Map<String, String>> getCurTreCodeByTaskId(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据任务ID获取对账登记表信息
     * @return
     */
    List<Map<String, String>> getCheckAccInfoByTaskId(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据对账登记表ID获取详细信息
     * @return
     */
    List<Map<String, Object>> getCheckAccSubInfoBySheetId(@Param(value = "params")Map<String, Object> params);

    /**
     * 新增对账登记表信息
     * @return
     */
    void addCheckAccountInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据SHEET_ID更新对账登记表信息
     * @return
     */
    void updateCheckAccountInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 新增对账登记表子表内容
     * @return
     */
    void addCheckAccSubInfo(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据SHEET_ID删除对账登记表信息
     * @return
     */
    void delCheckAccInfoBySheetId(@Param(value = "params")Map<String, Object> params);

    /**
     * 根据SHEET_ID删除对账登记表子表内容
     * @return
     */
    void delCheckAccSubBySheetId(@Param(value = "params")Map<String, Object> params);

}///:~
