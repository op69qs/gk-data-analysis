package org.jeecg.modules.reporting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.reporting.exception.ReportUploadException;
import org.jeecg.modules.reporting.service.ReportBatchService;
import org.jeecg.modules.reporting.vo.ReportBatchUploadResult;
import org.jeecg.modules.reporting.vo.ReportUploadCommand;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeecg.modules.reporting.service.ReportingUserScopeService;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "数据上报批次")
@RestController
@RequestMapping("/reporting/batches")
public class ReportBatchController {

    /** Future button permission; currently access follows the reporting menu permission. */
    public static final String UPLOAD_PERMISSION = "reporting:batch:upload";

    private final ReportBatchService batchService;
    private ReportingUserScopeService userScopeService;

    public ReportBatchController(ReportBatchService batchService) {
        this.batchService = batchService;
    }

    @Autowired(required = false)
    public void setUserScopeService(ReportingUserScopeService userScopeService) {
        this.userScopeService = userScopeService;
    }

    @AutoLog(value = "数据上报-上传ZIP")
    @ApiOperation("上传并创建可跟踪上报批次")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<ReportBatchUploadResult> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sourceDomain") String sourceDomain,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "accountingPeriod", required = false) String accountingPeriod,
            @RequestParam(value = "treasuryCode", required = false) String treasuryCode,
            @RequestParam(value = "treasuryName", required = false) String treasuryName) {
        ReportUploadCommand command = new ReportUploadCommand();
        command.setSourceDomain(sourceDomain);
        command.setBusinessType(businessType);
        command.setAccountingPeriod(accountingPeriod);
        command.setTreasuryCode(treasuryCode);
        command.setTreasuryName(treasuryName);
        CurrentUser currentUser = currentUser();
        if (userScopeService != null) {
            command.setAllowedTreasuryPrefix(userScopeService.requireTreasuryPrefix(currentUser.username));
        }

        try {
            ReportBatchUploadResult result = batchService.createUploadBatch(
                    file, command, currentUser.userId, currentUser.username);
            Result<ReportBatchUploadResult> response = new Result<>();
            response.setSuccess(true);
            response.setCode(200);
            response.setMessage("文件已归档并解压，解析任务已排队");
            response.setResult(result);
            return response;
        } catch (ReportUploadException exception) {
            ReportBatchUploadResult failed = new ReportBatchUploadResult(
                    exception.getBatchId(), null, "FAILED", exception.getStage(), 0);
            Result<ReportBatchUploadResult> result = new Result<>();
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage(exception.getMessage());
            result.setResult(failed);
            return result;
        } catch (IllegalArgumentException exception) {
            Result<ReportBatchUploadResult> result = new Result<>();
            result.setSuccess(false);
            result.setCode(400);
            result.setMessage(exception.getMessage());
            return result;
        }
    }

    private CurrentUser currentUser() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof LoginUser) {
                LoginUser loginUser = (LoginUser) principal;
                String username = loginUser.getUsername() == null ? "anonymous" : loginUser.getUsername();
                String userId = loginUser.getId() == null ? username : loginUser.getId();
                return new CurrentUser(userId, username);
            }
        } catch (RuntimeException ignored) {
            // Standalone tests have no Shiro SecurityManager; production requests are authenticated.
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
