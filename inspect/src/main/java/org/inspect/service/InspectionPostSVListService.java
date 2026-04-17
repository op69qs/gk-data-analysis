// InspectionPostSVListService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 事后监督交接清单
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionPostSVListService {

    /**
     * 获取任务信息
     * @param params
     */
    Map<String, String> getTaskInfoByTaskId(PageData params);


    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getTemplateInfo(PageData params);

    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getMainInfo(PageData params);

    /**
     * 获取子表信息
     * @param params
     */
    List<Map<String, Object>> getSubSheet(PageData params);

    /**
     * 获取全部记录
     * @param params
     */
    List<Map<String, Object>> getFullInfo(PageData params);

    /**
     * 修改主表信息
     * @param params
     */
    void editMainInfo(PageData params);

    /**
     * 新增主表信息
     * @param params
     */
    void addMainInfo(PageData params);

    /**
     * 删除主表信息
     * @param params
     */
    void deleteMainInfo(PageData params);

    /**
     * 新增子表信息
     * @param params
     */
    void addSubInfo(PageData params);

    /**
     * 删除子表信息
     * @param params
     */
    void delSubInfo(PageData params);
    

} ///:~
