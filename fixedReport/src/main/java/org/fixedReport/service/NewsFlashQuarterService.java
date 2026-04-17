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
public interface NewsFlashQuarterService {
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
    List<Map<String, Object>> getTableParams4(PageData params);

    /**
     * 获取生成报告的图形参数信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getGraphParams(PageData  params);
    List<Map<String, Object>> getGraphParams2(PageData  params);
    List<Map<String, Object>> getGraphParams3(PageData  params);
    List<Map<String, Object>> getGraphParams4(PageData  params);
    List<Map<String, Object>> getGraphParams5(PageData  params);
    List<Map<String, Object>> getGraphParams6(PageData  params);
}
