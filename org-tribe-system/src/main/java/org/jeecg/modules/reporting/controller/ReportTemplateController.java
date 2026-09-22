package org.jeecg.modules.reporting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.reporting.service.ReportTemplateService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Api(tags = "数据上报模板")
@RestController
@RequestMapping("/reporting/templates")
public class ReportTemplateController extends ReportingWebSupport {

    /** Future button permission; currently follows the reporting menu permission. */
    public static final String DOWNLOAD_PERMISSION = "reporting:template:download";

    private final ReportTemplateService templateService;

    public ReportTemplateController(ReportTemplateService templateService) {
        this.templateService = templateService;
    }

    @AutoLog(value = "数据上报-下载上报模板")
    @ApiOperation("按业务类型下载 Excel 上报模板（与原 DIS 快报收入模板一致）")
    @GetMapping("/{businessType}")
    public ResponseEntity<Resource> download(@PathVariable String businessType) throws IOException {
        ReportTemplateService.Template template;
        try {
            template = templateService.download(businessType);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.notFound().build();
        }
        String encoded = URLEncoder.encode(template.getFileName(), StandardCharsets.UTF_8.name())
                .replace("+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded);
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            contentType = MediaType.parseMediaType(template.getContentType());
        } catch (IllegalArgumentException ignored) {
            // fall back to binary
        }
        long length = template.getResource().contentLength();
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .headers(headers)
                .contentType(contentType);
        if (length >= 0) {
            builder.contentLength(length);
        }
        return builder.body(template.getResource());
    }
}
