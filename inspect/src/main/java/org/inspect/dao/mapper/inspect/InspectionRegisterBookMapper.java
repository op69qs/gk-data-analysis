// InspectionRegisterBookMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 例行检查-检查登记簿
 * @author Created by Samer on 2019/10/16.
 */
public interface InspectionRegisterBookMapper {

    /**
     * 获取整改台账
     * @param params
     */
    Map<String, String> getReformScheme(@Param(value = "params") PageData params);

    /**
     * 获取整改问题描述
     * @param params
     */
    List<Map<String, String>> getReformLedgerContent(@Param(value = "params") PageData params);

    /**
     * 获取任务ID整改问题清单二级
     * @param params
     */
    List<Map<String, String>> getReformLedgerLv2(@Param(value = "params") PageData params);

    /**
     * 获取任务ID整改问题清单一级
     * @param params
     */
    List<Map<String, String>> getReformLedgerLv1(@Param(value = "params") PageData params);

    /**
     * 获取检查内容二级
     * @param params
     */
    List<Map<String, String>> getInspectionContentLv2(@Param(value = "params") PageData params);

    /**
     * 获取检查内容一级
     * @param params
     */
    List<Map<String, String>> getInspectionContentLv1(@Param(value = "params") PageData params);

    /**
     * 新增检查登记簿检查内容
     * @param params
     */
    Map<String, String> addInspectionContentRegBook(@Param(value = "params") PageData params);

    /**
     * 获取被查库信息
     * @param params
     */
    Map<String, String> getTreInfoByTaskId(@Param(value = "params") PageData params);

    /**
     * 获取登记簿信息
     * @param params
     */
    Map<String, Object> getRegisterBookInfo(@Param(value = "params") PageData params);

    /**
     * 例行检查登记簿新增
     * @param params
     */
    void addRegisterBook(@Param(value = "params") PageData params);

    /**
     * 编辑例行检查登记簿
     * @param params
     */
    void updateRegisterBook(@Param(value = "params") PageData params);

}///:~
