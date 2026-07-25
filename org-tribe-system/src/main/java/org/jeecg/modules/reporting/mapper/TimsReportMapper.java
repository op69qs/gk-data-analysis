package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.parser.TimsReportRecord;

import java.sql.Date;
import java.util.List;

public interface TimsReportMapper {
    int deleteTimsIncome(@Param("dates") List<Date> dates, @Param("treCodes") List<String> treCodes);
    int insertTimsIncome(@Param("rows") List<TimsReportRecord> rows);
    int deleteTimsPayout(@Param("dates") List<Date> dates, @Param("treCodes") List<String> treCodes);
    int insertTimsPayout(@Param("rows") List<TimsReportRecord> rows);
    int deleteTimsStock(@Param("dates") List<Date> dates, @Param("treCodes") List<String> treCodes);
    int insertTimsStock(@Param("rows") List<TimsReportRecord> rows);

    int deleteStgIncome(@Param("periodKey") String periodKey);
    int insertStgIncome(@Param("rows") List<TimsReportRecord> rows,
                        @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
    int deleteStgPayout(@Param("periodKey") String periodKey);
    int insertStgPayout(@Param("rows") List<TimsReportRecord> rows,
                        @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
    int deleteStgStock(@Param("periodKey") String periodKey);
    int insertStgStock(@Param("rows") List<TimsReportRecord> rows,
                       @Param("periodKey") String periodKey, @Param("batchDate") String batchDate);
}
