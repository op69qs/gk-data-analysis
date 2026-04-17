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
public interface KydReportMapper {

    /**
     * 分行业
     * @param pd
     * @return
     */
    List<Map<String, Object>> getIndustryReportAll(@Param("params") PageData pd);
    Integer countIndustryReportAll(@Param("params") PageData pd);

    /**
     * 分企业
     * @param pd
     * @return
     */
    List<Map<String, Object>> getEnterpriseReportAll(@Param("params") PageData pd);
    Integer countEnterpriseReportAll(@Param("params") PageData pd);

    /**
     * 企业排名
     * @param pd
     * @return
     */
    List<Map<String, Object>> getEnterpriseRankingReportAll(@Param("params") PageData pd);
    List<Map<String, Object>> getIndustryDrop();
    Integer countEnterpriseRankingReportAll(@Param("params") PageData pd);
}
