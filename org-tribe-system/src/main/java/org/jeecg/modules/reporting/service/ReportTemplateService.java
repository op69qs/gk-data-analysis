package org.jeecg.modules.reporting.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Serves static Excel upload templates packaged with the reporting module
 * (aligned with legacy DIS template downloads).
 */
@Service
public class ReportTemplateService {

    private static final Map<String, TemplateSpec> TEMPLATES;

    static {
        Map<String, TemplateSpec> map = new HashMap<String, TemplateSpec>();
        map.put("FLASH_INCOME", new TemplateSpec(
                "reporting/templates/flash_income.xls",
                "快报_收入数据.xls",
                "application/vnd.ms-excel"));
        TEMPLATES = Collections.unmodifiableMap(map);
    }

    public Template download(String businessType) {
        if (businessType == null || businessType.trim().isEmpty()) {
            throw new IllegalArgumentException("业务类型不能为空");
        }
        String key = businessType.trim().toUpperCase(Locale.ROOT);
        TemplateSpec spec = TEMPLATES.get(key);
        if (spec == null) {
            throw new IllegalArgumentException("暂不支持该业务类型的模板下载：" + businessType);
        }
        Resource resource = new ClassPathResource(spec.classpath);
        if (!resource.exists()) {
            throw new IllegalStateException("上报模板文件缺失：" + spec.downloadName);
        }
        return new Template(resource, spec.downloadName, spec.contentType);
    }

    public boolean supports(String businessType) {
        if (businessType == null) {
            return false;
        }
        return TEMPLATES.containsKey(businessType.trim().toUpperCase(Locale.ROOT));
    }

    public static final class Template {
        private final Resource resource;
        private final String fileName;
        private final String contentType;

        public Template(Resource resource, String fileName, String contentType) {
            this.resource = resource;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public Resource getResource() {
            return resource;
        }

        public String getFileName() {
            return fileName;
        }

        public String getContentType() {
            return contentType;
        }
    }

    private static final class TemplateSpec {
        private final String classpath;
        private final String downloadName;
        private final String contentType;

        private TemplateSpec(String classpath, String downloadName, String contentType) {
            this.classpath = classpath;
            this.downloadName = downloadName;
            this.contentType = contentType;
        }
    }
}
