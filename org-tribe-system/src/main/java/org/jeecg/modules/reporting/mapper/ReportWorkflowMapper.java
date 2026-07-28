package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportBatch;

import java.sql.Date;
import java.util.List;

public interface ReportWorkflowMapper {
    ReportTask findLatestTask(@Param("batchId") String batchId, @Param("taskType") String taskType);
    ReportBatch findBatchForUpdate(@Param("batchId") String batchId);
    int countActiveTasks(@Param("batchId") String batchId);
    int claimTask(@Param("taskId") String taskId, @Param("startedTime") java.util.Date startedTime,
                  @Param("username") String username, @Param("leaseOwner") String leaseOwner,
                  @Param("leaseUntil") java.util.Date leaseUntil);
    int renewAndLockOwnedTask(@Param("taskId") String taskId, @Param("leaseOwner") String leaseOwner,
                              @Param("leaseUntil") java.util.Date leaseUntil,
                              @Param("now") java.util.Date now);
    int updateOwnedTaskProgress(@Param("taskId") String taskId,
                                @Param("leaseOwner") String leaseOwner,
                                @Param("progressPercent") int progressPercent,
                                @Param("resultSummary") String resultSummary,
                                @Param("leaseUntil") java.util.Date leaseUntil,
                                @Param("now") java.util.Date now);
    int lockOwnedTask(@Param("taskId") String taskId, @Param("leaseOwner") String leaseOwner);
    int completeOwnedTask(@Param("record") ReportTask record,
                          @Param("leaseOwner") String leaseOwner);
    int updateBatchState(@Param("record") ReportBatch record);
    int requeueExpiredTasks(@Param("now") java.util.Date now);
    int failExpiredProcessTasks(@Param("now") java.util.Date now);
    int failBatchesWithExpiredProcessTasks();
    int failStaleProcessCalls(@Param("cutoff") java.util.Date cutoff);
    List<ReportTask> findQueuedTasks(@Param("limit") int limit);
    List<ReportFile> findBatchFiles(@Param("batchId") String batchId);
    int countExternalRunningProcess();
    int countInternalRunningProcess(@Param("accountingPeriod") Date accountingPeriod,
                                    @Param("treasuryScope") String treasuryScope);
    int nextProcessAttempt(@Param("batchId") String batchId);
    int insertExternalProcessLog(@Param("id") String id, @Param("processName") String processName,
                                 @Param("accountingPeriod") Date accountingPeriod,
                                 @Param("userId") String userId, @Param("addTime") java.util.Date addTime);
    void callReportProcedure(@Param("accountingPeriod") Date accountingPeriod);
}
