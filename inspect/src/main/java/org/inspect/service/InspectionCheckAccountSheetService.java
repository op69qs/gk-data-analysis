// InspectionCheckAccountSheetService.java

package org.inspect.service;

import java.util.List;
import java.util.Map;

/**
 * 对账登记表
 * @author Created by Samer on 2019/10/21.
 */
public interface InspectionCheckAccountSheetService {

    /**
     * 获取子表信息
     * @return
     */
    List<Map<String, String>> getSheetSubInfo(Map<String, Object> params);

    /**
     * 根据登记表名查找表名
     * @return
     */
    List<Map<String, String>> getSheetNameByName(Map<String, Object> params);

    /**
     * 根据被查库ID任务ID查找对账单位ID
     * @return
     */
    List<Map<String, String>> getTaxOrgIdById(Map<String, Object> params);

    /**
     * 获取征收机构信息
     * @return
     */
    List<Map<String, String>> getTaxOrgInfo(Map<String, Object> params);

    /**
     * 根据当前任务ID获取核算主体包含的国库
     * @return
     */
    List<Map<String, String>> getCurTreCodeByTaskId(Map<String, Object> params);

    /**
     * 根据任务ID获取对账登记表信息
     * @return
     */
    List<Map<String, String>> getCheckAccInfoByTaskId(Map<String, Object> params);

    /**
     * 根据对账登记表ID获取详细信息
     * @return
     */
    List<Map<String, Object>> getCheckAccSubInfoBySheetId(Map<String, Object> params);

    /**
     * 新增对账登记表信息
     * @return
     */
    void addCheckAccountInfo(Map<String, Object> params);

    /**
     * 根据SHEET_ID更新对账登记表信息
     * @return
     */
    void updateCheckAccountInfo(Map<String, Object> params);

    /**
     * 新增对账登记表子表内容
     * @return
     */
    void addCheckAccSubInfo(Map<String, Object> params);

    /**
     * 根据SHEET_ID删除对账登记表信息
     * @return
     */
    void delCheckAccInfoBySheetId(Map<String, Object> params);

    /**
     * 根据SHEET_ID删除对账登记表子表内容
     * @return
     */
    void delCheckAccSubBySheetId(Map<String, Object> params);

}///:~
