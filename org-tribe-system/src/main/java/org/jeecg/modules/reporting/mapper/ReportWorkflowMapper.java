package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportTask;

import java.sql.Date;
import java.util.List;

public interface ReportWorkflowMapper {
    ReportTask findLatestTask(@Param("batchId") String batchId, @Param("taskType") String taskType);
    List<ReportFile> findBatchFiles(@Param("batchId") String batchId);
    int countExternalRunningProcess();
    int countInternalRunningProcess(@Param("accountingPeriod") Date accountingPeriod,
                                    @Param("treasuryScope") String treasuryScope);
    int insertExternalProcessLog(@Param("id") String id, @Param("processName") String processName,
                                 @Param("accountingPeriod") Date accountingPeriod,
                                 @Param("userId") String userId, @Param("addTime") java.util.Date addTime);
    void callReportProcedure(@Param("accountingPeriod") Date accountingPeriod);
}
