// InspectionPostSVListMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 事后监督交接清单
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionPostSVListMapper {

    /**
     * 获取任务信息
     * @param params
     */
    Map<String, String> getTaskInfoByTaskId(@Param(value = "params") PageData params);

    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getTemplateInfo(@Param(value = "params") PageData params);

    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getMainInfo(@Param(value = "params") PageData params);

    /**
     * 获取子表信息
     * @param params
     */
    List<Map<String, Object>> getSubSheet(@Param(value = "params") PageData params);

    /**
     * 获取全部记录
     * @param params
     */
    List<Map<String, Object>> getFullInfo(@Param(value = "params") PageData params);

    /**
     * 修改主表信息
     * @param params
     */
    void editMainInfo(@Param(value = "params") PageData params);

    /**
     * 新增主表信息
     * @param params
     */
    void addMainInfo(@Param(value = "params") PageData params);

    /**
     * 删除主表信息
     * @param params
     */
    void deleteMainInfo(@Param(value = "params") PageData params);

    /**
     * 新增子表信息
     * @param params
     */
    void addSubInfo(@Param(value = "params") PageData params);

    /**
     * 删除子表信息
     * @param params
     */
    void delSubInfo(@Param(value = "params") PageData params);

}///:~
