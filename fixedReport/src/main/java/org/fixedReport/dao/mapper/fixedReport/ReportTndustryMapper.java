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
public interface ReportTndustryMapper {
    /*获取检所有报表列表*/
    List<Map<String, Object>> getTndustryTaxData(@Param("params") PageData pd);
    List<Map<String, Object>> getTndustryTaxDataAll(@Param("params") PageData pd);
    List<Map<String, Object>> getTndustryTaxData1(@Param("params") PageData pd);
    List<Map<String, Object>> getTndustryTaxData1All(@Param("params") PageData pd);
    List<Map<String, Object>> getAllSubject();
    List<Map<String, Object>> getTndustryTaxAll(@Param("params") PageData pd);
    List<Map<String, Object>> getColALL(@Param("params") PageData pd);
    List<Map<String, Object>> getColHeB(@Param("params") PageData pd);
    List<Map<String, Object>> getColHeBF(@Param("params") PageData pd);
    int countTndustryTaxData(@Param("params") PageData pd);
    int countTndustryTaxData1(@Param("params") PageData pd);
}
