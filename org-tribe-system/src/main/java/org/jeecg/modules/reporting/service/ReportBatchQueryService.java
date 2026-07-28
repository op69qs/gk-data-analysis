package org.jeecg.modules.reporting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportParseError;
import org.jeecg.modules.reporting.entity.ReportProcessCall;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportParseErrorMapper;
import org.jeecg.modules.reporting.mapper.ReportProcessCallMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.jeecg.modules.reporting.vo.ReportBatchDetail;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ReportBatchQueryService {
    private final ReportBatchMapper batchMapper;
    private final ReportFileMapper fileMapper;
    private final ReportTaskMapper taskMapper;
    private final ReportTaskLogMapper logMapper;
    private final ReportParseErrorMapper errorMapper;
    private final ReportProcessCallMapper callMapper;
    private LegacyPendingService legacyPendingService;
    private ReportWorkflowMapper workflowMapper;

    public ReportBatchQueryService(ReportBatchMapper batchMapper, ReportFileMapper fileMapper,
                                   ReportTaskMapper taskMapper, ReportTaskLogMapper logMapper,
                                   ReportParseErrorMapper errorMapper, ReportProcessCallMapper callMapper) {
        this.batchMapper = batchMapper;
        this.fileMapper = fileMapper;
        this.taskMapper = taskMapper;
        this.logMapper = logMapper;
        this.errorMapper = errorMapper;
        this.callMapper = callMapper;
    }

    @Autowired(required = false)
    public void setLegacyPendingService(LegacyPendingService legacyPendingService) {
        this.legacyPendingService = legacyPendingService;
    }

    @Autowired(required = false)
    public void setWorkflowMapper(ReportWorkflowMapper workflowMapper) {
        this.workflowMapper = workflowMapper;
    }

    public IPage<ReportBatch> page(int pageNo, int pageSize, String sourceDomain,
                                   String businessType, String status, String accountingPeriod,
                                   String treasuryCode, String fileName) {
        return page(pageNo, pageSize, sourceDomain, businessType, status, accountingPeriod,
                treasuryCode, fileName, null);
    }

    public IPage<ReportBatch> page(int pageNo, int pageSize, String sourceDomain,
                                   String businessType, String status, String accountingPeriod,
                                   String treasuryCode, String fileName, String allowedTreasuryPrefix) {
        QueryWrapper<ReportBatch> query = new QueryWrapper<ReportBatch>()
                .eq("del_flag", 0)
                .orderByDesc("create_time");
        if (hasText(allowedTreasuryPrefix)) query.likeRight("treasury_code", allowedTreasuryPrefix.trim());
        if (hasText(sourceDomain)) query.eq("source_domain", sourceDomain.toUpperCase());
        if (hasText(businessType)) query.eq("business_type", businessType.toUpperCase());
        if (hasText(status)) query.eq("status", status.toUpperCase());
        if (hasText(accountingPeriod)) query.eq("accounting_period", ReportTaskService.monthEnd(accountingPeriod));
        if (hasText(treasuryCode)) query.like("treasury_code", treasuryCode.trim());
        if (hasText(fileName)) query.like("original_file_name", fileName.trim());
        int safePage = Math.max(1, pageNo);
        int safeSize = Math.min(100, Math.max(1, pageSize));
        return batchMapper.selectPage(new Page<>(safePage, safeSize), query);
    }

    public ReportBatchDetail detail(String batchId) {
        ReportBatch batch = requireBatch(batchId, false);
        ReportBatchDetail detail = new ReportBatchDetail();
        detail.setBatch(batch);
        detail.setFiles(fileMapper.selectList(new QueryWrapper<ReportFile>()
                .eq("batch_id", batchId).eq("del_flag", 0).orderByAsc("create_time")));
        detail.setTasks(taskMapper.selectList(new QueryWrapper<ReportTask>()
                .eq("batch_id", batchId).orderByAsc("sequence_no", "attempt_no", "create_time")));
        detail.setTimeline(logMapper.selectList(new QueryWrapper<ReportTaskLog>()
                .eq("batch_id", batchId).orderByAsc("event_time")));
        detail.setParseErrors(errorMapper.selectList(new QueryWrapper<ReportParseError>()
                .eq("batch_id", batchId).orderByAsc("file_id", "row_number")));
        detail.setProcessCalls(callMapper.selectList(new QueryWrapper<ReportProcessCall>()
                .eq("batch_id", batchId).orderByAsc("create_time")));
        return detail;
    }

    @Transactional
    public void logicalDelete(String batchId, String username) {
        ReportBatch batch = workflowMapper == null ? requireBatch(batchId, true)
                : workflowMapper.findBatchForUpdate(batchId);
        if (batch == null || Integer.valueOf(1).equals(batch.getDelFlag())) {
            throw new IllegalArgumentException("上报批次不存在或已删除");
        }
        Integer activeTasks = taskMapper.selectCount(new QueryWrapper<ReportTask>()
                .eq("batch_id", batchId).in("status", "QUEUED", "PROCESSING"));
        if ("PROCESSING".equals(batch.getStatus()) || "QUEUED".equals(batch.getStatus())
                || (activeTasks != null && activeTasks > 0)) {
            throw new IllegalStateException("正在执行的上报批次不能删除，请等待完成后再操作");
        }
        if (legacyPendingService != null) legacyPendingService.delete(batch);
        batch.setDelFlag(1);
        batch.setStatus("LOGICALLY_DELETED");
        batch.setUpdateBy(username);
        batch.setUpdateTime(new Date());
        batchMapper.updateById(batch);
    }

    private ReportBatch requireBatch(String batchId, boolean includeDeleted) {
        ReportBatch batch = batchMapper.selectById(batchId);
        if (batch == null || (!includeDeleted && Integer.valueOf(1).equals(batch.getDelFlag()))) {
            throw new IllegalArgumentException("上报批次不存在");
        }
        return batch;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
