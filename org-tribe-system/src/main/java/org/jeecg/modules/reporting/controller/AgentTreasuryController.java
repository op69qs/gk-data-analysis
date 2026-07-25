package org.jeecg.modules.reporting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;
import org.jeecg.modules.reporting.service.AgentTreasuryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "代理国库配置")
@RestController
@RequestMapping("/reporting/agent-treasuries")
public class AgentTreasuryController extends ReportingWebSupport {
    public static final String ADD_PERMISSION = "reporting:treasury:add";
    public static final String EDIT_PERMISSION = "reporting:treasury:edit";

    private final AgentTreasuryService service;
    public AgentTreasuryController(AgentTreasuryService service) { this.service = service; }

    @ApiOperation("查询代理国库配置")
    @GetMapping
    public Result<IPage<AgentTreasuryConfig>> page(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String treCode,
            @RequestParam(required = false) String treName,
            @RequestParam(required = false) String state) {
        return success(service.page(pageNo, pageSize, treCode, treName, state), "查询成功");
    }

    @AutoLog(value = "数据上报-新增代理国库")
    @ApiOperation("新增代理国库配置")
    @PostMapping
    public Result<String> add(@RequestBody AgentTreasuryConfig record) {
        CurrentUser user = currentUser();
        service.add(record, user.userId);
        return success(record.getTreCode(), "新增成功");
    }

    @AutoLog(value = "数据上报-修改代理国库")
    @ApiOperation("修改代理国库配置及启停状态")
    @PutMapping("/{treasuryCode}")
    public Result<String> update(@PathVariable String treasuryCode,
                                 @RequestBody AgentTreasuryConfig record) {
        CurrentUser user = currentUser();
        service.update(treasuryCode, record, user.userId);
        return success(treasuryCode, "修改成功");
    }
}
