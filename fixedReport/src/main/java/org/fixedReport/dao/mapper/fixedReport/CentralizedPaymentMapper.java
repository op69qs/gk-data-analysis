package org.fixedReport.dao.mapper.fixedReport;

import org.apache.ibatis.annotations.Param;
import org.fixedReport.util.PageData;

import java.util.List;
import java.util.Map;

public interface CentralizedPaymentMapper {
    List<Map<String,Object>> getData(@Param("params") PageData pd);

    List<Map<String,Object>> getDataAll(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    String getSum(@Param("params") PageData pd);

    List<Map<String,Object>> getBackData(@Param("params") PageData pd);

    List<Map<String,Object>> getBackDataAll(@Param("params") PageData pd);

    Integer getBackCount(@Param("params") PageData pd);

    String getBackSum(@Param("params") PageData pd);

    List<Map<String,Object>> getBudgetUnit(@Param("params") PageData pd);

    List<Map<String,Object>> getAgentBankClass(@Param("params") PageData pd);

    List<Map<String,Object>> getGuoKu(@Param("params") PageData pd);

    List<Map<String,Object>> getKeMu(@Param("params") PageData pd);

    List<Map<String,Object>> getKeMu2(@Param("params") PageData pd);

}
