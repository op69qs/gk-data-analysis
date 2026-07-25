package org.jeecg.modules.reporting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.reporting.service.ReportFileAccessService;
import org.springframework.core.io.InputStreamResource;
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
import java.nio.file.Files;

@Api(tags = "数据上报文件")
@RestController
@RequestMapping("/reporting/files")
public class ReportFileController {
    public static final String DOWNLOAD_PERMISSION = "reporting:file:download";

    private final ReportFileAccessService fileService;

    public ReportFileController(ReportFileAccessService fileService) {
        this.fileService = fileService;
    }

    @AutoLog(value = "数据上报-下载留存文件")
    @ApiOperation("按跟踪文件ID下载原ZIP或解压文件")
    @GetMapping("/{fileId}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileId) throws IOException {
        ReportFileAccessService.Download download = fileService.download(fileId);
        String encoded = URLEncoder.encode(download.getFileName(), StandardCharsets.UTF_8.name())
                .replace("+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded);
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        if (download.getContentType() != null) {
            try {
                contentType = MediaType.parseMediaType(download.getContentType());
            } catch (IllegalArgumentException ignored) {
                // Unknown legacy content type is served as binary.
            }
        }
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(download.getPath()))
                .contentType(contentType)
                .body(new InputStreamResource(Files.newInputStream(download.getPath())));
    }
}
