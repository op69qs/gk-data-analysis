// InspectionReportMapper.java

package org.fixedReport.dao.mapper.fixedReport;

import org.apache.ibatis.annotations.Param;
import org.fixedReport.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 数据核查
 * @author Created by dj on 2020/05/11.
 */
public interface NewsFlashMapper {
    /*获取检所有报表列表*/
    List<Map<String, Object>> getReportAll(@Param("params") PageData pd);
    void addEntityReport(@Param("params") PageData pd);
    int getNameIsExist(@Param("params") PageData pd);
    void editEntityReport(@Param("params") PageData pd);
    void delEntityReport(@Param("params") PageData pd);
    void delEntityReportFact(@Param("params") PageData pd);
    Integer countReportAll(@Param("params") PageData pd);
    /**
     * 获取生成报告的报表参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableParams(@Param(value = "params") PageData params);
    List<Map<String, Object>> getTableParams2(@Param(value = "params") PageData params);
    List<Map<String, Object>> getTableParams3(@Param(value = "params") PageData params);
    /**
     * 获取生成报告的文本及参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTextAndParams(@Param(value = "params") PageData params);
    void insertFact(@Param(value = "params")Map  params);
}
