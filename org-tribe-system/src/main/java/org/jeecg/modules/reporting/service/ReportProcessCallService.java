package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportProcessCall;
import org.jeecg.modules.reporting.mapper.ReportProcessCallMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.UUID;

@Service
public class ReportProcessCallService {
    public static final String PROCEDURE_NAME = "adm.p_guoku_lib_report_all";

    private final ReportWorkflowMapper workflowMapper;
    private final ReportProcessCallMapper callMapper;

    public ReportProcessCallService(ReportWorkflowMapper workflowMapper, ReportProcessCallMapper callMapper) {
        this.workflowMapper = workflowMapper;
        this.callMapper = callMapper;
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public ReportProcessCall callForBatch(ReportBatch batch, String taskId,
                                          String userId, String username) {
        if (batch == null || batch.getAccountingPeriod() == null) {
            throw new IllegalArgumentException("批次账期不能为空");
        }
        Date accountingPeriod = new Date(batch.getAccountingPeriod().getTime());
        String scope = batch.getTreasuryCode();
        if (workflowMapper.countExternalRunningProcess() > 0
                || workflowMapper.countInternalRunningProcess(accountingPeriod, scope) > 0) {
            throw new ReportProcessBusyException("数据加工正在执行，暂时无法重新调用");
        }

        java.util.Date now = new java.util.Date();
        String externalLogId = uuid();
        ReportProcessCall call = new ReportProcessCall();
        call.setId(uuid());
        call.setBatchId(batch.getId());
        call.setTaskId(taskId);
        call.setAccountingPeriod(accountingPeriod);
        call.setTreasuryScope(scope);
        call.setProcedureName(PROCEDURE_NAME);
        call.setProcedureArgument(accountingPeriod.toString());
        call.setStatus("PROCESSING");
        call.setAttemptNo(1);
        call.setExternalLogId(externalLogId);
        call.setRequestSummary("按批次账期调用 " + PROCEDURE_NAME);
        call.setStartedTime(now);
        call.setCreateBy(username);
        call.setCreateTime(now);
        callMapper.insert(call);

        try {
            workflowMapper.insertExternalProcessLog(externalLogId, PROCEDURE_NAME,
                    accountingPeriod, userId, now);
            workflowMapper.callReportProcedure(accountingPeriod);
            java.util.Date ended = new java.util.Date();
            call.setStatus("SUCCEEDED");
            call.setResultSummary("数据加工调用完成");
            call.setEndedTime(ended);
            call.setDurationMs(ended.getTime() - now.getTime());
            callMapper.updateById(call);
            return call;
        } catch (RuntimeException exception) {
            java.util.Date ended = new java.util.Date();
            call.setStatus("FAILED");
            call.setErrorMessage(exception.getMessage());
            call.setEndedTime(ended);
            call.setDurationMs(ended.getTime() - now.getTime());
            callMapper.updateById(call);
            throw exception;
        }
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
