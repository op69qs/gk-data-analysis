package org.fixedReport.service;

import org.fixedReport.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Created by dang on 2020/05/12
 */
@Service
public interface NewsFlashService {
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
    List<Map<String, Object>> getTableParams2(PageData params);
    List<Map<String, Object>> getTableParams3(PageData params);
    void addEntityReport(PageData pd);
    boolean getNameIsExist(PageData pd);
    void delEntityReport(PageData pd);
    void delEntityReportFact(PageData pd);
    void editEntityReport(PageData pd);
    void insertFact(Map  params);

}
