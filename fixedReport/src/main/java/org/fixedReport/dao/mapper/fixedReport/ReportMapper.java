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
public interface ReportMapper {

    /**
     * 获取生成报告的报表参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableParams(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getTableParams2(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getTableParams3(@Param(value = "params")PageData  params);

    /**
     * 获取生成报告的图形参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getGraphParams(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getGraphParams2(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getGraphParams3(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getGraphParams4(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getGraphParams5(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getAreaParams(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getNumParams(@Param(value = "params")PageData  params);
    List<Map<String, Object>> getAreaReport(@Param(value = "params")PageData  params);
    void insertFact(@Param(value = "params")Map  params);
    void updateEntityReport(@Param(value = "params")Map  params);

    /**
     * 获取生成报告的文本及参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTextAndParams(@Param(value = "params")PageData  params);
    Map<String, Object> getMonthlyReport(@Param(value = "params")PageData  params);
    Map<String, Object> getHtmlReport(@Param(value = "params")PageData  params);
}
