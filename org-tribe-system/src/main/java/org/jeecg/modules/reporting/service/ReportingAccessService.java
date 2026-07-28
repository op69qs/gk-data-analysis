package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.springframework.stereotype.Service;

/** Enforces treasury data scope for every batch/file endpoint independently of button permissions. */
@Service
public class ReportingAccessService {
    private final ReportingUserScopeService userScopeService;
    private final ReportBatchMapper batchMapper;
    private final ReportFileMapper fileMapper;

    public ReportingAccessService(ReportingUserScopeService userScopeService,
                                  ReportBatchMapper batchMapper, ReportFileMapper fileMapper) {
        this.userScopeService = userScopeService;
        this.batchMapper = batchMapper;
        this.fileMapper = fileMapper;
    }

    public String requirePrefix(String username) {
        return userScopeService.requireTreasuryPrefix(username);
    }

    public ReportBatch requireBatch(String batchId, String username) {
        ReportBatch batch = batchMapper.selectById(batchId);
        if (batch == null || Integer.valueOf(1).equals(batch.getDelFlag())) {
            throw new IllegalArgumentException("上报批次不存在");
        }
        assertScope(batch.getTreasuryCode(), username);
        return batch;
    }

    public ReportFile requireFile(String fileId, String username) {
        ReportFile file = fileMapper.selectById(fileId);
        if (file == null) throw new IllegalArgumentException("上报文件不存在");
        requireBatch(file.getBatchId(), username);
        return file;
    }

    private void assertScope(String batchScope, String username) {
        String allowed = requirePrefix(username);
        if (batchScope == null || !batchScope.startsWith(allowed)) {
            throw new SecurityException("无权访问其他国库范围的上报数据");
        }
    }
}
