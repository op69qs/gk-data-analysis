package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;

import java.util.List;
import java.util.Map;

public interface ReportingBusinessMapper {
    List<Map<String, Object>> queryKeyMonitoring(@Param("q") ReportingBusinessQuery query);
    long countKeyMonitoring(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryTimsMonitoring(@Param("q") ReportingBusinessQuery query);
    long countTimsMonitoring(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryTreasuries(@Param("guokuId") String guokuId);
}
