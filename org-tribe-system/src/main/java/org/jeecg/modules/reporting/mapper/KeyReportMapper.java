package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.parser.KeyReportRecord;

import java.util.List;

public interface KeyReportMapper {
    int deleteIncomeByZipName(@Param("zipName") String zipName);
    int insertIncome(@Param("rows") List<KeyReportRecord> rows);

    int deletePayoutByZipName(@Param("zipName") String zipName);
    int insertPayout(@Param("rows") List<KeyReportRecord> rows);

    int deleteStockByZipName(@Param("zipName") String zipName);
    int insertStock(@Param("rows") List<KeyReportRecord> rows);

    int deleteBackByZipName(@Param("zipName") String zipName);
    int insertBack(@Param("rows") List<KeyReportRecord> rows);
}
