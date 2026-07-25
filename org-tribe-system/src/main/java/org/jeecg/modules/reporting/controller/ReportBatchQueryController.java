package org.jeecg.modules.reporting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.service.ReportBatchQueryService;
import org.jeecg.modules.reporting.service.ReportTaskService;
import org.jeecg.modules.reporting.vo.ReportBatchDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeecg.modules.reporting.service.ReportingAccessService;

@Api(tags = "数据上报跟踪")
@RestController
@RequestMapping("/reporting/batches")
public class ReportBatchQueryController {
    public static final String RETRY_PERMISSION = "reporting:batch:retry";
    public static final String PROCESS_PERMISSION = "reporting:batch:process";
    public static final String DELETE_PERMISSION = "reporting:batch:delete";

    private final ReportBatchQueryService queryService;
    private final ReportTaskService taskService;
    private ReportingAccessService accessService;

    public ReportBatchQueryController(ReportBatchQueryService queryService, ReportTaskService taskService) {
        this.queryService = queryService;
        this.taskService = taskService;
    }

    @Autowired(required = false)
    public void setAccessService(ReportingAccessService accessService) {
        this.accessService = accessService;
    }

    @ApiOperation("分页查询上报批次")
    @GetMapping
    public Result<IPage<ReportBatch>> page(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sourceDomain,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String accountingPeriod,
            @RequestParam(required = false) String treasuryCode,
            @RequestParam(required = false) String fileName) {
        CurrentUser user = currentUser();
        String prefix = accessService == null ? null : accessService.requirePrefix(user.username);
        return success(queryService.page(pageNo, pageSize, sourceDomain, businessType, status,
                accountingPeriod, treasuryCode, fileName, prefix), "查询成功");
    }

    @ApiOperation("查询批次完整执行详情")
    @GetMapping("/{batchId}")
    public Result<ReportBatchDetail> detail(@PathVariable String batchId) {
        if (accessService != null) accessService.requireBatch(batchId, currentUser().username);
        return success(queryService.detail(batchId), "查询成功");
    }

    @AutoLog(value = "数据上报-按原批次重试")
    @ApiOperation("按原批次账期重新解析、入库或加工")
    @PostMapping("/{batchId}/retry")
    public Result<ReportTask> retry(@PathVariable String batchId, @RequestParam String taskType) {
        CurrentUser user = currentUser();
        if (accessService != null) accessService.requireBatch(batchId, user.username);
        return success(taskService.queueRetry(batchId, taskType, user.userId, user.username), "重试任务已排队");
    }

    @AutoLog(value = "数据上报-逻辑删除批次")
    @ApiOperation("逻辑删除批次，保留文件与执行审计")
    @DeleteMapping("/{batchId}")
    public Result<String> delete(@PathVariable String batchId) {
        CurrentUser user = currentUser();
        if (accessService != null) accessService.requireBatch(batchId, user.username);
        queryService.logicalDelete(batchId, user.username);
        return success(batchId, "已逻辑删除，原始文件和审计记录仍保留");
    }

    private <T> Result<T> success(T value, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage(message);
        result.setResult(value);
        return result;
    }

    private CurrentUser currentUser() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof LoginUser) {
                LoginUser loginUser = (LoginUser) principal;
                String username = loginUser.getUsername() == null ? "anonymous" : loginUser.getUsername();
                return new CurrentUser(loginUser.getId() == null ? username : loginUser.getId(), username);
            }
        } catch (RuntimeException ignored) {
            // Standalone tests have no Shiro SecurityManager.
        }
        return new CurrentUser("anonymous", "anonymous");
    }

    private static final class CurrentUser {
        private final String userId;
        private final String username;
        private CurrentUser(String userId, String username) {
            this.userId = userId;
            this.username = username;
        }
    }
}
