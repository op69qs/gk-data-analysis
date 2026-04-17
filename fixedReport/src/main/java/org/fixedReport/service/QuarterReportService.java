package org.fixedReport.service;

import org.fixedReport.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface QuarterReportService {
    /*获取检所有报表列表*/
    List<Map<String, Object>> getReportAll(PageData pd);
    Integer countReportAll(PageData pd);
    /**
     * 获取生成报告的文本及参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTextAndParams(PageData params);
    /**
     * 获取生成报告的报表参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableParams(PageData params);
    /**
     * 获取生成报告的报表参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableParams2(PageData params);
    List<Map<String, Object>> getTableParams3(PageData params);
    List<Map<String, Object>> getTableParams4(PageData params);
    /**
     * 获取生成报告的图形参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getGraphParams(PageData params);
    List<Map<String, Object>> getGraphParams2(PageData params);
    List<Map<String, Object>> getGraphParams3(PageData params);
    List<Map<String, Object>> getGraphParams4(PageData params);
    List<Map<String, Object>> getGraphParams5(PageData params);
    List<Map<String, Object>> getGraphParams6(PageData params);
    List<Map<String, Object>> getGraphParams7(PageData params);
    List<Map<String, Object>> getGraphParams8(PageData params);
    List<Map<String, Object>> getGraphParams9(PageData params);
    List<Map<String, Object>> getGraphParams10(PageData params);
    void addEntityReport(PageData pd);
    void delEntityReport(PageData pd);
    void editEntityReport(PageData pd);

}
