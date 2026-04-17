// InspecitonRegisterBookService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 例行检查-检查登记簿
 * @author Created by Samer on 2019/10/16.
 */
public interface InspectionRegisterBookService {

    /**
     * 组装整改台账
     * @param pd 查询条件
     * @param headStr 格式化前缀
     * @param endStr  格式化后缀
     * @param queryType 判断页面组装或者导出文件
     *                  "html" 页面
     *                  "doc"  导出文件
     * @return
     */
    String assembleReformScheme(PageData pd, String headStr, String endStr, String queryType);

    /**
     * 组装检查内容
     * @param pd 查询条件
     * @param headStr 格式化前缀
     * @param endStr  格式化后缀
     * @return
     */
    String assembleInspectionContent(PageData pd, String headStr, String endStr);

    /**
     * 获取被查库信息
     * @param params
     */
    Map<String, String> getTreInfoByTaskId(PageData params);

    /**
     * 获取登记簿信息
     * @param params
     */
    Map<String, Object> getRegisterBookInfo(PageData params);

    /**
     * 例行检查登记簿新增
     * @param params
     */
    void addRegisterBook(PageData params);

    /**
     * 编辑例行检查登记簿
     * @param params
     */
    void updateRegisterBook(PageData params);

}///:~
