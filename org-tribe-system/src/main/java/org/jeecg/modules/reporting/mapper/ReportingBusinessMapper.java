package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.vo.ReportChangeCommand;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingBusinessMapper {
    List<Map<String, Object>> queryKeyMonitoring(@Param("q") ReportingBusinessQuery query);
    long countKeyMonitoring(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryTimsMonitoring(@Param("q") ReportingBusinessQuery query);
    long countTimsMonitoring(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryIncome(@Param("q") ReportingBusinessQuery query);
    long countIncome(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryPayout(@Param("q") ReportingBusinessQuery query);
    long countPayout(@Param("q") ReportingBusinessQuery query);
    BigDecimal findIncomeAmount(@Param("q") ReportingBusinessQuery query);
    BigDecimal findPayoutAmount(@Param("q") ReportingBusinessQuery query);
    int insertChange(@Param("record") ReportChangeCommand record);
    List<Map<String, Object>> queryChanges(@Param("q") ReportingBusinessQuery query);
    long countChanges(@Param("q") ReportingBusinessQuery query);
    List<Map<String, Object>> queryTreasuries(@Param("guokuId") String guokuId);
}
