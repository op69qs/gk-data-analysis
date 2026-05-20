package org.jeecg.modules.audit.controller;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.audit.service.IDataAnalysisBizAuditService;
import org.jeecg.modules.audit.vo.DataAnalysisMenuEntryAuditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/biz-audit")
public class DataAnalysisBizAuditController {

    @Autowired
    private IDataAnalysisBizAuditService auditService;

    @PostMapping("/menu-entry")
    public Result<?> recordMenuEntry(@RequestBody DataAnalysisMenuEntryAuditRequest request,
                                     HttpServletRequest servletRequest) {
        auditService.recordMenuEntry(request, servletRequest);
        return Result.ok("记录成功");
    }
}
