package org.jeecg.modules.reporting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.reporting.service.ReportChangeService;
import org.jeecg.modules.reporting.service.ReportingUserScopeService;
import org.jeecg.modules.reporting.vo.ReportChangeCommand;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;
import org.jeecg.modules.reporting.vo.ReportingPageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "报送收入支出调整记录")
@RestController
@RequestMapping("/reporting/changes")
public class ReportChangeRecordController extends ReportingWebSupport {
    public static final String ADD_PERMISSION = "reporting:change:add";

    private final ReportChangeService service;
    private final ReportingUserScopeService userScopeService;
    public ReportChangeRecordController(ReportChangeService service, ReportingUserScopeService userScopeService) {
        this.service = service;
        this.userScopeService = userScopeService;
    }

    @ApiOperation("查询收入或支出报送基线并叠加最新调整")
    @GetMapping("/source")
    public Result<ReportingPageResult<Map<String, Object>>> source(
            ReportingBusinessQuery query,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        query.setGuokuId(userScopeService.requireGuokuId(currentUser().username));
        return success(service.source(query, pageNo, pageSize), "查询成功");
    }

    @ApiOperation("查询人工调整历史")
    @GetMapping
    public Result<ReportingPageResult<Map<String, Object>>> history(
            ReportingBusinessQuery query,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        query.setGuokuId(userScopeService.requireGuokuId(currentUser().username));
        return success(service.history(query, pageNo, pageSize), "查询成功");
    }

    @AutoLog(value = "数据上报-新增收入支出调整记录")
    @ApiOperation("新增调整记录；原金额与差额由服务端复算")
    @PostMapping
    public Result<String> add(@RequestBody ReportChangeCommand command) {
        CurrentUser user = currentUser();
        service.add(command, user.username, userScopeService.requireGuokuId(user.username));
        return success("OK", "调整记录已保存");
    }
}
