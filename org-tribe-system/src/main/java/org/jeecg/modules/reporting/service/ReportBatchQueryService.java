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
import org.jeecg.modules.reporting.vo.ReportBatchDetail;
import org.springframework.stereotype.Service;
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

    public IPage<ReportBatch> page(int pageNo, int pageSize, String sourceDomain,
                                   String businessType, String status, String accountingPeriod,
                                   String treasuryCode, String fileName) {
        QueryWrapper<ReportBatch> query = new QueryWrapper<ReportBatch>()
                .eq("del_flag", 0)
                .orderByDesc("create_time");
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
        ReportBatch batch = requireBatch(batchId, true);
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
