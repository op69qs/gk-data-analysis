package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.parser.TimsReportRecord;

import java.util.List;

public interface TimsReportMapper {
    int deleteStgIncome(@Param("periodKey") String periodKey);
    int insertStgIncome(@Param("rows") List<TimsReportRecord> rows,
                        @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
    long countStgIncome(@Param("periodKey") String periodKey);
    int deleteStgPayout(@Param("periodKey") String periodKey);
    int insertStgPayout(@Param("rows") List<TimsReportRecord> rows,
                        @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
    long countStgPayout(@Param("periodKey") String periodKey);
    int deleteStgStock(@Param("periodKey") String periodKey);
    int insertStgStock(@Param("rows") List<TimsReportRecord> rows,
                       @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
    long countStgStock(@Param("periodKey") String periodKey);
}
