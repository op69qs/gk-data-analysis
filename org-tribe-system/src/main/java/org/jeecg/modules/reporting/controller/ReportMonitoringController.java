package org.jeecg.modules.reporting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.reporting.service.ReportMonitoringService;
import org.jeecg.modules.reporting.service.ReportingUserScopeService;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;
import org.jeecg.modules.reporting.vo.ReportingPageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "数据上报监控")
@RestController
@RequestMapping("/reporting/monitoring")
public class ReportMonitoringController extends ReportingWebSupport {
    private final ReportMonitoringService service;
    private final ReportingUserScopeService userScopeService;

    public ReportMonitoringController(ReportMonitoringService service, ReportingUserScopeService userScopeService) {
        this.service = service;
        this.userScopeService = userScopeService;
    }

    @ApiOperation("KEY 代理国库上报与异常监控")
    @GetMapping("/key")
    public Result<ReportingPageResult<Map<String, Object>>> key(
            ReportingBusinessQuery query,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        query.setGuokuId(userScopeService.requireGuokuId(currentUser().username));
        return success(service.key(query, pageNo, pageSize), "查询成功");
    }

    @ApiOperation("TIMS 收入支出库存完整性与异常监控")
    @GetMapping("/tims")
    public Result<ReportingPageResult<Map<String, Object>>> tims(
            ReportingBusinessQuery query,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        query.setGuokuId(userScopeService.requireGuokuId(currentUser().username));
        return success(service.tims(query, pageNo, pageSize), "查询成功");
    }

    @ApiOperation("国库选择项")
    @GetMapping("/treasuries")
    public Result<List<Map<String, Object>>> treasuries() {
        return success(service.treasuries(userScopeService.requireGuokuId(currentUser().username)), "查询成功");
    }
}
